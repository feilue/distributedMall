package net.feilue.IMCode.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ServiceLauncherStart {
    private static final Log log = LogFactory.getLog(ServiceLauncherStart.class);
    public static void main(String[] args) {
        try {
            log.info("服务开始启动");
            SpringApplication.run(ServiceLauncherStart.class);
            log.info("服务启动成功");
        }catch (Exception e){
            log.error("服务启动失败");
            e.printStackTrace();
        }
    }
}
