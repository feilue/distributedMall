package cn.feilue.core;

import cn.feilue.commone.CheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @author hongtao
 */
public class CoreDateImpl {
    private static Logger log =   LoggerFactory.getLogger(CoreDateImpl.class);
    //农历（阴历）年，月，
    private static int yearCyl;
    private static int monCyl;
    private static int dayCyl;
    //公历（阳历）年，月，
    private static int year;
    private static int month;
    private static int day;
    private static boolean isLeap;
    private static int[] lunarInfo = {0x04bd8, 0x04ae0, 0x0a570, 0x054d5,
            0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, 0x04ae0,
            0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2,
            0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40,
            0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0,
            0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7,
            0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0,
            0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355,
            0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
            0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263,
            0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0,
            0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 0x095b0,
            0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46,
            0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50,
            0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954,
            0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0,
            0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0,
            0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50,
            0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
            0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6,
            0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0,
            0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0};
    private static int[] solarMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
            30, 31};
    private static String[] Gan = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛",
            "壬", "癸"};
    private static String[] Zhi = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未",
            "申", "酉", "戌", "亥"};
    private static String[] Animals = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊",
            "猴", "鸡", "狗", "猪"};
    private static int[] sTermInfo = {0, 21208, 42467, 63836, 85337, 107014,
            128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989,
            308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224,
            483532, 504758};
    private static String[] nStr1 = {" ", "一", "二", "三", "四", "五", "六", "七",
            "八", "九", "十"};
    private static String[] nStr2 = {"初", "十", "廿", "卅", "　"};
    //    private static String[] monthNong = {"", "正月", "二月", "三月", "四月", "五月", "六月", "七月",
