Feature: User Signup
	Scenario: User creates an account with an invalid username or an invalid Password
		Given I am on the Signup Page
		When I enter a username already in database
		And I enter a valid password
		And the confirm password matches
		And I click on the CREATE USER button
		Then I should get an error message about the username: "Username already taken, please sign up with a new one."
		
	Scenario: User creates an account with an valid username but invalid password
		Given I am on the Signup Page
		When I enter a username not in database
		And I enter a valid password
		And the confirm password does not match
		And I click on the CREATE USER button
		Then I should get an error message about the password: "Your passwords do not match."	
    
	Scenario: User creates an account with a valid username and password
		Given I am on the Signup Page
		When I enter a username not in database
		And I enter a valid password
		And the confirm password matches
		And I click on the CREATE USER button
		Then I should be redirected to the login page after successfully creating the account
		
	Scenario: User cancels an account creation
		Given I am on the Signup Page
		When I enter a username not in database
		And I enter a valid password
		And the confirm password matches
		And I click the cancel button
		Then I should be redirected to the login page
