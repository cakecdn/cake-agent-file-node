package net.cakecdn.agent.filenode.config;

import net.cakecdn.agent.filenode.config.bean.PulseMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${cake.node.uploadPath:http://localhost/}")
    private String uploadPath;
    @Value("${cake.node.downloadPath:http://localhost/}")
    private String downloadPath;
    @Value("${cake.node.tag:unknown-node}")
    private String nodeTag;

    @Bean
    PulseMeta pulseMeta() {
        PulseMeta pulseMeta = new PulseMeta();
        pulseMeta.setUploadPath(uploadPath);
        pulseMeta.setUploadPath(downloadPath);
        pulseMeta.setTag(nodeTag);
        return pulseMeta;
    }
}
