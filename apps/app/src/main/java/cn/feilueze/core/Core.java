package cn.feilueze.core;

import cn.feilueze.commone.CheckResult;
import cn.feilueze.commone.SendOpt;
import cn.feilueze.email.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Core {
    private static Logger log =   LoggerFactory.getLogger(Core.class);
    public Core() {
    }

    public  void taskSMS() {
        this.task(SendOpt.SMS.getCode());
    }

    public  void taskEmail() {
        this.task(SendOpt.EMAIL.getCode());
    }

    /**
     * @param opt 1:邮件；2:短信
     */
    public  void task(int opt) {
        CoreDateImpl core = new CoreDateImpl();
        CheckResult result = core.check();
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
        List<String> personnelList = new ArrayList<>();
        personnelList.add("14325393322@qq.com");
        personnelList.add("877767697@qq.com");
        switch (result.getFlag()) {
            case 1:
                System.out.println("发送十斋日邮件开始.... ");
                sendShiZaiEmail(result, personnelList);
                System.out.println("发送十斋日邮件结束.... ");
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

    }

    private  void sendShiZaiEmail(CheckResult result, List<String> personnelList) {
        SendEmail sendEmail = new SendEmail();
        sendEmail.sendToEmails(personnelList, "明天"+result.getDate()+"是十斋日", result.getShiZaiCents());
    }

    private  void sendShengDanEmail(CheckResult result, List<String> personnelList) {
        SendEmail sendEmail = new SendEmail();
        sendEmail.sendToEmails(personnelList, result.getName(), result.getPusaChristmasCents());
    }
}
