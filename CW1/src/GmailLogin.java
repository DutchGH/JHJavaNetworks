import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Store;
import javax.mail.MessagingException;  
import javax.mail.NoSuchProviderException;
import java.io.IOException;

/* This Class is More likely to be legacy code and should eventually be factored out
However, as the main application is built arround it, it will be kept */


public class GmailLogin {

    private final String username;
    private final String password;

    //Create new login with username and password
    public GmailLogin(String un, String pw) {
        username = un;
        password = pw;
   }

   //Returns the username for other classes to use.
    public String getUserName() {
        return this.username;
    }

}
