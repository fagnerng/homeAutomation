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
    console.log(str);
  });
}
http.request(getOptions, callback).end();



//
//
console.log("ligar disposito 00");
http.request(postOptions, callback).end();
//
//
console.log("ligar disposito 01");
myPath =  '/control.html?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id=1&status=on';
postOptions = {
  host: 'arduino.com.br',
  path: myPath,
  port: '5000',
  method: 'POST'
};
http.request(postOptions, callback).end();
//
//
console.log("desligar disposito 00");
myPath =  '/control.html?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id=0&status=off';
postOptions = {
  host: 'arduino.com.br',
  path: myPath,
  port: '5000',
  method: 'POST'
};
http.request(postOptions, callback).end();
//
//
console.log("desligar disposito 01");
myPath =  '/control.html?username=root&password=ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH&id=1&status=off';
postOptions = {
  host: 'arduino.com.br',
  path: myPath,
  port: '5000',
  method: 'POST'
};
http.request(postOptions, callback).end();
