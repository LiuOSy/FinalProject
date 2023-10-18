package com.liuosy.kanbantests.ui;

import com.liuosy.kanbantests.common.BaseUITest;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.Assert;

public class LoginTest extends BaseUITest {

    @DataProvider(name = "LoginHappyPassData")
    public Object[][] generateLoginHappyPassTestData() {
        return new Object[][] {
                {"admin", "admin"}
        };
    }

    @DataProvider(name = "LoginErrorData")
    public Object[][] generateLoginErrorTestData() {
        return new Object[][] {
                {"test", "admin", "Bad username or password"},
                {"admin", "test", "Bad username or password"}
        };
    }

    @Feature("UI Testing")
    @Test(testName = "UI Login - Happy Pass Test", groups = "UITests", dataProvider = "LoginHappyPassData", priority = 1)
    public void testHappyPassLogin(String login, String password) throws InterruptedException {

        loginToTheApplication(login, password);

        WebElement pageTitle = driver.findElement(By.xpath("//h1/span[contains(text(), \"Dashboard for admin\")]"));
        String pageTitleText = pageTitle.getText();

        Assert.assertEquals(pageTitleText, "Dashboard for admin", "Login Error!");

    }

    @Feature("UI Testing")
    @Test (testName = "UI Login - Incorrect Login/Password Test", dataProvider = "LoginErrorData", priority = 0)
    public void testIncorrectLoginData(String login, String password, String expectedErrorMessage) throws InterruptedException {

        loginToTheApplication(login, password);

        WebElement pageTitle = driver.findElement(By.xpath("//p[@class=\"alert alert-error\"]"));
        String pageTitleText = pageTitle.getText();

        Assert.assertEquals(pageTitleText, expectedErrorMessage, "Error!");
    }

    @AfterClass(alwaysRun = true)
    public void closeWebDriver(){
        driver.quit();
    }

    }
