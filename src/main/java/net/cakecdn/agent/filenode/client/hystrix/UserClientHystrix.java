package net.cakecdn.agent.filenode.client.hystrix;

import net.cakecdn.agent.filenode.client.AdminClient;
import net.cakecdn.agent.filenode.client.UserClient;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserClientHystrix implements UserClient {
    @Override
    public UserRemainingTraffic getTraffic(Long userId) {
        return null;
    }
}
