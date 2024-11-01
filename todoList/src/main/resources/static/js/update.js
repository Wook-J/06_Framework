const todoNo = new URLSearchParams(location.search).get("todoNo");
const updateBtn = document.querySelector("#updateBtn");
const backBtn = document.querySelector("#backBtn");
const todoTitle = document.querySelector("#todoTitle");

updateBtn.addEventListener("click", (e)=>{
  if(todoTitle.value.trim().length == 0){
    e.preventDefault();
    alert("제목을 입력하세요");
    todoTitle.value = "";
    todoTitle.focus();
  }
});

backBtn.addEventListener("click",()=>{
  location.href = `/todo/detail?todoNo=${todoNo}`;
});