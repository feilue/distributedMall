package cn.feilue.app.service.commone;


/**
 * 发送选项
 */

public enum SmsTemplateEnum {
    SHIZAIRI(1,"读经典书籍","SMS_252695077","{\"name\":\"@@name@@\"}","@@name@@"),
    ;

    private Integer code;
    private String signName;
    private String templateCode;
    private String templateParam;
    private String labelParam;

    SmsTemplateEnum() {
    }

    SmsTemplateEnum(int code, String signName, String templateCode, String templateParam,String labelParam) {
        this.code = code;
        this.signName = signName;
        this.templateCode = templateCode;
        this.templateParam = templateParam;
        this.labelParam = labelParam;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getLabelParam() {
        return labelParam;
    }

    public void setLabelParam(String labelParam) {
        this.labelParam = labelParam;
    }
}
