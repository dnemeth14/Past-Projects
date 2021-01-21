<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<title>Signup Screen</title>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
	<link rel="stylesheet" href="../css/signup.css">
	<link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@500&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
	<link rel="stylesheet" href="static/css/bootstrap.min.css">
	<link rel="stylesheet" href="static/css/custom.css">
	<script src="static/js/bootstrap.min.js"></script>
</head>
<body>
	<%
		String usernameError = (String) request.getAttribute("usernameError");
		if(usernameError == null) {
			usernameError = "";
		}
		String passwordError = (String) request.getAttribute("passwordError");
		if(passwordError == null) {
			passwordError = "";
		}
		String confpassError = (String) request.getAttribute("confpassError");
		if(confpassError == null) {
			confpassError = "";
		}
	%>
	
	<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
	<t:PageBanner></t:PageBanner>
	<div id="header">
		<div class="login">
			<div class="login-header">
				<p>Create Account</p>
			</div>

			<div class="login-form">
	    		<form method="GET" action="CreateAccountServlet">

					<div id="username">
						<p>Username:</p>
						<input type="text" placeholder="Username" name="username"/>
						<font color = "red"><%= usernameError %></font><br>
					</div>
	
					<div id="password">
						<p>Password:</p>
						<input type="password" placeholder="Password" name="password"/>
						<font color = "red"><%= passwordError %></font><br>		 						
					</div>
					
					<div id="confpassword">
						<p>Confirm Password:</p>
						<input type="password" placeholder="Confirmation" name="confPass"/>
						<font color = "red"><%= confpassError %></font><br>
					</div>
					
					<div id="btn">
						<div id="login-btn">
							<input type="button" value="Create User" class="login-button" onclick="submit()"/>
						</div>
						
						<div id="cancel-btn">
							<input type="hidden" value="notCanceled" name="cancel"/>
							<input id="cancel" type="button" value="Cancel" class="cancel-button" onclick= "window.location.href='index.jsp'"/>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="footer"> 
		<p> Ari Cohen | Andrew Manzanero | Yang Qiao | Naifeng Zhan | Daniel Nemeth &copy; 2020 </p> 
	</div>
</body>
</html>
