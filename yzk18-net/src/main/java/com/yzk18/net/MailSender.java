package com.yzk18.net;

import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.IOHelpers;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MailSender {
    private String hostName;
    private Integer smtpPort;
    private boolean auth=true;
    private String userName;
    private String password;
    private boolean SSLOnConnect=true;
    private String subject;
    private InternetAddress from;
    private List<InternetAddress> toAddressList= new LinkedList<>();
    private List<InternetAddress> ccAddressList= new LinkedList<>();
    private List<InternetAddress> bccAddressList= new LinkedList<>();
    private Multipart content;
    private Map<String,byte[]> attachments = new LinkedHashMap<>();

    public MailSender setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public MailSender setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
        return this;
    }

    public MailSender setAuth(boolean auth) {
        this.auth = auth;
        return this;
    }

    public MailSender setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public MailSender setPassword(String password) {
        this.password = password;
        return this;
    }

    public MailSender setSSLOnConnect(boolean SSLOnConnect) {
        this.SSLOnConnect = SSLOnConnect;
        return this;
    }

    public MailSender setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailSender setFrom(InternetAddress from) {
        this.from = from;
        return this;
    }

    public MailSender setFrom(String from) {
        try {
            return setFrom(new InternetAddress(from));
        } catch (AddressException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public MailSender setFrom(String from,String personal) {
        try
        {
            return setFrom(new InternetAddress(from,personal));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public MailSender setHtmlMessage(String message)
    {
        this.setMessage(message,"text/html;charset=utf-8");
        return this;
    }

    public MailSender setMessage(String message, String mimeType)
    {
        try
        {
            Multipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(message, mimeType);
            multipart.addBodyPart(contentPart);
            this.content = multipart;
        } catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MailSender setTextMessage(String message)
    {
        return this.setMessage(message,"text/plain;charset=utf-8");
    }

    public MailSender setContent(Multipart content)
    {
        this.content = content;
        return this;
    }

    public MailSender addTo(InternetAddress addr)
    {
        this.toAddressList.add(addr);
        return this;
    }

    public MailSender addTo(String address, String personal)
    {
        try {
            this.toAddressList.add(new InternetAddress(address,personal));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    public MailSender addTo(String address)
    {
        try {
            this.toAddressList.add(new InternetAddress(address));
        } catch (AddressException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    public MailSender addCC(InternetAddress addr)
    {
        this.ccAddressList.add(addr);
        return this;
    }

    public MailSender addCC(String address, String personal)
    {
        try {
            this.ccAddressList.add(new InternetAddress(address,personal));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    public MailSender addCC(String address)
    {
        try {
            this.ccAddressList.add(new InternetAddress(address));
        } catch (AddressException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    public MailSender addBCC(InternetAddress addr)
    {
        this.bccAddressList.add(addr);
        return this;
    }

    public MailSender addBCC(String address, String personal)
    {
        try {
            this.bccAddressList.add(new InternetAddress(address,personal));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    public MailSender addBCC(String address)
    {
        try {
            this.bccAddressList.add(new InternetAddress(address));
        } catch (AddressException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    public MailSender addAttachment(String displayName,byte[] bytes)
    {
        attachments.put(displayName,bytes);
        return this;
    }

    public MailSender addAttachment(File file)
    {
        String displayName = file.getName();
        byte[] bytes = IOHelpers.readAllBytes(file.toString());
        return addAttachment(displayName,bytes);
    }

    public void send()
    {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "SMTP");
        properties.put("mail.host", this.hostName);
        if(smtpPort!=null)
        {
            properties.put("mail.smtp.port", smtpPort);
        }
        properties.put("mail.smtp.ssl.enable", this.SSLOnConnect);
        properties.put("mail.smtp.auth", this.auth);
        Authenticator authenticator=null;
        if(this.auth)
        {
            authenticator = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(userName, password);
                }
            };
        }

        try
        {
            for (Map.Entry<String, byte[]> entryAttach : this.attachments.entrySet())
            {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(entryAttach.getValue(), "application/octet-stream");
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(entryAttach.getKey()));
                this.content.addBodyPart(attachmentBodyPart);
            }
        }
        catch (MessagingException | UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }


        // 获取默认session对象
        Session session;
        if(authenticator==null)
        {
            session = Session.getInstance(properties);
        }
        else
        {
            session = Session.getInstance(properties, authenticator);
        }

        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(this.from);
            for(InternetAddress addr : this.toAddressList)
            {
                message.addRecipient(Message.RecipientType.TO,addr);
            }
            for(InternetAddress addr : this.ccAddressList)
            {
                message.addRecipient(Message.RecipientType.CC,addr);
            }
            for(InternetAddress addr : this.bccAddressList)
            {
                message.addRecipient(Message.RecipientType.BCC,addr);
            }
            message.setSubject(this.subject);
            message.setContent(this.content);
            Transport.send(message);
        }catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
