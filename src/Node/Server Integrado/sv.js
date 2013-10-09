var express = require('express');
var crypto = require('crypto');
var app = express();

var users = [];
var devices = [];
var jsonDataBase;
var rootUser = false;
var userDevices = [];
var userLoged = false;
var selectedUser;
var iDs = [];
var userRegistred = "";
var registerHapenning = false;


app.use(express.static(__dirname + '/Paginas'));

app.configure(function(){
	app.use(express.static(__dirname + '/Templates'));
	console.log(__dirname + '/Paginas/img/');
	console.log(__dirname + '/Paginas');
});

var autLogin = "";
console.log("Server Initiated");

app.use(express.methodOverride());
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

function getNextUserID(){
	var jsonData = require('./BD/database.json');
	var length = jsonData.alldata.users_length;
	return length;
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

function createNewUser(user, deviceIDS){
	console.log("entrou na func");
	var jsonData = fillUsers();
	var devs = "1"//deviceIDS.split("-"); 
	
	var idValue = getNextUserID();
	console.log(idValue);
	
	jsonData.alldata.users_length = idValue+1;
	jsonData.alldata.users[jsonData.alldata.users.length] = {	"id": idValue, 
												"name": user.name, 
												"login": user.login, 
												"pass": user.pass, 
												"admin": "false", 
												"devices": devs, 
												"email": user.email };
	
	saveData(jsonData);
}

function allDevices(){

}

function fillUsers(){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	
	for (var i=0; i < usersLen; i++) {
	    console.log(jsonData.alldata.users[i].name);
	    users[i] = jsonData.alldata.users[i].name;
	}
	
	jsonDataBase = jsonData;
	return jsonData; 
}

//Essa função é nova!
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

console.log(getUserDevices("Joao"));

function findUserByLogin(login){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	for (var i=0; i < usersLen; i++) {
	    if ( login == jsonData.alldata.users[i].name){
			return i;
		}
	}
	return -1;
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

function findUserByEmail(email){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	for (var i=0; i < usersLen; i++) {
	    if ( email == jsonData.alldata.users[i].email){
			return i;
		}
	}
	return -1;
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

app.post('/updatedevices', function(req, res){
	if(/*registerHapenning*/ true){
		var index = findUserByLogin(userRegistred);	
	}else{
		var index = findUserByLogin(req.param('login'));
	}
	var jsonData = require('./BD/database.json');
	var devs = req.param('devices').split(",");
	console.log(req.param('devices'));
	console.log("idnex", index);
	
	if (index != -1){
		jsonData.alldata.users[index].devices = devs;
		saveData(jsonData);
	}
	registerHapenning = false;
	userRegistred = "";
});

//tratar
app.post('/choicedUser', function(req, res){
	login = req.param('login');
	console.log(login+"aaaaaa");
	choicedUser = getUserDevices(login);
});

app.get('/idDevicesUser', function(req, res){
	res.send(iDs.toString());
});

app.get('/choicedUser', function(req, res){
	res.send(choicedUser.toString());
	console.log(choicedUser.toString());
});


// app.post('/checkLogin', function(req, res){
// 	fillUsers();
// 	var jsonData = fillUsers();
// 	var index = findUserByLogin (req.param('login'));
// 	console.log(req.param('pass'));
// 	var err = "";
// 	if (index ==-1 ){
// 		err= "ue";
// 	}else{
// 		validatePassword(req.param('pass'),jsonData.alldata.users[index].pass, function(e,o){
// 			if (o) {
// 				err= 'usuario logado';
// 				autLogin = "Ok!"
// 			}else {
// 				err='senha incorreta';
// 				res.send("No!");
// 			}
// 		});
// 	}
// });


app.get('/getdevices', function(req, res){
	res.type('text/plain');
	fillDevices();
	res.send(devices.toString());
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
	console.log("foi no post!!");
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
		//funcaoFind
		registerHapenning = true;
		userRegistred = req.param('name');
		user.name = req.param('name');
		user.login = req.param('login');
		user.pass = req.param('pass');
		devs = []
		user.email = req.param('email');
		createNewUser(user, "1");
	}
	console.log("Usuario registrado!");
});

app.get('/login', function(req, res){
	
	res.sendfile(__dirname + '/Templates/login.html');
});

app.get('/root', function(req, res){
	res.send(rootUser);
});

app.get('/logout', function(req, res){
	userLoged = false;
	rootUser = false;
	devices = [];
	res.redirect('/login');
});

app.get('/home', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/menu');
	}else{
		res.sendfile(__dirname + '/Templates/inicio.html');
	}
	
});

