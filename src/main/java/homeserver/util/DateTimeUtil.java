package homeserver.util;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    
    /**
     * 현재 시각을 반환한다.
     * @return
     */
    public static Date getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * 입력받은 시각을, 5분단위 정각에 잘라 반환한다.
     * ex. if time == '2019/06/25 22:52:30': returns '2019/06/25 22:50:00'
     * @param time
     * @return
     */
    public static Date roundTo5Minute(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.MINUTE, time.getMinutes() / 5 * 5);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * 입력받은 시각을, 1시간 단위 정각에 잘라 반환한다.
     * ex. if time == '2019/06/25 22:52:30': returns '2019/06/25 22:50:00'
     * @param time
     * @return
     */
    public static Date roundTo1Hour(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * 입력받은 시각에 지정한 minutes를 더한 시각을 반환한다.
     * @param fromTime
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date fromTime, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromTime);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
}
