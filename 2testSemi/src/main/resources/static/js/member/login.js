console.log("login.js와 연결됨");
const loginForm = document.querySelector("#loginForm");
const loginEmail = document.querySelector("input[name='memberEmail']");
const loginPw = document.querySelector("input[name='memberPw']");

loginForm.addEventListener("submit", e => {
  if(loginEmail.value.trim().length === 0){
    alert("이메일을 작성해 주세요!!");
    e.preventDefault();
    loginEmail.focus();
    return;
  }

  if(loginPw.value.trim().length === 0){
    alert("비밀번호를 작성해 주세요!!");
    e.preventDefault();
    loginPw.focus();
    return;
  }
});

/* ********** 쿠키(이메일 저장) 활용 ********** */
const getCookie = (key) => {
  const cookies = document.cookie;
  const cookiArray = cookies.split("; ").map(el => el.split("="));
  const obj = {};

  for(let i=0; i<cookiArray.length; i++){
    const k = cookiArray[i][0];
    const v = cookiArray[i][1];
    obj[k] = v;
  }

  return obj[key];
}

const saveId = getCookie("saveId");

if(saveId != undefined){
  loginEmail.value = saveId;
  document.querySelector("input[name='saveId']").checked = true;
}