app.get('/menu', function(req, res){
	if(userLoged){
		res.sendfile(__dirname + '/Templates/inicio.html');
	}else{
		res.redirect('/login');
	}
	
});

app.get('/deviceUpdating', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/menu');
	}else{
		res.sendfile(__dirname + '/Templates/cadastro2.html');
	}
	
});

app.get('/userRegister', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/menu');
	}else{
		res.sendfile(__dirname + '/Templates/cadastro.html');
	}
	
});

app.get('/user', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/menu');
	}else{
		res.sendfile(__dirname + '/Templates/usuarios.html');
	}
	
});

app.get('/DeviceManagement', function(req, res){
		if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/menu');
	}else{
		res.sendfile(__dirname + '/Templates/dispositivos.html');
	}
	
});

app.post('/checkLogin', function(req, res){
	// var jsonData = require('./BD/database.json');
	// var usersLen = jsonData.alldata.users.length


	// if (req.param('name') != undefined && req.param('name') != ""){ //testei enviando requisicao POST a "http://localhost:9000/adduser?" e a "http://localhost:9000/adduser?name="
	// 	//~ users.push(req.param('name'));
	// 	userName=true;
	// }
	// var userLogin = req.param('name');
	// var autenticado = false;
	// var userID;
	// for(var i=0; i<usersLen;i++){
	// 	console.log(users[i] + " " + userLogin );
	// 	if(users[i] == userLogin){
	// 		console.log("Condicao");
	// 		autenticado = true;
	// 		userID = i;
	// 	}
	// }
	fillUsers();
	var userLogin = req.param('login');
	autenticado = false;
	var jsonData = fillUsers();
	var index = findUserByLogin(req.param('login'));
	console.log(req.param('login'));
	console.log(req.param('pass'));
	console.log(jsonData.alldata.users[index].senha);
	var err = "";
	if (index == -1 ){
		console.log("user not found");
		autenticado = false;
	}else{
		if(jsonData.alldata.users[index].senha == req.param('pass')){
				console.log("ai sim papai");
				err= 'usuario logado';
				autLogin = "Ok!";
				autenticado = true;
			}else {
				err='senha incorreta';
				res.send("No!");
				console.log("nopass");
				autenticado = false;
			}
		}
	console.log("entrou no post")
	console.log(autenticado)
	if(autenticado){
		autLogin = "Ok!";
		userLoged = true;
		if(userLogin == users[0]){
			rootUser = true;
			fillDevices();

		}else{
			rootUser = false;
			devices = [];
			console.log(userID);
			console.log(devices);
			devicesSize = jsonData.alldata.users[userID].devices.length;
			for(var i=0; i<devicesSize;i++){
				var deviceID = jsonData.alldata.users[userID].devices[i];
				devices[i] = jsonData.alldata.devices[deviceID].name;
			}
			userDevices =  jsonData.alldata.users[userID].devices;
			console.log(devices);
		}
	}else{
		autLogin = "No!"
	}

});

app.get('/allstatus', function(req, res){
	var status = allDevicesStatus();
	res.send(status.toString());
});

app.get('/checkLogin', function(req, res){
	fillUsers();
	res.send(autLogin);
});

app.post('/takeStatus',function(req,res){
	console.log("entrou no post");
	var id = req.param('id');
	var status = req.param('status');
	console.log(userDevices[parseInt(id)]);
	changeStatus(userDevices[id],status);
});


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

app.listen(9000);

