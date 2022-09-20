package cn.feilueze.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AppStart {
    private static final Log log = LogFactory.getLog(AppStart.class);
    public static void main(String[] args) {
        try {
            log.info("服务开始启动");
            SpringApplication.run(AppStart.class);
            log.info("服务启动成功");
        }catch (Exception e){
            log.error("服务启动失败");
            e.printStackTrace();
        }

    }
}
