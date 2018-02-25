package job.com.news.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Zafar.Hussain on 13/02/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, BackgroundService.class);
        context.startService(background);
    }

}
