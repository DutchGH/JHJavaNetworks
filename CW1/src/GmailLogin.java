import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by jake on 23/02/2017.
 */
public class GmailLogin extends Login{

    private final String username;
    private final String password;
    protected Session userSession;
    private Properties sendProp;
    private Properties recProp;

    public GmailLogin(String un, String pw) {
        username = un;
        password = pw;
        sendProp = setSendProp();
        //recProp = setRecProp();
        userSession = setSession(un, pw);
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

    public String getUserName() {
        return this.username;
    }

    public Properties getSendProp() {
        return this.sendProp;
    }

    public Properties setRecProp() {
        String host = "imap.gmail.com";
        String username = this.username;
        String password = this.password;
        Properties props = new Properties();
        props.setProperty("mail.imap.ssl.enable", "true");
        // set any other needed mail.imap.* properties here
        Session session = Session.getInstance(props);
        Store store = session.getStore("imap");
        store.connect(host, username, password);
        return props;
    }

    public Properties getRecProp() {
        return this.recProp;
    }



    //Creates A new Gmail authentication session
    public Session setSession(String un, String pw) {
        return Session.getInstance(this.getSendProp(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(un, pw);
            }
        });
    }

    public Session getSession() {
        return this.userSession;
    }

}
