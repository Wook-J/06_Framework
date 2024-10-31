const addBtn = document.querySelector("#addBtn");
const title = document.querySelector("[name=todoTitle]");

addBtn.addEventListener("click", (e)=>{
  if(title.value.trim().length === 0){
    e.preventDefault();
    alert("제목을 입력하세요");
    title.focus();
    title.value = "";
  }
})