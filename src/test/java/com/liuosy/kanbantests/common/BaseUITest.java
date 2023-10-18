package com.liuosy.kanbantests.common;

import io.restassured.http.Method;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;


public class BaseUITest extends BaseTest {

    public final static String UI_URL = "http://localhost/login";
    protected int projectId;
    protected String newTaskName = String.format("LS New Task + %s", tmst);
    protected WebDriver driver;

    public BaseUITest(){
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty() || browser.equalsIgnoreCase(Browser.CHROME.toString())) {
            driver = new ChromeDriver(getCommonChromeOptions());
        } else if (browser.equalsIgnoreCase(Browser.CHROME_HEADLESS.toString())) {
            driver = new ChromeDriver(getHeadlessChromeOptions());
        } else if (browser.equalsIgnoreCase(Browser.FIREFOX.toString())) {
            driver = new FirefoxDriver(getCommonFirefoxOptions());
            driver.manage().window().maximize();
        }
        else throw new RuntimeException("Incorrect browser value");
    }

    final protected ChromeOptions getCommonChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("wm-window-animations-disabled");
        options.addArguments("ash-disable-smooth-screen-rotation");
        options.addArguments("disable-smooth-scrolling");
        options.addArguments("disable-infobars");
        options.addArguments("disable-default-apps");
        options.addArguments("disable-extensions");
        options.setAcceptInsecureCerts(true);
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("history.saving_disabled", true);
        preferences.put("browser.show_home_button", false);
        preferences.put("credentials_enable_service", false);
        preferences.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", preferences);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation", "load-extension"});
        options.addArguments("--no-sandbox");
        return options;
    }

    final protected ChromeOptions getHeadlessChromeOptions() {
        ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
        return options;
    }

    final protected FirefoxOptions getCommonFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("app.update.disabledForTesting",true);
        options.setCapability("toolkit.cosmeticAnimations.enabled",false);
        options.setCapability("datareporting.policy.dataSubmissionPolicyAccepted",false);
        return options;
    }


    protected void createUserAndProject() {
        userId = createUser();
        projectId = createProject();
        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "addProjectUser", String.format("{\"project_id\": \"%d\", \"user_id\": \"%d\"}", projectId, userId));

        sendRequest(Method.POST, requestBodyJson, 200);

        System.out.printf("UserName = %s. User Password = %s.%n", newUserName, newUserPassword);
        System.out.printf("UserID = %d. ProjectID = %d.", userId, projectId);

    }

    protected void loginToTheApplication(String login, String password) throws InterruptedException {
        driver.manage().window().maximize();
        driver.get(UI_URL);
        WebElement userName =  driver.findElement(By.xpath("//*[@id=\"form-username\"]"));
        WebElement userPassword =  driver.findElement(By.xpath("//*[@id=\"form-password\"]"));
        WebElement signInButton = driver.findElement(By.xpath("//button[@type=\"submit\"]"));

        userName.clear();
        userName.sendKeys(login);

        userPassword.clear();
        userPassword.sendKeys(password);

        signInButton.click();
        Thread.sleep(1000);

    }

    protected void createAndEnterTheNewTask() {

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement projectLink = driver.findElement(By.xpath("//a[contains(text(), 'LS Test Project')]"));
        wait.until(elementToBeClickable(projectLink));
        projectLink.click();

        WebElement selectButton = driver.findElement(By.xpath("//i[@class=\"fa fa-cog\"]"));
        wait.until(elementToBeClickable(selectButton));
        selectButton.click();

        WebElement addNewTaskButton = driver.findElement(By.xpath("//ul[@class=\"dropdown-submenu-open\"]//i[@class=\"fa fa-plus fa-fw js-modal-large\"]"));
        wait.until(elementToBeClickable(addNewTaskButton));
        addNewTaskButton.click();

        WebElement taskTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id=\"form-title\"]")));
        taskTitle.clear();
        taskTitle.sendKeys(newTaskName);
        WebElement saveTaskButton = driver.findElement(By.xpath("//button[@type=\"submit\"]"));
        wait.until(elementToBeClickable(saveTaskButton));
        saveTaskButton.click();

        WebElement taskCreated = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"task-board-title\"]//a[contains(text(), 'LS New Task')]")));
        taskCreated.click();

    }

}
