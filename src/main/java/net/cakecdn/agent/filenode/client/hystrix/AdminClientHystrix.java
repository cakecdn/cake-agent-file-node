package net.cakecdn.agent.filenode.client.hystrix;

import net.cakecdn.agent.filenode.client.AdminClient;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AdminClientHystrix implements AdminClient {

    @Override
    public Map<Long, Long> useTraffic(Map<Long, Long> using, String nodeName) {
        return null;
    }

    @Override
    public UserRemainingTraffic exchangeTraffic(Long userId, Long usedTrafficBytes) {
        return null;
    }
}
