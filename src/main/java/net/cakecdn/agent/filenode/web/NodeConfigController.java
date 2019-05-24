package net.cakecdn.agent.filenode.web;

import net.cakecdn.agent.filenode.config.bean.AgentConfig;
import net.cakecdn.agent.filenode.dto.AjaxResult;
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
    public AjaxResult setEndpointUrl(@RequestBody String nodeUrl) {
        agentConfig.setEndpointUrl(nodeUrl);
        return AjaxResult.success(nodeUrl);
    }

    @PostMapping("/health-check-path")
    public AjaxResult setHealthCheckPath(@RequestBody String nodeUrl) {
        agentConfig.setEndpointUrl(nodeUrl);
        return AjaxResult.success(nodeUrl);
    }

    @GetMapping
    public AjaxResult getConfigs() {
        return AjaxResult.success(agentConfig);
    }

}
