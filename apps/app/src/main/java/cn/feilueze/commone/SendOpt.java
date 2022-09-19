package cn.feilueze.commone;

/**
 * 发送选项
 */
public enum SendOpt {
    EMAIL(1,"邮件"),
    SMS(2,"短信")
    ;

    SendOpt() {
    }

    private Integer code;
    private String optName;
    SendOpt(int code, String optName) {
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
