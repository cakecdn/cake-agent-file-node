package net.cakecdn.agent.filenode.web;

import net.cakecdn.agent.filenode.config.bean.AgentConfig;
import net.cakecdn.agent.filenode.dto.AjaxResult;
import net.cakecdn.agent.filenode.dto.StringBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/node-config")
public class NodeConfigController {

    private final AgentConfig agentConfig;

    @Autowired
    public NodeConfigController(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    @PostMapping("/endpoint-url")
    public AjaxResult setEndpointUrl(@RequestBody StringBody nodeUrl) {
        agentConfig.setEndpointUrl(nodeUrl.getValue());
        return AjaxResult.success(nodeUrl.getValue());
    }

    @PostMapping("/health-check-path")
    public AjaxResult setHealthCheckPath(@RequestBody StringBody healthCheckPath) {
        agentConfig.setEndpointUrl(healthCheckPath.getValue());
        return AjaxResult.success(healthCheckPath.getValue());
    }

    @GetMapping
    public AjaxResult getConfigs() {
        return AjaxResult.success(agentConfig);
    }

}
