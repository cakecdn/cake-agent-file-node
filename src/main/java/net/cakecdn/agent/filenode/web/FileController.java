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

    @GetMapping("/")
    @ResponseBody
    public AjaxResult moduleStrings(
            HttpServletResponse response
    ) {
        response.setStatus(403);
        return AjaxResult.failure("403 forbidden.");
    }

    @PostMapping("/{userId}")
    @ResponseBody
    public AjaxResult upload(
            @PathVariable Long userId,
            @RequestParam MultipartFile file,
            HttpServletRequest request
    ) throws IOException {
        String fileName = file.getOriginalFilename();
        fileService.upload(userId, "/", fileName, file);

        return AjaxResult.failure("error.");
    }

    @PostMapping("/{userId}/{filePath}/**")
    @ResponseBody
    public AjaxResult upload(
            @PathVariable Long userId,
            @PathVariable String filePath,
            @RequestParam MultipartFile file,
            HttpServletRequest request
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

        if (file.isEmpty()) {
            return AjaxResult.failure("error: empty body.");
        }
        String fileName = file.getOriginalFilename();
        fileService.upload(userId, path, fileName, file);

        return AjaxResult.failure("error");
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public AjaxResult get(
            @PathVariable Long userId,
            HttpServletResponse response
    ) throws IOException {

        InfoList infoList = fileService.traversing(userId, "/", response);

        if (infoList != null)
            return AjaxResult.success(infoList);

        return AjaxResult.failure("404 not found.");
    }

    @GetMapping("/{userId}/{filePath}/**")
    @ResponseBody
    public AjaxResult get(
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

    @PostMapping("/mkdir/{userId}")
    public AjaxResult mkdir(@PathVariable Long userId, @RequestBody String dirName) {
        boolean success = fileService.mkdir(userId, "/", dirName);

        return AjaxResult.whether(success);
    }

    @PostMapping("/mkdir/{userId}/{filePath}/**")
    public AjaxResult mkdir(
            @PathVariable Long userId,
            @PathVariable String filePath,
            @RequestBody String dirName,
            HttpServletRequest request
    ) {
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

        boolean success = fileService.mkdir(userId, path, dirName);

        return AjaxResult.whether(success);
    }
}
