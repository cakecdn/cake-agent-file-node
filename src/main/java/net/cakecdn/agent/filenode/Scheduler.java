package net.cakecdn.agent.filenode;

import net.cakecdn.agent.filenode.client.AdminClient;
import net.cakecdn.agent.filenode.config.bean.PulseMeta;
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

    private final PulseMeta pulseMeta;
    private final AdminClient adminClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    public Scheduler(
            PulseMeta pulseMeta,
            AdminClient adminClient
    ) {
        this.pulseMeta = pulseMeta;
        this.adminClient = adminClient;
    }

    @Scheduled(cron = "30 * * * * ?")
    public void useTraffic() {
        try {
            PulseMeta newPulseMeta = adminClient.pulse(pulseMeta);
            pulseMeta.refreshPulseMeta(newPulseMeta);
            LOGGER.info("常规健康心跳。节点标签: {" + newPulseMeta.getTag() +
                    "} ，上载地址: {" + newPulseMeta.getDownloadPath() +
                    "} ，下载地址: {" + newPulseMeta.getDownloadPath() +
                    "} ，节点现有流量: {" + newPulseMeta.getNodeRemaining() + "} 。");
        } catch (Exception e) {
            LOGGER.error("常规健康心跳异常: " + e.getMessage());
        }
    }
}
