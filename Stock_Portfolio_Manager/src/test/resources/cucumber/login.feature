Feature: Log into the user's account

	Scenario: Login with the correct Username and Password
		Given I am on the Login Page
		When I enter the correct Username
		And I enter the correct Password
		And I click on the LOGIN button
		Then I am redirected to the home page
	
	Scenario: Login with the correct Username and incorrect password
		Given I am on the Login Page
		When I enter the correct Username
		And I enter the incorrect Password
		And I click on the LOGIN button
		Then I should get an error message: "Error: Username or Password does not exist"
	
	Scenario: Login with the incorrect Username and correct password
		Given I am on the Login Page
		When I enter the incorrect Username
		And I enter the correct Password
		And I click on the LOGIN button
		Then I should get an error message: "Error: Username or Password does not exist"

	Scenario: Login with the incorrect Username and incorrect password
		Given I am on the Login Page
		When I enter the incorrect Username
		And I enter the incorrect Password
		And I click on the LOGIN button
		Then I should get an error message: "Error: Username or Password does not exist"

	Scenario: Fail to login and navigate to the Sign Up Page
		Given I am on the Login Page
		When I click the Sign up button at the bottom of the Webpage
		Then I am redirected to the Sign Up Page
