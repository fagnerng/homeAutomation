var express = require('express');
var app = express();

var users = [];
var devices = [];
var jsonDataBase;

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


function createNewUser(name, deviceIDS){
	console.log("emtroooou2");
	var devs = deviceIDS.split("-"); 
	
	console.log(devs);
	console.log(name);
	
	jsonDataBase.alldata.users[users.length] = {"name": name, "admin": "false", "devices": devs};
	
	
	fs = require('fs');
	var outputFilename = './BD/database.json';

	fs.writeFile(outputFilename, JSON.stringify(jsonDataBase, null, 4), function(err) {
		if(err) {
		  console.log(err);
		} else {
		  console.log("JSON saved to ");
		}
	}); 
	
	console.log(jsonDataBase);
}


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

app.get('/getdevices', function(req, res){
	res.type('text/plain');
	
	var jsonData = require('./BD/database.json');
	var devicesLen = jsonData.alldata.devices.length
	
	for (var i=0; i < devicesLen; i++) {
	    console.log(jsonData.alldata.devices[i].name);
	    devices[i] = jsonData.alldata.devices[i].name;
	}
	
	//~ res.send(jsonData);
	res.send(devices.toString());
});

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

app.listen(9000);

