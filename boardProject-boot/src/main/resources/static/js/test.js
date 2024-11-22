console.log("test.js와 연결됨");

const emailBtn = document.querySelector("#emailBtn");
const pwBtn= document.querySelector("#pwBtn");

const div1 = document.querySelector("#div1");
const div2 = document.querySelector("#div2");

emailBtn.addEventListener("click", () => {

  div2.classList.add("hidden");
  div1.classList.remove("hidden");

})


pwBtn.addEventListener("click", () => {

  div1.classList.add("hidden");
  div2.classList.remove("hidden");

})