var express = require('express');
var app = express();
var hostPort = 9000;
var dbPath = "./jsonDB/";
var htmlPath = __dirname +"/public";

app.configure(function(){
	app.use(express.bodyParser());
	app.use(express.cookieParser());
	app.use(express.session({ secret: 'super-duper-secret-secret' }));
	app.use(express.methodOverride());
	app.use(express.static(htmlPath));

});
app.configure('development', function(){
	app.use(express.errorHandler());
});
console.log("Server Initiated");

app.use(express.methodOverride());




/////////////////////////////////UTIL///////////////////////////////////
//funcao atualiza o banco de dados do sistema
function saveData(jsonData){
	fs = require('fs');
	var outputFilename = './BD/database.json';
	fs.writeFileSync(outputFilename, JSON.stringify(jsonData, null, 4));
}

//funcao retorna o user agente de onde a requisicao foi solicitada
function getUserAgent(headers){
	var userAgent = headers["user-agent"];
	if(userAgent.indexOf("Android")!= -1){
		return "Android";
	}else if(userAgent.indexOf("Windows")!= -1){
		return "Windows";
	}else if(userAgent.indexOf("iPhone")!= -1){
		return "iPhone";
	}else if(userAgent.indexOf("iPad")!= -1){
		return "iPad";
	}else{
		return "unknow";
	}
}


// verifica password do usuario
function validatePassword(user, pass, callback){
	var usersDB = require(dbPath + 'usersDB.json');
	if (usersDB[user].pass == pass){
		callback(null,usersDB[user]);
	}else{
		callback("invalid-password");
	}
	
}
//funcao inicia sessao do usuario
function manualLogin(user, pass, callback){
	var loginDB = require(dbPath + 'loginDB.json');
	if (loginDB[user]== undefined) {
			callback('user-not-found');
	}	else{
		validatePassword(user, pass, callback );
			
	}
	
}


/////////////////////////////////UTIL///////////////////////////////////


app.get('/', function(req, res){
	console.log(req.cookies['user']);
	console.log(req.cookies['pass']);
	if (req.cookies['user'] == undefined || req.cookies['pass'] == undefined){
			res.sendfile(htmlPath+'/login.html');
	}else{
	// attempt automatic login //
		manualLogin(req.cookies['user'], req.cookies['pass'], function(e, o){
			if (o != null){
			    req.session.user = o;
				res.redirect('/home');
			}else{
				console.log("erro");
				}
			});
		}
});


app.post('/', function(req, res){
	manualLogin(req.param('user'), req.param('pass'), function(e, o){
			if (!o){
				res.send(e, 400);
			}	else{
				req.session.user = o;
				if (req.param('remember-me') == 'on'){
					res.cookie('user', o.user, { maxAge: 900000 });
					res.cookie('pass', o.pass, { maxAge: 900000 });
					
				}
				res.redirect('/home');
				
			}
		});
	
});

app.get('/home',function (req,res){
	if (req.session.user == null){
		res.redirect('/');
	}else 
		res.send("<a href = \"/logout\">logout</a>");
});

app.get('/logout',function (req,res){
	res.clearCookie('user');
	res.clearCookie('pass');
	req.session.destroy(function(e){ res.redirect('/'); });
	
});

app.get('/*',function (req,res){
	res.send("not found", 404);
});
app.listen(hostPort);

