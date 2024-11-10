console.log("main.js 연결됨");

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