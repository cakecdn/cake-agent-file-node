package net.cakecdn.agent.filenode.web;

import net.cakecdn.agent.filenode.dto.AjaxResult;
import net.cakecdn.agent.filenode.dto.info.InfoList;
import net.cakecdn.agent.filenode.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @ResponseBody
    @PostMapping("/file")
    public AjaxResult upload(
            @RequestParam MultipartFile file,
            @RequestParam Long userId,
            @RequestParam String path
    ) throws IOException {
        if (file.isEmpty()) {
            return AjaxResult.failure("error: empty body.");
        }
        String fileName = file.getOriginalFilename();
        fileService.upload(userId, path, fileName, file);

        return AjaxResult.failure("error");
    }

    @GetMapping("/{userId}/{filePath}/**")
    @ResponseBody
    public AjaxResult moduleStrings(
            @PathVariable Long userId,
            @PathVariable String filePath,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        final String patternPath =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, patternPath);

        String path;

        if (!arguments.isEmpty()) {
            path = filePath + '/' + arguments;
        } else {
            path = filePath;
        }

        InfoList infoList = fileService.traversing(userId, path, response);

        if (infoList != null)
            return AjaxResult.success(infoList);
        else return null;
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public AjaxResult moduleStrings(
            @PathVariable Long userId,
            HttpServletResponse response
    ) throws IOException {

        InfoList infoList = fileService.traversing(userId, "/", response);

        if (infoList != null)
            return AjaxResult.success(infoList);

        return AjaxResult.failure("404 not found.");
    }

    @GetMapping("/")
    @ResponseBody
    public AjaxResult moduleStrings(
            HttpServletResponse response
    ) {
        response.setStatus(403);
        return AjaxResult.failure("403 forbidden.");
    }
}
