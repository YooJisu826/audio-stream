package com.example.demo.common.socket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.demo.common.util.Parser.extractMessage;

@Slf4j
@Component
public class SocketIOController {

    private static final String CHAT = "message";

    @Autowired
    private final SocketIOService socketIOService;
    private final SocketIONamespace namespace;

    /**
     * 소켓 이벤트 리스너 등록
     */
    public SocketIOController(SocketIOServer server, SocketIOService socketIOService) {
        this.socketIOService = socketIOService;
        // 소켓 리스너 등록
        this.namespace = server.addNamespace("/socketio");
        this.namespace.addConnectListener(listenConnected());
        this.namespace.addDisconnectListener(listenDisconnected());
        this.namespace.addEventListener(CHAT, SocketMsgDTO.class, listenReceived());
    }

    /**
     * 데이터 리스너
     */
    private DataListener<SocketMsgDTO> listenReceived() {
        return (client, data, ackSender) -> {
            log.info("[SocketIOController]-[listenReceived]-[{}] Received message '{}'",
                    client.getSessionId().toString(), data);

            // "get-audio" 메시지 수신 시 Wav 파일을 Client에게 streaming
            // data 로부터 message 내용 파싱 - 여기서 message 는 SocketMsgDTO class 의 필드 이름을 뜻함
            String message = extractMessage(String.valueOf(data), "message");
            if (message.equals("get-audio")) {
                socketIOService.streamWavToClient(client);
            }

            namespace.getBroadcastOperations().sendEvent(CHAT, data);
        };
    }

    /**
     * 클라이언트 연결 리스너
     */
    private ConnectListener listenConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("[SocketIOController]-[listenConnected]-[{}] Connected to Socket Module through '{}'",
                    client.getSessionId().toString(), handshakeData.getUrl());
        };
    }

    /**
     * 클라이언트 연결 해제 리스너
     */
    private DisconnectListener listenDisconnected() {
        return client -> {
            log.info("[SocketIOController]-[listenDisconnected]-[{}] Disconnected from Socket Module.",
                    client.getSessionId().toString());
            client.disconnect();
        };
    }

}
