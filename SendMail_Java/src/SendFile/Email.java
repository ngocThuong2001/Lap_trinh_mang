package SendFile;

public class Email {
    private String userName;
    private String pass;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String text;
    private String filePath;

    public Email(String userName, String pass, String[] to, String[] cc, String[] bcc, String subject, String text, String filePath) {
        this.userName = userName;
        this.pass = pass;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.text = text;
        this.filePath = filePath;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }

    public String[] getTo() {
        return to;
    }

    public String[] getCc() {
        return cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getFilePath() {
        return filePath;
    }
    
    
}