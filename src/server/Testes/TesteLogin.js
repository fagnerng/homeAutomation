<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head profile="http://selenium-ide.openqa.org/profiles/test-case">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="selenium.base" href="http://localhost:9000/" />
<title>New Test</title>
</head>
<body>
<table cellpadding="1" cellspacing="1" border="1">
<thead>
<tr><td rowspan="1" colspan="3">New Test</td></tr>
</thead><tbody>

<!--Logar com usuario inexistente-->
<tr>
	<td>type</td>
	<td>id=login</td>
	<td>Teste</td>
</tr>
<tr>
	<td>click</td>
	<td>id=button</td>
	<td></td>
</tr>
<tr>
	<td>waitForAlert</td>
	<td>Informações incorretas</td>
	<td></td>
</tr>
<!--Logar com usuario existente e senha incorreta-->
<tr>
	<td>type</td>
	<td>id=login</td>
	<td>joao</td>
</tr>
<tr>
	<td>type</td>
	<td>id=pass</td>
	<td>UmaSenhaIncorreta</td>
</tr>
<tr>
	<td>click</td>
	<td>id=button</td>
	<td></td>
</tr>
<tr>
	<td>waitForAlert</td>
	<td>Informações incorretas</td>
	<td></td>
</tr>
<!--Deixar todos os campos vazios-->
<tr>
	<td>type</td>
	<td>id=login</td>
	<td></td>
</tr>
<tr>
	<td>type</td>
	<td>id=pass</td>
	<td></td>
</tr>
<tr>
	<td>click</td>
	<td>id=button</td>
	<td></td>
</tr>
<tr>
	<td>waitForAlert</td>
	<td>Informações incorretas</td>
	<td></td>
</tr>
<!--Logar com usuário válido-->
<tr>
	<td>type</td>
	<td>id=login</td>
	<td>joao</td>
</tr>
<tr>
	<td>type</td>
	<td>id=pass</td>
	<td>brunoffp</td>
</tr>
<tr>
	<td>click</td>
	<td>id=button</td>
	<td></td>
</tr>
<tr>
	<td>assertLocation</td>
	<td>http://localhost:9000/inicio.html</td>
	<td></td>
</tr>
</tbody></table>
</body>
</html>
