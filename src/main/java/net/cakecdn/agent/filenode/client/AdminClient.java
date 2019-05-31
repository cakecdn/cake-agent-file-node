package net.cakecdn.agent.filenode.client;

import net.cakecdn.agent.filenode.client.hystrix.AdminClientHystrix;
import net.cakecdn.agent.filenode.config.bean.PulseMeta;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "cake-api-admin", fallback = AdminClientHystrix.class)
@Component(value = "adminClient")
public interface AdminClient {

    @PostMapping("/system/pulse")
    PulseMeta pulse(@RequestBody PulseMeta pulseMeta);

    @PostMapping("/system/traffics/exchange")
    UserRemainingTraffic exchangeTraffic(@RequestParam("userId") Long userId, @RequestBody Long usedTrafficBytes);
}
