package creatio.com.entrayecto.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import creatio.com.entrayecto.Main;
import creatio.com.entrayecto.R;

/**
 * Created by Layge on 13/07/2017.
 */

public class FirebaseService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_car))
                        .setSmallIcon(R.drawable.ic_car)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setContentTitle("Fixer")
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentInfo("Ahora");
        Intent resultIntent = new Intent(this, Main.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        Boolean login = pref.getBoolean("login", false);
        if (login) {
            stackBuilder.addParentStack(Main.class);
            resultIntent = new Intent(this, Main.class);
        }

        stackBuilder.addNextIntent(resultIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setVibrate(new long[100]);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());
    }
}
