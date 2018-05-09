package com.test.automation.POMFramework.utilities;

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;


/**
// Useful url for email sending
http://www.assertselenium.com/java/emailable-reports-for-selenium-scripts/
http://learn-automation.com/send-report-through-email-in-selenium-webdriver/
*/

public class SendEmails {

	public static void sendEmai(String filename, String emailReciever){
	
			Properties props = new Properties();
			// this will set host of server- you can change based on your requirement 
			props.put("mail.smtp.host", "smtp.stls.frb.org");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			// This will handle the complete authentication
			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("Olu.Eniola@stls.frb.org", "my window password");
						}
					});
			try {
				// Create object of MimeMessage class
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("Olu.Eniola@stls.frb.org"));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailReciever));
				message.setSubject("Oh, bug(s) detected and need(s) to be fixed");
				// Create object to add Multi-media type content
				BodyPart messageBodyPart1 = new MimeBodyPart();
				// Set the body of email
				messageBodyPart1.setText(("Hello, kindly check the test result with the screenshot and help fix the bug ... :-). \n Test execution report folder below: \n") +
						("path to testOut \\ExtentHtmlReport")  + "\n" + "Thanks");
				// Create another object to add another content
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				// Create data source and pass the filename
				DataSource source = new FileDataSource(filename);
				messageBodyPart2.setDataHandler(new DataHandler(source));
				messageBodyPart2.setFileName(filename);
				// Create object of MimeMultipart class
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart2);
				multipart.addBodyPart(messageBodyPart1);
				message.setContent(multipart);
				Transport.send(message);
				System.out.println("=====Email Sent=====");
			} catch (MessagingException e) {
				throw new RuntimeException(e);
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
