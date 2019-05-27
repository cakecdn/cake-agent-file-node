package net.cakecdn.agent.filenode.service;

import net.cakecdn.agent.filenode.dto.info.InfoList;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {
    boolean fileExists(Long userId, String basePath, String fileName);

    void upload(Long userId, String basePath, String fileName, MultipartFile file) throws IOException;
    
    boolean mkdir(Long userId, String basePath, String dstFolder);

    boolean mkdirs(Long userId, String basePath, String dstFolder);

    boolean delete(Long userId, String basePath, String fileOrFolderToBeRemoved);

    boolean rename(Long userId, String basePath, String src, String dst);

    InfoList traversing(Long userId, String basePath, HttpServletResponse response) throws IOException;
}
