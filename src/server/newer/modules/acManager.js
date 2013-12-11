var crypto 		= require('crypto');
var dbPath = "../jsonDB/";
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

var tokens = {};
tokens.fagnerng = {};
tokens.fagnerng.token= 'abc';
tokens.fagnerng.house= 'house_000';
tokens.fagnerng.admin= true;
//login by user and pass from cookies
exports.autoLogin = function(user, pass, callback)
{
	findByTable(user,'users', function(o) {
		if (o != undefined){
			o.pass == pass ? getOneUserByLogin(o['user'],callback) : callback(null);
		}	else{
			callback(null);
		}
	});
}
//login by user and pass inputed manualy
exports.manualLogin = function(user, pass, callback)
{
	getOneUserByLogin(user, function(e, o) {
		if (o == null){
			callback({err:'user-not-found'});
		}	else{
			validatePassword(pass, o.pass, function(err, res) {
				if (res){
					callback(null,o);
				}	else{
					callback({err:'invalid-password'});
				}
			});
		}
	});
}

exports.upUser = function(body, callback)
{
	if(body.user == undefined || body.token == undefined){
		callback({err:'missing-parameters'});
	}else{
		getOneUserByLogin(body.user,function(e,o){
			if (o != null){
				validateToken(body.user, body.token, function(err, res) {
					if (res){
						if (tokens[body.user].admin){
							saveGeoPosition(body.long, body.lati, o.house);
						}
						body.token = undefined;
						body.devices = undefined;
						body.house = undefined;
						if (body.pass){
							saltAndHash(body.pass, function(hash){
								body.pass = hash;
							});									
						}
						for (i in body){
							if (o[i] !== undefined && body[i] !== undefined){
									o[i] = body[i];
							}
	
						}
						if (tokens[body.user].admin){
							insertUser(	
							{   name: o.name,
								email: o.email,
								child: o.user,
								pass: o.pass,
								house: o.house
							});	
						}else{
							var backDev = []
							for (var a = 0;a < o.devices.length;a++){
								backDev[backDev.length] = o.devices[a].id
							}
							
							
								insertUser(	
								{   name: o.name,
									email: o.email,
									child: o.user,
									pass: o.pass,
									house: o.house,
									devices: backDev
								});	
						}
					
						callback(null,o);
						
					}else{
						callback(err);
					}
				});
		
		
		
			}else{
				callback(e);
			}	
		
		});
	}
}

exports.getUser = function(user, token, callback)
{
	if(user != undefined ){
		if (token != undefined){
			validateToken(user, token, function(err, res) {
					if (res != undefined && res == true){
							getOneUserByLogin(user,callback);
					}else{
						callback(err);
					}
			});
		}
	}else {
			callback({err:'missing-parameters'});
	}
}
exports.switchDev = function(body, callback)

{
	var templateBody = {
		user:"",
		token:"",
		devices:"",
		status:""
		
	};

	for (var i in templateBody){
		if (body[i] == undefined || body[i]== ""){
			callback({err:"missing-parameters"});
		}
	}
			validateToken(body.user, body.token, function(err, res) {
				if (res){
					if (body.name != undefined && body.name != "" && tokens[body.user].admin){
						saveNameDevice({house:tokens[body.user], id:body.devices, name:body.name});
					}
					var house = require(dbPath + "houseDB.json")[tokens[body.user].house];
					var devices = require(dbPath + "deviceDB.json")
					if(typeof( body.status ) != 'boolean'){
						callback({err:'invalid-status'})
					}else{
						var status = body.status ? "on" : "off";
						if (body.devices > devices[tokens[body.user].house].lenght){
							callback({err:'device-not-found'});
						}
						switchPower({id:body.devices, status:status, host:house.ip, temperature:body.temperature},callback);
						 devices[tokens[body.user].house][body.devices].status = body.status;
						saveData(devices, 'device');
					}
				}else{
					callback(err);
				}
			});
		
	}

exports.getDev = function(body, callback)

{
	var templateBody = {
		user:"",
		token:""
		
	};
	for (var i in templateBody){
		if (body[i] == undefined || body[i]== ""){
			callback({err:"missing-parameters"});
		}
	}
			validateToken(body.user, body.token, function(err, res) {
				
				if (res || err.err == 'expired-token'){
		
				
				var alldevices = require(dbPath+ "deviceDB.json")[tokens[body.user].house] 
				if (tokens[body.user].admin != true){
					var tempIDDev = require(dbPath + "usersDB.json")[body.user].devices
					var tempDev = []
					for (var i= 0 ; i< tempIDDev.length;i++){
						tempDev[tempDev.length] = alldevices[tempIDDev[i]]
					}
					alldevices = tempDev;
				}
				callback(null, alldevices);
				
				
				}
				else {
				callback(err);
				}
			});
}


