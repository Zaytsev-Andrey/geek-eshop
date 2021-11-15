Feature: AddProductToCart

  Scenario Outline: Successful add product to cart and check result
    Given I open web browser
    When I navigate to "<urlProductGallery>" page
    And I add product "<product>" to cart
    Then I navigate to "<urlCart>" page
    And I check product "<product>" in cart

    Examples:
      | urlProductGallery | product | urlCart |
      | product-gallery.url | Logitech M90 | product-cart.url |