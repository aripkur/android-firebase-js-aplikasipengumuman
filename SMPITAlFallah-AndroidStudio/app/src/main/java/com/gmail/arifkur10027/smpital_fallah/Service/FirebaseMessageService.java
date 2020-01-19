package com.gmail.arifkur10027.smpital_fallah.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.gmail.arifkur10027.smpital_fallah.DetailPengumumanActivity;
import com.gmail.arifkur10027.smpital_fallah.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

/**
 * Created by Entong on 7/2/2019.
 */

public class FirebaseMessageService extends com.google.firebase.messaging.FirebaseMessagingService {

    DatabaseReference reference;
    FirebaseUser auth;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            inputdetailpengumuman(remoteMessage.getData().get("pengumumanid"), remoteMessage.getSentTime());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notif2(remoteMessage.getData().get("pengumumanid"), remoteMessage.getData().get("judul"), remoteMessage.getData().get("isi"));
            } else {
                notif1(remoteMessage.getData().get("pengumumanid"), remoteMessage.getData().get("judul"), remoteMessage.getData().get("isi"));
            }
        }
    }

    private void notif1(String pengumumanid,  String judul, String isi) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent i = new Intent(this, DetailPengumumanActivity.class);
        i.putExtra("pengumumanid", pengumumanid);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(judul)
                .setContentText(isi)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent);

        manager.notify(0, builder.build());

    }
    private void notif2(String pengumumanid, String judul, String isi) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, DetailPengumumanActivity.class);

        i.putExtra("pengumumanid", pengumumanid);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationChannel notificationChannel = new NotificationChannel(pengumumanid, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

        // Configure the notification channel.
        notificationChannel.setDescription("Channel description");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        notificationChannel.enableVibration(true);
        manager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, pengumumanid)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(judul)
                .setContentText(isi);

        manager.notify(1, builder.build());
    }

    public void inputdetailpengumuman(String pengumumanid, Long waktuterbit){
        reference = FirebaseDatabase.getInstance().getReference("DetailPengumuman");
        auth = FirebaseAuth.getInstance().getCurrentUser();
        String userid = auth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idPengumuman", pengumumanid);
        hashMap.put("userid", userid);
        hashMap.put("waktuterbit", waktuterbit);
        hashMap.put("waktusampai", ServerValue.TIMESTAMP);

        reference.push().setValue(hashMap);
    }
}
