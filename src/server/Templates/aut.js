var boxRequest = new XMLHttpRequest();

//=====================Notificação de bloqueio de acesso para usuario sem permissão=================>
var bloqRequest = new XMLHttpRequest();

bloqRequest.onreadystatechange=function() {
	if (bloqRequest.readyState==4 && bloqRequest.status==200) {
		var status = bloqRequest.responseText;
		if (status == "false"){
			alert("Pagina disponível apenas para o administrador");
		}
	}
}
    
function checaRoot(){
	bloqRequest.open("GET", "/root", false);	
  	bloqRequest.send();
}  
