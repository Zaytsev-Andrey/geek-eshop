package ru.geekmarket.steps;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.geekmarket.DriverInitializer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private WebDriver webDriver = null;

    @Given("^I open web browser$")
    public void iOpenWebBrowser() throws Throwable {
        webDriver = DriverInitializer.getDriver();
    }

    @When("^I navigate to \"([^\"]*)\" page$")
    public void iNavigateToLoginHtmlPage(String url) throws Throwable {
        Thread.sleep(1000);
        webDriver.get(DriverInitializer.getProperty(url));
    }

    @When("^I click on \"([^\"]*)\" button$")
    public void iClickOnLoginButton(String button) throws Throwable {
        Thread.sleep(1000);
        WebElement webElement = webDriver.findElement(By.id(button));
        webElement.click();
    }

    @When("^I provide username as \"([^\"]*)\" and password as \"([^\"]*)\"$")
    public void iProvideUsernameAsAndPasswordAs(String username, String password) throws Throwable {
        Thread.sleep(1000);
        WebElement webElement = webDriver.findElement(By.id("username"));
        webElement.sendKeys(username);
        webElement = webDriver.findElement(By.id("password"));
        webElement.sendKeys(password);
    }

    @When("^I provide category title as \"([^\"]*)\"$")
    public void iProvideCategoryAs(String title) throws InterruptedException {
        Thread.sleep(1000);
        WebElement webElement = webDriver.findElement(By.id("title"));
        webElement.sendKeys(title);
    }

    @Then("^name should be \"([^\"]*)\"$")
    public void nameShouldBe(String name) throws Throwable {
        Thread.sleep(1000);
        WebElement webElement = webDriver.findElement(By.id("dd_user"));
        assertThat(webElement.getText()).isEqualTo(name);
    }

    @Then("^I add product \"([^\"]*)\" to cart$")
    public void iAddProductToCart(String productTitle) throws InterruptedException {
        Thread.sleep(1000);
        List<WebElement> carts = webDriver.findElements(By.xpath("//*[@id='product-cart']"));

        for (WebElement currentCart : carts) {
            String currentProductTitle = currentCart.findElement(By.className("card-title")).getText();
            if (productTitle.equals(currentProductTitle)) {
                currentCart.findElement(By.id("btn-add-to-cart")).click();
                break;
            }
        }
    }

    @Then("^I check product \"([^\"]*)\" in cart$")
    public void iCheckProductInCart(String productTitle) throws InterruptedException {
        Thread.sleep(1000);
        boolean isFound = webDriver.findElements(By.xpath("//*[@id='cart']")).stream()
                        .map(we -> we.findElement(By.xpath("//*[@id='cart']/tbody/tr/td[1]")).getText())
                        .anyMatch(productTitle::equals);


        assertThat(isFound).isTrue();
    }

    @Then("^I check added category \"([^\"]*)\"$")
    public void iCheckAddedCategory(String title) throws InterruptedException {
        boolean isFound = false;
        WebElement buttonNext;

        while (true) {
            Thread.sleep(1000);
            List<WebElement> rows = webDriver.findElements(By.xpath("//*[@id='categories']/tbody/tr/td[1]"));
            isFound = rows.stream()
                    .map(WebElement::getText)
                    .anyMatch(title::equals);
            buttonNext = webDriver.findElement(By.id("btn-next-page"));
            if (!isFound && buttonNext.isEnabled()) {
                buttonNext.findElement(By.tagName("a")).click();
            } else {
                break;
            }
        }

        assertThat(isFound).isTrue();
    }

    @Given("^any user logged in$")
    public void userLoggedIn() throws InterruptedException {
        Thread.sleep(1000);
        webDriver.findElement(By.id("dd_user"));
    }

    @Then("^user logged out$")
    public void userLoggedOut() throws InterruptedException {
        Thread.sleep(1000);
        webDriver.findElement(By.id("username"));
        webDriver.findElement(By.id("password"));
    }

    @After
    public void quitBrowser() {
        webDriver.quit();
    }

}
