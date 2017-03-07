/**
 * Created by jake on 23/02/2017.
 */

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
public class CheckMail {

        /* Returns a list of emails sent to the user address.
        It sets up a new IMAP connection and stores the inboxeds messages in
        a Message Array*/
        public void readMail(String un, String pw) {
        try {
            String host = "imap.gmail.com";
            String username = un;
            String password = pw;
            Properties props = new Properties();
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.imap.host", host);
            props.setProperty("mail.imap.port", "993");
            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(host, username, password);
            Folder emailFolder = store.getFolder("INBOX");  
            emailFolder.open(Folder.READ_ONLY);  

            //retrieve the messages from the folder in an array and print it  
            Message[] messages = emailFolder.getMessages();  
            for (int i = 0; i < 150; i++) {  
                Message message = messages[i];  
                System.out.println("---------------------------------");  
                System.out.println("Email Number " + (i + 1));  
                System.out.println("Subject: " + message.getSubject());  
                System.out.println("From: " + message.getFrom()[0]);  
                //System.out.println("Text: " + message.getContent().toString());  
            }  

            emailFolder.close(false);  
            store.close();
        } 
        //catch (IOException e) {e.printStackTrace();}
        catch (NoSuchProviderException e) {e.printStackTrace();}   
        catch (MessagingException e) {e.printStackTrace();}  
    }  

}
