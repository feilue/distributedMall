package cn.feilue.app.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * @author hongtao
 */
public class SendEmail {
    private static Logger log =   LoggerFactory.getLogger(SendEmail.class);
    public String emailHost = "smtp.163.com";       //发送邮件的主机
    public String transportType = "smtp";           //邮件发送的协议
    public String fromUser = "涛涛";           //发件人名称
    public String fromEmail = "feilueze@163.com";  //发件人邮箱
    public String authCode = "NSJEMWOROVIFRSOW";    //发件人邮箱授权码

    /**
     *  这里可以做 动态邮箱 服务账号去发送针对 公共发送邮件有限制
     *  若限制严重 需要自己搭建邮件服务器
     * @param emailHost
     * @param transportType
     * @param fromEmail
     * @param authCode
     */
    public SendEmail(String emailHost,String transportType,String fromEmail,String authCode) {
        this.emailHost = emailHost;
        this.transportType = transportType;
        this.fromEmail = fromEmail;
        this.authCode = authCode;
    }

    public SendEmail() {
    }

    public void sendToEmail(String toEmail, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        if (toEmail.isEmpty()) {
            return;
        }

        //初始化默认参数
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", transportType);
        props.setProperty("mail.host", emailHost);
        props.setProperty("mail.user", fromUser);
        props.setProperty("mail.from", fromEmail);
        //获取Session对象
        Session session = Session.getInstance(props, null);
        //开启后有调试信息
        session.setDebug(false);

        //通过MimeMessage来创建Message接口的子类
        MimeMessage message = new MimeMessage(session);

        String formName = MimeUtility.encodeWord("涛涛") + " <" + fromEmail + ">";
        InternetAddress from = new InternetAddress(formName);
        message.setFrom(from);

        //设置收件人：
        InternetAddress to = new InternetAddress(toEmail);
        message.setRecipient(Message.RecipientType.TO, to);



        //设置邮件主题
        message.setSubject(subject);

        //设置邮件内容,这里我使用html格式，其实也可以使用纯文本；纯文本"text/plain"
        message.setContent(content, "text/html;charset=UTF-8");

        //保存上面设置的邮件内容
        message.saveChanges();

        //获取Transport对象
        Transport transport = session.getTransport();
        //smtp验证，就是你用来发邮件的邮箱用户名密码（若在之前的properties中指定默认值，这里可以不用再次设置）
        transport.connect(emailHost, fromEmail, authCode);
        //发送邮件
        transport.sendMessage(message, message.getAllRecipients()); // 发送
    }

    /**
     * 群发邮件
     *
     * @param toEmails
     * @param subject
     * @param content
     */
    public void sendToEmails(List<String> toEmails, String subject, String content) {
        if (toEmails.isEmpty()) {
            return;
        }
        toEmails.forEach(entity -> {
            try {
                sendToEmail(entity, subject, content);
                Thread.sleep(1000 * 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


}
