package net.cakecdn.agent.filenode.service;

import net.cakecdn.agent.filenode.dto.info.InfoList;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {
    boolean fileExists(Long userId, String path, String fileName);

    void upload(Long userId, String path, String fileName, MultipartFile file) throws IOException;

    InfoList traversing(Long userId, String path, HttpServletResponse response) throws IOException;
}
