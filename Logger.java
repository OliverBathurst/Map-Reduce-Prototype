import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class logs down the time along with debug information
 */
class Logger {
    private final SimpleDateFormat LOG_TIME = new SimpleDateFormat("HH:mm:ss");

    Logger(){}

    void log(String str){
        System.out.println(LOG_TIME.format(new Date()) + " " + str);
    }
    void logCritical(String s){
        System.out.println(LOG_TIME.format(new Date()) + " " + s);
        System.exit(1);
    }
}
