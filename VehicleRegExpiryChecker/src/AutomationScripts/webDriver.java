package AutomationScripts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class webDriver {

	public static void main(String[] args) {
		//Path of Chrome driver
		System.setProperty("webdriver.chrome.driver","E:\\Selenium\\chromedriver-win64\\chromedriver.exe");
		//chromeDriver class
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		//statements for initiating drivers in other brow
		//webDriver driver = new FirefoxDriver();
		//EdgeDriver driver = EdgeDriver();
		driver.get("https://account.ezyreg.sa.gov.au/account/check-registration.htm");

	}

}
