package net.cakecdn.agent.filenode.client;

import net.cakecdn.agent.filenode.client.hystrix.AdminClientHystrix;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "cake-api-admin", fallback = AdminClientHystrix.class)
@Component(value = "adminClient")
public interface AdminClient {

    @PostMapping("/system/traffics/node/{nodeName}")
    Map<Long, Long> useTraffic(@RequestBody Map<Long, Long> using, @PathVariable("nodeName") String nodeName);

    @PostMapping("/system/traffics/exchange")
    UserRemainingTraffic exchangeTraffic(@RequestParam("userId") Long userId, @RequestBody Long usedTrafficBytes);
}
