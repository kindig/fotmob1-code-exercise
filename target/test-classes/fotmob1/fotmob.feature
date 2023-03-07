Feature: Check Team standing information
  I want to make sure the games played, points and goal differential are calculated correctly

  Scenario: Verify Points are correct
    Given User goes to https://fotmob.com
    When User selects the Premier League
    When User examines a team
      | Brighton & Hove Albion |
      | Manchester United |
      | Arsenal |
    Then the teams points are correct


#  Scenario: Check the number of points received
#    When we get their number of wins
#    And get their number of draws
#    And get their number of losses
#    Then It should add up to the number of points
#
#  Scenario: Check the goal difference
#    When we get their plus minus
#    Then The difference should be the goal difference


