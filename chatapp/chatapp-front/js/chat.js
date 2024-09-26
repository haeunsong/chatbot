// 로그인 시스템 대신 임시방편
let username = prompt("아이디를 입력하세요: ");
let roomNum = prompt("채팅방 번호를 입력하세요: ");

document.querySelector("#username").innerHTML = username;

// SSE 연결하기
const eventSource = new EventSource(
  `http://localhost:8081/chat/roomNum/${roomNum}`
);

// const eventSource = new EventSource(
//   "http://localhost:8081/sender/GOD/receiver/HAEUN"
// );
eventSource.onmessage = (e) => {
  const data = JSON.parse(e.data);
  if (data.sender === username) {
    // 내가 보낸 메시지 => 파란박스(오른쪽)
    initMyMsg(data);
  } else {
    // 상대가 보낸 메시지 => 회색박스(왼쪽)
    initYourMsg(data);
  }
};

// 파란박스 만들기
function getSendMsgBox(data) {
  let md = data.createdAt.substring(5, 10);
  let tm = data.createdAt.substring(11, 16);
  convertTime = tm + " | " + md;

  return `<div class="sent_msg">
  <p>${data.msg}</p>
  <span class="time_date"> ${convertTime} / ${data.sender}</span>
</div>`;
}
// 회색박스 만들기
function getReceiveBox(data) {
  let md = data.createdAt.substring(5, 10);
  let tm = data.createdAt.substring(11, 16);
  convertTime = tm + " | " + md;

  return `<div class="received_withd_msg">
  <p>${data.msg}</p>
  <span class="time_date"> ${convertTime} / ${data.sender}</span>
</div>`;
}
// 최초 초기화될 때 1번방 3건이 있으면 3건을 다 가져온다.
// addMsg() 함수 호출시에 DB 에 insert 되고, 그 데이터가 자동으로 흘러들어온다 (SSE)
// 서버로부터 받은 메시지를 처리하는 함수
// eventSource 를 통해 수신된 데이터를 기반으로 메시지를 채팅창에 추가하는 역할을 한다.
// 메시지 파란박스 초기화(데이터 들어올 때마다 초기화된다)
function initMyMsg(data) {
  let chatBox = document.querySelector("#chat-box");

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

  let sendBox = document.createElement("div");
  sendBox.className = "outgoing_msg";

  sendBox.innerHTML = getSendMsgBox(data);
  chatBox.append(sendBox);

  // 스크롤 조금씩 내려주기
  document.documentElement.scrollTop = document.body.scrollHeight;
}
// 상대방 메시지
// 회색박스 초기화하기
function initYourMsg(data) {
  let chatBox = document.querySelector("#chat-box");

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

  let receivedBox = document.createElement("div");
  receivedBox.className = "received_msg";

  receivedBox.innerHTML = getReceiveBox(data);
  chatBox.append(receivedBox);

  document.documentElement.scrollTop = document.body.scrollHeight;
}
// 작성한 메시지를 서버로 전송하기
// AJAX 채팅 메시지를 전송한다. -> roomNum 에 채팅을 전송하는 개념 (상대방에게 전송하기보다는)
async function addMsg() {
  let msgInput = document.querySelector("#chat-outgoing-msg");

  // 서버로 전송할 메시지 데이터
  let chat = {
    sender: username,
    roomNum: roomNum,
    msg: msgInput.value,
  };

  // 서버에 메시지 전송
  fetch("http://localhost:8081/chat", {
    method: "post",
    body: JSON.stringify(chat),
    headers: {
      "Content-Type": "application/json; charset=UTF-8", // mime 타입 설정
    },
  });
  // msg 에 있는 내용만 비워주기
  msgInput.value = "";
}

// 보내기 화살표 버튼 눌렀을 때 메시지 전송 처리
document.querySelector("#chat-send").addEventListener("click", () => {
  addMsg();
});

// 보내기 화살표 엔터 눌렀을 때의 메시지 전송 처리
document
  .querySelector("#chat-outgoing-msg")
  .addEventListener("keydown", (e) => {
    if (e.keyCode === 13) {
      e.preventDefault();
      addMsg();
    }
  });
