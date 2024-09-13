# Audio Streaming Test Service

이 문서는 Audio Streaming Test Service의 사용 방법을 설명합니다.

## 1. Events 등록

서버에 연결하기 전 다음과 같은 이벤트를 설정합니다.

### 1-1. Event 수신 설정

- **이벤트 이름**: `audio-stream`
  - 설명: 오디오 스트리밍 이벤트
  - 내용: 오디오 파일(Binary)이 최대 1024 Bytes로 내려감
- **이벤트 이름**: `audio-stream-end`
  - 설명: 오디오 스트리밍 완료 이벤트
  - 내용: `TotalBytes:{totalBytes}` (text)

### 1-2. Event 발신 설정

- **이벤트 이름**: `message`
  - 설명: 메시지 이벤트

## 2. Socket.IO 서버에 Connect

### 기본 연결 주소: `ws://localhost:8081/socketio`
- 기본 형식은 `ws://{socket_host}:{socket_port}/socketio`를 따릅니다.
- `SocketIOConfig.class`에서 설정할 수 있습니다.

## 3. 오디오 스트리밍 요청

오디오 스트리밍을 요청하려면 다음과 같이 메시지를 전송합니다.

- **이벤트 이름**: `message`
- **발송할 메시지**: 
  ```json
  { "message": "get-audio" }
  ```
