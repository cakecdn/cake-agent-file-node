package net.cakecdn.agent.filenode.client;

import net.cakecdn.agent.filenode.CakeAgentFileNodeApplicationTests;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AdminClientTest extends CakeAgentFileNodeApplicationTests {

    @Autowired
    AdminClient adminClient;

    @Test
    public void useTraffic() {
        Map<Long, Long> map = new HashMap<>();
        map.put(123456L, 1000L);
        Map<Long, Long> newMap = adminClient.useTraffic(map, "unknown-node");
        System.out.println(newMap);
    }
}