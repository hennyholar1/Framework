package com.test.automation.POMFramework.utilities;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;


/**
// Useful url for email sending
http://www.assertselenium.com/java/emailable-reports-for-selenium-scripts/
http://learn-automation.com/send-report-through-email-in-selenium-webdriver/
*/

public class SendEmails {

	public static void sendSimleEmail()   {

		Email email = new SimpleEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("username", "password"));
		email.setSSLOnConnect(true);
		try {
			email.setFrom("eniolaoludare@gmail.com");
			email.setSubject("Oh, just found a bug that needs to be fixed");	
			email.setMsg(("Hello, kindly check the test result with the screenshot and help fix the bug ... :-). \n Test execution report folder below: \n") +
					("C:\\Users\\Da Novenos\\eclipse-workspace\\POMFramework\\test-output\\ExtentHtmlReport")  + "\n" + "Thanks" );
			email.addTo("eniola.oludare@yahoo.com");
			email.send();
			System.out.println("Email sent");	
		} catch (EmailException e) {
			System.out.println("Connection error occurred: Email could not be sent!");;
		}
	}

	
	public static void sendEmailWithAttachment() throws EmailException  {

		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath("mypictures/john.jpg");
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Picture of John");
		attachment.setName("John");

		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("mail.myserver.com");			// Get server SMTP
		email.addTo("jdoe@somewhere.org", "John Doe");	// Get the port number
		email.setFrom("me@apache.org", "Me");
		email.setSubject("The picture");
		email.setMsg("Here is the picture you wanted");

		// add the attachment
		email.attach(attachment);

		// send the email
		email.send();

	}

	
	public static void emailWithReferenceAttachment() throws EmailException, MalformedURLException {
		
		// Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		attachment.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription("Apache logo");
		attachment.setName("Apache logo");

		// Create the email message
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("mail.myserver.com");
		email.addTo("jdoe@somewhere.org", "John Doe");
		email.setFrom("me@apache.org", "Me");
		email.setSubject("The logo");
		email.setMsg("Here is Apache's logo");

		// add the attachment
		email.attach(attachment);

		// send the email
		email.send();

	}
}
