Feature: Login

  Scenario Outline: Successful Login to the page and logout after
    Given I open web browser
    When I navigate to "<urlLogin>" page
    And I provide username as "<username>" and password as "<password>"
    And I click on "<btnLogin>" button
    Then name should be "<name>"
    And I click on "<btnProfile>" button
    And I click on "<btnLogout>" button
    Then user logged out

    Examples:
      | username | password | name | urlLogin | btnLogin | btnProfile | btnLogout |
      | admin@mail.ru | admin | admin@mail.ru | login.url | btn-login | navbarDropdownMenuLink | btn-logout |