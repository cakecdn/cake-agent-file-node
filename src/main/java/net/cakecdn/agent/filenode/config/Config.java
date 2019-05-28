package net.cakecdn.agent.filenode.config;

import net.cakecdn.agent.filenode.config.bean.AgentConfig;
import net.cakecdn.agent.filenode.config.bean.Traffic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${cake.node.endpointUrl:http://localhost/}")
    private String endpointUrl;
    @Value("${cake.node.healthCheckPath:/health-check}")
    private String healthCheckPath;

    @Bean
    AgentConfig agentConfig() {
        AgentConfig agentConfig = new AgentConfig();
        agentConfig.setEndpointUrl(endpointUrl);
        agentConfig.setHealthCheckPath(healthCheckPath);
        return agentConfig;
    }

    @Bean
    Traffic traffic() {
        return new Traffic();
    }
}
