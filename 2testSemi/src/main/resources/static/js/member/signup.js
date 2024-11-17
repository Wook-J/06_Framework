console.log("회원가입 유효성 검사");

const checkObj = {
  "memberEmail": false,
  "authKey": false,
  "memberPw": false,
  "memberPwConfirm": false,
  "memberNickname": false,
  // "memberName": false, // 여기는 boardProject와 다른부분(DB건드면서 추가해야 함)
  "memberTel": false
}
// memberName의 경우 입력하는 경우에 true로 바뀌는 걸로 세팅할 것!

let authTimer;    // 타이머 역할을 할 setInterval을 저장할 변수

/* ********** 이메일 유효성 검사 ********** */
const memberEmail = document.querySelector("#memberEmail");     // input 태그
const emailMessage = document.querySelector("#emailMessage");   // span 태그

memberEmail.addEventListener("input", e => {
  checkObj.authKey = false;
  document.querySelector("authKeyMessage").innerText = "";
  clearInterval(authTimer);

  const inputEmail = e.target.value;

  if (inputEmail.trim().length === 0) {
    emailMessage.innerText = "띄어쓰기 없이 이메일을 입력해주세요.";
    emailMessage.classList.add('error');
    emailMessage.classList.remove('confirm');
    checkObj.memberEmail = false;
    memberEmail.value = "";
    return;
  }

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
        emailMessage.innerText = "이미 사용중인 이메일입니다.";
        emailMessage.classList.add('error');
        emailMessage.classList.remove('confirm');
        checkObj.memberEmail = false;
        return;
      }

      emailMessage.innerText = "사용가능한 이메일입니다.";
      emailMessage.classList.add('confirm');
      emailMessage.classList.remove('error');
      checkObj.memberEmail = true;
    })
    .catch(error => {
      console.log(error);
    });
});

/* ********** 인증번호 유효성 검사 ********** */
const sendAuthKeyBtn = document.querySelector("#sendAuthKeyBtn");   // button 태그
const checkAuthKeyBtn = document.querySelector("#checkAuthKeyBtn"); // button 태그
const authKey = document.querySelector("#authKey");                 // input 태그
const authKeyMessage = document.querySelector("#authKeyMessage");   // span 태그

const initTime = "05:00";
const initMin = 4;      // 타이머 초기값 (분)
const initSec = 59;     // 타이머 초기값 (초)
let min = initMin;      // 실제 줄어드는 시간 저장 변수(분)
let sec = initSec;      // 실제 줄어드는 시간 저장 변수(초)

function addZero(number) {
  if (number < 10) return "0" + number;
  else return number;
}

// 인증번호 받기 버튼 클릭 시
sendAuthKeyBtn.addEventListener("click", () => {
  checkObj.authKey = false;
  authKeyMessage.innerText = "";

  if (!checkObj.memberEmail) {
    alert("유효한 이메일을 작성 후 클릭해 주세요");
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
      if (result == 1) console.log("인증 번호 발송 성공!!");
      else console.log("인증번호 발송 실패...");
    })

  authKeyMessage.innerText = initTime;
  authKeyMessage.classList.remove("confirm", "error");

  alert("인증번호가 발송되었습니다. 5분내로 인증해주세요");

  authTimer = setInterval(() => {
    authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

    if (min == 0 && sec == 0) {
      checkObj.authKey = false;
      clearInterval(authTimer);
      authKeyMessage.classList.add('error');
      authKeyMessage.classList.remove('confirm');
      return;
    }

    if (sec == 0) {
      sec = 60;
      min--;
    }

    sec--;
  }, 1000)
});



// 인증번호 확인 버튼 클릭 시
checkAuthKeyBtn.addEventListener("click", () => {

  if (min == 0 && sec == 0) {
    alert("인증번호 입력 제한시간을 초과하였습니다.");
    return;
  }

  if (authKey.value.length < 6) {
    alert("인증번호 6자리를 입력해주세요.");
    return;
  }

  const obj = {
    "email": memberEmail.value,
    "authKey": authKey.value
  };

  fetch("/email/checkAuthKey", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj)
  })
    .then(resp => resp.text())
    .then(result => {
      if (result == 0) {
        alert("인증번호가 일치하지 않습니다. 정확한 인증번호를 입력하세요");
        checkObj.authKey = false;
      }

      clearInterval(authTimer);
      authKeyMessage.innerText = "인증되었습니다";
      authKeyMessage.classList.add("confirm");
      authKeyMessage.classList.remove("error");
      checkObj.authKey = true;
    });

});

/* ********** 비밀번호 유효성 검사 ********** */
const memberPw = document.querySelector("#memberPw");               // input 태그
const memberPwConfirm = document.querySelector("#memberPwConfirm"); // input 태그
const pwMessage = document.querySelector("#pwMessage");             // span 태그

