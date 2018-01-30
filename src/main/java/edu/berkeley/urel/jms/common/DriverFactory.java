package edu.berkeley.urel.jms.common;

import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;


public class DriverFactory {
	private static volatile DriverFactory instance;
	private WebDriver driver;

	public static DriverFactory getInstance() {
		DriverFactory localInstance = instance;
		if (localInstance == null) {
			synchronized (DriverFactory.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new DriverFactory();
				}
			}
		}
		return localInstance;
	}

	public WebDriver getDriver() throws WebDriverException {
		return getInstance().driver;
	}

	private WebDriver initFirefox() {
		return new FirefoxDriver();
	}
	
	private WebDriver initChrome() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("chrome.switches","--disable-extensions");
		//System.setProperty("webdriver.chrome.driver", "/usr/local/selenium-grid/mac-node/chromedriver");
		//System.setProperty("webdriver.chrome.logfile", "/usr/local/selenium-grid/mac-node/chromedriver.log");
		System.setProperty("webdriver.chrome.driver", ResourceBundle.getBundle("config").getString("webdriver.chrome.driver"));
		System.setProperty("webdriver.chrome.logfile", ResourceBundle.getBundle("config").getString("webdriver.chrome.logfile"));
	    System.setProperty("webdriver.chrome.args", "--disable-logging");
	    System.setProperty("webdriver.chrome.silentOutput", "true");
		return new ChromeDriver(options);
	}

	public static void start(String browser) {
		if (getInstance().driver == null) {
			switch(browser){
			case "FFX":
				getInstance().driver = getInstance().initFirefox();
				break;
			case "CRM":
				//todo: configure ChromeDriver
				getInstance().driver = getInstance().initChrome();
				break;
			default:	
				getInstance().driver = getInstance().initFirefox();
				break;

			}
			
		}
	}
	public static void start() {
		start("FFX");
	}

	public static void quit() {
		getInstance().driver.quit();
		getInstance().driver = null;
	}
}


