
local post = {}
local response = nil
local code = nil
local http = require("socket.http")
local ltn12 = require("ltn12")
post.morpheus = "http://10.0.0.2"
post.port = 3000
post.endpoint = "/control"


local sink = {}
local loading = nil
function sink.table(t)
	t = t or {}
	local f = function(chunk, err)
		if chunk then
			if (loading) then loading() end
			table.insert(t, chunk)
		end
		return 1
	end
	return f, t
end

post.POST = function (reqbody, fLoading)
	loading = fLoading
	response = {}
	local a
	a, code = http.request {
	   method = "POST",
	   url = post.urlDefault()..reqbody ,
	   headers= {
				["Content-Type"] = "application/json",
				["Content-Length"] = string.len(reqbody),
	   },
	   source = ltn12.source.string(reqbody),
	   sink = sink.table(response)
	}
	return table.concat(response), code
end 

post.GET = function (url)
 loading = fLoading
	response = {}
	local a
	a, code = http.request {
	   method = "GET",
	   url = post.urlDefault() ,
	   headers= {
				["Content-Type"] = "application/json",
				
	   },
	   sink = sink.table(response)
	}
	return table.concat(response), code

end
post.urlDefault = function()
return post.morpheus..":".. post.port.. post.endpoint 
end

return post
