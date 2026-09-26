package com.discipline309.app;

import android.app.*;import android.content.*;import android.os.*;import android.speech.tts.TextToSpeech;import java.util.*;

public class AlarmReceiver extends BroadcastReceiver {
 static final String CH="discipline";
 @Override public void onReceive(Context c, Intent i){
  String title=i.getStringExtra("title"); if(title==null) title="Discipline reminder";
  String msg=i.getStringExtra("msg"); if(msg==null) msg="නැගිටින්න! දැන්ම වැඩේ පටන් ගන්න. Do it! Do it! 🔥";
  NotificationManager nm=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
  if(Build.VERSION.SDK_INT>=26) nm.createNotificationChannel(new NotificationChannel(CH,"Discipline alarms",NotificationManager.IMPORTANCE_HIGH));
  Intent open=new Intent(c,MainActivity.class); PendingIntent pi=PendingIntent.getActivity(c,0,open,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
  Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(c,CH):new Notification.Builder(c);
  b.setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setContentTitle(title).setContentText(msg).setStyle(new Notification.BigTextStyle().bigText(msg)).setAutoCancel(true).setContentIntent(pi).setPriority(Notification.PRIORITY_HIGH);
  nm.notify((int)(System.currentTimeMillis()%100000),b.build());
  try { TextToSpeech t=new TextToSpeech(c, status->{ if(status==TextToSpeech.SUCCESS){ t.setLanguage(new Locale("si","LK")); t.setSpeechRate(.95f); t.speak(msg,TextToSpeech.QUEUE_FLUSH,null,"discipline"); }}); } catch(Exception ignored){}
 }
}
