package cn.feilue.app.service.commone;

/**
 * 发送选项
 */
public enum SmsTemplate {
    EMAIL(1,"邮件"),
    SMS(2,"短信")
    ;

    SmsTemplate() {
    }

    private Integer code;
    private String optName;
    SmsTemplate(int code, String optName) {
        this.code = code;
        this.optName = optName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }


}
