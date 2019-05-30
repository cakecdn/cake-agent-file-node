package net.cakecdn.agent.filenode.config.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.cakecdn.agent.filenode.dto.NodeUsedTraffic;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import net.cakecdn.agent.filenode.exception.TrafficNotFoundException;
import net.cakecdn.agent.filenode.exception.TrafficRunDryException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Getter
@Setter
@ToString
public class Traffic {

    private Map<Long, Long> remaining;
    private Map<Long, Long> used;

    public Traffic() {
        this.remaining = new HashMap<>();
        this.used = new HashMap<>();
    }

    public long getRemaining(long userId) {
        return remaining.getOrDefault(userId, 0L);
    }

    public long getUsed(long userId) {
        return used.getOrDefault(userId, 0L);
    }

    public void setRemaining(long userId, long remaining) {
        this.remaining.put(userId, remaining);
    }

    public void setRemaining(Map<Long, Long> remaining) {
        this.remaining = remaining;
        this.used = new HashMap<>();
        for (Map.Entry<Long, Long> e : this.remaining.entrySet()) {
            long key = e.getKey();
            this.used.put(key, 0L);
        }
    }

    public void clearRemaining() {
        this.remaining = new HashMap<>();
    }

    public void setUsed(long userId, long used) {
        this.used.put(userId, used);
    }

    public void useTraffic(long userId, long using) throws TrafficRunDryException, TrafficNotFoundException {
        Long userRemainingTraffic = this.remaining.get(userId);
        Long nodeUsedTraffic = this.used.getOrDefault(userId, 0L); // 已用流量默认为0

        // 无剩余流量信息
        if (userRemainingTraffic == null) throw new TrafficNotFoundException();
        // 剩余流量不够
        if (userRemainingTraffic <= 0 || userRemainingTraffic < using) throw new TrafficRunDryException();

        remaining.put(userId, userRemainingTraffic - using); // 剩余流量减少
        used.put(userId, nodeUsedTraffic + using);           // 已用流量增加
    }
}
