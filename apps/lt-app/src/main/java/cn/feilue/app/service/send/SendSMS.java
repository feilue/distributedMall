package cn.feilue.app.service.send;

import cn.feilue.app.service.commone.SmsTemplateEnum;
import cn.feilue.app.service.config.SMSConfig;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class SendSMS {
    private static Logger log =   LoggerFactory.getLogger(SendSMS.class);
    @Autowired
    private SMSConfig smsConfig;

    public void send(List<String> phones, SmsTemplateEnum smsTemplate, String templateParam){
        if(Objects.isNull(phones) || phones.isEmpty()){
            return ;
        }
        phones.forEach(entity->{
            try {
                send(entity,smsTemplate,templateParam);
                Thread.sleep(smsConfig.getDelay());
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     *
     * @param phone
     * @param smsTemplate
     * @param templateParam 短信模板参数
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void send(String phone, SmsTemplateEnum smsTemplate,String templateParam) throws ExecutionException, InterruptedException {
        if(StringUtils.isEmpty(phone)){
            return ;
        }
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(smsConfig.getAccessKeyId())
                .accessKeySecret(smsConfig.getAccessKeySecret())
                .build());

        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou") // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();

        // Parameter settings for API request

        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(smsTemplate.getSignName())
                .templateCode(smsTemplate.getTemplateCode())
                .phoneNumbers(phone)
                .templateParam(templateParam)
                .build();

        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = response.get();
        log.info(new Gson().toJson(resp));
        client.close();
    }

}
