// 다음 주소 API
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var addr = ''; // 주소 변수

      //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
      if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
        addr = data.roadAddress;
      } else { // 사용자가 지번 주소를 선택했을 경우(J)
        addr = data.jibunAddress;
      }

      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById('postcode').value = data.zonecode;
      document.getElementById("address").value = addr;

      // 커서를 상세주소 필드로 이동한다.
      document.getElementById("detailAddress").focus();
    }
  }).open();
}

// 주소 검색 버튼 클릭 시   - 호출할 때 함수명만! 소괄호 필요없음!
document.querySelector("#searchAddress").addEventListener("click", execDaumPostcode);


// ----------------------------------------------
// **** 회원 가입 유효성 검사 *****

// 필수 입력 항목의 유효성 검사 여부를 체크하기 위한 객체
const checkObj = {
  "memberEmail": false,
  "memberPw": false,
  "memberPwConfirm": false,
  "memberNickname": false,
  "memberTel": false,
  "authKey": false
};

//---------------------------------------------------

/* 이메일 유효성 검사 */
const memberEmail = document.querySelector("#memberEmail");       // input 태그(이메일)
const emailMessage = document.querySelector("#emailMessage");     // span 태그(이메일 설명)

memberEmail.addEventListener("input", e => {
  checkObj.authKey = false;
  emailMessage.innerText = "";
  clearInterval(authTimer);

  const inputEmail = e.target.value;

  if (inputEmail.trim().length === 0) {    // 입력된 이메일이 없을 경우
    emailMessage.innerText = "스페이스를 누르지 마세요.";
    emailMessage.classList.remove('confirm', 'error');

    checkObj.memberEmail = false;

    memberEmail.value = '';
    memberEmail.focus();

    return;
  }

  // 정규식 검사 (/^과 $/ 사이에 넣고 싶은 정규식 작성)
  const regExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!regExp.test(inputEmail)) {
    emailMessage.innerText = "알맞은 이메일 형식으로 작성해주세요.";
    emailMessage.classList.add('error');
    emailMessage.classList.remove('confirm');

    checkObj.memberEmail = false;

    return;
  }

  fetch("/member/checkEmail?memberEmail=" + inputEmail)
    .then(resp => resp.text())
    .then(count => {
      if (count == 1) {
        emailMessage.innerText = "이미 사용중인 이메일 입니다.";
        emailMessage.classList.add('error');
        emailMessage.classList.remove('confirm');

        checkObj.memberEmail = false;

        return;
      }

      emailMessage.innerText = "사용가능한 이메일 입니다.";
      emailMessage.classList.add('confirm');
      emailMessage.classList.remove('error');

      checkObj.memberEmail = true;
    })
    .catch(error => {
      console.log(error);
    });
});

/* 이메일 인증 */
const sendAuthKeyBtn = document.querySelector("#sendAuthKeyBtn");   // button태그(인증번호 받기)
const authKey = document.querySelector("#authKey");                 // input태그(인증번호)
const authKeyMessage = document.querySelector("#authKeyMessage");   // span태그(인증번호 설명)
const checkAuthKeyBtn = document.querySelector("#checkAuthKeyBtn"); // button태그(인증하기)

let authTimer; // 타이머 역할을 할 setInterval을 저장할 변수

const initMin = 4; // 타이머 초기값 (분)
const initSec = 59; // 타이머 초기값 (초)
const initTime = "05:00";

// 실제 줄어드는 시간을 저장할 변수
let min = initMin;
let sec = initSec;

sendAuthKeyBtn.addEventListener("click", () => {
  checkObj.authKey = false;
  authKeyMessage.innerText = "";

  if (!checkObj.memberEmail) {
    alert("유효한 이메일을 작성후 클릭해 주세요");
    return;
  }

  min = initMin;
  sec = initSec;
  clearInterval(authTimer);

  fetch("/email/signup", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: memberEmail.value
  })
    .then(resp => resp.text())
    .then(result => {
      if (result == 1) console.log("인증번호 발송 성공!!");
      else console.log("인증번호 발송 실패ㅠㅠ");
    });

  authKeyMessage.innerText = initTime;
  authKeyMessage.classList.remove('confirm', 'error');

  alert("인증번호가 발송되었습니다. 5분내로 인증해주세요");

  authTimer = setInterval(() => {
    authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

    if(min == 0 && sec == 0){     // 0분 0초인 경우
      checkObj.authKey = false;
      clearInterval(authTimer);

      authKeyMessage.classList.add('error');
      authKeyMessage.classList.remove('confirm');

      return;
    }

    if(sec == 0){
      sec = 60;
      min--;
    }

    sec--;
  }, 1000);   // ms 단위(interval : 1초)
});

function addZero(number){
  if(number < 10) return "0" + number;
  return number;
}

checkAuthKeyBtn.addEventListener("click", () => {

  if (min === 0 && sec === 0) {   // 타이머가 00:00인 경우
    alert("인증번호 입력 제한시간을 초과하였습니다.");
    return;
  }

  if (authKey.value.length < 6) { // 인증번호가 제대로 입력 안된 경우(길이가 6미만인 경우)
    alert("인증번호를 정확히 입력해 주세요.");
    return;
  }

  // 문제 없는 경우(제한시간, 인증번호 길이 유효 시)
  const obj = {
    "email": memberEmail.value,
    "authKey": authKey.value
  };

  // 인증번호 확인용 비동기 요청 보냄
  fetch("/email/checkAuthKey", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj)    // obj JS 객체를 JSON 으로 변경
  })
    .then(resp => resp.text())
    .then(result => {   // 1 or 0

      if (result == 0) {
        alert("인증번호가 일치하지 않습니다!");
        checkObj.authKey = false;
        return;
      }

      clearInterval(authTimer);   // 타이머 멈춤
      authKeyMessage.innerText = "인증 되었습니다.";
      authKeyMessage.classList.remove("error");
      authKeyMessage.classList.add("confirm");
      checkObj.authKey = true;
    })

});