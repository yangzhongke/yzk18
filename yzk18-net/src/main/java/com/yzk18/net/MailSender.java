package com.yzk18.net;

import com.yzk18.commons.IOHelpers;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * <div lang="zh-cn">SMTP邮件发送器</div>
 */
public class MailSender {
    private String hostName;
    private Integer smtpPort;
    private boolean auth=true;
    private String userName;
    private String password;
    private boolean sSLOnConnect =true;
    private String subject;
    private InternetAddress from;
    private List<InternetAddress> toAddressList= new LinkedList<>();
    private List<InternetAddress> ccAddressList= new LinkedList<>();
    private List<InternetAddress> bccAddressList= new LinkedList<>();
    private Multipart content;
    private Map<String,byte[]> attachments = new LinkedHashMap<>();

    /**
     * <div lang="zh-cn">SMTP服务器地址</div>
     * @param hostName
     * @return
     */
    public MailSender setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    /**
     * <div lang="zh-cn">SMTP端口，如果不设置则用默认端口</div>
     * @param smtpPort
     * @return
     */
    public MailSender setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
        return this;
    }

    /**
     * <div lang="zh-cn">设置是否SMTP，默认为true</div>
     * @param auth
     * @return
     */
    public MailSender setAuth(boolean auth) {
        this.auth = auth;
        return this;
    }

    /**
     * <div lang="zh-cn">SMTP用户名</div>
     * @param userName
     * @return
     */
    public MailSender setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * <div lang="zh-cn">SMTP密码，有的邮箱服务商要求是授权码，而不是登录密码。</div>
     * @param password <div lang="zh-cn"></div>
     * @return
     */
    public MailSender setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * <div lang="zh-cn">是否使用SSL连接，默认为true</div>
     * @param sSLOnConnect
     * @return
     */
    public MailSender setSSLOnConnect(boolean sSLOnConnect) {
        this.sSLOnConnect = sSLOnConnect;
        return this;
    }

    /**
     * <div lang="zh-cn">设置邮件主题</div>
     * @param subject
     * @return
     */
    public MailSender setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * <div lang="zh-cn">设置“发送者”地址</div>
     * @param from <div lang="zh-cn"></div>
     * @return
     */
    public MailSender setFrom(InternetAddress from) {
        this.from = from;
        return this;
    }

    /**
     * <div lang="zh-cn">设置“发送者”地址</div>
     * @param from <div lang="zh-cn">邮箱</div>
     * @return
     */
    public MailSender setFrom(String from) {
        try
        {
            return setFrom(new InternetAddress(from));
        } catch (AddressException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * <div lang="zh-cn">设置“发送者”地址</div>
     * @param from <div lang="zh-cn">邮箱地址</div>
     * @param personal <div lang="zh-cn">显示的名字</div>
     * @return
     */
    public MailSender setFrom(String from,String personal) {
        try
        {
            return setFrom(new InternetAddress(from,personal));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * <div lang="zh-cn">设置HTML格式的邮件正文</div>
     * @param htmlMessage <div lang="zh-cn">html内容</div>
     * @return
     */
    public MailSender setHtmlMessage(String htmlMessage)
    {
        this.setMessage(htmlMessage,"text/html;charset=utf-8");
        return this;
    }

    /**
     * <div lang="zh-cn">设置邮件正文</div>
     * @param message <div lang="zh-cn">邮件正文字符串</div>
     * @param mimeType <div lang="zh-cn">mimeType，比如text/plain,text/html等</div>
     * @return
     */
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

    /**
     * <div lang="zh-cn">设置普通邮件正文</div>
     * @param message <div lang="zh-cn">文本内容</div>
     * @return
     */
    public MailSender setTextMessage(String message)
    {
        return this.setMessage(message,"text/plain;charset=utf-8");
    }

    /**
     * <div lang="zh-cn">设置邮件正文</div>
     * @param content <div lang="zh-cn">邮件正文</div>
     * @return
     */
    public MailSender setContent(Multipart content)
    {
        this.content = content;
        return this;
    }

    /**
     * <div lang="zh-cn">增加【收件人】</div>
     * @param addr <div lang="zh-cn">邮件地址</div>
     * @return
     */
    public MailSender addTo(InternetAddress addr)
    {
        this.toAddressList.add(addr);
        return this;
    }

    /**
     * <div lang="zh-cn">增加【收件人】</div>
     * @param address <div lang="zh-cn">邮件地址</div>
     * @param personal <div lang="zh-cn">显示名字</div>
     * @return
     */
    public MailSender addTo(String address, String personal)
    {
        try
        {
            this.toAddressList.add(new InternetAddress(address,personal));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    /**
     * <div lang="zh-cn">增加【收件人】</div>
     * @param address <div lang="zh-cn">邮件地址</div>
     * @return
     */
    public MailSender addTo(String address)
    {
        try
        {
            this.toAddressList.add(new InternetAddress(address));
        } catch (AddressException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    /**
     * <div lang="zh-cn">增加【抄送地址】</div>
     * @param addr <div lang="zh-cn">邮件地址</div>
     * @return
     */
    public MailSender addCC(InternetAddress addr)
    {
        this.ccAddressList.add(addr);
        return this;
    }

    /**
     * <div lang="zh-cn">增加【抄送地址】</div>
     * @param address <div lang="zh-cn">邮件地址</div>
     * @param personal <div lang="zh-cn">显示名字</div>
     * @return
     */
    public MailSender addCC(String address, String personal)
    {
        try
        {
            this.ccAddressList.add(new InternetAddress(address,personal));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    /**
     * <div lang="zh-cn">增加【抄送地址】</div>
     * @param address <div lang="zh-cn">邮件地址</div>
     * @return
     */
    public MailSender addCC(String address)
    {
        try
        {
            this.ccAddressList.add(new InternetAddress(address));
        } catch (AddressException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    /**
     * <div lang="zh-cn">增加【抄送地址】</div>
     * @param addr <div lang="zh-cn">邮件地址</div>
     * @return
     */
    public MailSender addBCC(InternetAddress addr)
    {
        this.bccAddressList.add(addr);
        return this;
    }

    /**
     * <div lang="zh-cn">增加【暗抄地址】</div>
     * @param address <div lang="zh-cn">邮件地址</div>
     * @param personal <div lang="zh-cn">显示名字</div>
     * @return
     */
    public MailSender addBCC(String address, String personal)
    {
        try
        {
            this.bccAddressList.add(new InternetAddress(address,personal));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    /**
     * <div lang="zh-cn">增加【暗抄地址】</div>
     * @param address <div lang="zh-cn">邮件地址</div>
     * @return
     */
    public MailSender addBCC(String address)
    {
        try
        {
            this.bccAddressList.add(new InternetAddress(address));
        } catch (AddressException e) {
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    /**
     * <div lang="zh-cn">增加附件</div>
     * @param displayName <div lang="zh-cn">附件“显示”的文件名</div>
     * @param bytes <div lang="zh-cn">文件内容的字节数组</div>
     * @return
     */
    public MailSender addAttachment(String displayName,byte[] bytes)
    {
        attachments.put(displayName,bytes);
        return this;
    }

    /**
     * <div lang="zh-cn">增加附件</div>
     * @param file <div lang="zh-cn">本地的文件</div>
     * @return
     */
    public MailSender addAttachment(File file)
    {
        String displayName = file.getName();
        byte[] bytes = IOHelpers.readAllBytes(file.toString());
        return addAttachment(displayName,bytes);
    }

    /**
     * <div lang="zh-cn">发送邮件</div>
     */
    public void send()
    {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "SMTP");
        properties.put("mail.host", this.hostName);
        if(smtpPort!=null)
        {
            properties.put("mail.smtp.port", smtpPort);
        }
        properties.put("mail.smtp.ssl.enable", this.sSLOnConnect);
        properties.put("mail.smtp.auth", this.auth);
        Authenticator authenticator=null;
        if(this.auth)
        {
            authenticator = new Authenticator()
            {
                public PasswordAuthentication getPasswordAuthentication()
                {
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

        try
        {
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
        }catch (MessagingException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
