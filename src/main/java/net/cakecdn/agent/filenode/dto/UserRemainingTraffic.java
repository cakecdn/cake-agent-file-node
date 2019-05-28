package net.cakecdn.agent.filenode.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRemainingTraffic {
    private Long userId;
    private Long remainingTrafficBytes;
}
