var crypto 		= require('crypto');
var dbPath = "../jsonDB/";
var tokens = {};
tokens.fagnerng = {};
tokens.fagnerng.token= 'abc';
tokens.fagnerng.house= 'house_000';
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
						body.user = undefined;
						body.token = undefined;
						body.devices = undefined;
						body.house = undefined;
						if (body.pass){
							saltAndHash(body.pass, function(hash){
								body.pass = hash;
							});									
						}
						for (i in body){
							if (o[i] != undefined && body[i] != undefined){
									o[i] = body[i];
							}
	
						}
						
						insertUser(	
						{   name: o.name,
							email: o.email,
							user: o.user,
							pass: o.pass,
							house: o.house
						});	
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
	if(user == undefined || token == undefined){
		callback({err:'missing-parameters'});
	}else{
		validateToken(user, token, function(err, res) {
				if (res != undefined && res == true){
					
					getOneUserByLogin(user,callback);
					
				}	else{
					
					callback(err);
				}
		});
	}
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
								
								retorno[retorno.length] = {user:i, devices:users[i].devices};
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
	var user = body.user;
	var token = body.token;
	var child = body.child;
	var devices = body.devices;
	if(user == undefined || token == undefined || child == undefined || devices == undefined){
		callback({err:'missing-parameters'});
	}else{
		validateToken(user, token, function(err, res) {
				if (res){
					saveChild(child, devices);
					callback(null, "done")
				}	else{
					callback(err);
				}
		});
	}
}




//create new user
exports.addNewAccount = function(newData, callback)
{
	validateToken(user, token, function(err, res) {
		if (res){
				
			findByTable(newData.user,'login', function(o) {
				if (o != undefined){
					callback({err:'username-taken'});
				}else{
					findByTable(newData.email,'email', function(o) {
					if (o != undefined){
							callback({err:'email-taken'});
					}	else{
						var userModel = { 
							user: "",
							token:"",
							child: "",
							name: "",
							emai: "",
							pass: "",
							house: "",
							devices: ""
					  }
						for (var i in userModel){
							if (newData[i] == undefined)
							callback({err:'missing-parameters: '+ i});
						}
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
}

//update password
exupdatePassword = function(user, newPass, callback)
{
	findByTable(user,'user', function(o){
		if (o == undefined){
			callback({err:'user-not-found'});
		}	else{
			saltAndHash(newPass, function(hash){
		       
		        var users = require(dbPath+'usersDB.json')
		        users[user].pass = hash;
		        saveData(users, 'users');
			});
		}
	});
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
						tokens[u.user] = {};
						tokens[u.user].token= "abc"//generateSalt(64);
						tokens[u.user].house = u.house;
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


// find user by login
getOneUserByLogin = function(user, callback){
	var loginDB = require(dbPath + 'loginDB.json');
	if( loginDB[user] != undefined){
		var o = require(dbPath+'usersDB.json')[user];
		var h = require(dbPath+'houseDB.json')[o['house']]
		var admin = h['admin'] == o['user'];
		var devices = [];		
		var allDevs = require(dbPath+'deviceDB.json')[o['house']];
		
		if (admin){
			devices = allDevs;
		}else{	
			var tempDevs = o['devices'];
			for (var i = 0; i<tempDevs.length;i++){
				devices[i]=allDevs[parseInt(tempDevs[i])]
			}
		}
		console.log(o);
		callback(null,{
				user 		: 	o.user,
				name 		: 	o.name,
				email 		: 	o.email,
				pass		: 	o.pass,
				house		: 	o.house,
				long		:	h.longitude,
				lati		:	h.latitude ,
				admin		: 	admin,
				devices		: 	devices
			});
	}
	else {
		callback({err:'user-not-found'});
	}

	
}
//private methods
/* private encryption & validation methods */
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
	logins[newData['user']] = newData['house'];
	saveData(logins, 'login');
	
	var emails = require(dbPath+'email'+'DB.json');
	emails[newData['email']] = newData['user'];
	saveData(emails, 'email');
	
	var users  = require(dbPath+'users'+'DB.json');
	users[newData['user']] = newData;
	saveData(users, 'users');
}

var delUser = function(oldData){
	
	
	
	
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

/* auxiliary methods */
