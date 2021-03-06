package ChatApp;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
public class Client3 extends JFrame implements ActionListener
{
    String username;
    PrintWriter pw;
    BufferedReader br;
    JTextArea  chatmsg;
    JTextField chatip;
    JButton send, exit;
    Socket chatusers;

    public Client3(String uname,String servername) throws Exception {
        super(uname);
        this.username = uname;
        chatusers  = new Socket(servername,80);
        br = new BufferedReader( new InputStreamReader( chatusers.getInputStream()) ) ;
        pw = new PrintWriter(chatusers.getOutputStream(),true);
        pw.println(uname);
        buildInterface();
        new Client3.MessagesThread().start();
    }

    public void buildInterface() throws SQLException
    {
        send = new JButton("Send");
        exit = new JButton("Exit");
        chatmsg = new JTextArea();
        chatmsg.setRows(30);
        chatmsg.setColumns(20);
        chatmsg.setEditable(false);
        chatip  = new JTextField(20);
        JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel bp = new JPanel( new FlowLayout());
        bp.add(chatip);

        bp.add(send);
        bp.add(exit);
        bp.setBackground(Color.green);
        bp.setName("Instant Messenger");
        add(bp,"South");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(10,200);
        setVisible(true);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == exit ) {
            pw.println("end");
            System.exit(0);
        }else {
            pw.println(chatip.getText());
            chatip.setText(null);
        }
    }

    public void saveInDB(String msg, String sender, String starrer){

    }

    class  MessagesThread extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while(true) {
                    line = br.readLine();
                    chatmsg.append(line + "\n");
                }
            } catch(Exception ex) {}
        }
    }

    public static void main(String[] args) {
        String userName = JOptionPane.showInputDialog(null,"Please enter your name to begin:", "Instant Chat Application",
                JOptionPane.PLAIN_MESSAGE);
        String servername = "localhost";
        try {
            new Client3( userName ,servername);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
