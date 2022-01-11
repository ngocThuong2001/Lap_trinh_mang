package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.JOptionPane;
public class ClientTransferController implements ActionListener {
    private ClientTransferView view;
 
    public ClientTransferController(ClientTransferView view) {
        this.view = view;
        view.getBtnBrowse().addActionListener(this);
        view.getBtnSendFile().addActionListener(this);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(view.getBtnBrowse().getText())) {
            view.chooseFile();
        }
        if (e.getActionCommand().equals(view.getBtnSendFile().getText())) {
            String host = view.getTextFieldHost().getText().trim();
            int port = Integer.parseInt(view.getTextFieldPort().getText().trim());
            String sourceFilePath = view.getTextFieldFilePath().getText();
            if (host != "" && sourceFilePath != "") {
          
                String destinationDir = "D:\\";
                TCPClient tcpClient = new TCPClient(host, port, 
                    view.getTextAreaResult());
                tcpClient.connectServer();
                tcpClient.sendFile(sourceFilePath, destinationDir);
                tcpClient.closeSocket();
            } else {
                JOptionPane.showMessageDialog(view, "Host, Port "
                    + "và FilePath phải khác rỗng.");
            }
        }
    }
}