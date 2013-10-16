var loginRequest = new XMLHttpRequest();

loginRequest.onreadystatechange=function() {
  	if (loginRequest.readyState==4 && loginRequest.status==200) {
	    var response = loginRequest.responseText;
	    if (response == "false"){
	    	window.location = "http://localhost:9000/login";
	    }

		}
}

function checklog(){
	loginRequest.open("GET", "http://localhost:9000/loginStatus", false);
	loginRequest.send();
}
