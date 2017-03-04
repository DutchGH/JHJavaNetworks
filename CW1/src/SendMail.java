/**
 * Created by jake on 23/02/2017.
 */

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
private String to;
private String from;
private String message;
private String subject;

    public SendMail(String t, String f, String sub, String msg) {
        to = t;
        from = f;
        subject = sub;
        message = msg;
    }


    public String getMessage() {
        return this.message;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public String getSubject() {
        return this.subject;
    }


    public void send(Session s) {
    try {

        Message message = new MimeMessage(s);

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