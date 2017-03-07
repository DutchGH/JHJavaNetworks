/**
 * Created by jake on 23/02/2017.
 */

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.Console;
import java.util.Properties;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
private String to;
private String from;
private String message;
private String subject;
private Session userSession;
private Properties sendProperties;

  
    public SendMail(String un, String pw) {
        sendProperties = setSendProp();
        userSession = setSession(un, pw);
        to = "";
        from = un;
        message = "";
        subject = "";
    }

    public Session setSession(String un, String pw) {
    return Session.getInstance(this.getSendProp(), new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(un, pw);
            }
        });
    }

    //Sets the message for the object
    public void setMessage(String message) {
        this.message = message;
    }

    //Return the objects message contents 
    public String getMessage() {
        return this.message;
    }

    //Set the mails sender
    public void setFrom(String from) {
        this.from = from;
    }

    //Retreieve Sender Information from object
    public String getFrom() {
        return this.from;
    }

    //Set Destination address for object 
    public void setTo(String to) {
        this.to = to;
    }

    //Return receipent information
    public String getTo() {
        return this.to;
    }

    //Set subject for mail object
    public void setSubject(String subject) {
        this.subject = subject;
    }

    //retrieve subject string
    public String getSubject() {
        return this.subject;
    }

    //Set up SMTP server for Gmail Account
    public Properties setSendProp() {
        String host = "smtp.gmail.com";
        Properties gSend = new Properties();
        gSend.put("mail.smtp.auth", "true");
        gSend.put("mail.smtp.starttls.enable", "true");
        gSend.put("mail.smtp.host", host);
        gSend.put("mail.smtp.port", "587");

        return gSend;
    }

        public Properties getSendProp() {
        return this.sendProperties;
    }




    public Session getSession() {
        return this.userSession;
    }
    public void compose(Console console){

        System.out.println("Who would You like to send the email to?");
        String to = console.readLine("[%s] ", "To");
        setTo(to);
        System.out.println("What is the Subject?");
        String sub = console.readLine("[%s] ", "Subject");
        setSubject(sub);
        System.out.println("What would you like to say?");
        String msg = console.readLine("[%s] ", "Message");
        setMessage(msg);

    }

    //Creates a new mime sessions using the gmail session
    public void send() {
    try {

        Message message = new MimeMessage(this.userSession);

        message.setFrom(new InternetAddress(this.getFrom()));

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getTo()));

        message.setSubject(this.getSubject());

        message.setText(this.getMessage());

        //Send The Message
        Transport.send(message);

        System.out.println("Message Sent!");


    } catch (AddressException e) {
        e.printStackTrace();
    } catch (javax.mail.MessagingException e) {
        e.printStackTrace();
    }

}


}