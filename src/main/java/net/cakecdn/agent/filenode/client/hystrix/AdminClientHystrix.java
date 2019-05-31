package net.cakecdn.agent.filenode.client.hystrix;

import net.cakecdn.agent.filenode.client.AdminClient;
import net.cakecdn.agent.filenode.config.bean.PulseMeta;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AdminClientHystrix implements AdminClient {

    @Override
    public PulseMeta pulse(PulseMeta pulseMeta) {
        return null;
    }

    @Override
    public UserRemainingTraffic exchangeTraffic(Long userId, Long usedTrafficBytes) {
        return null;
    }
}
