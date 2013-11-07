var express = require('express');
var app = express();
var hostPort = 9000;
var dbPath = "./jsonDB/";
var htmlPath = __dirname +"/public";
var AM = require('./modules/acManager');
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
	console.log(req.cookies);
	if (req.cookies['user'] == undefined || req.cookies['pass'] == undefined){
			res.sendfile(htmlPath+'/login.html');
	}else{
		
	// attempt automatic login //
		AM.autoLogin(req.cookies['user'], req.cookies['pass'], function(e, o){
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
	AM.manualLogin(req.param('user'), req.param('pass'), function(e, o){
			if (!o){
				res.send(e, 400);
			}	else{
				req.session.user = o;
				console.log(req.param('remember-me'));
				if (req.param('remember-me') == 'on'){
					res.cookie('user', o.user, { maxAge: 900000 });
					res.cookie('pass', o.pass, { maxAge: 900000 });
					
				}
				res.redirect('/home');
				
			}
		});
	
});

app.get('/home',function (req,res){
	if (req.session.user == undefined ||req.session.user==null){
		res.redirect('/');
	}else 
		var o = req.session.user;
		var html  = ""
		if ( o != undefined ){
			html = "<html><body>Welcome "+o.user +" \n<a href = \"/logout\">logout</a></html>";
		}else{
			html ='Welcome "+"o.user "+"  \n<a href = \"/logout\">logout</a></html>';
		}
		html+= '<script src="/vendor/jquery.min.js"></script>\n';
		html+= '<script src="/vendor/jquery.form.js"></script>\n</body>\n';
		res.send(html);
});

app.get('/logout',function (req,res){
	res.clearCookie('user');
	res.clearCookie('pass');
	req.session.destroy(function(e){ res.redirect('/'); });
	
});

////metodos apenas para teste

app.get('/signup',function (req,res){
	var data = {
        name: "brunoffp",
        email: "brunoffp@mail.com",
        user: "brunoffp",
        pass: "brunoffp",
        house: "house_001"
    };
	AM.addNewAccount(data, function(e, o){
		if(o == null){
			res.send(e, 200);	
		}else{
			res.send("create", 200);	
		}
	});
	
	
});

app.get('/getUser',function (req,res){
	AM.getOneUserByLogin('andersongsf',function(e, o){
		if(o == null){
			res.send(e, 200);	
		}else{
			res.send(o, 200);	
		}
	});
});

app.get('/*',function (req,res){
	res.send("not found", 404);
});


app.listen(hostPort);

