import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by jake on 23/02/2017.
 */
public class GmailLogin {

    private final String username;
    private final String password;
    protected Session gmailSession;
    private Properties gProp;

    public GmailLogin(String un, String pw) {
        username = un;
        password = pw;
        initGmailSmtp();
        gmailSession = gSession(un, pw);
    }

    //Set up SMTP server for Gmail Account
    public void initGmailSmtp() {
        String host = "smtp.gmail.com";
        gProp = new Properties();
        gProp.put("mail.smtp.auth", "true");
        gProp.put("mail.smtp.starttls.enable", "true");
        gProp.put("mail.smtp.host", host);
        gProp.put("mail.smtp.port", "587");
    }

    //Creates A new Gmail authentication session
    public Session gSession(String un, String pw) {
        return Session.getInstance(gProp, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(un, pw);
            }
        });
    }

    public void sendGmail(Session gmailSession, String to, String from, String sub, String msg) {
        try {

            Message message = new MimeMessage(gmailSession);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject(sub);

            message.setText(msg);

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
