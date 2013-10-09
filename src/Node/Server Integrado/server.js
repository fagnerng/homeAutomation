// var express = require('express');
// var app = express();


// var users = [];
// var devices = [];
var jsonDataBase;
var rootUser = false;
var userDevices = [];
var userLoged = false;

app.use(express.static(__dirname + '/Paginas'));

app.configure(function(){
	app.use(express.static(__dirname + '/Paginas/img'));
	console.log(__dirname + '/Paginas/img/');
	console.log(__dirname + '/Paginas');
});

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


// function createNewUser(name, deviceIDS){
// 	console.log("emtroooou2");
// 	var devs = deviceids.split("-"); 
	
// 	console.log(devs);
// 	console.log(name);
	
// 	jsonDataBase.alldata.users[users.length] = {"name": name, "admin": "false", "devices": devs};
	
	
// 	fs = require('fs');
// 	var outputFilename = './BD/database.json';

// 	fs.writeFile(outputFilename, JSON.stringify(jsonDataBase, null, 4), function(err) {
// 		if(err) {
// 		  console.log(err);
// 		} else {
// 		  console.log("JSON saved to ");
// 		}
// 	}); 
	
// 	console.log(jsonDataBase);
// }

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

//diferente!
function fillUsers(){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	
	for (var i=0; i < usersLen; i++) {
	    console.log(jsonData.alldata.users[i].name);
	    users[i] = jsonData.alldata.users[i].name;
	}
	
	jsonDataBase = jsonData; 
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

function fillDevices(){
	
	var jsonData = require('./BD/database.json');
	var devicesLen = jsonData.alldata.devices.length
	
	for (var i=0; i < devicesLen; i++) {
	    console.log(jsonData.alldata.devices[i].name);
	    devices[i] = jsonData.alldata.devices[i].name;
	    userDevices[i] = i;
	}
}

app.get('/login', function(req, res){
	
	res.sendfile(__dirname + '/Paginas/login.html');


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
		res.sendfile(__dirname + '/Paginas/Home.html');
	}
	
});

app.get('/menu', function(req, res){
	if(userLoged){
		res.sendfile(__dirname + '/Paginas/menu.html');
	}else{
		res.redirect('/login');
	}
	
});

app.get('/userRegister', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/menu');
	}else{
		res.sendfile(__dirname + '/Paginas/userRegister.html');
	}
	
});

app.get('/user', function(req, res){
	if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/menu');
	}else{
		res.sendfile(__dirname + '/Paginas/Users.html');
	}
	
});

app.get('/DeviceManagement', function(req, res){
		if(!(userLoged)){
		res.redirect('/login');
	}else if (!rootUser){
		res.redirect('/menu');
	}else{
		res.sendfile(__dirname + '/Paginas/DeviceManagement.html');
	}
	
});

app.post('/checkLogin', function(req, res){
	fillUsers();
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length


	if (req.param('name') != undefined && req.param('name') != ""){ //testei enviando requisicao POST a "http://localhost:9000/adduser?" e a "http://localhost:9000/adduser?name="
		//~ users.push(req.param('name'));
		userName=true;
	}
	var userLogin = req.param('name');
	var autenticado = false;
	var userID;
	for(var i=0; i<usersLen;i++){
		console.log(users[i] + " " + userLogin );
		if(users[i] == userLogin){
			console.log("Condicao");
			autenticado = true;
			userID = i;
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
			devicesSize = jsonData.alldata.users[userID].devices.length;
			for(var i=0; i<devicesSize;i++){
				var deviceID = jsonData.alldata.users[userID].devices[i];
				devices[i] = jsonData.alldata.devices[deviceID].name;
			}
			 userDevices =  jsonData.alldata.users[userID].devices;
			console.log(devices);
		}
	}else{
		autLogin = "No"
	}

});

app.get('/checkLogin', function(req, res){
	fillUsers();
	res.send(autLogin);
});


app.get('/getusers', function(req, res){
	res.type('text/plain');
	
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	
	for (var i=0; i < usersLen; i++) {
	    console.log(jsonData.alldata.users[i].name);
	    users[i] = jsonData.alldata.users[i].name;
	}
	
	jsonDataBase = jsonData; //updating the jsonText pra poder altera-lo adicionando novos dados
	//~ res.send(jsonData);
	res.send(users.toString());
});

app.get('/allstatus', function(req, res){
	var status = allDevicesStatus();
	res.send(status.toString());
});

app.get('/getdevices', function(req, res){
	res.type('text/plain');
	res.send(devices.toString());
});

app.post('/takeStatus',function(req,res){
	console.log("entrou no post");
	var id = req.param('id');
	var status = req.param('status');
	console.log(userDevices[parseInt(id)]);
	changeStatus(userDevices[id],status);
})


app.post('/adduser', function(req, res){
	var userName = false;
	var userDevice = false;
	
	if (req.param('name') != undefined && req.param('name') != ""){ //testei enviando requisicao POST a "http://localhost:9000/adduser?" e a "http://localhost:9000/adduser?name="
		//~ users.push(req.param('name'));
		userName=true;
	}
	if (req.param('devices') != undefined && req.param('devices') != ""){
		devices.push(req.param('devices'));
		userDevice=true;
	}
	
	if (userName && userDevice){
		console.log("emtroooou1");
		createNewUser(req.param('name'), req.param('devices'));
	}
});

var http = require('http');
var myPath =  '/control.html?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id=0&status=on';

//The url we want is: 'www.random.org/integers/?num=1&min=1&max=10&col=1&base=10&format=plain&rnd=new'
var getOptions = {
  host: 'arduino.com.br',
  port: '5000',
  path: '/control.html'
};

callback = function(response) {
  var str = '';

  //another chunk of data has been recieved, so append it to `str`
  response.on('data', function (chunk) {
    str += chunk;
  });

  //the whole response has been recieved, so we just print it out here
  response.on('end', function () {
    ///str é a referencia para string de retorno do arduino
    console.log(str);
  });
}

function switchPower(id, status){
  console.log("dispositivo "+ id + " status : "+status);
  myPath =  '/control.html?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id='+id+'&status='+status;
  var postOptions = {
  host: 'arduino.com.br',
  path: myPath,
  port: '5000',
  method: 'POST'
}
};

app.listen(9000);

