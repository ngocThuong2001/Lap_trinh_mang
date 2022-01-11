package SendMail;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class FiletransferClient extends Frame {
public static String strHostAddress = "";
public static int intPortNumber = 0, intMaxClients = 0;
public static Vector vecConnectionSockets = null;
public static FiletransferClient objFileTransfer;
public static String strFileName = "",strFilePath = "";
public static Socket clientSocket = null;
public static ObjectOutputStream outToServer = null;
public static ObjectInputStream inFromServer = null;
public static void main (String [] args) throws IOException {
	BufferedReader stdin = new BufferedReader(new InputStreamReader
	(System.in));
	System.out.print("Nhap dia chi cua may server de ket noi: ");
	System.out.flush();
	strHostAddress = stdin.readLine();
	System.out.print("Nhap dia chi cong de ket noi voi may server: ");
	System.out.flush();
	intPortNumber = Integer.parseInt(stdin.readLine());
	objFileTransfer = new FiletransferClient();
}
public Label lblSelectFile;
public Label lblTitle;
public Label lblStudentName;
public Label lblStudentClass;
public TextField tfFile;
public Button btnBrowse;
public Button btnSend;
public Button btnReset;
public FiletransferClient () {
	setTitle("Chuong trinh truyen File phia Client");
	setSize(400 , 300);
	setLayout(null);
	addWindowListener(new WindowAdapter () { public void windowClosing
	(WindowEvent e) { System.exit(0); } } );
	lblTitle = new Label("Chuong trinh truyen File may Client ");
	add(lblTitle);
	lblTitle.setBounds(50,30,450,50);
	lblSelectFile = new Label("Duong dan file can truyen:");
	add(lblSelectFile);
	lblSelectFile.setBounds(50,100,200,20);
	lblStudentName = new Label("Client: Nguyen Thanh Cam");
	add(lblStudentName);
	lblStudentName.setBounds(130,250,200,20);
	lblStudentClass = new Label("Lop : Lap Trinh Mang");
	add(lblStudentClass);
	lblStudentClass.setBounds(130,270,100,20);
	tfFile = new TextField("");
	add(tfFile);
	tfFile.setBounds(50,134,200,20);
	btnBrowse = new Button("Chon File");
	btnBrowse.addActionListener(new buttonListener());
	add(btnBrowse);
	btnBrowse.setBounds(283,133,70,20);
	btnSend = new Button("Gui");
	btnSend.addActionListener(new buttonListener());
	add(btnSend);
	btnSend.setBounds(100,200,50,20);
	btnReset = new Button("Xoa");
	btnReset.addActionListener(new buttonListener());
	add(btnReset);
	btnReset.setBounds(170,200,50,20);
	show();
	try {
		clientSocket = new Socket (strHostAddress,intPortNumber);
		outToServer = new
		ObjectOutputStream(clientSocket.getOutputStream());
		outToServer.flush();
		inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		int intFlag = 0;
		while (true) {
		Object objRecieved = inFromServer.readObject();
		switch (intFlag) {
		case 0:
		if (objRecieved.equals("IsFileTransfered")) {
		intFlag++;
		}
		break;
		case 1:
		strFileName = (String) objRecieved;
		int intOption =
		JOptionPane.showConfirmDialog(this,clientSocket.getInetAddress().getHostName()+" dang gui "+strFileName+"!\nBan co chac chan nhankhong?","Thongbao",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE); //xacnhan
		if (intOption == JOptionPane.YES_OPTION) {
		intFlag++;
		} else {
		intFlag = 0;
		}
		break;
		case 2:
		byte[] arrByteOfReceivedFile = (byte[])objRecieved;
		FileOutputStream outToHardDisk = new FileOutputStream(strFileName);
		outToHardDisk.write(arrByteOfReceivedFile);
		intFlag = 0;
		JOptionPane.showMessageDialog(this,"Ban dong y nhan file nay tu Server","Thong bao",JOptionPane.INFORMATION_MESSAGE);//file dc nhan;su chung thuc, su xac thuc
		break;
		}
		Thread.yield();
		}
		} catch (Exception e) {System.out.println(e);}
		}
public static String showDialog () {
	FileDialog fd = new FileDialog(new Frame(),"Select File...",FileDialog.LOAD);
	fd.show();
	return fd.getDirectory()+fd.getFile();
}
private class buttonListener implements ActionListener {
	public void actionPerformed (ActionEvent ae) {
	byte[] arrByteOfSentFile = null;
	if (ae.getSource() == btnBrowse) {
	strFilePath = showDialog();
	tfFile.setText(strFilePath);
	int intIndex = strFilePath.lastIndexOf("\\");
	strFileName = strFilePath.substring(intIndex+1);
	}
	if (ae.getSource() == btnSend) {
	try {
	FileInputStream inFromHardDisk = new FileInputStream (strFilePath);
	int size = inFromHardDisk.available();
	arrByteOfSentFile = new byte[size];
	inFromHardDisk.read(arrByteOfSentFile,0,size);
	outToServer.writeObject("IsFileTransfered");
	outToServer.flush();
	outToServer.writeObject(strFileName);
	outToServer.flush();
	outToServer.writeObject(arrByteOfSentFile);
	outToServer.flush();
	JOptionPane.showMessageDialog(null,"Ban da gui thanh cong file toi Server","Xac nhan",JOptionPane.INFORMATION_MESSAGE);
	} catch (Exception ex) {}
	}
	if (ae.getSource() == btnReset) {
	tfFile.setText("");
	}}}
	}
class ThreadedConnectionSocket extends Thread {
	public Socket connectionSocket;
	public ObjectInputStream inFromClient;
	public ObjectOutputStream outToClient;
	public ThreadedConnectionSocket (Socket s) {
		connectionSocket = s;
		try {
			outToClient = new
				ObjectOutputStream(connectionSocket.getOutputStream());
				outToClient.flush();
				inFromClient = new
				ObjectInputStream(connectionSocket.getInputStream( ));
				} catch (Exception e) {System.out.println(e);}
				start();
				}
				public void run () {
				try {
				int intFlag = 0;
				String strFileName = "";
				while (true) {
				Object objRecieved = inFromClient.readObject();
				switch (intFlag) {
				case 0:
				if (objRecieved.equals("IsFileTransfered")) {
				intFlag++;
				}
				break;
				case 1:
				strFileName = (String) objRecieved;
				int intOption =
				JOptionPane.showConfirmDialog(null,connectionSocket.getInetAddress()
				.getHostName()+" dang gui "+strFileName+"!\nBan co chac chan nhan khong?","Thong bao",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (intOption == JOptionPane.YES_OPTION) {
				intFlag++;
				} else {
				intFlag = 0;
				}
				break;
				case 2:
				byte[] arrByteOfReceivedFile = (byte[])objRecieved;
				FileOutputStream outToHardDisk = new FileOutputStream(strFileName);
				outToHardDisk.write(arrByteOfReceivedFile);
				intFlag = 0;
				JOptionPane.showMessageDialog(null,"Ban da gui thanh cong file toi Server","Xac nhan",JOptionPane.INFORMATION_MESSAGE);
				break;
				}
				Thread.yield();
				}
				} catch (Exception e) {System.out.println(e);}
			}
}