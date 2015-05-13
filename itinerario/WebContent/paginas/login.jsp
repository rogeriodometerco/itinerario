<!doctype html>

<html>
<head>
<style>
body {
	margin: 30px;
}

body * {
	display: block;
}
</style>
</head>

<body>
	<form method="post" action="j_security_check">
		<h3>Área restrita</h3>
		<label>Usuário</label> 
		<input type="text" id="j_username" name="j_username" /> 
		<label>Senha</label> 
		<input type="password" id="j_password" name="j_password" /> 
		<input type="submit" name="submit" value="Login" />
	</form>
</body>
</html>