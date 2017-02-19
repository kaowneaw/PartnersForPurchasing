package su.ict.business59.partnersforpurchasing;

import android.app.Application;

/**
 * Created by kaowneaw on 1/15/2017.
 */

public class SHOPSHARE extends Application {
    public static final String MALE_ID = "11";
    public static final String FEMALE_ID = "12";
    public static final String FEMALE_Male_ID = "146";
    public static final String ONLINE = "true";
    public static final String OFLINE = "false";

    public static String getPathImg(String path) {
        if (path.charAt(0) == 'a') {
            return "http://www.itmystyle.com/ict_shopshare/" + path;
        } else {
            return path;
        }
    }
}
