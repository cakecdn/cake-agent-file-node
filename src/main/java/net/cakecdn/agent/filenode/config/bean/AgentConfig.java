package net.cakecdn.agent.filenode.config.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentConfig {
    private String endpointUrl;
    private String healthCheckPath;
}
