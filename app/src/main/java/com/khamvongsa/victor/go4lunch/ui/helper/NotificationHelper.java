package com.khamvongsa.victor.go4lunch.ui.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.khamvongsa.victor.go4lunch.model.RestaurantNotification;

import java.util.Calendar;

/**
 * Created by <Victor Khamvongsa> on <13/10/2022>
 */
public class NotificationHelper {

    private static final String KEY_RESTAURANT_NAME = "restaurantName";
    private static final String KEY_RESTAURANT_ADDRESS = "restaurantAddress";
    private static final String KEY_USERS_EATING_LIST = "usersEatingList";

    //AlarmManager
    private static AlarmManager mAlarmManager;
    private static PendingIntent mPendingIntent;
    private static String mCustomNotificationName;
    private static String mCustomNotificationAddress;
    private static String mCustomNotificationWorkmates;


    public static void setAlarm(Context context, String restaurantName, String restaurantAddress, String restaurantWorkmates) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        //intent.putExtra(KEY_PLACE_ID, mPlaceId);
        intent.putExtra(KEY_RESTAURANT_NAME, restaurantName);
        intent.putExtra(KEY_RESTAURANT_ADDRESS, restaurantAddress);
        intent.putExtra(KEY_USERS_EATING_LIST, restaurantWorkmates);

        mPendingIntent = PendingIntent.getBroadcast(context, 0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar timeAlarm = Calendar.getInstance();
        Calendar actualTime = Calendar.getInstance();
        long tomorrowAtNoonInMilliseconds = 129600000 + actualTime.getTimeInMillis() ;

        if (actualTime.get(Calendar.HOUR_OF_DAY) <= 11 ){
            timeAlarm.set(Calendar.HOUR_OF_DAY, 12);
            timeAlarm.set(Calendar.MINUTE, 0);
            timeAlarm.set(Calendar.SECOND, 0);
        }else {
            timeAlarm.setTimeInMillis(tomorrowAtNoonInMilliseconds - (actualTime.get(Calendar.HOUR_OF_DAY)*3600000 +
                    actualTime.get(Calendar.MINUTE)*60000 + actualTime.get(Calendar.SECOND)*1000 + actualTime.get(Calendar.MILLISECOND)));
        }

        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, timeAlarm.getTimeInMillis(), mPendingIntent );
    }



    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        mPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (mAlarmManager == null){
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        mAlarmManager.cancel(mPendingIntent);
    }

    public static void customNotificationText(Context context, RestaurantNotification restaurantNotification){
        if (restaurantNotification != null){
            String listWorkmatesWithBrackets =  restaurantNotification.getUsersEatingList().toString();
            String listWorkmates = listWorkmatesWithBrackets.substring(1, listWorkmatesWithBrackets.length() - 1);
            mCustomNotificationName = restaurantNotification.getName() ;
            mCustomNotificationAddress = restaurantNotification.getAddress();
            mCustomNotificationWorkmates = listWorkmates;
            setAlarm(context, mCustomNotificationName, mCustomNotificationAddress, mCustomNotificationWorkmates);
        }
    }
}
