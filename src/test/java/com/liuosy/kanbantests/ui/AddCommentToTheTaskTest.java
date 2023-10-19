package com.liuosy.kanbantests.ui;

import com.liuosy.kanbantests.common.BaseUITest;
import io.qameta.allure.Feature;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;


public class AddCommentToTheTaskTest extends BaseUITest {

    //Precondition - Creating User, Project and Task
    @BeforeClass (alwaysRun = true)
    public void dataSetUp() throws InterruptedException {
        createUserAndProject();
        loginToTheApplication(newUserName, newUserPassword);
        createAndEnterTheNewTask();
    }

    @Feature("UI Testing")
    @Test(testName = "Add Comment To The Task - Happy Pass", groups = "UITests")
    public void addCommentToTheTaskTest() {

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement addAComment = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()=\"Add a comment\"]")));
        addAComment.click();

        WebElement commentField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\"modal-box\"]//textarea[@name=\"comment\"]")));
        commentField.clear();
        commentField.sendKeys("Test comment");
        WebElement saveCommentButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"modal-box\"]//button[@type=\"submit\"]")));
        saveCommentButton.click();

        WebElement commentAdded = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"markdown\"]/p[text()=\"Test comment\"]")));
        String commentActualText = commentAdded.getText();
        Assert.assertEquals(commentActualText, "Test comment");

    }

    @AfterClass (alwaysRun = true)
    public void dataSetupRemoval(){
        deleteProject(projectId);
        deleteUser();
        driver.quit();

    }
}
