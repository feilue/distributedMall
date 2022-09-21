package cn.feilue.app;

import cn.feilue.app.service.core.Core;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AppStart {
    private static final Log log = LogFactory.getLog(AppStart.class);
    public static void main(String[] args) {
        try {
            log.info("服务开始启动");
            ApplicationContext applicationContext = SpringApplication.run(AppStart.class);
            Core core = applicationContext.getBean(Core.class);
            core.start();
            log.info("服务启动成功");
        }catch (Exception e){
            log.error("服务启动失败");
            e.printStackTrace();
        }

    }
}
