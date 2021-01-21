Feature: User security
	Scenario: Logout of account
		Given I am logged into my account
		When I go to the home page
		And I click the logout button
		Then I should be signed out and redirected to the login page
			
	Scenario: Homepage access
		Given I am not logged in
		When I try to access the landing page
		Then I should be redirected to the login page
	
	Scenario: Failing to login
		Given I am on the login page
		When I unsuccessfully try to login 3 times in a minute
		Then the account with that username should be locked for a minute
			
	Scenario: Forced logout after inactivity
		Given I am logged in
		When I don't take any action for 2 minutes
		Then I should be logged out and redirected to the login page
	