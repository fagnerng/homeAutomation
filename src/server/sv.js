var express = require('express');
var crypto = require('crypto');
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var xmlhttp = new XMLHttpRequest();
var xmlhttp2 = new XMLHttpRequest();
var response = [];
var app = express();
var host = "192.168.2.28";
var port = "3000";

var users = [];			// Carrega todos os usuarios do sistema
var devices = [];		// Carrega todos os devices do sistema
var rootUser = false;	// Flag para indicar se o usuario logado e root
var userDevices = [];	// IDs dos Devices do Usuario atual
var userLoged = false;  // Flag para controle de acesso ao sistema (login/logout)
var selectedUser;		// Usuario selecionado no select box na tela de Gerenciamento
var userDevicesManage = [];	// IDs dos Devices da tela de Gerenciamento 
var userRegistered = ""; // Usuario que esta sendo registrado naquele momento
var registerHapenning = false; // Flag para controle de execucao de registro
var callback;
var deviceTimes = []; // Lista que guarda o setInterval de cada timer
var deviceFormattedTimes = []; // Lista que guarda o tempo formatado
var UserAlreadyExists = false; //Flag que indica se o usuario a ser cadastrado já existe
var IdLoggedUser; //Id do usuario que esta logado atualmente
var PasswordChangeStatus = false // Flag que indica se a troca de senha poderá ser realizada com sucesso

app.configure(function(){
	app.use(express.static(__dirname + '/Templates/res'));
});

var autLogin = "false";
console.log("Server Initiated");

app.use(express.methodOverride());

xmlhttp.onreadystatechange=function() {

	if (xmlhttp.status==200 && xmlhttp.readyState == 4){
		try{
			response = JSON.parse(xmlhttp.responseText);
			var jsonData = require('./BD/database.json');
			var devicesLen = jsonData.alldata.devices.length;
			for (var i=0; i < devicesLen; i++) {
				jsonData.alldata.devices[i].status = response.devices[i].status;
				
			}
			saveData(jsonData);
		}catch(e){
			console.log(e);
		}
	}
}

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

function ChangeUserPassword(id,oldPass,newPass){
	var jsonData = require('./BD/database.json');	
	var usersBAK = jsonData.alldata.users;
	console.log(oldPass);
	console.log(jsonData.alldata.users[IdLoggedUser].pass);
	if(jsonData.alldata.users[IdLoggedUser].pass == oldPass){
		jsonData.alldata.users[IdLoggedUser].pass = newPass;
		console.log("TRUEEEEE");
		PasswordChangeStatus = true;
		saveData(jsonData);
		return true
	}else{
		console.log("falseeeeeeee");
		PasswordChangeStatus = false;
		return false;
	}

}

function getLinkDefault(){
	return "http://"+host+":"+port+"/control";
}

function switchPower(id, status){
	try{
		xmlhttp2.open("POST", getLinkDefault()+'?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id='+id+'&status='+status, true);
		xmlhttp2.send();
	}catch(e){
		//~ console.log(e);
	}
};

setTimeout(getPowerStatus, 1000);



function getPowerStatus(){
	try{
		xmlhttp.open("GET", getLinkDefault(), true);
		//~ xmlhttp.open("GET", "http://arduino.com.br:3000/control", true);

		xmlhttp.send();
	}catch(e){
		//~ console.log(e);
		/////////////////////////////////////////////////////o arduino nao foi encontrado
	}
	if(userLoged){setTimeout(getPowerStatus, 1000);}
	else{setTimeout(getPowerStatus, 30000);}
}

function removeUserByID(id){ // Trata para não remover o root (admin==true)
	var jsonData = require('./BD/database.json');	
	var usersBAK = jsonData.alldata.users;
	var idposition = 0;
	jsonData.alldata.users = []
	if(usersBAK[id].admin != "true"){
		for ( i = 0;i<usersBAK.length;i++){
			if( i!= id ){
				jsonData.alldata.users[idposition] = usersBAK[i];
				idposition++;
			}
		}
		saveData(jsonData);
	}
}

function saveData(jsonData){
	fs = require('fs');
	var outputFilename = './BD/database.json';
	fs.writeFileSync(outputFilename, JSON.stringify(jsonData, null, 4));
}

function isRoot(login){
	var jsonData = require('./BD/database.json');
	var length = jsonData.alldata.users.length;
	for(i=0; i < length; i++){
		if (login == jsonData.alldata.users[i].login){
			if ("true" == jsonData.alldata.users[i].admin){
				return true;
			}
		}
	}
	return false;
}

//TRATAR
function getUserLoginbyID(id){
	var jsonData = require('./BD/database.json');
	return jsonData.alldata.users[id].login;
}

function changeStatus(id,status){
	switchPower(id,status)
	var jsonData = require('./BD/database.json');
	jsonData.alldata.devices[id].status = status;
	saveData(jsonData);
}

