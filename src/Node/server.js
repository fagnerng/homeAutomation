var express = require('express');
var crypto = require('crypto');
var app = express();

var users = [];
var devices = [];
var rootUser = false;
var userDevices = [];
var userLoged = false;
var selectedUser;
var iDs = [];


var autLogin = ""
console.log("Server Initiated");

app.use(express.methodOverride());
app.configure(function(){
	app.use(express.static(__dirname + '/Paginas/img'));
	console.log(__dirname + '/Paginas/img/');
	console.log(__dirname + '/Paginas');
});

var allowCrossDomain = function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization');	
    if ('OPTIONS' == req.method) 
      res.send(200);
    else 
      next();
};
app.use(allowCrossDomain);

function saveData(jsonData){
	fs = require('fs');
	var outputFilename = './BD/database.json';

	fs.writeFile(outputFilename, JSON.stringify(jsonData, null, 4), function(err) {
		if(err) {
		  console.log(err);
		} else {
		  console.log("JSON saved to ");
		}
	}); 	
}
function changeStatus(id,status){
	var jsonData = require('./BD/database.json');
	jsonDataBase = jsonData;
	jsonDataBase.alldata.devices[id].status = status;

	fs = require('fs');
	var outputFilename = './BD/database.json';
	console.log("in!");
	fs.writeFile(outputFilename, JSON.stringify(jsonDataBase, null, 4), function(err) {
		if(err) {
		  console.log(err);
		} else {
		  console.log("JSON saved to ");
		}
	}); 
}

function removeUserByLogin(login){ // Trata para não remover o root (admin=true)
	var jsonData = require('./BD/database.json');	
	var index = findUserByLogin(login);
	var usersBAK = jsonData.alldata.users;
	jsonData.alldata.users = []
	for ( i = 0;i<usersBAK.length;i++){
		if( i!= index){
			jsonData.alldata.users[i] = usersBAK[i];
		}
	}
	saveData(jsonData);
}

function createNewUser(user){ //Adicionar os devices que ele vai ter
	var jsonData = fillUsers();
	
	jsonData.alldata.users[jsonData.alldata.users.length] = {
												"name": user.name, 
												"login": user.login, 
												"pass": user.pass, 
												"admin": "false", 
												"devices": user.devs, 
												"email": user.email };
	
	saveData(jsonData);
}

function fillUsers(){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	
	for (var i=0; i < usersLen; i++) {
		if (jsonData.alldata.users[i] != null){
			users[i] = jsonData.alldata.users[i].name;
		}
	}
	
	return jsonData; 
}

function fillDevices(){
	
	var jsonData = require('./BD/database.json');
	var devicesLen = jsonData.alldata.devices.length
	
	for (var i=0; i < devicesLen; i++) {
	    console.log(jsonData.alldata.devices[i].name);
	    devices[i] = jsonData.alldata.devices[i].name;
	    userDevices[i] = i;
	}
}
function findUserByLogin(login){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	for (var i=0; i < usersLen; i++) {
		if (jsonData.alldata.users[i] != null){
			if ( login == jsonData.alldata.users[i].login){
				return i;
			}
		}
	}
	return -1;
}

function findUserByEmail(email){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	for (var i=0; i < usersLen; i++) {
		if (jsonData.alldata.users[i] != null){
			if ( email == jsonData.alldata.users[i].email){
				return i;
			}
		}
	}
	return -1;
}

function getUserDevices(login){
	var jsonData = require('./BD/database.json');
	var indice = findUserByLogin(login);
	console.log(indice);
	var devicesUserLen = jsonData.alldata.users[indice].devices.length;
	iDs = jsonData.alldata.users[indice].devices;
	var devicesUser = [];
	for(var i=0; i<devicesUserLen; i++){
		devicesUser[i] =  jsonData.alldata.devices[jsonData.alldata.users[indice].devices[i]].name;
	}
	return devicesUser;
}

function allDevicesStatus(){
	var devicesStatus = []
	var jsonData = require('./BD/database.json');
	var DevicesLen = jsonData.alldata.devices.length;

	for (var i=0; i < userDevices.length; i++) {
	    console.log(jsonData.alldata.devices[userDevices[i]].name);
	    devicesStatus[i] = jsonData.alldata.devices[userDevices[i]].status;
	}

	return devicesStatus;
}

