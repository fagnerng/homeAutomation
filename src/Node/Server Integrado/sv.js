var express = require('express');
var crypto = require('crypto');
var app = express();

var users = [];			// Carrega todos os usuarios do sistema
var devices = [];		// Carrega todos os devices do sistema
var rootUser = false;	// Flag para indicar se o usuario logado e root
var userDevices = [];	// IDs dos Devices do Usuario atual
var userLoged = false;  // Flag para controle de acesso ao sistema (login/logout)
var selectedUser;		// Usuario selecionado no select box na tela de Gerenciamento
var userDevicesManage = [];	// IDs dos Devices da tela de Gerenciamento 
var userRegistered = ""; // Usuario que esta sendo registrado naquele momento
var registerHapenning = false; // Flag para controle de execucao de registro

app.configure(function(){
	app.use(express.static(__dirname + '/Templates/res'));
});

var autLogin = "false";
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

function isRoot(login){
	var jsonData = require('./BD/database.json');
	var length = jsonData.alldata.users.length;
	console.log("isROOT", length);
	for(i=0; i < length; i++){
		console.log("Admin",jsonData.alldata.users[i].admin);
		if (login == jsonData.alldata.users[i].login){
			if ("true" == jsonData.alldata.users[i].admin){
				return true;
			}
		}
	}
	return false;
}

function changeStatus(id,status){
	//switchPower(id,status)
	var jsonData = require('./BD/database.json');
	jsonData.alldata.devices[id].status = status;

	fs = require('fs');
	var outputFilename = './BD/database.json';
	console.log("in!");// AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa
	fs.writeFile(outputFilename, JSON.stringify(jsonData, null, 4), function(err) {
		if(err) {
		  console.log(err);
		} else {
		  console.log("JSON saved to ");
		}
	}); 
}

function createNewUser(user, deviceIDS){
	var jsonData = fillUsers();
	
	jsonData.alldata.users[jsonData.alldata.users.length] = { 
												"name": user.name, 
												"login": user.login, 
												"pass": user.pass, 
												"admin": "false", 
												"devices": [], 
												"email": user.email };
	
	saveData(jsonData);
}

function fillUsers(){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	
	for (var i=0; i < usersLen; i++) {
	    users[i] = jsonData.alldata.users[i].login;
	}
	return jsonData; 
}

function getUserDevices(login){
	var jsonData = require('./BD/database.json');
	var indice = findUserByLogin(login);
	console.log(indice); // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa
	var devicesUserLen = jsonData.alldata.users[indice].devices.length;
	userDevicesManage = jsonData.alldata.users[indice].devices;
	var devicesUser = [];
	for(var i=0; i<devicesUserLen; i++){
		devicesUser[i] = jsonData.alldata.devices[jsonData.alldata.users[indice].devices[i]].name;
	}
	return devicesUser;
}

function findUserByLogin(loginName){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length;
	
	for (var i=0; i < usersLen; i++) {
	    if (loginName == jsonData.alldata.users[i].login){
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
	    devices[i] = jsonData.alldata.devices[i].name;
	    userDevices[i] = i;
	}
}

app.get('/', function(req, res){
	res.redirect('/login');
});

app.get('/getusers', function(req, res){
	fillUsers();
	res.type('text/plain');
	res.send(users.toString());
});

app.get('/devices', function(req, res){
	var id = req.param('id');
	var status = req.param('status');
	switchPower(id,status);
});

app.get('/idDevicesUser', function(req, res){
	res.send(userDevicesManage.toString());
});

app.get('/res/home.png', function(req, res){
	res.sendfile(__dirname +"/Templates/res/home.png");
});

app.get('/crypt.js', function(req, res){
	res.sendfile(__dirname +"/Templates/crypt.js");
});

app.get('/choicedUser', function(req, res){
	res.send(choicedUser.toString());
	console.log(choicedUser.toString());
});

app.get('/getdevices', function(req, res){
	res.type('text/plain');
	res.send(devices.toString());
});

app.get('/login', function(req, res){
	
	res.sendfile(__dirname + '/Templates/login.html');
});

app.get('/allDevices', function(req, res){
	var jsonData = require('./BD/database.json');
	var devicesLen = jsonData.alldata.devices.length
	var allDevices = [];
	
	for (var i=0; i < devicesLen; i++) {
	    console.log(jsonData.alldata.devices[i].name);
	    allDevices[i] = jsonData.alldata.devices[i].name;
	}
	res.send(allDevices.toString());
});

app.get('/root', function(req, res){ // DELETAR DEPOIS AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
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
		res.redirect('/inicio.html');
	}else{
		res.sendfile(__dirname + '/Templates/inicio.html');
	}	
});

