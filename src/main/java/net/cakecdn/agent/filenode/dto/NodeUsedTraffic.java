package net.cakecdn.agent.filenode.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NodeUsedTraffic {
    private Long userId;
    private Long usedTrafficBytes;
}
