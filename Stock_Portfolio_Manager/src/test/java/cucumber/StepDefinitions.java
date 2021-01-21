package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertTrue;

/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
	private static final String ROOT_URL = "http://localhost:8080/";

	private final WebDriver driver = new ChromeDriver();

	@Given("I am on the Login Page")
	public void i_am_on_the_Login_Page() {
	    driver.get(ROOT_URL);
	}
	
	@When("I enter the correct Username")
	public void i_enter_the_correct_Username() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[1]/input"));
	    queryBox.sendKeys("trojan");
	}
	
	@When("I enter the correct Password")
	public void i_enter_the_correct_Password() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[2]/input"));
	    queryBox.sendKeys("test");
	}
	
	@When("I click on the LOGIN button")
	public void i_click_on_the_LOGIN_button() {
		WebElement searchButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[3]/input"));
		searchButton.click();
	}
	
	@Then("I am redirected to the home page")
	public void i_am_redirected_to_the_home_page() {
		String result = driver.getCurrentUrl();
	    assertTrue(result.equalsIgnoreCase("http://localhost:8080/homepage.jsp"));
	}

	@When("I enter the incorrect Password")
	public void i_enter_the_incorrect_Password() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[2]/input"));
	    queryBox.sendKeys("1234");
	}

	@Then("I should get an error message: {string}")
	public void i_should_get_an_error_message(String string) {
		WebElement errormsg = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/font"));
		assertTrue(errormsg.getText().equalsIgnoreCase("Error: Username or Password does not exist"));   
	}
	
	@When("I enter the incorrect Username")
	public void i_enter_the_incorrect_Username() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[1]/input"));
	    queryBox.sendKeys("123446646");
	}
	
	@When("I click the Sign up button at the bottom of the Webpage")
	public void i_click_the_Sign_up_button_at_the_bottom_of_the_Webpage() {
		WebElement searchButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div/div[2]/a"));
		searchButton.click();
	}
	
	@Then("I am redirected to the Sign Up Page")
	public void i_am_redirected_to_the_Sign_Up_Page() {
		String result = driver.getCurrentUrl();
	    assertTrue(result.equalsIgnoreCase("http://localhost:8080/CreateAccount.jsp"));
	}
	
	@Given("I am on the Signup Page")
	public void i_am_on_the_Signup_Page() {
		String SIGNUP_URL = "http://localhost:8080/CreateAccount.jsp";
		driver.get(SIGNUP_URL);
	}
	
	@When("I enter a username not in database")
	public void i_enter_a_username_not_in_database() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[1]/input"));
		String username = "23bkjdi3u78";
	    queryBox.sendKeys(username);
	}
	
	@When("I enter a valid password")
	public void i_enter_a_valid_password() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[2]/input"));
	    queryBox.sendKeys("test");
	}
	
	@When("the confirm password does not match")
	public void the_confirm_password_does_not_match() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[3]/input"));
	    queryBox.sendKeys("test123");
	}
	
	@When("I click on the CREATE USER button")
	public void i_click_on_the_CREATE_USER_button() {
		WebElement searchButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[4]/div[1]/input"));
		searchButton.click();
	}
	
	@When("I enter a username already in database")
	public void i_enter_a_username_already_in_database() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[1]/input"));
	    queryBox.sendKeys("test");
	}
	
	@When("the confirm password matches")
	public void the_confirm_password_matches() {
		WebElement queryBox = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[3]/input"));
	    queryBox.sendKeys("test");
	}
	
	@When("I click the cancel button")
	public void i_click_the_cancel_button() {
		WebElement searchButton = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[4]/div[2]/input[2]"));
		searchButton.click();
	}
	
	@Then("I should be redirected to the login page")
	public void i_should_be_redirected_to_the_login_page() {
		String result = driver.getCurrentUrl();
	    assertTrue(result.equalsIgnoreCase("http://localhost:8080/index.jsp"));
	}
	
	@Then("I should get an error message about the password: {string}")
	public void i_should_get_an_error_message_about_the_password(String string) {
	    WebElement errormsg = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[3]/font"));
		assertTrue(errormsg.getText().equalsIgnoreCase("Your passwords do not match.")); 
	}
	
	@Then("I should get an error message about the username: {string}")
	public void i_should_get_an_error_message_about_the_username(String string) {
		WebElement errormsg = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/form/div[1]/font"));		
		assertTrue(errormsg.getText().equalsIgnoreCase("Username already taken, please sign up with a new one.")); 
	}
	
	@Then("I should be redirected to the login page after successfully creating the account")
	public void i_should_be_redirected_to_the_login_page_after_successfully_creating_the_account() {
		String result = driver.getCurrentUrl();
	    assertTrue(result.equalsIgnoreCase("http://localhost:8080/CreateAccountServlet?username=23bkjdi3u78&password=test&confPass=test&cancel=notCanceled"));
	}
	
	@After()
	public void after() {
		driver.quit();
	}
}
