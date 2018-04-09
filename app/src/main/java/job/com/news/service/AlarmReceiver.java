package job.com.news.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Zafar.Hussain on 13/02/2018.
 */
//changes 16_03
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("","AlarmReceiver called");
        Intent background = new Intent(context, BackgroundService.class);
        context.startService(background);
    }

}
