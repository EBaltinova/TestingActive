package ru.stqa.pft.mantis.tests;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.appmanager.HttpSession;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;


public class PasswordChangeTests extends TestBase {

    @Test
    public void passwordChange() throws IOException, MessagingException {
        String user = "user1606368166948";
        String email = user + "@localhost";
        String password = "password";
        app.passChange().signIn("administrator", "root");
        app.passChange().manageUsers();
        List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);
        String confirmationLink = findConfirmationLink(mailMessages, email);
        app.passChange().finish(confirmationLink, password);
    }
    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);


    }
}
