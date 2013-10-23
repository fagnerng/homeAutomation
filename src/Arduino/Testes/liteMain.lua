local TCP = require("post")
local json = require("json")
local username = "root"
local password = "ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH"
local failName = "O nome nao corresponde"
local failPower = "O status do dispositivo nao corresponde"
local failCode = "O Codigo de retorno nao corresponde"
local failParans = "o numero de Parametros nao corresponde"

local function getReqBody(ID, lPower)
return "?username="..username.."&password="..password.."&status="..lPower .."&id="..ID
end
local function switchPower(ID, POWER)
	local lPower
	if POWER then lPower = "on" else lPower = "off" end
	local rqBody = getReqBody(ID, lPower)
	return TCP.POST( rqBody)
end
local function getDevice(ID)
local response, code = TCP.GET()
response = json:decode(response)
 return response.devices[ID+1], code


end

local function toString(device)
local retorno = "name = ".. device.name
retorno = retorno .. "\nstatus = ".. device.status
return retorno
end 

local function assert(esperado, observado, msgErro)
	if (esperado) then 
		if (observado) then
			if tostring(esperado) == tostring(observado) then return "Pass" else return "Err: esperado: ".. esperado..", observado: "..observado.." msg : ".. msgErro end
		end
	end
end


local function runTeste()
local on,off = "on", "off"
-- seta desligados todos os dispositivos
switchPower(0)
-- pega a instacia atual dos dispositivos
local ventilador,codev = getDevice(0)

--colecao de testes a serem realizados
local colecaoTestes = {}
	-- testa  estado inicial desligado
	--liga os dispositivos e atualiza as instancias do mesmo
	switchPower(0,"on")
	ventilador, codev = getDevice(0)
	colecaoTestes[1] = assert(on, ventilador.status,failPower)
	local luser = username
	username = "odername"
	local a
	a, codev = switchPower(0)
	colecaoTestes[2] = assert(codev, "402",failCode)
	username = luser
	a, codev = switchPower(0)
	colecaoTestes[3] = assert(codev, "200",failCode)
	
	local mqreqbody = getReqBody
	getReqBody = function() return "" end
	a, codev = switchPower(0)
	colecaoTestes[4] = assert(codev, "403",failParans)
	getReqBody = mqreqbody
	a, codev = switchPower(0)
	colecaoTestes[5] = assert(codev, "200",failParans)
	
	getReqBody = function(id, power) 
	return "?username="..username.."&password="..password.."&status=nada&id="..id
	end
	a, codev = switchPower(0)
	colecaoTestes[6] = assert(codev, "405",failParans)
	getReqBody = mqreqbody
	a, codev = switchPower(0)
	colecaoTestes[7] = assert(codev, "200",failParans)
	for i,v in pairs (colecaoTestes) do
	print (i.." - ".. v)

	end
end


runTeste()



