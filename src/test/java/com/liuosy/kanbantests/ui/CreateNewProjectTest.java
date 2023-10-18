package com.liuosy.kanbantests.ui;

import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;


public class CreateNewProjectTest {
    private String webAddress = "http://localhost/login";
    private String adminLogin = "admin";
    private String adminPassword = "admin";
    String newProjectName = "LS New Project";



        @Feature("UI Testing")
        @Test (testName = "Create New Project By Admin User", groups = "UITests")
        public void createNewProject() throws InterruptedException {
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get(webAddress);

            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            SoftAssert softAssert = new SoftAssert();

            WebElement userName =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"form-username\"]")));
            WebElement userPassword =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"form-password\"]")));
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type=\"submit\"]")));

            userName.clear();
            userName.sendKeys(adminLogin);
            userPassword.clear();
            userPassword.sendKeys(adminPassword);
            signInButton.click();


            WebElement newProjectButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class=\"page-header\"]//a[@href=\"/project/create\"]")));
            newProjectButton.click();


            WebElement projectName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id=\"form-name\"]")));
            projectName.clear();
            projectName.sendKeys(newProjectName);

            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("*//button[@class=\"btn btn-blue\"]")));
            saveButton.click();

            WebElement projectTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), \"LS New Project\")]")));
            String projectTitleActual = projectTitle.getText();
            softAssert.assertEquals(projectTitleActual, newProjectName);

            WebElement projectOwner = driver.findElement(By.xpath("//div[@class=\"sidebar-content\"]//li[contains (text(), \"Project owner\")]/strong[text() = 'admin']"));
            softAssert.assertEquals(projectOwner.getText(), adminLogin);
            softAssert.assertAll();

            driver.quit();

    }

}