var express = require('express');
var app = express();
var hostPort = 9000;
var dbPath = "./jsonDB/";
var htmlPath = __dirname +"/public";
var AM = require('./modules/acManager');
var debug = true

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
	}else if(userAgent.indexOf("Linux")!= -1){
		return "Desktop";
	}else if(userAgent.indexOf("Windows")!= -1){
		return "Desktop";
	}else if(userAgent.indexOf("iPhone")!= -1){
		return "Desktop";
	}else if(userAgent.indexOf("Apache")!= -1){
		return "Apache";
	}else if(userAgent.indexOf("iPad")!= -1){
		return "Desktop";
	}else{
		return "Desktop";
	}
}


// verifica password do usuario
function avalidatePassword(user, pass, callback){
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
		avalidatePassword(user, pass, callback );
			
	}
	
}


/////////////////////////////////UTIL///////////////////////////////////


app.get('/', function(req, res){
	
	if (getUserAgent(req.headers)=="Desktop"){
	if (req.cookies['user'] == undefined || req.cookies['pass'] == undefined){
			var fs = require('fs');
			fs.readFile('./public/login.html',{encoding:'utf8'}, function (err, data) {
			if (err) throw err;
				res.send(data);
			});
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
	}else{
		res.redirect('/android');
	}

});


app.post('/', function(req, res){
	AM.manualLogin(req.param('user'), req.param('pass'), function(e, o){
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

app.post('/alogin',function (req,res){
	//~ if (getUserAgent(req.headers)=="Desktop"){
		//~ res.redirect("/");
	//~ }else
		if (debug){
		console.log("aloginpost: ")
		console.log(req.body);
	}
	{
		AM.AndroidLogin(req.body['user'],req.body['pass'],function(e, o){
		if(o != null){
			res.send(o, 200);	
		}else
		{
			res.send(e, 300);	
		}
	});
	}
});


app.get('/auser',function (req,res){
	//~ if (getUserAgent(req.headers)=="Desktop"){
		//~ res.redirect("/");
	//~ }else
		if (debug){
		console.log("auserget: ");
		console.log(req.params);
	}
	{
		var token = req.param('token');
		var user = req.param('user');
		var pass = req.param('pass');
		if (token == undefined){
			AM.AndroidLogin(user,pass,function(e, o){
			if(o != null){
				token = o.token;
			}else{
				res.send(e, 300);	
			}
		});
		}
		AM.getUser(user,token,function(e, o){

			if(o != null){
				o.pass = undefined;
				o.token = token;
				console.log(o);
				res.send(o, 200);	
				
			}else{
				res.send(e, 300);	
			}
		});
				
	}
});

app.post('/auser',function (req,res){
	//~ if (getUserAgent(req.headers)=="Desktop"){
		//~ res.redirect("/");
	//~ }else
		if (debug){
		console.log("auserpost: ");
		console.log(req.body);
	}
	{
		var body = req.body;
		AM.upUser(body,function(e, o){
		if(o != null){
			res.send(o, 200);	
		}else
		{
			res.send(e, 300);	
		}
	});
	
	}
});



app.get('/achild',function (req,res){
	//~ if (getUserAgent(req.headers)=="Desktop"){
		//~ res.redirect("/");
	//~ }else
		if (debug){
		console.log(req.body);
	}
	{
		AM.getMyChild(req.param('user'),req.param('token'), req.param('child'),function(e, o){
		if(o != null){
			res.send(o, 200);	
		}else
		{
			res.send(e, 300);	
		}
	});
	
	}
});
app.post('/achild',function (req,res){
	//~ if (getUserAgent(req.headers)=="Desktop"){
		//~ res.redirect("/");
	//~ }else
		if (debug){
		console.log(req.body);
	}
	{
		AM.upMyChild(req.body,function(e, o){
		if(o != null){
			res.send(o, 200);	
		}else
		{
			res.send(e, 300);	
		}
	});
	
	}
});

app.put('/achild',function (req,res){
	
	if (debug){
		console.log(req.body);
	}
	//~ if (getUserAgent(req.headers)=="Desktop"){
		//~ res.redirect("/");
	//~ }else
	{
		AM.addNewAccount(req.body,function(e, o){
		if(o != null){
			res.send(o, 200);	
		}else
		{
			res.send(e, 300);	
		}
	}
	);
	
	}
});

app.delete('/achild',function (req,res){
	//~ if (getUserAgent(req.headers)=="Desktop"){
		//~ res.redirect("/");
	//~ }else
		if (debug){
		console.log(req.body);
	}
	{
		AM.delChild(req.body, function(e,o){

		if(o != null){
			res.send(o, 200);	
		}else
		{
			res.send(e, 300);	
		}
	}
	);
	
	}
});


app.get('/alogout',function (req,res){
	
	if (getUserAgent(req.headers)=="Desktop"){
		res.redirect("/");
	}else{
		res.send("not-implemented-yet", 200);	
		
	}
});



app.get('/*',function (req,res){
	res.send('not-found', 404);
});


app.listen(hostPort);

