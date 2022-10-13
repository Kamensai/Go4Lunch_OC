package com.khamvongsa.victor.go4lunch.ui.helper;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import com.khamvongsa.victor.go4lunch.R;
import com.khamvongsa.victor.go4lunch.ui.MainActivity;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Created by <Victor Khamvongsa> on <07/10/2022>
 */
// Help From https://www.youtube.com/watch?v=xSrVWFCtgaE&ab_channel=Foxandroid
public class AlarmReceiver extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "NOTIFICATION TAG";
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    private static final String KEY_RESTAURANT_NAME = "restaurantName";
    private static final String KEY_RESTAURANT_ADDRESS = "restaurantAddress";
    private static final String KEY_USERS_EATING_LIST = "usersEatingList";
    private static final String KEY_PLACE_ID = "placeId";

    private String mCustomNotificationName;
    private String mCustomNotificationAddress;
    private String mCustomNotificationWorkmates;

    private String mUserChosenRestaurant;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intentFrom) {

        if (intentFrom != null && intentFrom.getStringExtra(KEY_RESTAURANT_NAME) != null
                && intentFrom.getStringExtra(KEY_RESTAURANT_ADDRESS) != null
                && intentFrom.getStringExtra(KEY_USERS_EATING_LIST) != null) {
            mCustomNotificationName = intentFrom.getStringExtra(KEY_RESTAURANT_NAME);
            mCustomNotificationAddress = intentFrom.getStringExtra(KEY_RESTAURANT_ADDRESS);
            mCustomNotificationWorkmates = intentFrom.getStringExtra(KEY_USERS_EATING_LIST);

            Log.e(TAG, mCustomNotificationName);
            Log.e(TAG, mCustomNotificationAddress);
            Log.e(TAG, mCustomNotificationWorkmates);
        }

        // Create an Intent that will be shown when user will click on the Notification
        Intent intentToLaunch = new Intent(context, MainActivity.class);
        //intentToLaunch.putExtra(NavigationHelper.KEY_PLACE_ID, mUserChosenRestaurant);
        // TODO : Ask François what it does
        intentFrom.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToLaunch, PendingIntent.FLAG_CANCEL_CURRENT);

        // Create a Channel (Android 8)
        String channelId = String.valueOf(R.string.default_notification_channel_id) ;

        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_lunch)
                        .setContentTitle(mCustomNotificationName)
                        .setContentText(mCustomNotificationAddress)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(mCustomNotificationWorkmates))
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        // Show notification
        notificationManagerCompat.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
