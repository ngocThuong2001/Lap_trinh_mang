package SendFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JFileChooser;

/**
 *
 * @author Administrator
 */
public class Action {

    private Screen gui;

    public Action() {
        this.gui = new Screen();
        prepareGui();
    }

    public Screen getGui() {
        return gui;
    }

    public static Session Login(String username, String pass) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", MailConfig.HostSend);
        props.put("mail.smtp.socketFactory.port", MailConfig.SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", MailConfig.SSL_PORT);

        PasswordAuthentication Auth = new PasswordAuthentication(username, pass);
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return Auth;
            }
        });
        return session;
    }

    public void prepareGui() {
        gui.getBtm_chooseFile().setActionCommand("chooseFile");
        gui.getBtm_chooseFile().addActionListener(new ButtonClick());
        gui.getBtn_send().setActionCommand("send");
        gui.getBtn_send().addActionListener(new ButtonClick());
    }

    public File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        File f = null;
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            f = fileChooser.getSelectedFile();
        }
        return f;
    }

    private void showFile(File f) {
        gui.getLb_file().setText(f.getPath());
    }

    class ButtonClick implements ActionListener {

        File file;

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getActionCommand().equals("chooseFile")) {
                file = chooseFile();
                if (file != null) {
                    showFile(file);
                }
            } else if (ae.getActionCommand().equals("send")) {
                SendAction();
            }
        }

        private String[] getListEmail(String str) {
            if (str.equals("")) {
                return null;
            } else if (!str.contains(",")) {
                String[] strArr = {str};
                return strArr;
            }
            return str.split(",");
        }

        private void SendAction() {
            String username = gui.getTf_username().getText().trim();
            String pass = gui.getTf_password().getText().trim();
            String[] listTo = getListEmail(gui.getTf_to().getText().trim());
            String[] listCc = getListEmail(gui.getTf_cc().getText().trim());
            String[] listBcc = getListEmail(gui.getTf_bcc().getText().trim());
            String filePath = gui.getLb_file().getText().trim();
            String mess = gui.getTf_message().getText();
            String subject = gui.getTf_subject().getText();
            Session sess = Login(username, pass);
            SendMail sender = new SendMail(sess);
            System.out.println(filePath);
            sender.sendEmail(listTo, listCc, listBcc, mess, filePath, subject);
        }
    }
}