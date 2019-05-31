package net.cakecdn.agent.filenode.config.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "cake.node")
public class CakeProperty {
    private String tag;
    private String fileDir;
    private String uploadUrl;
    private String downloadUrl;
}
