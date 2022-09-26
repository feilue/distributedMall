package cn.feilue.app.service.core;

import cn.feilue.app.service.PersonnelService;
import cn.feilue.app.service.commone.CheckResult;
import cn.feilue.app.service.commone.SendOptEnum;
import cn.feilue.app.service.commone.SmsTemplateEnum;
import cn.feilue.app.service.config.SMSConfig;
import cn.feilue.app.service.send.SendEmail;
import cn.feilue.app.service.send.SendSMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class Core {
    @Autowired
    private PersonnelService personnelService;
    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private SendSMS sendSMS;
    @Autowired
    private CoreDateImpl coreDate;
    @Autowired
    private SMSConfig smsConfig;

    private static Logger log =   LoggerFactory.getLogger(Core.class);
    public Core() {
    }

    public  void taskSMS() {
        this.task(SendOptEnum.SMS.getCode());
    }

    public  void taskEmail() {
        this.task(SendOptEnum.EMAIL.getCode());
    }

    /**
     * @param opt 1:邮件；2:短信
     */
    public  void task(int opt) {
        CheckResult result = coreDate.check();
        System.out.println("result = " + result);
        switch (opt) {
            case 1:
                sendEmail(result);
                break;
            case 2:
                sendSMS(result);
                break;
            default:
                log.info("未定义发送方式");
        }
    }

    private  void sendEmail(CheckResult result) {
        //获取人员列表
        List<String> personnelList = personnelService.recipientList(SendOptEnum.EMAIL);
        log.info("接收件人邮件列表:"+personnelList.toString());
        switch (result.getFlag()) {
            case 1:
                sendShiZaiEmail(result, personnelList);
                break;
            case 2:
                sendShengDanEmail(result, personnelList);
                break;
            case 3:
                sendShiZaiEmail(result, personnelList);
                sendShengDanEmail(result, personnelList);
                break;
        }

    }

    private  void sendSMS(CheckResult result) {
        List<String> personnelList = personnelService.recipientList(SendOptEnum.SMS);

        log.info("接收件人短信列表:"+personnelList.toString());
        switch (result.getFlag()) {
            case 1:
                sendShiZaiPhone(result, personnelList);
                break;
            case 2:
                sendShengDanPhone(result, personnelList);
                break;
            case 3:
                sendShiZaiPhone(result, personnelList);
                try {
                    Thread.sleep(smsConfig.getResendSendTime());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sendShengDanPhone(result, personnelList);
                break;
        }
    }

    private  void sendShiZaiPhone(CheckResult result, List<String> personnelList) {
        log.info("发送十斋日短信开始.... ");
        String templateParam = SmsTemplateEnum.SHIZAIRI.getTemplateParam();
        String labelParam = SmsTemplateEnum.SHIZAIRI.getLabelParam();
        templateParam = templateParam.replace(labelParam,result.getDate());
        sendSMS.send(personnelList, SmsTemplateEnum.SHIZAIRI,templateParam);
        log.info("发送十斋日短信结束.... ");
    }

    private  void sendShengDanPhone(CheckResult result, List<String> personnelList) {
        log.info("发送佛菩萨圣诞日短信开始.... ");
        String templateParam = SmsTemplateEnum.SHENDAN.getTemplateParam();
        String labelParam = SmsTemplateEnum.SHENDAN.getLabelParam();
        templateParam = templateParam.replace(labelParam,result.getDate());
        sendSMS.send(personnelList, SmsTemplateEnum.SHENDAN,templateParam);
        log.info("发送佛菩萨圣诞日短信结束.... ");
    }

    private  void sendShiZaiEmail(CheckResult result, List<String> personnelList) {
        log.info("发送十斋日邮件开始.... ");
        sendEmail.sendToEmails(personnelList, "明天"+result.getDate()+"是十斋日", result.getShiZaiCents());
        log.info("发送十斋日邮件结束.... ");
    }

    private  void sendShengDanEmail(CheckResult result, List<String> personnelList) {
        log.info("发送菩萨圣诞日邮件开始.... ");
        sendEmail.sendToEmails(personnelList, result.getName(), result.getPusaChristmasCents());
        log.info("发送菩萨圣诞日邮件结束.... ");
    }

    public void start(){

        Runnable taskEmail = () -> {
            log.info("发送邮件定时器开始启动 = =====");
            this.taskEmail();
            log.info("发送邮件定时器启动成功 = =====");
        };

        Runnable taskSMS = () -> {

            log.info("发送短信定时器开始启动 = =====");
            taskSMS();
            log.info("发送短信定时器启动成功 = =====");
        };

        Runnable resendSendSMS = () -> {

            log.info("重发短信定时器开始启动 = =====");
            try {
                sendSMS.timingResendSend();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.info("重发短信定时器启动成功 = =====");
        };

        log.info("发送服务启动开始......");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(3);
//        executorService.scheduleAtFixedRate(taskEmail, 0, 1, TimeUnit.MINUTES);
//        executorService.scheduleAtFixedRate(taskSMS, 0, 1, TimeUnit.MINUTES);
//        executorService.scheduleAtFixedRate(resendSendSMS,0,1,TimeUnit.MINUTES);
        log.info("发送服务启动成功......");
        taskSMS();
    }

}
