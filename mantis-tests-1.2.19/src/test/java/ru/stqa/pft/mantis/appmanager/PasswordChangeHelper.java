package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class PasswordChangeHelper extends HelperBase {

    public PasswordChangeHelper(ApplicationManager app) {
        super(app);
    }
    public void signIn(String user, String password) {
        type(By.name("username"), user);
        type(By.name("password"), password);
        click(By.cssSelector("input[value='Login']"));
    }
    public void manageUsers() {
        click(By.cssSelector("body > div:nth-child(4) > p > span:nth-child(1) > a"));
        click(By.cssSelector("body > table:nth-child(8) > tbody > tr:nth-child(20) > td:nth-child(1) > a"));
        click(By.cssSelector("input[value='Reset Password']"));
    }
    public void finish(String confirmationLink, String password) {
        driver.get(confirmationLink);
        type(By.name("password"), password);
        type(By.name("password_confirm"), password);
        click(By.cssSelector("input[value='Update User']"));
    }


}