const checkPw = () => {

  if (memberPw.value === memberPwConfirm.value) {
    pwMessage.innerText = "비밀번호가 일치합니다.";
    pwMessage.classList.add("confirm");
    pwMessage.classList.remove("error");
    checkObj.memberPwConfirm = true;
    return;
  }

  pwMessage.innerText = "비밀번호가 일치하지 않습니다";
  pwMessage.classList.add("error");
  pwMessage.classList.remove("confirm");
  checkObj.memberPwConfirm = false;
}

memberPw.addEventListener("input", e => {
  const inputPw = e.target.value;

  if (inputPw.trim().length === 0) {
    pwMessage.innerText = "영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.";
    pwMessage.classList.remove("confirm", "error");
    checkObj.memberPw = false;
    memberPw.value = "";
    return;
  }

  const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;

  if (!regExp.test(inputPw)) {
    pwMessage.innerText = "비밀번호가 유효하지 않습니다.";
    pwMessage.classList.add("error");
    pwMessage.classList.remove("confirm");
    checkObj.memberPw = false;
    return;
  }

  pwMessage.innerText = "유효한 비밀번호 형식입니다";
  pwMessage.classList.add("confrim");
  pwMessage.classList.remove("error");
  checkObj.memberPw = true;

  if (memberPwConfirm.value.length > 0) {
    checkPw();
  }
});

memberPwConfirm.addEventListener("input", () => {
  if (checkObj.memberPw) {
    checkPw();
    return;
  }

  checkObj.memberPwConfirm = false;
});

/* ********** 닉네임 유효성 검사 ********** */
const memberNickname = document.querySelector("#memberNickname");   // input 태그
const nickMessage = document.querySelector("#nickMessage");         // span 태그

memberNickname.addEventListener("input", e => {

  const inputNickname = e.target.value;

  if (inputNickname.trin().length === 0) {
    nickMessage.innerText = "빈칸없이 한글, 영어 또는 숫자로 2~10글자를 입력해주세요.";
    nickMessage.classList.remove("confirm", "error");
    checkObj.memberNickname = false;
    memberNickname.value = "";
    return;
  }

  const regExp = /^[가-힣\w\d]{2,10}$/;

  if (!regExp.test(inputNickname)) {
    nickMessage.innerText = "유효하지 않은 닉네임 형식입니다.";
    nickMessage.classList.add("error");
    nickMessage.classList.remove("confirm");
    checkObj.memberNickname = false;
    return;
  }

  fetch("/member/checkNickname?memberNickname=" + inputNickname)
    .then(resp => resp.text())
    .then(count => {

      if (count == 1) { // 중복 O
        nickMessage.innerText = "이미 사용중인 닉네임 입니다.";
        nickMessage.classList.add("error");
        nickMessage.classList.remove("confirm");
        checkObj.memberNickname = false;
        return;
      }

      nickMessage.innerText = "사용 가능한 닉네임 입니다.";
      nickMessage.classList.add("confirm");
      nickMessage.classList.remove("error");
      checkObj.memberNickname = true;
    })
    .catch(err => console.log(err));

});

/* ********** 전화번호 유효성 검사 ********** */
const memberTel = document.querySelector("#memberTel");     // input 태그
const telMessage = document.querySelector("#telMessage");   // span 태그

memberTel.addEventListener("input", e => {

  const inputTel = e.target.value;

  if (inputTel.trim().length === 0) {
    telMessage.innerText = "전화번호를 입력해주세요.(- 제외)";
    telMessage.classList.remove("confirm", "error");
    checkObj.memberTel = false;
    memberTel.value = "";
    return;
  }

  const regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;

  if (!regExp.test(inputTel)) {
    telMessage.innerText = "유효하지 않은 전화번호 형식입니다.";
    telMessage.classList.add("error");
    telMessage.classList.remove("confirm");
    checkObj.memberTel = false;
    return;
  }

  telMessage.innerText = "유효한 전화번호 형식입니다.";
  telMessage.classList.add("confirm");
  telMessage.classList.remove("error");
  checkObj.memberTel = true;

});

/* ********** 다음주소 API 활용 부분 ********** */
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


/* ********** (최종) 회원가입 버튼 클릭 시 ********** */
const signUpForm = document.querySelector("#signUpForm");     // form 태그

signUpForm.addEventListener("submit", e => {

  for (let key in checkObj) {
    if (!checkObj[key]) {

      let str;

      switch (key) {
        case "memberEmail": str = "이메일이 유효하지 않습니다."; break;
        case "authKey": str = "이메일이 인증되지 않았습니다."; break;
        case "memberPw": str = "비밀번호가 유효하지 않습니다."; break;
        case "memberPwConfrim": str = "비밀번호가 일치하지 않습니다"; break;
        case "memberNickname": str = "닉네임이 유효하지 않습니다"; break;
        // case "memberName": str = "이름을 입력해주세요"; break;   // 입력만 하면 true
        case "memberTel": str = "전화번호가 유효하지 않습니다"; break;
      }

      alert(str);
      document.getElementById(key).focus;
      e.preventDefault();
      return;
    }

  }
});