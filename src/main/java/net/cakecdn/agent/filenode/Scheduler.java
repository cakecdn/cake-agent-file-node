package net.cakecdn.agent.filenode;

import net.cakecdn.agent.filenode.client.TrafficClient;
import net.cakecdn.agent.filenode.config.bean.Traffic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableScheduling
class Scheduler {

    private final Traffic traffic;
    private final TrafficClient trafficClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
    private final String nodeName;

    @Autowired
    public Scheduler(
            Traffic traffic,
            TrafficClient trafficClient,
            @Value("${spring.cloud.consul.discovery.instance-id:unknown-node}") String nodeName) {
        this.traffic = traffic;
        this.trafficClient = trafficClient;
        this.nodeName = nodeName;
    }

    @Scheduled(cron = "30 * * * * ?") // 每半小时执行一次（每小时的30分）
    public void useTraffic() {
        Map<Long, Long> newRemainingTraffic = trafficClient.useTraffic(traffic.getUsed(), nodeName);
        traffic.setRemaining(newRemainingTraffic);
        LOGGER.info("已将最新的流量使用信息报告给用户端点微服务。");
    }
}
