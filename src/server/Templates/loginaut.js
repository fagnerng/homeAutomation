var loginRequest = new XMLHttpRequest();

loginRequest.onreadystatechange=function() {
  	if (loginRequest.readyState==4 && loginRequest.status==200) {
	    var response = loginRequest.responseText;
	    if (response == "false"){
	    	window.location = "/login";
	    }

		}
}

function checklog(){
	loginRequest.open("GET", "/loginStatus", false);
	loginRequest.send();
}
