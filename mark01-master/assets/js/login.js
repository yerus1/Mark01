var signup=document.getElementById("login");
signup.addEventListener("click",xrvf);

window.addEventListener('DOMContentLoaded', async(event) => {
    if(await sterfyDom()){
	window.location.href="dashboard.html";
		}
});

function xrvf(){
	if(document.getElementById("inputEmail").value.length!=0&&document.getElementById("inputPassword").value.length!=0){
		negon();
	}else{
		
	}
}
