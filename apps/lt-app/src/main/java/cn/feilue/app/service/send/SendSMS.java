package cn.feilue.app.service.send;

import cn.feilue.app.service.commone.Constants;
import cn.feilue.app.service.commone.ResendSMS;
import cn.feilue.app.service.commone.SmsTemplateEnum;
import cn.feilue.app.service.commone.WhetherEnum;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class SendSMS {
    private static Logger log =   LoggerFactory.getLogger(SendSMS.class);

    private static List<ResendSMS> resendSMSQueue = new ArrayList<>();
    @Autowired
    private SMSConfig smsConfig;

    public void send(List<String> phones, SmsTemplateEnum smsTemplate, String templateParam){
        if(Objects.isNull(phones) || phones.isEmpty()){
            return ;
        }
        ResendSMS resendSMS = new ResendSMS();

        phones.forEach(entity->{
            resendSMS.setPhone(entity);
            resendSMS.setSmsTemplate(smsTemplate);
            resendSMS.setTemplateParam(templateParam);
            try {
                String success = send(entity, smsTemplate, templateParam);
                if(Constants.SMS_CALL_STATUS_NO.equals(success)){
                    resendSMSQueue.add(resendSMS);
                }
                Thread.sleep(smsConfig.getDelay());
            } catch ( Exception e) {
                resendSMSQueue.add(resendSMS);
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
    public String send(String phone, SmsTemplateEnum smsTemplate,String templateParam) throws ExecutionException, InterruptedException {
        if(StringUtils.isEmpty(phone)){
            return Constants.SMS_CALL_STATUS_NO;
        }
        SendSmsResponse resp = null;
        try {
            StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                    .accessKeyId(smsConfig.getAccessKeyId())
                    .accessKeySecret(smsConfig.getAccessKeySecret())
                    //.securityToken("<your-token>") // use STS token
                    .build());

            // Configure the Client
            AsyncClient client = AsyncClient.builder()
                    .region("cn-hangzhou") // Region ID
                    //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                    .credentialsProvider(provider)
                    //.serviceConfiguration(Configuration.create()) // Service-level configuration
                    // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride("dysmsapi.aliyuncs.com")
                            //.setReadTimeout(Duration.ofSeconds(30))
                    )
                    .build();

            // Parameter settings for API request
            String templateCode = smsTemplate.getTemplateCode();
            String signName = smsTemplate.getSignName();

            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .phoneNumbers(phone)
                    .signName(signName)
                    .templateCode(templateCode)
                    .templateParam("{\"name\":\"八月三十\"}")
                    .build();
      /*  SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .phoneNumbers(phone)
                .signName(signName)
                .templateCode("SMS_252695077")
                .templateParam("{\"name\":\"八月三十\"}")
                {\"name\":\"八月廿九\"}
                .build();*/

            // Asynchronously get the return value of the API request
            CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
             resp = response.get();

            client.close();
        }catch (Exception e){
            return Constants.SMS_CALL_STATUS_NO;
        }

        if(resp != null && Constants.SMS_CALL_STATUS_YES.equals(resp.getBody().getCode())){
            return Constants.SMS_CALL_STATUS_YES;
        }else{
            return Constants.SMS_CALL_STATUS_NO;
        }

    }


    /**
     *  发送失败时重新发送
     * @param phone
     * @param smsTemplate
     * @param templateParam
     * @throws Exception
     */
    public WhetherEnum resendSend(String phone, SmsTemplateEnum smsTemplate, String templateParam) throws  Exception{
        WhetherEnum result = WhetherEnum.NO;
        try {
             result = Constants.SMS_CALL_STATUS_YES.equals(send(phone, smsTemplate, templateParam))?WhetherEnum.YES:WhetherEnum.NO;
        }catch (Exception e){
            log.error("重发短信出错："+phone);
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 定时重发短信
     * @throws Exception
     */
    public void timingResendSend() throws Exception {
        List<String> resendCollect = resendSMSQueue.stream().map(ResendSMS::getPhone).collect(Collectors.toList());
        log.info("短信重发队列"+resendCollect);
        /*for (ResendSMS resendSMS : resendSMSQueue) {
            WhetherEnum whetherEnum = resendSend(resendSMS.getPhone(), resendSMS.getSmsTemplate(), resendSMS.getTemplateParam());
            if(WhetherEnum.YES.equals(whetherEnum)){
                resendSMSQueue.remove(resendSMS);
            }
            Thread.sleep(smsConfig.getResendSendTime());
        }*/
    }


}
