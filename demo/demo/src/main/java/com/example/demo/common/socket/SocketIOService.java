package com.example.demo.common.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;

import java.io.InputStream;

@Slf4j
@Component
public class SocketIOService {

    /**
     * 클라이언트에 WAV 파일 스트리밍
     */
    public void streamWavToClient(SocketIOClient client) {
        try {
            // 파일 불러 오기
            ClassPathResource resource = new ClassPathResource("static/sample.wav");
            InputStream inputStream = resource.getInputStream();

            byte[] buffer = new byte[1024];  // 스트리밍할 버퍼 사이즈 설정
            int bytesRead;                   // 현재 버퍼에 있는 bytes 담는 변수
            int totalBytesRead = 0;          // 총 bytes 크기

            // 파일을 클라이언트로 스트리밍
            log.info("[SocketIOService]-[streamWavToClient]- Audio stream started for client '{}'",
                    client.getSessionId().toString());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // 총 bytes 크기 계산
                totalBytesRead += bytesRead;
                // 클라이언트에게 이벤트를 통해 바이너리 데이터를 전송
                client.sendEvent("audio-stream", buffer);
                log.info("[SocketIOService]- BytesRead: {}, Buffer: {}", bytesRead, buffer);
            }

            // 스트리밍 완료
            inputStream.close();
            log.info("[SocketIOService]-[streamWavToClient]- Audio stream finished");
            // 완료 시 완료 이벤트 전송
            client.sendEvent("audio-stream-end", "TotalBytes:" + totalBytesRead);

        } catch (Exception e) {
            log.error("Error streaming WAV file to client", e);
        }
    }

}
