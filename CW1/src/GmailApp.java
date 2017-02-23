/**
 * Created by jake on 23/02/2017.
 */

import javax.mail.MessagingException;

public class GmailApp {


    //Run Main Application Here
    public static void main(String[] args) throws MessagingException {
        System.out.println("========== GMAIL APPLICATION ==========");
        System.out.println("========== JACOB HOLLAND - SC15J3H ==========");

        GmailLogin jHolland = new GmailLogin("", "");
        jHolland.sendGmail(jHolland.gmailSession, "jhollandmail@gmail.com", "jhollandmail@gmail.com", "Hello World", "This is a Test");

        System.out.println("Test Complete");

    }


}