function switchPower(id, status){
  var http = require('http');
  console.log("dispositivo "+ id + " status : "+status);
  var myPath =  '/control.html?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id='+id+'&status='+status;
  var postOptions = {
  host: '192.168.2.28',
  path: myPath,
  port: '3000',
  method: 'POST'
	};

	console.log("ligar disposito 00");
	http.request(postOptions, console.log).end();
};



app.delete('/removeuser', function(req, res){
	removeUserByLogin(req.param('login'));
});

app.get('/getusers', function(req, res){
	fillUsers();
	res.type('text/plain');
	
	//updating the jsonText pra poder altera-lo adicionando novos dados
	//~ res.send(jsonData);
	res.send(users.toString());
});

app.get('/devices', function(req, res){
	var id = req.param('id');
	var status = req.param('status');
	switchPower(id,status);
});


app.get('/checkLogin', function(req, res){
	var jsonData = fillUsers();
	var index = findUserByLogin (req.param('login'));
	var err = "";
	if (index ==-1 ){
		err= 'usuario inexistente';
	}else{
		validatePassword(req.param('pass'),jsonData.alldata.users[index].pass, function(e,o){
			if (o) {
				err= 'usuario logado';
			}else {
				err='senha incorreta';
			}
		});
	}
	res.send(err);
});


app.get('/getdevices', function(req, res){
	res.type('text/plain');	
	var JsonDevices = []
	var jsonData = require('./BD/database.json');
	var DevicesLen = jsonData.alldata.devices.length;


	for (var i=0; i < DevicesLen; i++) {
	    console.log(jsonData.alldata.devices[i].name);
	    JsonDevices[i] = jsonData.alldata.devices[i].name;
	}
	res.send(JsonDevices.toString());
});

//New
app.post('/deleteDevices', function(req, res){
	var devices = "";
	var usuario = req.param('user'); // O usuario que tera os dispositivos selecionados
	var dispositivos = req.param('selected'); // O id dos dispositivos a serem deletados
});

//New 
app.post('/deleteUser', function(req, res){
	console.log(req.param('user'));
	var usuario = req.param('user'); // O usuario que tera os dispositivos selecionados
});


app.post('/adduser', function(req, res){
	var user={};
	if (req.param('name') != undefined && req.param('name') != ""){ //testei enviando requisicao POST a "http://localhost:9000/adduser?" e a "http://localhost:9000/adduser?name="
		userName=true;
	}
	
	var loginValid = findUserByLogin(req.param('login'));
	if (loginValid != -1){
		console.log("Login Existente!");
	}
	var emailValid = findUserByEmail(req.param('email'));
	if (emailValid != -1){
		console.log("Email Existente!");
		
	}
		
	if (loginValid + emailValid == -2){
		user.name = req.param('name');
		user.login = req.param('login');
		user.pass = req.param('pass');
		user.devs = req.param('devices').split("-");
		user.email = req.param('email');
		createNewUser(user);
	}
});

app.post('/checkLogin', function(req, res){
	var jsonData = fillUsers();
	var usersLen = jsonData.alldata.users.length

	if (req.param('name') != undefined && req.param('name') != ""){ //testei enviando requisicao POST a "http://localhost:9000/adduser?" e a "http://localhost:9000/adduser?name="
		//~ users.push(req.param('name'));
		userName=true;
	}
	var userLogin = req.param('name');
	var autenticado = false;
	for(var i=0; i<usersLen;i++){
		console.log(users[i] + " " + userLogin );
		if(users[i] == userLogin){
			console.log("Condicao");
			autenticado = true;
		}
	}
	console.log("entrou no post")
	console.log(autenticado)
	if(autenticado){
		autLogin = "Ok!";
	}else{
		autLogin = "No"
	}
});

app.post('/updatedevices', function(req, res){
	var index = findUserByLogin(req.param('login'));
	var jsonData = require('./BD/database.json');
	var devs = req.param('devices').split("-");
	console.log(req.param('devices'));
	console.log("idnex", index);
	
	if (index != -1){
		jsonData.alldata.users[index].devices = devs;
		saveData(jsonData);
	}
});

app.post('/deleteDevices', function(req, res){
	var devices = "";
	var usuario = req.param('user'); // O usuario que tera os dispositivos selecionados
	var dispositivos = req.param('selected'); // O id dos dispositivos a serem deletados
});


app.post('/deleteUser', function(req, res){
	console.log(req.param('user'));
	var usuario = req.param('user'); // O usuario que tera os dispositivos selecionados
});


app.post('/choicedUser', function(req, res){
	login = req.param('login');
	console.log(login+"aaaaaa");
	choicedUser = getUserDevices(login);
});

app.listen(9000);

