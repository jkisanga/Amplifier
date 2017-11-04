package katson.com.amplifier.app;

/**
 * Created by user on 5/27/2017.
 */

public class Config {

  //public final static String url = "http://192.168.43.90:8080/ilri/public/";
   //public final static String url = "http://192.168.8.103:8080/ilri/public/";
    public final static String url = "http://amplifaya.katsonstz.com/";

    public static final String THUMBNAIL_PATH = "DOC_PATH";
    public static final String FILE_TITLE = "file_title";
    public static final String TITLE = "title";
    public static final String CHANNEL_URL = "channel_url";
    public static final String DATE = "date";
    public static final String SUMMARY = "summary";
    public static final String IMAGE_PATH = "image_path";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String AUDIO_PATH= "file_title";
    public static final String CATEGORY_NAME = "category_name";

    public static final String CATEGORY_ID = "category_id";




    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

}