//            "八月", "九月", "十月", "冬月", "腊月"};
    private static String[] monthNong = {"", "正月", "二月", "三月", "四月", "五月", "六月", "七月",
            "八月", "九月", "十月", "十一月", "十二月"};
    private static String[] yearName = {"零", "壹", "贰", "叁", "肆", "伍", "陆",
            "柒", "捌", "玖"};

    //    private static String[] publicShiZai = {"初一","初八","十四","十五","十八","廿三","廿四","廿八","廿九"};
    private static List<String> publicShiZai = new ArrayList<>();
    //    private static String[] smallShiZai = {"廿七"};
    private static List<String> smallShiZai = new ArrayList<>();
    //    private static String[] bigShiZai = {"三十"};
    private static List<String> bigShiZai = new ArrayList<>();
    private static String pusaChristmasCents = "明天【&&date&&】是【&&name&&】，诸位善识、凡逢诸佛菩萨圣诞佳期。若能持斋、礼拜、诵经、念佛、或者放生、布施、修桥、造路等等之善行者，胜过日常功课，有百千万亿倍之功德。";
    private static String shiZaiCents = "明天【&&date&&】是十斋日恭请诸位善识 恭诵《地藏本愿经》，对佛菩萨像前读此经一遍，就能使四方境内离一切灾患。此人居家，百千岁中永离恶道，衣食丰裕，殃去福至。";

    private static Map<String, String> pusaChristmas = new HashMap<>();

    static {
        pusaChristmas.put("正月初一", "弥勒菩萨圣诞");
        pusaChristmas.put("正月初六", "定光古佛圣诞");
        pusaChristmas.put("正月初九", "帝释天尊圣诞");
        pusaChristmas.put("二月初八", "释迦牟尼佛出家");
        pusaChristmas.put("二月十五", "释迦牟尼佛涅槃");
        pusaChristmas.put("二月十九", "观音菩萨圣诞");
        pusaChristmas.put("二月廿一", "普贤菩萨圣诞");
        pusaChristmas.put("三月十六", "准提菩萨圣诞");
        pusaChristmas.put("四月初四", "文殊菩萨圣诞");
        pusaChristmas.put("四月初八", "释迦牟尼佛圣诞");
        pusaChristmas.put("四月廿八", "药王菩萨圣诞");
        pusaChristmas.put("五月十三", "伽蓝菩萨圣诞");
        pusaChristmas.put("六月初三", "韦驮菩萨圣诞");
        pusaChristmas.put("六月十九", "观音菩萨成道");
        pusaChristmas.put("七月十三", "大势至菩萨圣诞");
        pusaChristmas.put("七月十五", "佛欢喜");
        pusaChristmas.put("七月廿一", "普庵祖师圣诞");
        pusaChristmas.put("七月廿四", "龙树菩萨圣诞");
        pusaChristmas.put("七月三十", "地藏王菩萨圣诞");
        pusaChristmas.put("八月十五", "月光菩萨圣诞");
        pusaChristmas.put("八月廿二", "燃灯古佛圣诞");
        pusaChristmas.put("九月十九", "观音菩萨出家");
        pusaChristmas.put("九月三十", "药师佛圣诞");
        pusaChristmas.put("十月初五", "达摩祖师圣诞");
        pusaChristmas.put("十一月十七", "阿弥陀佛圣诞");
        pusaChristmas.put("十一月十九", "光菩萨圣诞");
        pusaChristmas.put("十二月初八", "释迦牟尼佛成道");
        pusaChristmas.put("十二月廿九", "华严菩萨圣诞");
        publicShiZai.add("初一");
        publicShiZai.add("初八");
        publicShiZai.add("十四");
        publicShiZai.add("十五");
        publicShiZai.add("十八");
        //        publicShiZai.add("廿一");  //TODO 测试
        //        publicShiZai.add("廿二");  //TODO 测试
        publicShiZai.add("廿三");
        publicShiZai.add("廿四");
        publicShiZai.add("廿八");
        publicShiZai.add("廿九");
        smallShiZai.add("廿七");
        bigShiZai.add("三十");
    }


    public CoreDateImpl() {
    }

    /**
     * 传回农历 y年的总天数
     *
     * @param y
     * @return
     */
    private  int lYearDays(int y) {
        int i;
        int sum = 348; //29*12
        for (i = 0x8000; i > 0x8; i >>= 1) {
            sum += (lunarInfo[y - 1900] & i) == 0 ? 0 : 1; //大月+1天
        }
        return (sum + leapDays(y)); //+闰月的天数
    }

    /**
     * 传回农历 y年闰月的天数
     *
     * @param y
     * @return
     */
    private  int leapDays(int y) {
        if (leapMonth(y) != 0) {
            return ((lunarInfo[y - 1900] & 0x10000) == 0 ? 29 : 30);
        } else {
            return (0);
        }
    }

    /**
     * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
     *
     * @param y
     * @return
     */
    private  int leapMonth(int y) {
        return (lunarInfo[y - 1900] & 0xf);
    }

    /**
     * 传回农历 y年m月的总天数
     *
     * @param y
     * @param m
     * @return
     */
    private  int monthDays(int y, int m) {
        return ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0 ? 29 : 30);
    }

    /**
     * 算出农历, 传入 期物件, 传回农历 期物件
     * 该物件属性有 .year .month .day .isLeap .yearCyl .dayCyl .monCyl
     *
     * @param objDate
     */
    private  void Lunar1(Date objDate) {
        int i, leap = 0, temp = 0;
        Calendar cl = Calendar.getInstance();
        cl.set(1900, 0, 31); //1900-01-31是农历1900年正月初一
        Date baseDate = cl.getTime();
        //1900-01-31是农历1900年正月初一
        int offset = (int) ((objDate.getTime() - baseDate.getTime()) / 86400000); //天数(86400000=24*60*60*1000)
        //1899-12-21是农历1899年腊月甲子
        dayCyl = offset + 40;
        //1898-10-01是农历甲子月
        monCyl = 14;
        //得到年数
        for (i = 1900; i < 2050 && offset > 0; i++) {
            //农历每年天数
            temp = lYearDays(i);
            offset -= temp;
            monCyl += 12;
        }
        if (offset < 0) {
            offset += temp;
            i--;
            monCyl -= 12;
        }
        year = i; //农历年份
        yearCyl = i - 1864; //1864年是甲子年
        leap = leapMonth(i); //闰哪个月
        isLeap = false;
        for (i = 1; i < 13 && offset > 0; i++) {
            //闰月
            if (leap > 0 && i == (leap + 1) && isLeap == false) {
                --i;
                isLeap = true;
                temp = leapDays(year);
            } else {
                temp = monthDays(year, i);
            }
            //解除闰月
            if (isLeap == true && i == (leap + 1)) {
                isLeap = false;
            }
            offset -= temp;
            if (isLeap == false) {
                monCyl++;
            }
        }
        if (offset == 0 && leap > 0 && i == leap + 1) {
            if (isLeap) {
                isLeap = false;
            } else {
                isLeap = true;
                --i;
                --monCyl;
            }
        }
        if (offset < 0) {
            offset += temp;
            --i;
            --monCyl;
        }
        month = i; //农历月份
        day = offset + 1; //农历天份
    }

    /**
     * 传入 offset 传回干支, 0=甲子
     *
     * @param num
     * @return
     */
    private  String cyclical(int num) {
        return (Gan[num % 10] + Zhi[num % 12]);
    }

    /**
     * 中文 期
     *
     * @param d
     * @return
     */
    private  String cDay(int d) {
        String s;
        switch (d) {
            case 10:
                s = "初十";
                break;
            case 20:
                s = "二十";
                break;
            case 30:
                s = "三十";
                break;
            default:
                s = nStr2[(int) (d / 10)];//取商
                s += nStr1[d % 10];//取余
        }
        return (s);
    }

    private  String cYear(int y) {
        String s = " ";
        int d;
        while (y > 0) {
            d = y % 10;
            y = (y - d) / 10;
            s = yearName[d] + s;
        }
        return (s);
    }

    private  int getYear() {
        return (year);
    }

    private  int getMonth() {
        return (month);
    }

    private  int getDay() {
        return (day);
    }

    private  int getMonCyl() {
        return (monCyl);
    }

    private  int getYearCyl() {
        return (yearCyl);
    }

    private  int getDayCyl() {
        return (dayCyl);
    }

    private  boolean getIsLeap() {
        return (isLeap);
    }


    /**
     * 检查日期是否是十斋日
     *
     * @return
     */
    public  CheckResult check() {
        CheckResult result = new CheckResult();
        ArrayList<String> tarList = new ArrayList<>();
        tarList.addAll(publicShiZai);
        int flag = 0;

        Calendar calendar = Calendar.getInstance();
        // 获取
        int date = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, date + 1);

        Lunar1(calendar.getTime()); //农历
        boolean small = monthDays(getYear(), getMonth()) == 29;
        String month = monthNong[getMonth()];
        String day = cDay(getDay());
        String tarDate = month + day;

        //合并 日期
        if (small) {
            tarList.addAll(smallShiZai);
        } else {
            tarList.addAll(bigShiZai);
        }

        result.setDate(tarDate);
        //判断是否是十斋日
        if (tarList.contains(day)) {
            flag = flag + 1;
            result.setFlag(flag);
            String replaceShiZaiCents = shiZaiCents.replace("&&date&&", tarDate);
            result.setShiZaiCents(replaceShiZaiCents);
        }

        //判断是否佛菩萨圣诞
        Set<String> pusaChristmasKeys = pusaChristmas.keySet();
        if (pusaChristmasKeys.contains(tarDate)) {
            flag = flag + 2;
            result.setFlag(flag);
            result.setName(pusaChristmas.get(tarDate));
            String replacePusaChristmasCents = pusaChristmasCents.replace("&&date&&", tarDate);
            replacePusaChristmasCents = replacePusaChristmasCents.replace("&&name&&", pusaChristmas.get(tarDate));
            result.setPusaChristmasCents(replacePusaChristmasCents);
        }

        return result;
    }

    /**
     * 检查日期是否是十斋日   test
     *
     * @return
     */
    public  CheckResult check(String year, String months, String days) {
        CheckResult result = new CheckResult();
        ArrayList<String> tarList = new ArrayList<>();
        tarList.addAll(publicShiZai);
        int flag = 0;

        int SY, SM, SD;

        SY = Integer.parseInt(year);
        SM = Integer.parseInt(months);
        SD = Integer.parseInt(days);

        Calendar cl = Calendar.getInstance();
        cl.set(SY, SM - 1, SD);
        // 获取
        int date = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, date + 1);

        Lunar1(cl.getTime()); //农历
        boolean small = monthDays(getYear(), getMonth()) == 29;
        String month = monthNong[getMonth()];
        String day = cDay(getDay());
        String tarDate = month + day;

        //合并 日期
        if (small) {
            tarList.addAll(smallShiZai);
        } else {
            tarList.addAll(bigShiZai);
        }

        result.setDate(tarDate);
        //判断是否是十斋日
        if (tarList.contains(day)) {
            flag = flag + 1;
            result.setFlag(flag);
            //            result.setName("十斋日");
            String replaceShiZaiCents = shiZaiCents.replace("&&date&&", tarDate);
            result.setShiZaiCents(replaceShiZaiCents);
        }

        //判断是否佛菩萨圣诞
        Set<String> pusaChristmasKeys = pusaChristmas.keySet();
        if (pusaChristmasKeys.contains(tarDate)) {
            flag = flag + 2;
            result.setFlag(flag);
            result.setName(pusaChristmas.get(tarDate));
            String replacePusaChristmasCents = pusaChristmasCents.replace("&&date&&", tarDate);
            replacePusaChristmasCents = replacePusaChristmasCents.replace("&&name&&", pusaChristmas.get(tarDate));
            result.setPusaChristmasCents(replacePusaChristmasCents);
        }

        return result;
    }

    public static void main(String[] args) {
        CoreDateImpl core = new CoreDateImpl();
        CheckResult check = core.check("2022", "09", "17");
        System.out.println("check = " + check);
    }

}


