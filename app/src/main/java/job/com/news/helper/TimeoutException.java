package job.com.news.helper;

import java.net.SocketTimeoutException;

/**
 * Created by Zafar.Hussain on 09/04/2018.
 */

public class TimeoutException extends SocketTimeoutException {
    @Override
    public String getMessage() {
        return "Something went wrong, Please try again!";
    }
}
