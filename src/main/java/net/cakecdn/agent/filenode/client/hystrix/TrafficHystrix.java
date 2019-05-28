package net.cakecdn.agent.filenode.client.hystrix;

import net.cakecdn.agent.filenode.client.TrafficClient;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrafficHystrix implements TrafficClient {
    @Override
    public UserRemainingTraffic getTraffic(Long userId) {
        return null;
    }

    @Override
    public Map<Long, Long> useTraffic(Map<Long, Long> using) {
        return null;
    }

    @Override
    public UserRemainingTraffic exchangeTraffic(Long userId, Long usedTrafficBytes) {
        return null;
    }
}
