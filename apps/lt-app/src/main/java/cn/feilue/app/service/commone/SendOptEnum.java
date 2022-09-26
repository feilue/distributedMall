package cn.feilue.app.service.commone;

/**
 * 发送选项
 */
public enum SendOptEnum {
    EMAIL(1,"邮件"),
    SMS(2,"短信")
    ;

    SendOptEnum() {
    }

    private Integer code;
    private String optName;
    SendOptEnum(int code, String optName) {
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

    public SendOptEnum getSendOpEnum(Integer code){
        SendOptEnum[] values = SendOptEnum.values();
        for (SendOptEnum value : values) {
            if(value.code == code){
                return value;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SendOptEnum sendOpEnum = SendOptEnum.SMS.getSendOpEnum(1);
        System.out.println("sendOpEnum = " + sendOpEnum );
    }

}
