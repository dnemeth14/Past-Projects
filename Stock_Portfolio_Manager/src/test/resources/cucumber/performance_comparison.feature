Feature: View and compare the historical performance of stocks

	Scenario: Add stocks to compare
		Given I am on the Homepage
		When I toggle on the view button of a stock in my stock list
		And I toggle on the view button of a second stock in my stock list
		Then the graph should be updated with a line graph of the selected stocks' % change in price over 3 months
		
	Scenario: Default view of stocks and portfolio in graph
		Given I have logged in
		When I enter the homepage
		Then the graph should be updated with a line graph of the portfolio value's % change in price over 3 months
		
	Scenario: Remove stocks from comparing 
		Given I am on the Homepage
		When I toggle off the view button of a stock in my stock list
		And I toggle off the view button of a second stock in my stock list
		Then the graph should no longer show these stocks
		
	Scenario: Change the default time range for comparing
		Given I am on the Homepage
		And I enter a date in the past for the first input box at the bottom of the line graph
		Then the line graph will update to show the change in price from the current date to the specified time range.
	
	Scenario: Change the time range for comparison through user input
  		Given I am on the Homepage
  		When I enter '09/02/2010' in the first input box at the bottom of the line graph
		And I enter '09/02/2020' in the second input box at the bottom of the line graph
  		Then I should be able to see the line graph of the portfolio value and toggled stocks % price changes by day from '09/02/2010' to '09/02/2020'
		
	Scenario: Change the time range for comparison through user input with error
  		Given I am on the Homepage
  		When I enter '-1' in the first input box at the bottom of the line graph
  		Then I should be able to see an error message saying "Invalid input"
