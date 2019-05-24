package net.cakecdn.agent.filenode.dto.info;

import net.cakecdn.agent.filenode.dto.Info;

import java.io.File;
import java.io.IOException;

public class PathInfo extends Info {
    public PathInfo(File f) throws IOException {
        super(f);
    }
}
