package com.example.demo.common.socket;

import lombok.*;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocketMsgDTO {
    private String message;

    @Override
    public String toString() {
        return message;
    }
}