function createNewUser(user, deviceIDS){
	var jsonData = fillUsers();
	
	jsonData.alldata.users[jsonData.alldata.users.length] = { 
												"name": user.name, 
												"login": user.login, 
												"pass": user.pass, 
												"admin": "false", 
												"devices": [0], 
												"email": user.email };
	
	saveData(jsonData);
}

function fillUsers(){
	users = [];
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
	var devicesStatus = [];
	var jsonData = require('./BD/database.json');
	for (var i=0; i < userDevices.length; i++) {
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

function refillTime(){	
	var jsonData = require('./BD/database.json');
	var devicesLen = jsonData.alldata.devices.length
	
	for (var i=0; i < devicesLen; i++) {
	    jsonData.alldata.devices[i].timer = false;
	}

	saveData(jsonData);
}

refillTime();


function fillDevices(){	
	var jsonData = require('./BD/database.json');
	var devicesLen = jsonData.alldata.devices.length
	
	for (var i=0; i < devicesLen; i++) {
	    devices[i] = jsonData.alldata.devices[i].name;
	    userDevices[i] = i;
	    
	    if (deviceFormattedTimes[i] == undefined)
			deviceFormattedTimes[i] = "0:00";
	}
}

function activateTimer(idDevices, seconds){
	var jsonData = require('./BD/database.json');
	
	if (jsonData.alldata.devices[idDevices].timer){
		clearTimeout(deviceTimes[idDevices]);
	}
	
	if (seconds > 0){
		jsonData.alldata.devices[idDevices].timer = true;
		deviceTimes[idDevices] = setTimeout( function(aidDevices){
					var jsonData = require('./BD/database.json');
					deviceFormattedTimes[idDevices] = "0:00";
					if (jsonData.alldata.devices[aidDevices].timer){
						switchPower(aidDevices, "off");
						jsonData.alldata.devices[aidDevices].timer = false;
					}
					//~ }, seconds * 60000, idDevices); // Minutes
					}, seconds * 1000, idDevices); // Seconds
		saveData(jsonData);
	}else{
		jsonData.alldata.devices[idDevices].timer = false;
	}
}

function endTimer(sec, id){
	
	var d = new Date();
	var mins = d.getMinutes();
	var hora = d.getHours();
	mins = (mins + sec)
	if (mins > 59){
		hora = parseInt(hora + mins/60);
		mins = mins%60;
	}if (mins <10){
		deviceFormattedTimes[id] = hora + ":0" + mins;
	}else{
		deviceFormattedTimes[id] = hora + ":" + mins;
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
	registerHapenning = false;
	userRegistered = "";
	var id = req.param('id');
	var status = req.param('status');
	switchPower(id,status);
});

app.get('/idDevicesUser', function(req, res){
	res.send(userDevicesManage.toString());
});

app.get('/loginStatus', function(req, res){
	res.send(userLoged);
});


app.get('/statusdev', function(req, res){
	getPowerStatus('0',function (e){
		res.send(e);
	});
	switchPower('0','on', function (e){
	});
});

app.get('/res/home.png', function(req, res){
	registerHapenning = false;
	userRegistered = "";
	res.sendfile(__dirname +"/Templates/res/home.png");
});

app.get('/room.jpg', function(req, res){
	res.sendfile(__dirname +"/Templates/res/room.jpg");
});
app.get('/res/seta.jpg', function(req, res){
	res.sendfile(__dirname +"/Templates/res/seta.jpg");
});
app.get('/res/seta2.jpg', function(req, res){
	res.sendfile(__dirname +"/Templates/res/seta2.jpg");
});

app.get('/crypt.js', function(req, res){
	res.sendfile(__dirname +"/Templates/crypt.js");
});

app.get('/Manual.pdf', function(req, res){
	res.sendfile(__dirname +"/Templates/Manual.pdf");
});

app.get('/aut.js', function(req, res){
	res.sendfile(__dirname +"/Templates/aut.js");
});

app.get('/loginaut.js', function(req, res){
	res.sendfile(__dirname +"/Templates/loginaut.js");
});

app.get('/choicedUser', function(req, res){
	res.send(choicedUser.toString());
});

app.get('/logedUserDevices', function(req, res){
	res.send(userDevices.toString());
});

app.get('/times', function(req, res){
	res.send(deviceFormattedTimes.toString());
});

app.get('/getdevices', function(req, res){
	res.type('text/plain');
	res.send(devices.toString());
});

app.get('/login', function(req, res){
	if(userLoged){
		res.redirect('/inicio.html');
	}else{
		res.sendfile(__dirname + '/Templates/login.html');
	}
	
});

app.get('/allDevices', function(req, res){
	var jsonData = require('./BD/database.json');
	var devicesLen = jsonData.alldata.devices.length
	var allDevices = [];
	
	for (var i=0; i < devicesLen; i++) {
	    allDevices[i] = jsonData.alldata.devices[i].name;
	}
	res.send(allDevices.toString());
});

app.get('/root', function(req, res){
	res.send(rootUser);
});

app.get('/logout', function(req, res){
	userDevices = [];
	userLoged = false;
	rootUser = false;
	devices = [];
	res.redirect('/login');
});

app.get('/home', function(req, res){
	registerHapenning = false;
	userRegistered = "";
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/inicio.html');
	}else{
		res.sendfile(__dirname + '/Templates/inicio.html');
	}	
});

app.get('/muser', function(req, res){
	var jsonData = require('./BD/database.json');
	var musers = jsonData.alldata.users;
	var response = [];
	for (i = 0; i < musers.length;i++){
		if (musers[i].admin == "false") {
			var tempUser  = {}
			tempUser.name = musers[i].name;
			tempUser.login = musers[i].login;
			tempUser.devices = musers[i].devices;
			response[response.length] = tempUser;
			console.log(tempUser);
		}
	}

	res.send(response);
		
});

app.get('/inicio.html', function(req, res){
	registerHapenning = false;
	userRegistered = "";
	if(userLoged){
		res.sendfile(__dirname + '/Templates/inicio.html');
	}else{
		res.redirect('/login');
	}	
});


app.get('/changePass.html', function(req, res){
	registerHapenning = false;
	userRegistered = "";
	if(userLoged){
		res.sendfile(__dirname + '/Templates/changePass.html');
	}else{
		res.redirect('/login');
	}	
});

app.get('/cadastro2', function(req, res){
	//UserAlreadyExists = false;
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

app.get('/ajuda', function(req, res){
	res.sendfile(__dirname + '/Templates/Manual.pdf');
});

app.get('/reg', function(req, res){
	res.send(registerHapenning);
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
	registerHapenning = false;
	userRegistered = "";
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/inicio.html');
	}else{
		res.sendfile(__dirname + '/Templates/usuarios.html');
	}
	
});

app.get('/dispositivos.html', function(req, res){
	registerHapenning = false;
	userRegistered = "";
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
	//getPowerStatus();
	res.send(status.toString());
	res.end;
});

app.get('/checkUser', function(req, res){
	res.send(UserAlreadyExists.toString());
	//UserAlreadyExists = false;
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
	var usuarios = req.param('users').split(",");
	if (usuarios[0] != ""){
		for (var i=usuarios.length-1; i> -1; i--){
		removeUserByID(usuarios[i]);
		}
	}	
});

app.post('/updatedevices', function(req, res){
	if(registerHapenning){
		var index = findUserByLogin(userRegistered);	
	}else{
		var index = findUserByLogin(req.param('login'));
	}
	var jsonData = require('./BD/database.json');
	console.log(req.param('devices'));
	if (req.param('devices') == ""){
		var devs = [];
	}else{
		var devs = req.param('devices').split(",");
	}	
	
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

app.post('/changePassword', function(req, res){
	//TRATAR se login é valido ou nao
	oldPass = req.param('oldPass');
	newPass = req.param('newPass');
	if (ChangeUserPassword(IdLoggedUser,oldPass,newPass)){
		res.send("Senha modificada com sucesso!");
	}else{
		res.send("A senha atual está incorreta");
	}
});


//Por padrão, se um regitro for feito sem especificar os dispositivos, o dispositivo 0 será atribuido
//Aquele usuário.
app.post('/adduser', function(req, res){
	var user={};
	if (req.param('name') != undefined && req.param('name') != ""){
		userName=true;
	}
	
	var loginValid = findUserByLogin(req.param('login'));
	if (loginValid != -1){
		UserAlreadyExists = true;
	}else{
		UserAlreadyExists = false;
	}

	if (loginValid == -1){
		registerHapenning = true;
		userRegistered = req.param('login');
		user.name = req.param('name');
		user.login = req.param('login');
		user.pass = req.param('pass');
		devs = [];
		user.email = req.param('email');
		createNewUser(user, "1");
	}
});

app.post('/checkLogin', function(req, res){

	fillUsers();
	var userLogin = req.param('login');
	autenticado = false;
	var jsonData = fillUsers();
	var index = findUserByLogin(req.param('login'));
	IdLoggedUser = index;
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
		userDevices = [];
		autLogin = "true";
		userLoged = true;
		getPowerStatus();
		if(isRoot(userLogin)){
			rootUser = true;
			fillDevices();
		}else{
			rootUser = false;
			devices = [];
			devicesSize = jsonData.alldata.users[index].devices.length;
			for(var i=0; i<devicesSize;i++){
				var deviceID = jsonData.alldata.users[index].devices[i];
				devices[i] = jsonData.alldata.devices[deviceID].name;
			}
			userDevices = jsonData.alldata.users[index].devices;

		}
	}else{
		autLogin = "false";
	}
});

app.post('/takeStatus',function(req,res){
	var id = req.param('id');
	var status = req.param('status');
	var secs = parseInt(req.param('timer'))/**60*/ ;
	
	changeStatus(userDevices[id],status);
	
	if (secs > 0 && status == "on"){
		activateTimer(id, secs);
		endTimer(secs, userDevices[id]);
	}
	
});

app.listen(9000);

