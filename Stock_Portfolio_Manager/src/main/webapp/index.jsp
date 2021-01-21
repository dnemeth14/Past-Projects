<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Login Screen</title>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
	<link rel="stylesheet" href="../css/login.css">
	<link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@500&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
	<link rel="stylesheet" href="static/css/bootstrap.min.css">
	<link rel="stylesheet" href="static/css/custom.css">
	<script src="static/js/bootstrap.min.js"></script>
</head>
<body>
	<%
		String error_msg = (String) request.getAttribute("error");
		if(error_msg == null) {
			error_msg = "";
		}
	%>

	<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
	<t:PageBanner></t:PageBanner>
	<div id="header">
		<div class="login">
			<div class="login-header">
				<p>Welcome Back</p>
			</div>
			
			<div class="login-form">
				<form name="loginform" action="LoginServlet">
					<div id="username">
						<p>Username:</p>
						<input id="username_input" name="username" type="text" placeholder="Username"/><br/>
					</div>
					<div id="password">
						<p>Password:</p>
						<input id="password_input" name="password" type="password" placeholder="Password"/><br/>
					</div>
					<div id="login-btn">
						<input id="submit_btn" type="button" value="Login" class="login-button" onclick="submit()" />
					</div>
				</form>

				<font color = "red"><%= error_msg %></font><br>
				
				<div class="no-access">
					<div class="no-access-child">Don't have an account?</div>
					<div class="no-access-child"><a href="CreateAccount.jsp" class="sign-up">Sign Up!</a></div>
				</div>
			</div>
		</div>
	</div>
	<div id="footer"> 
		<p> Ari Cohen | Andrew Manzanero | Yang Qiao | Naifeng Zhan | Daniel Nemeth &copy; 2020 </p> 
	</div>
</body>
</html>