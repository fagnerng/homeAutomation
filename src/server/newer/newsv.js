var express = require('express');
var app = express();
var tokens = [];
var dbPath = "./jsonDB/";
var htmlPath = "/html/";
var logins = require(dbPath+'/loginDB.json');

app.configure(function(){
	app.use(express.static(__dirname + '/Templates/res'));
});

console.log("Server Initiated");

app.use(express.methodOverride());


var allowCrossDomain = function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization');	
    if ('OPTIONS' == req.method) 
      res.send(200);
    else 
      next();
};
app.use(allowCrossDomain);

/////////////////////////////////UTIL///////////////////////////////////
//funcao atualiza o banco de dados do sistema
function saveData(jsonData){
	fs = require('fs');
	var outputFilename = './BD/database.json';
	fs.writeFileSync(outputFilename, JSON.stringify(jsonData, null, 4));
}

//funcao retorna o user agente de onde a requisicao foi solicitada
function getUserAgent(headers){
	var userAgent = headers["user-agent"];
	if(userAgent.indexOf("Android")!= -1){
		return "Android";
	}else if(userAgent.indexOf("Windows")!= -1){
		return "Windows";
	}else if(userAgent.indexOf("iPhone")!= -1){
		return "iPhone";
	}else if(userAgent.indexOf("iPad")!= -1){
		return "iPad";
	}else{
		return "unknow";
	}
}

//funcao encerra sessao do usuario
function logout(user){
	if ( tokens[user]!= undefined )
		tokens[user]= undefined ;
}
//funcao inicia sessao do usuario
function login(user, pass){
	var loginDB = require(dbPath+"loginDB.json");
	if ( loginDB[user]!= undefined ){
		var userDB = require(dbPath+"usersDB.json");
		if (userDB[user].password == pass){
			 tokens[user] = 'sadihagsddsa';
			 return tokens[user], 200;
		}
		
		return 'senha invalida inexistente',101;
	}
		
	return 'usuario inexistente' , 100;
}


/////////////////////////////////UTIL///////////////////////////////////
app.get('/', function(req, res){
	
	res.redirect('/login');
	
});

app.get('/login', function(req, res){
	var login = req.param("login");
	var token = req.param("token");
	if (login != undefined &&  token != undefined )
		res.send("ja esta logado");
	else
		res.sendfile(__dirname + htmlPath+'/index.html');
});


app.post('/login', function(req, res){
	console.log("login");
	var ulogin = req.param("login");
	var upass = req.param("pass");
	var token, code = login(ulogin, upass)
	console.log(login(ulogin, upass))
	if(code == 200 ){
		res.send("logado com sucesso");
	}else{
		res.send(token);
	}
	
});
app.listen(80);

