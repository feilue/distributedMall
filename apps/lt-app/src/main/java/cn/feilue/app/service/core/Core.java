package cn.feilue.app.service.core;

import cn.feilue.app.service.commone.CheckResult;
import cn.feilue.app.service.commone.SendOpt;
import cn.feilue.app.service.email.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
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
