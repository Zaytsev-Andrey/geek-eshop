Feature: AddCategory

  Scenario Outline: Successful add category and check result
    Given I open web browser
    When I navigate to "<urlLogin>" page
    And I provide username as "<username>" and password as "<password>"
    And I click on "<btnLogin>" button
    Then I navigate to "<urlCategories>" page
    And I click on "<btnAddCategory>" button
    Then I provide category title as "<title>"
    And I click on "<btnSave>" button
    Then I check added category "<title>"
    And I click on "<btnProfile>" button
    And I click on "<btnLogout>" button
    Then user logged out

    Examples:
      | username | password | title | btnLogin | btnAddCategory | btnSave | btnProfile | btnLogout | urlLogin | urlCategories |
      | admin@mail.ru | admin | Tablet | btn-login | btn-add-category | btn-save | navbarDropdownMenuLink | btn-logout | login.url | categories.url |