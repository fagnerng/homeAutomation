local TCP = require("post")
local json = require("json")
local username = "root"
local password = "ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH"
local failName = "O nome nao corresponde"
local failPower = "O status do dispositivo nao corresponde"
local failCode = "O Codigo de retorno nao corresponde"
local failParans = "o numero de Parametros nao corresponde"

--funcoes utilitarias
local function getReqBody(ID, lPower)
return "?username="..username.."&password="..password.."&status="..lPower .."&id="..ID
end
local function switchPower(ID, POWER)
	local lPower
	if POWER then lPower = POWER else lPower = "off" end
	local rqBody = getReqBody(ID, lPower)
	return TCP.POST( rqBody)
end
local function getDevice(ID)
local response, code = TCP.GET()
response = json:decode(response)
 return response.devices[ID+1], code


end 
local function assert(esperado, observado, msgErro)
	if (esperado) then 
		if (observado) then
			if tostring(esperado) == tostring(observado) then return "Pass" else return "Err: esperado: ".. esperado..", observado: "..observado.." msg : ".. msgErro end
		end
	end
end

--funcao que cria todos os testes
local function runTeste()
local on,off = "on", "off"


--colecao de testes a serem realizados
local colecaoTestes = {}
	-- testfa a alteracao de estado dos dispositivo(liga desliga)
	-- seta desligados todos os dispositivos
	
	--desliga e verifica
	switchPower(0, off)
	local ventilador,codev = getDevice(0)
	ventilador, codev = getDevice(0)
	colecaoTestes[1] = assert(off, ventilador.status,failPower)
	--liga e verifica
	switchPower(0,on)
	ventilador, codev = getDevice(0)
	colecaoTestes[2] = assert(on, ventilador.status,failPower)
	
	
	--/*******************testa erros de autenticacao*********/
	
	
	
	local a
	
	-- username invalido/inexistente
	local luser = username
	username = "odername"
	a, codev = switchPower(0,off)
	colecaoTestes[3] = assert(codev, "402",failCode)
	-- teste de regressao, corrige o username para testar
	username = luser
	a, codev = switchPower(0, off)
	colecaoTestes[4] = assert(codev, "200",failCode)
	
	--/****testa se todos os parametros necessarios foram informado **/
	-- muda o link de requisição para testar erro
	
	local mqreqbody = getReqBody
	getReqBody = function() return "" end
	a, codev = switchPower(0, off)
	colecaoTestes[5] = assert(codev, "403",failParans)
	--teste de regressao
	getReqBody = mqreqbody
	a, codev = switchPower(0, off)
	colecaoTestes[6] = assert(codev, "200",failParans)

	--/************testa se o estado a ser setado é valido*********************/
	local status = "invalido"
	a, codev = switchPower(0, status)
	colecaoTestes[7] = assert(codev, "405",failParans)
	--teste de regressao
	getReqBody = mqreqbody
	a, codev = switchPower(0, off)
	colecaoTestes[8] = assert(codev, "200",failParans)
	
	
	
	
	for i,v in pairs (colecaoTestes) do
	print (i.." - ".. v)

	end
end

--roda todos os testes
runTeste()



