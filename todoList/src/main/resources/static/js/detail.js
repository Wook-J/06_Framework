const todoNo = new URLSearchParams(location.search).get("todoNo");

// 목록으로 버튼 동작 (메인페이지로 요청)
const gotoList = document.querySelector("#goToList");
gotoList.addEventListener("click", ()=>{
  location.href ="/";   // 메인페이지 요청 (GET 방식)
});

// 완료 여부 변경 버튼
const completBtn = document.querySelector(".complete-btn");
completBtn.addEventListener("click", (e)=>{
  // 요소.dataset : data-* 속성에 저장된 값 반환
  // data-todo-no 세팅한 속성값 얻어오기
  // data-todo-no (html은 - 기준) -> dataset.todoNo (js는 카멜케이스)
  const todoNo = e.target.dataset.todoNo;

  let complete = e.target.innerText;    // 기존 완료 여부 값 얻어오기
  complete = (complete === 'Y') ? 'N' : 'Y';

  // 완료 여부 수정 요청하기 (백틱[`] 사용)
  location.href = `/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;
});

// 수정 버튼
const updateBtn = document.querySelector("#updateBtn");
updateBtn.addEventListener("click", ()=>{
  // location.search : 쿼리스트링만 얻어오기!
  // URLSearchParams : 쿼리스트링을 객체 형태(K=V 형태)로 다룰 수 있는 객체

  location.href = `/todo/update?todoNo=${todoNo}`;
});

// 삭제버튼
const deleteBtn = document.querySelector("#deleteBtn");
deleteBtn.addEventListener("click", ()=>{
  if(!confirm("정말 삭제하시겠습니까?")) return;
  location.href = `/todo/delete?todoNo=${todoNo}`;
});