package cn.feilue.app.service.core;

import cn.feilue.app.service.PersonnelService;
import cn.feilue.app.service.commone.CheckResult;
import cn.feilue.app.service.commone.SendOptEnum;
import cn.feilue.app.service.commone.SmsTemplateEnum;
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
       /* switch (opt) {
            case 1:
                sendEmail(result);
                break;
            case 2:
                sendSMS(result);
                break;
            default:
                log.info("未定义发送方式");
        }*/
    }

    private  void sendEmail(CheckResult result) {
        //获取人员列表
        List<String> personnelList = personnelService.recipientList(SendOptEnum.EMAIL);
        personnelList.add("877767697@qq.com");
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
        personnelList.add("15327779338");
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
        String templateParam = SmsTemplateEnum.SHIZAIRI.getTemplateParam();
        String labelParam = SmsTemplateEnum.SHIZAIRI.getLabelParam();
        templateParam = templateParam.replace(labelParam,result.getDate()+"是"+result.getName()+"日");
        sendSMS.send(personnelList, SmsTemplateEnum.SHIZAIRI,templateParam);
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
        log.info("发送服务启动开始......");
        Runnable runnable = () -> {
            log.info("发送定时器开始执行 = =====");
            this.taskEmail();
        };
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.DAYS);
        log.info("发送服务启动成功......");
    }

}