exports.getMyChild = function(user, token, child, callback)
{
	if(user == undefined || token == undefined){
		callback({err:'missing-parameters'});
	}else{
		validateToken(user, token, function(err, res) {
				if (res){
					if(child == undefined ){
						var usersHouse = require( dbPath + "loginDB.json");
						var users = require(dbPath + "usersDB.json")
						var retorno = [];
						for (i in usersHouse ){
							if ( usersHouse[i] == tokens[user].house && i!= user){
								
								retorno[retorno.length] = {user:i, devices:users[i].devices, name:users[i].name };
							}
							
						}
						callback (null, retorno);
					}else{

						getOneUserByLogin(child,function(e, o){
							if (o!= undefined){
								if (o.house == tokens[user].house){
									var mChild = {user:o.user};
									mChild.devices=[];
									for (var i = 0;i<o.devices.length;i++){
										mChild.devices[i] = parseInt(o.devices[i].id);
									}
									callback(null,mChild);
								}else{
									callback({err:'not-a-child'});
									
								}
							}else{
								callback({err:'child-not-found'});
							}
							
						});
					}}	else{
					callback(err);
				}
		});
	}
}
exports.upMyChild = function(body, callback)
{
	var userModel = {user:"",token:"",child:"", devices:""};
	for (var i in userModel){
		if(body[i] == undefined){
			callback({err:'missing-parameters: '+ i});
		}
	}
	validateToken(body.user, body.token, function(err, res) {
				if (res){
					saveChild(body.child, body.devices);
					callback(null, "ok")
				}	else{
					callback(err);
				}
	});
}




//create new user
exports.addNewAccount = function(newData, callback)
{
	var userModel = { 
							user: "",
							token:"",
							child: "",
							name: "",
							email: "",
							pass: "",
							devices: ""
					  }
	for (var i in userModel){
		if (newData[i] == undefined || newData[i] == "")
		callback({err:'missing-parameters: '+ i});
	}
	newData.house = tokens[newData.user].house;
	if (tokens[newData.user].admin){
	validateToken(newData.user, newData.token, function(err, res) {
		if (res){
			
			findByTable(newData.child,'login', function(o) {
				if (o != undefined){
					callback({err:'username-taken'});
				}else{
					findByTable(newData.email,'email', function(o) {
					if (o != undefined){
							callback({err:'email-taken'});
					}	else{
							saltAndHash(newData.pass, function(hash){
							newData.pass = hash;
							//newData.date = moment().format('MMMM Do YYYY, h:mm:ss a');
							insertUser(newData);
							callback(null,'ok')
							});
						}
					});
				}
			});
		}	else{
				callback(err);
			}
		
		});
	}else {
	callback({err:'not-a-admin'})
	}
}

exports.delChild = function (newData, callback){
	var userModel = { 
							user: "",
							token:"",
							child: "",
					 }
	for (var i in userModel){
		if (newData[i] == undefined){
			callback({err:'missing-parameters: '+ i});
		}
	}
	if (newData.user == newData.child){
		callback({err:'canot-remove-yourself'});
	}else if (tokens[newData.user].admin){
	validateToken(newData.user, newData.token, function(err, res) {
		if (res){
			findByTable(newData.child,'users', function(o) {
				if (o != undefined){
					if (o.house == tokens[newData.user].house){
						delUser(o.user);
						callback(null,"ok");
					}else{
						callback({err:'not-a-child'});
						return;
					}
				}else{
					callback({err:'child-not-found'});
					return;
				}
			});
		}	else{
				callback(err);
				return;
			}
		
		});
	}else {
	callback({err:'not-a-admin'});
	return;
	}
	
	
}


//generate token for user
exports.AndroidLogin = function(user, pass, callback){
	getOneUserByLogin(user, function(e, o) {
		if (o == null){
			callback({err:'user-not-found'});
		}	else{
			validatePassword(pass, o.pass, function(err, res) {
				if (res){
					getOneUserByLogin(o.user,function(er, u){
						if (tokens[u.user]){
							if (tokens[u.user].timeout){
								clearTimeout(tokens[u.user].timeout);
							}
						}
						var admin = require(dbPath +"houseDB.json")[u.house].admin == u.user;
						tokens[u.user] = {};
						tokens[u.user].token= "abc"//generateSalt(64);
						tokens[u.user].house = u.house;
						tokens[u.user].admin = admin
						
						
						callback(null, {user:u.user, token:tokens[u.user].token});
								tokens[u.user].timeout =  setTimeout(function(){
										
										tokens[u.user].timeout = undefined;
										tokens[u.user].token = '&xpired';
										},600000);
						
						
						});
				}	else{
					callback({err:'invalid-password'});
				}
			});
		}
	});	
}

//private methods
/* private encryption & validation methods */

// find user by login
getOneUserByLogin = function(user, callback){
	var loginDB = require(dbPath + 'loginDB.json');
	if( loginDB[user] != undefined){
		var o = require(dbPath+'usersDB.json')[user];
		var h = require(dbPath+'houseDB.json')[o['house']]
		var admin = h['admin'] == o['user'];
		var devices = [];		
		var allDevs = require(dbPath+'deviceDB.json')[o['house']];
		getStatusDev({id:o['house'], ip:h['ip']});
			if (admin){
				devices = allDevs;
			}else{	
				var tempDevs = o['devices'];
				for (var i = 0; i<tempDevs.length;i++){
					devices[i]=allDevs[parseInt(tempDevs[i])]
				}
			}
			var userObject = {
					user 		: 	o.user,
					name 		: 	o.name,
					email 		: 	o.email,
					pass		: 	o.pass,
					house		: 	o.house,
					long		:	h.longitude,
					lati		:	h.latitude ,
					admin		: 	admin,
					devices		: 	devices
				};
				callback(null,userObject);
				
		}
	else {
		callback({err:'user-not-found'});
	}

	
}

