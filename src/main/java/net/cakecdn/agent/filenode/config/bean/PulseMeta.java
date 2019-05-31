package net.cakecdn.agent.filenode.config.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.cakecdn.agent.filenode.dto.NodeUsedTraffic;
import net.cakecdn.agent.filenode.dto.UserRemainingTraffic;
import net.cakecdn.agent.filenode.exception.NodeTrafficRunDryException;
import net.cakecdn.agent.filenode.exception.TrafficNotFoundException;
import net.cakecdn.agent.filenode.exception.TrafficRunDryException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Getter
@Setter
@ToString
public class PulseMeta {

    private String tag;
    private String uploadPath;
    private String downloadPath;
    private long nodeRemaining;
    private long nodeUsed;
    private Map<Long, Long> userRemaining;
    private Map<Long, Long> userUsed;

    public PulseMeta() {
        this.userRemaining = new HashMap<>();
        this.userUsed = new HashMap<>();
    }

    public long getUserRemaining(long userId) {
        return userRemaining.getOrDefault(userId, 0L);
    }

    public long getUserUsed(long userId) {
        return userUsed.getOrDefault(userId, 0L);
    }

    public void setUserRemaining(long userId, long userRemaining) {
        this.userRemaining.put(userId, userRemaining);
    }

    @Deprecated
    public void setUserRemaining(Map<Long, Long> userRemaining) {
        this.userRemaining = userRemaining;
        this.userUsed = new HashMap<>();
        for (Map.Entry<Long, Long> e : this.userRemaining.entrySet()) {
            long key = e.getKey();
            this.userUsed.put(key, 0L);
        }
    }

    public void setUserUsed(long userId, long userUsed) {
        this.userUsed.put(userId, userUsed);
    }

    public void useTraffic(long userId, long using) throws TrafficRunDryException, TrafficNotFoundException, NodeTrafficRunDryException {
        Long userRemainingTraffic = this.userRemaining.get(userId);
        Long nodeUsedTraffic = this.userUsed.getOrDefault(userId, 0L); // 已用流量默认为0

        // 节点剩余流量不够
        if (nodeRemaining <= 0 || nodeRemaining < using) throw new NodeTrafficRunDryException();
        // 没有该用户的剩余流量信息
        if (userRemainingTraffic == null) throw new TrafficNotFoundException();
        // 用户剩余流量不够
        if (userRemainingTraffic <= 0 || userRemainingTraffic < using) throw new TrafficRunDryException();

        userRemaining.put(userId, userRemainingTraffic - using); // 用户剩余流量减少
        nodeRemaining -= using;                                  // 节点剩余流量减少
        userUsed.put(userId, nodeUsedTraffic + using);           // 用户已用流量增加
    }

    public void refreshPulseMeta(PulseMeta newPulseMeta) {
        this.tag = newPulseMeta.getTag();
        this.uploadPath = newPulseMeta.getUploadPath();
        this.downloadPath = newPulseMeta.getDownloadPath();
        this.nodeRemaining = newPulseMeta.getNodeRemaining();
        this.userRemaining = newPulseMeta.getUserRemaining();
        this.userUsed = new HashMap<>();
        this.nodeUsed = 0L;
    }
}
