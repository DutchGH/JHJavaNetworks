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



/**
 * Created by jake on 23/02/2017.
 */
public class GmailLogin {

    private final String username;
    private final String password;
    private Session userSession;
    private Properties sendProp;

    public GmailLogin(String un, String pw) {
        username = un;
        password = pw;
   }

    public String getUserName() {
        return this.username;
    }

}
