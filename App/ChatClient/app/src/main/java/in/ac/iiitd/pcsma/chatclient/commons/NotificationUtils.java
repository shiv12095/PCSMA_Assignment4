package in.ac.iiitd.pcsma.chatclient.commons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import in.ac.iiitd.pcsma.chatclient.R;

/**
 * Created by shiv on 1/5/16.
 */
public class NotificationUtils {

    private Context context;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    public void showNotificationMessage(String userName, final String message, Intent intent) {
        if (TextUtils.isEmpty(message))
            return;

        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);
        showSmallNotification(mBuilder, icon, message, userName, resultPendingIntent);
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String message, String userName, PendingIntent resultPendingIntent) {
        NotificationCompat.InboxStyle inboxStyle;
        inboxStyle = new NotificationCompat.InboxStyle();
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker("Hello").setWhen(0)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE )
                .setContentIntent(resultPendingIntent)
                .setStyle(inboxStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }
}
