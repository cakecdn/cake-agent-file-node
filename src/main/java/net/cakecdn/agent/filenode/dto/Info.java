package net.cakecdn.agent.filenode.dto;


import lombok.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Info {

    private String name;
    private String path;
    private String parentPath;
    private String url;
    private String parentUrl;
    private String contentType;
    private long sizeBytes;

    public Info(File f) throws IOException {
        this.setName(f.getName());
        this.setParentPath(f.getParent());
        this.setPath(f.getPath());
        this.setSizeBytes(f.length());
        Path path = Paths.get(f.toURI());
        this.setContentType(Files.probeContentType(path));
    }
}
