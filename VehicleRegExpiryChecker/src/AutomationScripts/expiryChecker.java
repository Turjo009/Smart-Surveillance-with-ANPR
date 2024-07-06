package AutomationScripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class expiryChecker {

	public static void main(String[] args) {
		//Path of Chrome driver
		System.setProperty("webdriver.chrome.driver","E:\\Selenium\\chromedriver-win64\\chromedriver.exe");
		
		//chromeDriver class
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		//statements for initiating drivers in other browsers:
		//webDriver driver = new FirefoxDriver();
		//EdgeDriver driver = EdgeDriver();
		
		//Read plate number from file
        String plateNumberText = "";
        try {
            plateNumberText = new String(Files.readAllBytes(Paths.get("plate_number.txt"))).trim();
        } catch (IOException e) {
            System.out.println("Unable to read the plate number file.");
            e.printStackTrace();
            driver.quit();
            return;
        }
		
		driver.get("https://account.ezyreg.sa.gov.au/account/check-registration.htm");
		
		// Create an instance of WebDriverWait
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try { 
		//provide Plate Number
		 WebElement plateNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("plateNumber")));
		
		plateNumber.sendKeys("UXP936");
		
		//Clicking on Continue button
		WebElement continueButton = driver.findElement(By.id("step-1-2-submit"));
		continueButton.click();
		
		//Getting the XPath of the result
		String xpath = "/html[1]/body[1]/div[2]/div[3]/div[7]/div[2]/div[2]/div[1]/div[1]/div[2]/div[1]/span[1]";

		//Get Expiry Date
		WebElement expiryDate = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		String showExpiryAndRegistration = expiryDate.getText();
		String expiryDateText = expiryDate.getText().split("\\s+")[0];
		
		 //Print Expiry date
		System.out.println("Expiry Date: " + showExpiryAndRegistration);
		
		//formatting the date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy"); 
		
		//Checking if registration is expired or not;
		 try {
	            LocalDate expiryDateParsed = LocalDate.parse(expiryDateText, formatter);
	            LocalDate cutoffDate = LocalDate.now();
	            
	            if (expiryDateParsed.isBefore(cutoffDate)) {
	                System.out.println("Registration is expired.");
	            } else {
	                System.out.println("Registration is valid.");
	            }
	        } catch (DateTimeParseException e) {
	            System.out.println("Unable to parse the expiry date: " + expiryDateText);
	        }
		} finally {
				driver.quit();
			}
		
		

	}

}
