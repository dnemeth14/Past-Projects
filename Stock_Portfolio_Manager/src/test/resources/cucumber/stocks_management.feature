Feature: Search, add or remove stocks from the portfolio

	Scenario: Popup to Add/Remove Stock
		Given I am on the Homepage
		When I click Add/Remove Stocks
		Then I should see a popup dialogue to add/remove stocks
	
	Scenario: Valid Add
		Given I am at the add/remove stock popup
		When I enter 'AMZN' as the index
		And I enter '10/7/2020' as the buydate
		And I enter 5 as the shares
		And I click the submit bitton
		Then I should see a message that the transaction was successful
		And the stocks should appear in my portfolio
		
	Scenario: Invalid Add No Date
		Given I am at the add/remove stock popup
		When I enter 'AMZN' as the index
		And I enter '' as the buydate
		And I enter 5 as the shares
		And I click the submit bitton
		Then I should see an error message that the buydate is invalid

	Scenario: Valid Remove
		Given I am at the add/remove stock popup
		And I own '10' shares of 'AMZN' on '10/7/2020'
		When I enter 'AMZN' as the index
		And I enter '10/7/2020' as the selldate
		And I enter '10' as the shares
		And I click the submit button
		Then I should see a message that the transaction was successful
		And the stocks should disappear from my portfolio
		
	Scenario: Valid Add and Remove
		Given I am at the add/remove stock popup
		When I enter 'AMZN' as the index
		And I enter '10/7/2020' as the buydate
		And I enter '10/8/2020' as the selldate
		And I enter '10' as the shares
		And I click the submit button
		Then I should see a message that the transaction was successful
		And the transaction should be seen in the portfolio line graph
	
	Scenario: Add shares to the portfolio with invalid buy date
		Given I am at the add/remove stock popup
		When I enter 'AMZN' as the index
		And I enter '10' in the input box of Buy Shares 
		And I enter '09/23/2022' in the input box of Buy Date 
		And I click the Submit button
		Then I should get an error message: "Error: Invalid Buy Date"
		
	Scenario: Add shares to the portfolio with invalid share amount
		Given I am at the add/remove stock popup
		When I enter 'AMZN' as the index
		And I enter '0' in the input box of Buy Shares 
		And I enter '09/23/2020' in the input box of Buy Date 
		And I click the Submit button
		Then I should get an error message: "Error: Invalid Share Amount"

	Scenario: Add shares to the portfolio with invalid index
		Given I am at the add/remove stock popup
		When I enter 'AMAZON' as the index
		And I enter '10' in the input box of Buy Shares 
		And I enter '09/23/2020' in the input box of Buy Date 
		And I click the Submit button
		Then I should get an error message: "Error: Invalid Index"
	
	Scenario: Remove shares from the portfolio with invalid sell date
		Given I am at the add/remove stock popup
		And I own '5' shares of 'AMZN' on '10/7/2020'
		When I enter 'AMZN' as the index
		And I enter '10/7/2020' as the selldate
		And I enter '10' as the shares
		And I click the submit button
		Then I should see an error message: "Error: Invalid Share Amount"
	
	Scenario: Invalid Add and Remove
		Given I am at the add/remove stock popup
		When I enter 'AMZN' as the index
		And I enter '10/8/2020' as the buydate
		And I enter '10/7/2020' as the selldate
		And I enter '10' as the shares
		And I click the submit button
		Then I should see an error message: "Error: Buy Date must be before sell date"
		
	Scenario: Valid Cancel
		Given I am at the add/remove stock popup
		When I click cancel
		Then I should be redirected to the home page
		
	Scenario: Valid Upload csv
		Given I am at the add/remove stock popup
		When I click upload stocks
		When I enter the csv filename I would like to upload
		When Stocks from the csv file are parsed without any error
		Then Parsed stocks should be reflected as my own in the Homepage
	
	Scenario: Invalid Upload csv
		Given I am at the add/remove stock popup
		When I click upload stocks
		When I enter the csv filename I would like to upload
		When Stocks from the csv file are parsed gives any error
		Then I should see an error message: "Error: Invalid csv file"
