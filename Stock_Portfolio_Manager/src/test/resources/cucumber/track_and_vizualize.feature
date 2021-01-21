Feature: Track and visualize changes in value over time of user’s portfolio

  Scenario: Initial chart view
    Given I log in as the 'test' user with 'test' password
    Then The graph should show dates from the earliest purchase date up to a year ago until today

  Scenario: Zoom in
    Given I log in as the 'test' user with 'test' password
    When I click on the 'zoom in' button
    Then The start date on the graph should 'decrease' by half a month

  Scenario: Zoom out
    Given I log in as the 'test' user with 'test' password
    When I click on the 'zoom out' button
    Then The start date on the graph should 'increase' by half a month

  Scenario: Set new date range
    Given I log in as the 'test' user with 'test' password
    When I enter a date of '1/1/2020' into the 'from' input
    And I enter a date of '1/6/2020' into the 'to' input
    Then The graph should show dates from '1/1/2020 to 1/6/2020'
    
   Scenario: Multiple line colors
    Given I log in as the 'test' user with 'test' password
    Then the graph should show each stock, the benchmark, and total value as different colors on the graph
