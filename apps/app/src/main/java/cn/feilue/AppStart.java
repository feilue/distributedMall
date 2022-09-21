package cn.feilue;

import cn.feilue.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * @author hongtao
 */
public class AppStart {
    private static Logger log =   LoggerFactory.getLogger(AppStart.class);

    public static void main(String[] args) {
        Core core = new Core();

        log.info("服务启动开始......");
        Runnable runnable = () -> {
            log.info("定时器开始执行 = =====");
            core.taskEmail();
        };
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.DAYS);
        log.info("服务启动成功......");
    }


}
