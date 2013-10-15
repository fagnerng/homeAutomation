function removeUserByLogin(login){
	var jsonData = require('./BD/database.json');	
	var index = findUserByLogin(login);
	delete jsonData.alldata.users[index];
	saveData(jsonData);
}
