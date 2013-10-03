var express = require('express');
var app = express();

var users = [];
var devices = [];
var jsonDataBase;

var autLogin = ""
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

function fillUsers(){
	var jsonData = require('./BD/database.json');
	var usersLen = jsonData.alldata.users.length
	
	for (var i=0; i < usersLen; i++) {
	    console.log(jsonData.alldata.users[i].name);
	    users[i] = jsonData.alldata.users[i].name;
	}
	
	jsonDataBase = jsonData; 
}


app.get('/getusers', function(req, res){
	fillUsers();
	res.type('text/plain');
	
	//updating the jsonText pra poder altera-lo adicionando novos dados
	//~ res.send(jsonData);
	res.send(users.toString());
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

app.get('/devices', function(req, res){
	var id = req.param('id');
	var status = req.param('status');
	switchPower(id,status);
});


app.get('/checkLogin', function(req, res){
	fillUsers();
	res.send(autLogin);
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
	
	if (userName){
		console.log("emtroooou1");
		createNewUser(req.param('name'), "1");
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

var postOptions = {
  host: 'arduino.com.br',
  path: myPath,
  port: '5000',
  method: 'POST'
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
http.request(getOptions, callback).end();



//
//
function switchPower(id, status){
  console.log("dispositivo "+ id + " status : "+status);
  myPath =  '/control.html?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id='+id+'&status='+status;
  var postOptions = {
  host: '192.168.2.28',
  path: myPath,
  port: '3000',
  method: 'POST'
};

console.log("ligar disposito 00");
http.request(postOptions, callback).end();
};

app.listen(9000);

