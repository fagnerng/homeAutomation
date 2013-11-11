var crypto 		= require('crypto');
var dbPath = "../jsonDB/";
var tokens = {};

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
			callback('user-not-found');
		}	else{
			validatePassword(pass, o.pass, function(err, res) {
				if (res){
					getOneUserByLogin(o['user'],callback);
				}	else{
					callback('invalid-password');
				}
			});
		}
	});
}

exports.getUser = function(user, token, callback)
{
	if(user == undefined || token == undefined){
		callback('missing-parameters');
	}else{
		validateToken(user, token, function(err, res) {
				if (res){
					getOneUserByLogin(user,callback);
				}	else{
					callback(err);
				}
		});
	}
}




//create new user
exports.addNewAccount = function(newData, callback)
{
	findByTable(newData.user,'login', function(o) {
		if (o != undefined){
			callback('username-taken');
		}else{
			findByTable(newData.email,'email', function(o) {
			if (o != undefined){
					callback('email-taken');
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
}

//update password
exports.updatePassword = function(user, newPass, callback)
{
	findByTable(user,'user', function(o){
		if (o == undefined){
			callback('user-not-found', null);
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
			callback('user-not-found');
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
						tokens[u.user].token= generateSalt(64);
						
						callback(null, {user:u.user, token:tokens[u.user].token});
								tokens[u.user].timeout =  setTimeout(function(){
										
										tokens[u.user].timeout = undefined;
										tokens[u.user].token = '&xpired';
										},60000);
						
						
						});
				}	else{
					callback('invalid-password');
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
		callback(null,{
				user 		: 	o['user'],
				name 		: 	o['name'],
				email 		: 	o['email'],
				pass		: 	o['pass'],
				house		: 	o['house'],
				admin		: 	admin,
				devices		: 	devices
			});
	}
	else {
		callback('user-not-found');
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

var validateToken = function(user, token, callback)
{
	if (tokens[user]!=undefined){
		if (tokens[user].token == '&xpired'){
			callback('expired-token')
		}else{
			callback('invalid-token', tokens[user].token ==token);
		}
	}else{
		callback('nonexistent-session');
	}
	
}

/* auxiliary methods */
