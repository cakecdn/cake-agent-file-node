package net.cakecdn.agent.filenode.service;

import net.cakecdn.agent.filenode.client.TrafficClient;
import net.cakecdn.agent.filenode.config.bean.AgentConfig;
import net.cakecdn.agent.filenode.config.bean.Traffic;
import net.cakecdn.agent.filenode.dto.FileTypeEnum;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import net.cakecdn.agent.filenode.dto.info.FileInfo;
import net.cakecdn.agent.filenode.dto.info.InfoList;
import net.cakecdn.agent.filenode.dto.info.PathInfo;
import net.cakecdn.agent.filenode.exception.TrafficNotFoundException;
import net.cakecdn.agent.filenode.exception.TrafficRunDryException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Value("${cake.node.fileDir:/tmp}")
    private String baseFilePath;
    private final AgentConfig agentConfig;
    private final Traffic traffic;
    private final TrafficClient trafficClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    public FileServiceImpl(
            AgentConfig agentConfig,
            Traffic traffic,
            TrafficClient trafficClient
    ) {
        this.agentConfig = agentConfig;
        this.traffic = traffic;
        this.trafficClient = trafficClient;
    }

    @Override
    public boolean fileExists(Long userId, String basePath, String fileName) {
        File dst = new File(pathAppend(baseFilePath, userId.toString(), basePath, fileName));
        return dst.exists();
    }

    @Override
    public void upload(Long userId, String basePath, String fileName, MultipartFile file) throws IOException {
        File dst = new File(pathAppend(baseFilePath, userId.toString(), basePath, fileName));

        // 创建目录
        if (!dst.getParentFile().exists()) {
            if (dst.getParentFile().mkdirs()) {
                throw new IOException("Make directory failed.");
            }
        }
        file.transferTo(dst);
    }

    @Override
    public boolean mkdir(Long userId, String basePath, String dstFolder) {
        File dst = new File(pathAppend(baseFilePath, userId.toString(), basePath, dstFolder));
        return dst.mkdir();
    }

    @Override
    public boolean mkdirs(Long userId, String basePath, String dstFolder) {
        File dst = new File(pathAppend(baseFilePath, userId.toString(), basePath, dstFolder));
        return dst.mkdirs();
    }

    @Override
    public boolean delete(Long userId, String basePath, String fileOrFolderToBeRemoved) {
        File dst = new File(pathAppend(baseFilePath, userId.toString(), basePath, fileOrFolderToBeRemoved));
        if (dst.isDirectory()) {
            return deleteDirectory(dst);
        } else {
            return dst.delete();
        }
    }

    @Override
    public boolean rename(Long userId, String basePath, String src, String dst) {
        File srcFileOrPath = new File(pathAppend(baseFilePath, userId.toString(), basePath, src));
        File dstFileOrPath = new File(pathAppend(baseFilePath, userId.toString(), basePath, dst));
        return srcFileOrPath.renameTo(dstFileOrPath);
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    @Override
    public InfoList traversing(Long userId, String basePath, HttpServletResponse response) throws IOException {
        String baseUrl = agentConfig.getEndpointUrl();

        File dst = new File(pathAppend(baseFilePath, userId.toString(), basePath));
        InfoList infoList = new InfoList();

        if (dst.isDirectory()) {
            File[] fs = dst.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    if (!f.isDirectory()) {
                        FileInfo fileInfo = new FileInfo(f);
                        fileInfo.setUrl(pathAppend(baseUrl, userId.toString(), basePath, f.getName()));
                        fileInfo.setParentUrl(parentUrl(fileInfo.getUrl()));
                        fileInfo.setType(FileTypeEnum.FILE);
                        infoList.getFiles().add(fileInfo);
                    } else {
                        PathInfo pathInfo = new PathInfo(f);
                        pathInfo.setUrl(pathAppend(baseUrl, userId.toString(), basePath, f.getName()));
                        pathInfo.setParentUrl(pathAppend(baseUrl, userId.toString(), basePath));
                        pathInfo.setParentUrl(parentUrl(pathInfo.getUrl()));
                        pathInfo.setType(FileTypeEnum.DIRECTORY);
                        pathInfo.setSizeBytes(FileUtils.sizeOfDirectory(f));
                        infoList.getPaths().add(pathInfo);
                    }
                }
            }

            return infoList;
        } else if (dst.isFile()) {
            long sizeBytes = dst.length();
            // 流量统计模块
            try {
                traffic.useTraffic(userId, sizeBytes);
            } catch (TrafficRunDryException e) {
                long used = traffic.getUsed(userId);
                LOGGER.info("用户 {" + userId + "} 的流量耗尽" +
                        "，当前节点剩余流量为: {" + traffic.getRemaining(userId) +
                        "} ，低于最近一次请求需要的流量为：{" + sizeBytes + "} 。");
                // 可能已重新充值但未刷新，以报告现用流量的方式重新获取流量信息。
                UserRemainingTraffic userRemainingTraffic = trafficClient.exchangeTraffic(userId, traffic.getUsed(userId));
                traffic.setUsed(userId, 0);
                traffic.setRemaining(userId, userRemainingTraffic.getRemainingTrafficBytes());
                LOGGER.info("用户 {" + userId + "} 已将已用流量 {" +
                        used + "} 报告给用户端点微服务，重新获取的流量为: {" +
                        userRemainingTraffic.getRemainingTrafficBytes() + "} ，已用流量已清零。");
                // 没有流量则禁止访问：需要付款。
                response.setStatus(402);
                return null;
            } catch (TrafficNotFoundException e) {
                // 没有找到流量信息则重新获取
                UserRemainingTraffic userRemainingTraffic = trafficClient.getTraffic(userId);
                traffic.getRemaining().put(userId, userRemainingTraffic.getRemainingTrafficBytes());
                LOGGER.info("用户 {" + userId + "} 在节点上的流量剩余信息没找到，重新获取到的流量为: {" + userRemainingTraffic.getRemainingTrafficBytes() + "} 。");
            }
            // content-type
            String contentType = Files.probeContentType(Paths.get(dst.toURI()));
            response.setHeader("content-type", contentType);
            response.setContentType(contentType);
            // 数据流
            InputStream is = new FileInputStream(dst);
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) >= 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
            return null;
        }

        response.setStatus(404);
        return infoList;
    }

    private String pathAppend(String... pathPatterns) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pathPatterns.length; i++) {
            sb.append(pathStandardize(pathPatterns[i]));
            if (i < pathPatterns.length - 1 && !StringUtils.equals(pathPatterns[i], "/")) {
                sb.append("/");
            }
        }
        return sb.toString();
    }

    private String parentUrl(String path) {
        String[] pathPatterns = path.split("/");
        List<String> pathPatternList = new ArrayList<>(Arrays.asList(pathPatterns));


        if (StringUtils.equals(pathPatternList.get(pathPatternList.size() - 1), "/")) {
            pathPatternList.remove(pathPatternList.size() - 1);
        }
        pathPatternList.remove(pathPatternList.size() - 1);
        pathPatternList.remove(pathPatternList.size() - 1);

        StringBuilder sb = new StringBuilder();

        int cursorIndex = 0;
        for (String pathPattern : pathPatternList) {
            sb.append(pathPattern);
            if (cursorIndex < pathPatternList.size() - 1 && !StringUtils.equals(pathPattern, "/")) {
                sb.append("/");
            }
            cursorIndex++;
        }

        return sb.toString();
    }

    private String pathStandardize(String path) {

        // 去掉字符串首的斜线
        if (path.length() != 1 && path.charAt(0) == '/') {
            path = path.substring(1);
        }

        // 去掉字符串尾的斜线
        if (path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }
}
