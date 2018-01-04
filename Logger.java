import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class prints the time of an event along with debug information
 * logCritical stops execution with an error code of 1
 */
class Logger {
    private final SimpleDateFormat LOG_TIME = new SimpleDateFormat("HH:mm:ss");

    Logger(){}

    void log(String str){
        System.out.println(LOG_TIME.format(new Date()) + " " + str);
    }
    void logCritical(String str){
        System.out.println(LOG_TIME.format(new Date()) + " " + str);
        System.exit(1);
    }
}
