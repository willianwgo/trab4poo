package Supermarket.Servidor;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailApp
{
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
        
    public static void sendEmail(String produto, String email) {
        try{
            // Step1
            System.out.println("Enviando email...");
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");

            // Step2
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            generateMailMessage.setSubject("Produto disponivel!");
            String emailBody = "Seu produto (" + produto + ") ja esta disponivel para compra!. " + "<br><br> Supermarket, <br>Supermarket Admin";
            generateMailMessage.setContent(emailBody, "text/html");

            // Step3
            Transport transport = getMailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", "pootrab4@gmail.com", "pootrab44");
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        }
        catch(AddressException e) {}
        catch(MessagingException e) {}
    }
}