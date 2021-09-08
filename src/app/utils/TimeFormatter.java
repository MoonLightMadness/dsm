package app.utils;

/**
 * @ClassName : utils.base.constant.TimeFormatter
 * @Description :
 * @Date 2021-03-31 19:28:36
 * @Author ZhangHL
 */
public class TimeFormatter {
    /*============================================================
    *  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
       Date date=new Date();
    *  sdf.format(date);
    *============================================================*/

    public static final String MILLISEC_LEVEL = "yyyy-MM-dd HH:mm:ss.SS";

    public static final String SEC_LEVEL = "yyyy-MM-dd HH:mm:ss";

    public static final String MINUTE_LEVEL = "yyyy-MM-dd HH:mm";

    public static final String HOUR_LEVEL = "yyyy-MM-dd HH";

    public static final String DAY_LEVEL = "yyyy-MM-dd";

    public static final String MONTH_LEVEL = "yyyy-MM";

    public static final String YEAR_LEVEL = "yyyy";

    public static final String MONTH = "MM";

    public static final String DAY = "dd";

    public static final String HOUR = "HH";

    public static final String MINUTE = "mm";

    public static final String SECOND = "ss";

    public static final String MILLI = "SS";

    public static final String NORMALTIME = "HH:mm:ss";

    /**
     * 1000ms = 1s
     */
    public static final int SEC_FATOR = 1000;
    /**
     * 1000*60 ms = 1 min
     */
    public static final int MINUTE_FATOR = 1000 * 60;
    /**
     * 1000*60*60 ms = 1 h
     */
    public static final int HOUR_FATOR = 1000 * 60 * 60;
    /**
     * 1000*60*60*24 ms = 1 day
     */
    public static final int DAY_FATOR = 1000 * 60 * 60 * 24;
    /**
     * 1000 * 60 * 60 * 24 * 12 ms = 1 year
     */
    public static final int YEAR_FATOR = 1000 * 60 * 60 * 24 * 12;


}