app.get('/inicio.html', function(req, res){
	if(userLoged){
		res.sendfile(__dirname + '/Templates/inicio.html');
	}else{
		res.redirect('/login');
	}	
});

app.get('/cadastro2', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/inicio.html');
	}else{
		res.sendfile(__dirname + '/Templates/cadastro2.html');
	}
});

app.get('/contato.html', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else{
		res.sendfile(__dirname + '/Templates/contato.html');
	}	
});

app.get('/cadastro.html', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/inicio.html');
	}else{
		res.sendfile(__dirname + '/Templates/cadastro.html');
	}
	
});

app.get('/usuarios.html', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/inicio.html');
	}else{
		res.sendfile(__dirname + '/Templates/usuarios.html');
	}
	
});

app.get('/dispositivos.html', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/inicio.html');
	}else{
		res.sendfile(__dirname + '/Templates/dispositivos.html');
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

//New
app.post('/deleteDevices', function(req, res){
	var devices = "";
	var usuario = req.param('user'); // O usuario que tera os dispositivos selecionados
	var dispositivos = req.param('selected'); // O id dos dispositivos a serem deletados
});

//New 
app.post('/deleteUser', function(req, res){
	var usuario = req.param('user'); // O usuario que tera os dispositivos selecionados
});

app.post('/updatedevices', function(req, res){
	if(registerHapenning){
		console.log(userRegistered);
		var index = findUserByLogin(userRegistered);	
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
	userRegistered = "";
});

app.post('/choicedUser', function(req, res){
	//TRATAR se login é valido ou nao
	login = req.param('login');
	choicedUser = getUserDevices(login);
});

app.post('/adduser', function(req, res){
	console.log("foi no post!!");
	var user={};
	if (req.param('name') != undefined && req.param('name') != ""){
		userName=true;
	}
	
	var loginValid = findUserByLogin(req.param('login'));
	if (loginValid != -1){
		console.log("Login Existente!");
	}
	var emailValid = findUserByEmail(req.param('email'));
	if (emailValid != -1){
		console.log("Email Existente!");
		//Tratar se o email ja existe
	}
		
	if (loginValid + emailValid == -2){
		registerHapenning = true;
		userRegistered = req.param('login');
		user.name = req.param('name');
		user.login = req.param('login');
		user.pass = req.param('pass');
		devs = [];
		user.email = req.param('email');
		createNewUser(user, "1");
	}
	console.log("Usuario registrado!");
});

app.post('/checkLogin', function(req, res){

	fillUsers();
	var userLogin = req.param('login');
	autenticado = false;
	var jsonData = fillUsers();
	var index = findUserByLogin(req.param('login'));
	var err = "";
	if (index == -1 ){
		autenticado = false;
	}else{
		if(jsonData.alldata.users[index].pass == req.param('pass')){
			err= 'usuario logado';
			autLogin = "true";
			autenticado = true;
		}else {
			err='senha incorreta';
			res.send("No!");
			autenticado = false;
		}
	}
	if(autenticado){
		autLogin = "true";
		userLoged = true;
		if(isRoot(userLogin)){
			rootUser = true;
			fillDevices();
		}else{
			rootUser = false;
			devices = [];
			console.log(index);
			console.log(devices);
			devicesSize = jsonData.alldata.users[index].devices.length;
			for(var i=0; i<devicesSize;i++){
				var deviceID = jsonData.alldata.users[index].devices[i];
				devices[i] = jsonData.alldata.devices[deviceID].name;
			}
			userDevices = jsonData.alldata.users[index].devices;
			console.log(devices);
		}
	}else{
		autLogin = "false";
	}
});

app.post('/takeStatus',function(req,res){
	console.log("entrou no post");
	var id = req.param('id');
	var status = req.param('status');
	console.log(userDevices[parseInt(id)]);
	changeStatus(userDevices[id],status);
});

app.listen(9000);

