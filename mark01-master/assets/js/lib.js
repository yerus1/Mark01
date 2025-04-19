var options={
    method:"POST",
};

async function negon(){
	document.getElementById("loading").style.display="block"
    //login
    var datas=await fetch("http://localhost:8080/app/signin?email="+document.getElementById("inputEmail").value+"&password="+document.getElementById("inputPassword").value,options);
    var result=await datas.json()
    if(datas.status==200){
    if(result["status"]=="success"){
        settingQuoteNumber()
        window.location.href="index.html"
    }else{
	document.getElementById("loading").style.display="none"
    document.getElementsByClassName("hide")[0].style.display="block";
    console.log("login failed")
    }
    }else{
        console.log("false cred");
    }
}
