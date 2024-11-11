console.log("main.js 연결됨");

/* 이메일 저장 클릭하여 로그인 한 후 로그아웃 한 경우 이전 이메일 남겨놓기 */
const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");

const getCookie = (key) => {
  const cookieList = document.cookie.split("; ").map(el => el.split("="));
  const obj = {};

  // cookie 배열을 JS 객체 변환
  for(let i=0; i<cookieList.length; i++){
    const k = cookieList[i][0];
    const v = cookieList[i][1];
    obj[k] = v;
  }

  return obj[key];
}

if(loginEmail != null){
  const saveId = getCookie("saveId");

  if(saveId != undefined){
    loginEmail.value = saveId;
    document.querySelector("#loginForm input[name='saveId']").checked = true;
  }
}


/* 이메일, 비밀번호 미작성 시 로그인 막기 */
const loginForm = document.querySelector("#loginForm");
const loginPw = document.querySelector("#loginForm input[name='memberPw']");

// #loginForm 이 화면에 존재할 때 (== 로그인 상태가 아닐 때)
// 타임리프 문법 따르면 로그인 시 #loginForm 요소가 존재하지 않으므로
// 로그인 상태일 때 loginForm 관련 코드 수행 시 에러 발생
if(loginForm != null){

  loginForm.addEventListener("submit", e =>{
    
    if(loginEmail.value.trim().length === 0){   // 이메일 미작성
      alert("이메일을 작성해주세요!!");
      e.preventDefault();
      loginEmail.focus();
      return;
    }

    if(loginPw.value.trim().length === 0){      // 비밀번호 미작성
      alert("비밀번호를 작성해주세요!!");
      e.preventDefault();
      loginPw.focus();
      return;
    }
  });
}

/* *************** left section *************** */
const leftBasicBtn = document.querySelector("#leftBasicBtn");
const leftBasicCatecory = document.querySelector("#leftBasicCategory");

leftBasicBtn.addEventListener("click", ()=>{
  leftBasicCatecory.classList.toggle("left-normal");
  leftBasicCatecory.classList.toggle("left-hidden");
})

const leftPlusBtn = document.querySelector("#leftPlusBtn");
const leftPlusCategory = document.querySelector("#leftPlusCategory");

leftPlusBtn.addEventListener("click", ()=>{
  leftPlusCategory.classList.toggle("left-normal");
  leftPlusCategory.classList.toggle("left-hidden");
})

const leftEtcBtn = document.querySelector("#leftEtcBtn");
const leftEtcCategory = document.querySelector("#leftEtcCategory");

leftEtcBtn.addEventListener("click", ()=>{
  leftEtcCategory.classList.toggle("left-normal");
  leftEtcCategory.classList.toggle("left-hidden");
})