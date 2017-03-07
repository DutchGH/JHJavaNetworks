/**
 * Created by jake on 23/02/2017.
 */

import java.io.Console;
import java.net.Authenticator;


public class GmailApp {
    public static void main(String[] args) throws Exception{
        Boolean running = true;
        Boolean endProg = false;
        Console console = System.console();
        
        System.out.println("========== GMAIL APPLICATION ==========");
        System.out.println("========== JACOB HOLLAND - SC15J3H ==========");

        String user = console.readLine("[%s] ", "Username");
        char[] paswd = console.readPassword("[%s] ", "Password");

        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }

        GmailLogin acc = new GmailLogin(user, new String(paswd));

        while (running) {
            System.out.printf("\n\nWhat would you like to do?\n\n");
            System.out.printf("'read' - Read Email\n'send' - Send Email\n'exit' - Exit");
            String command = console.readLine("[%s] ", "Command");
            String input = command.toLowerCase();
            switch (input) {
                case "read":
                    CheckMail inbox = new CheckMail();
                    inbox.readMail(acc.getUserName(), new String(paswd));
                    break;
                case "send":
                    SendMail mail = new SendMail(acc.getUserName(), new String(paswd));
                    mail.compose(console);
                    mail.send();
                    break;
                case "exit":
                    System.out.println("Have A Nice Day!");
                    running = false;
            }

        }
    }

}
