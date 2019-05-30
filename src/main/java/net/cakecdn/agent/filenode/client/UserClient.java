package net.cakecdn.agent.filenode.client;

import net.cakecdn.agent.filenode.client.hystrix.UserClientHystrix;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "cake-api-user", fallback = UserClientHystrix.class)
@Component(value = "userClient")
public interface UserClient {

    @GetMapping("/system/user-traffics")
    UserRemainingTraffic getTraffic(@RequestParam("userId") Long userId);

}
