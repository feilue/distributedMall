package cn.feilue.commone;

public class CheckResult {
    /**
     *  null : 表示没有
     *  1：十斋日
     *  2：佛菩萨圣诞
     *  3：十斋日与佛菩萨圣诞
     */
    private Integer flag;

    /**
     * 时间
     */
    private String date;

    /**
     * 佛菩萨名号
     */
    private String name;

    /**
     * 十斋日 文本
     */
    private String  shiZaiCents;

    /**
     * 佛菩萨圣诞日文本
     */
    private String pusaChristmasCents;

    public CheckResult() {
    }

    public Integer getFlag() {
        return this.flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShiZaiCents() {
        return this.shiZaiCents;
    }

    public void setShiZaiCents(String shiZaiCents) {
        this.shiZaiCents = shiZaiCents;
    }

    public String getPusaChristmasCents() {
        return this.pusaChristmasCents;
    }

    public void setPusaChristmasCents(String pusaChristmasCents) {
        this.pusaChristmasCents = pusaChristmasCents;
    }

    public String toString() {
        return "CheckResult{flag=" + this.flag + ", date='" + this.date + '\'' + ", name='" + this.name + '\'' + ", shiZaiCents='" + this.shiZaiCents + '\'' + ", pusaChristmasCents='" + this.pusaChristmasCents + '\'' + '}';
    }
}
