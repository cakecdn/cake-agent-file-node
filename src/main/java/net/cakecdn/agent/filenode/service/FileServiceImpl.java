package net.cakecdn.agent.filenode.service;

import net.cakecdn.agent.filenode.dto.info.FileInfo;
import net.cakecdn.agent.filenode.dto.info.InfoList;
import net.cakecdn.agent.filenode.dto.info.PathInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
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
    @Value("${cake.node.fileUrl:http://localhost:7003/}")
    private String fileUrl;

    @Override
    public boolean fileExists(Long userId, String path, String fileName) {
        File dest = new File(pathAppend(baseFilePath, userId.toString(), path, fileName));
        return dest.exists();
    }

    @Override
    public void upload(Long userId, String path, String fileName, MultipartFile file) throws IOException {
        File dest = new File(pathAppend(baseFilePath, userId.toString(), path, fileName));

        // 创建目录
        if (!dest.getParentFile().exists()) {
            if (dest.getParentFile().mkdirs()) {
                throw new IOException("Make directory failed.");
            }
        }
        file.transferTo(dest);
    }

    @Override
    public InfoList traversing(Long userId, String path, HttpServletResponse response) throws IOException {
        File dest = new File(pathAppend(baseFilePath, userId.toString(), path));
        InfoList infoList = new InfoList();

        if (dest.isDirectory()) {
            File[] fs = dest.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    if (!f.isDirectory()) {
                        FileInfo fileInfo = new FileInfo(f);
                        fileInfo.setUrl(pathAppend(fileUrl, userId.toString(), path, f.getName()));
                        fileInfo.setParentUrl(parentUrl(fileInfo.getUrl()));
                        infoList.getFiles().add(fileInfo);
                    } else {
                        PathInfo pathInfo = new PathInfo(f);
                        pathInfo.setUrl(pathAppend(fileUrl, userId.toString(), path, f.getName()));
                        pathInfo.setParentUrl(pathAppend(fileUrl, userId.toString(), path));
                        pathInfo.setParentUrl(parentUrl(pathInfo.getUrl()));
                        infoList.getPaths().add(pathInfo);
                    }
                }
            }
        } else if (dest.isFile()) {
            String contentType = Files.probeContentType(Paths.get(dest.toURI()));

            response.setHeader("content-type", contentType);
            response.setContentType(contentType);

            InputStream is = new FileInputStream(dest);
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
