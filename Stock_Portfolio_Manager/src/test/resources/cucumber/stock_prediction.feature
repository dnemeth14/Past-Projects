Feature: Prediction of future values of the portfolio

  Scenario: Show prediction 
    Given I am on the Homepage
    When I select a stock
    When I click on 'predict'
    Then The graph should show a predicted trend of the selected stock

  Scenario: Cancel prediction
    Given I am on the Homepage
    When A stock is selected
    When The stock prediction is being shown
    When I click on 'predict'
    Then The graph should not show stock prediction anymore
