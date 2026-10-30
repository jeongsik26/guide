// 채팅방 페이지(chatRoom.html)에서 roomId, session_member_id 를 먼저 선언한다

// 같은 서버(origin)의 웹소켓 엔드포인트로 연결
const socket = new SockJS('/connection');
const stompClient = Stomp.over(socket);
stompClient.debug = null;

function appendMessage(message) {
    const chatMessagesDiv = document.getElementById("chat_list");
    const mine = String(message.memberId) === String(session_member_id);

    const row = document.createElement("div");
    row.className = mine ? 'msg my_msg' : 'msg other_msg';

    // 상대 메시지에만 이름 표시
    if (!mine) {
        const name = document.createElement("span");
        name.className = 'msg-name';
        name.textContent = message.memberName;
        row.appendChild(name);
    }

    const bubble = document.createElement("p");
    bubble.className = 'bubble';
    bubble.textContent = message.chatMsg;
    row.appendChild(bubble);

    chatMessagesDiv.appendChild(row);
    chatMessagesDiv.scrollTop = chatMessagesDiv.scrollHeight;
}

// 이전 대화 불러오기
fetch('/api/chat/room/' + roomId + '/messages')
    .then(response => response.ok ? response.json() : [])
    .then(messages => messages.forEach(appendMessage))
    .catch(error => console.error('이전 메시지를 불러오지 못했습니다.', error));

stompClient.connect({}, function () {
    stompClient.subscribe("/topic/chat/room/" + roomId, function (message) {
        appendMessage(JSON.parse(message.body));
    });
});

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("chatForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const messageInput = document.querySelector("#chat_content");
        const message = messageInput.value;
        if (!message.trim()) {
            return;
        }

        // 보낸 사람은 서버가 로그인 세션으로 채운다
        stompClient.send("/app/message", {}, JSON.stringify({chatMsg: message, roomId: roomId}));
        messageInput.value = "";
    });
});
