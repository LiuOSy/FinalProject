package com.liuosy.kanbantests.ui;

import com.liuosy.kanbantests.common.BaseUITest;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class CloseTheTaskTest extends BaseUITest {

    //Precondition - Creating User, Project and Task
    @BeforeClass(alwaysRun = true)
    public void dataSetUp() throws InterruptedException {
        createUserAndProject();
        loginToTheApplication(newUserName, newUserPassword);
        createAndEnterTheNewTask();
    }

    @Feature("UI Testing")
    @Test(testName = "Close The Task", groups = "UITests")
    public void closeTheTaskTest() throws InterruptedException {

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        SoftAssert softAssert = new SoftAssert();

        //Verify task status before closure
        String taskStatusBeforeTest = driver.findElement(By.xpath("//span[contains(text(), 'open')]")).getText();
        softAssert.assertEquals(taskStatusBeforeTest, "open");


        WebElement closeTheTaskLink = driver.findElement(By.xpath("//a[text() =\"Close this task\"]"));
        wait.until(elementToBeClickable(closeTheTaskLink));
        closeTheTaskLink.click();


        //Verify 'Close a task' modal window is opened

        WebElement modalHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Close a task')]")));
        softAssert.assertEquals(modalHeader.getText(), "Close a task");

        WebElement confirmTaskClosure = driver.findElement(By.xpath("//button[@id=\"modal-confirm-button\"]"));
        confirmTaskClosure.click();

        //Verify task status before closure
        WebElement taskStatusAfterTest = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'closed')]")));
        softAssert.assertEquals(taskStatusAfterTest.getText(), "closed");
        softAssert.assertAll();

    }

    @AfterClass(alwaysRun = true)
    public void dataSetupRemoval(){
        deleteProject(projectId);
        deleteUser();
        driver.quit();

    }

}
