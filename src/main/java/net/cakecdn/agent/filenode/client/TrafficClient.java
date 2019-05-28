package net.cakecdn.agent.filenode.client;

import net.cakecdn.agent.filenode.client.hystrix.TrafficHystrix;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "cake-api-user", fallback = TrafficHystrix.class)
@Component(value = "trafficClient")
public interface TrafficClient {
    @GetMapping("/system/user-traffics")
    UserRemainingTraffic getTraffic(@RequestParam("userId") Long userId);

    @PostMapping("/system/user-traffics")
    Map<Long, Long> useTraffic(@RequestBody Map<Long, Long> using);

    @PostMapping("/system/user-traffics/exchange")
    UserRemainingTraffic exchangeTraffic(@RequestParam("userId") Long userId, @RequestBody Long usedTrafficBytes);
}
