package net.cakecdn.agent.filenode.dto.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class InfoList {
    List<PathInfo> paths;
    List<FileInfo> files;

    public InfoList() {
        paths = new ArrayList<>();
        files = new ArrayList<>();
    }
}