//take object by table
findByTable = function(data, table, callback){
	var tableDB = require( dbPath + table + 'DB.json');
	callback(tableDB[data]);
}

function saveData(jsonData, filePath){
	try {
		fs = require('fs');
		var outputFilename = './jsonDB/'+filePath+'DB.json';
		fs.writeFileSync(outputFilename, JSON.stringify(jsonData, null, 4));
	}catch(e){
		console.log('err',e);
	}
}
var insertUser = function(newData){
	var logins = require(dbPath+'login'+'DB.json');
	logins[newData['child']] = newData['house'];
	saveData(logins, 'login');
	
	var emails = require(dbPath+'email'+'DB.json');
	emails[newData['email']] = newData['child'];
	saveData(emails, 'email');
	
	var users  = require(dbPath+'users'+'DB.json');
	users[newData['child']] = {
        name: newData['name'],
        email: newData['email'],
        user: newData['child'],
        pass: newData['pass'],
        house: newData['house'],
        devices:newData['devices']
        
    };
	saveData(users, 'users');
}

var delUser = function(user){
	var logins = require(dbPath+'login'+'DB.json');
	var emails = require(dbPath+'email'+'DB.json');
	var users  = require(dbPath+'users'+'DB.json');
	var email = users[user].email
	emails[email] = undefined
	logins[user] = undefined
	users[user] = undefined
	saveData(logins, 'login');
	saveData(emails, 'email');
	saveData(users, 'users');
}

var generateSalt = function(len)
{
	var leng = 10;
	if (len){leng = len;}
	var set = '0123456789abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ';
	var salt = '';
	for (var i = 0; i < leng; i++) {
		var p = Math.floor(Math.random() * set.length);
		salt += set[p];
	}
	return salt;
}

var md5 = function(str) {
	return crypto.createHash('md5').update(str).digest('hex');
}

var saltAndHash = function(pass, callback)
{
	var salt = generateSalt();
	callback(salt + md5(pass + salt));
}

var validatePassword = function(plainPass, hashedPass, callback)
{
	var salt = hashedPass.substr(0, 10);
	var validHash = salt + md5(plainPass + salt);
	callback(null, hashedPass === validHash);
}
var saveChild = function (child, devices){
	var tableDB = require(dbPath+'users'+'DB.json');
	tableDB[child].devices = devices;
	saveData(tableDB, 'users');
	
}

var saveGeoPosition = function (longitude, latitude, house){

	if (longitude != undefined && latitude != undefined){
		var tableDB = require(dbPath+'house'+'DB.json');
		tableDB[house].longitude = longitude;
		tableDB[house].latitude = latitude;
		saveData(tableDB, 'house');
	}
	
	
	
	
}
var validateToken = function(user, token, callback)
{
	if (tokens[user]!=undefined){
		if (tokens[user].token == '&xpired'){
			callback({err:'expired-token'})
		}else{
			callback({err:'invalid-token'}, tokens[user]['token'] == token);
		}
	}else{
		callback({err:'nonexistent-session'});
	}
	
}
var getStatusDev= function(hostHouse, callback){
	var xmlhttp = new XMLHttpRequest();
	var devices = require(dbPath + "deviceDB.json");
	var nameHouse = hostHouse.id
	xmlhttp.onreadystatechange=function() {

	if (xmlhttp.status==200 && xmlhttp.readyState == 4){
		try{
			response = JSON.parse(xmlhttp.responseText);
			var devicesLen = response.devices.length;
			for (var i=0; i < devicesLen; i++) {
				devices[nameHouse][i].status = response.devices[i].status == "on"? true : false;
				
			}
			saveData(devices, "device");
			callback();
		}catch(e){
		}
	}
}

	try{
		xmlhttp.open("GET", getLinkDefault(hostHouse.ip),true);
		xmlhttp.send();
	}catch(e){
	}
	
}


var xmlhttp2 = new XMLHttpRequest();
function saveNameDevice(body){
	var alldev = require(dbPath + "deviceDB.json")
	alldev[body.house][body.id].name = body.name;
	saveData(alldev, "device");
	
}
function switchPower(body,callback){
	try{
		if (body.temperature != undefined){
			xmlhttp2.open("POST", getLinkDefault(body.host)+"?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id="+body.id+'&status='+body.status+'&temperature='+body.temperature, true);
		}else{
			xmlhttp2.open("POST", getLinkDefault(body.host)+"?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id="+body.id+'&status='+body.status, true);
		}
		xmlhttp2.send();
		callback(null,"");
	}catch(e){
	}
};

function getLinkDefault(host){
	return "http://"+host+":3000/control";
}

/* auxiliary methods */
