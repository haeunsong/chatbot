const eventSource = new EventSource(
  "http://localhost:8081/sender/GOD/receiver/HAEUN"
);
eventSource.onmessage = (e) => {
  console.log("Message received:", e.data);
  const data = JSON.parse(e.data);
  initMsg(data);
};
function getSendMsgBox(msg, time) {
  return `<div class="sent_msg">
  <p>${msg}</p>
  <span class="time_date"> ${time}</span>
</div>`;
}
// 서버로부터 받은 메시지를 처리하는 함수
// eventSource 를 통해 수신된 데이터를 기반으로 메시지를 채팅창에 추가하는 역할을 한다.
function initMsg(data) {
  let chatBox = document.querySelector("#chat-box");
  let msgInput = document.querySelector("#chat-outgoing-msg");

  // 날짜를 가독성 있게 포맷
  let date = new Date(data.createdAt);
  let formattedDate =
    date.getHours() +
    ":" +
    date.getMinutes() +
    " | " +
    (date.getMonth() + 1) +
    "월 " +
    date.getDate() +
    "일";

  let chatOutGoingBox = document.createElement("div");
  chatOutGoingBox.className = "outgoing_msg";

  chatOutGoingBox.innerHTML = getSendMsgBox(data.msg, formattedDate);
  chatBox.append(chatOutGoingBox);
  msgInput.value = "";
}
// 작성한 메시지를 서버로 전송하기
function addMsg() {
  let chatBox = document.querySelector("#chat-box");
  let msgInput = document.querySelector("#chat-outgoing-msg");

  let chatOutGoingBox = document.createElement("div");
  chatOutGoingBox.className = "outgoing_msg";

  let date = new Date();
  let now =
    date.getHours() +
    ":" +
    date.getMinutes() +
    " | " +
    date.getMonth() +
    "월 " +
    date.getDate() +
    "일";

  let chat = {
    sender: "GOD",
    receiver: "HAEUN",
    msg: msgInput.value,
  };
  fetch("http://localhost:8081/chat", {
    method: "post",
    body: JSON.stringify(chat),
    headers: {
      "Content-Type": "application/json; charset=UTF-8", // mime 타입 설정
    },
  });
  chatOutGoingBox.innerHTML = getSendMsgBox(msgInput.value, now);
  chatBox.append(chatOutGoingBox);
  msgInput.value = "";
}

// 보내기 화살표 버튼 눌렀을 때의 처리
document.querySelector("#chat-send").addEventListener("click", () => {
  addMsg();
});

// 보내기 화살표 엔터 눌렀을 때의 처리
document
  .querySelector("#chat-outgoing-msg")
  .addEventListener("keydown", (e) => {
    if (e.keyCode === 13) {
      addMsg();
    }
  });
