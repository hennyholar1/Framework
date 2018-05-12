package com.test.automation.POMFramework.utilities;

// package utilities;

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;


public class SendEmails {
	
	public static void sendEmai(String pathToFileToBeAttached, String emailReciever){
	
		// I need smtp host, port, socketFactory port information 
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
				messageBodyPart1.setText(
						("Hello, kindly check the test result/report with the screenshot and help fix the bug ... :-). "
						+ "\n Test execution report folder below: \n") 
						+ ("path to testOut \\ExtentHtmlReport")  + "\n" + "Thanks");
				
				// Create another object to add another content
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				// Create data source and pass the filename
				DataSource source = new FileDataSource(pathToFileToBeAttached);
				messageBodyPart2.setDataHandler(new DataHandler(source));
				messageBodyPart2.setFileName(pathToFileToBeAttached);
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

	
	
	// supply your SMTP host name
		private static String host = "";	
		private static String to = "";
		private static String from = "";
		private static String cc = "";
		private static String bcc = "";

		
	public static void sendEmai(String from, String to, String subject, String contents) throws MessagingException {		
		
		Properties prop = System.getProperties();
		prop.setProperty("mail.smtp.host", host);
		
		Session session = Session.getDefaultInstance(prop);
		
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setSubject(subject);
		message.setContent(contents, "text/html");

		List<String> toList = getAddress(to);
		for (String address : toList) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
		}
		
		List<String> ccList = getAddress(cc);
		for (String address : ccList) {
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(address));
		}
		
		List<String> bccList = getAddress(bcc);
		for (String address : bccList) {
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(address));
		}
		
		Transport.send(message);
		}
		
		public static void sendEmai(String to, String subject, String contents) throws MessagingException {
			sendEmai(from, to, subject, contents);
		}
		
		public static void sendMai(String subject, String contents) throws MessagingException {
			sendEmai(from, to, subject, contents);
		}
		
		private static List getAddress(String address) {
			List addressList = new ArrayList();
			
			if (address.isEmpty())
				return addressList;
			
			if (address.indexOf(";") > 0) {
				String[] addresses = address.split(";");
				
				for (String a : addresses) {
					addressList.add(a);
				}
			} else {
				addressList.add(address);
			}
			
			return addressList;
		}

	
	
/*
public static void sendEmail(String message, String testCaseName)
{
MailMessage mail = new MailMessage();
mail.To.Add("your-to-email-address-goes-here");
mail.From = new MailAddress("your-from-email-address-goes-here ");
mail.Subject = "your-mail-subject-goes-here";
mail.Body = "Test Case Name: " + testCaseName;
mail.Body += Environment.NewLine;
mail.Body += message;
SmtpClient smtp = new SmtpClient();
smtp.Host = "localhost";
smtp.Port = 25;
smtp.Send(mail);
}*/

}
