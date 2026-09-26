package com.discipline309.app;

import android.app.*;import android.os.*;import android.content.*;import android.content.pm.PackageManager;import android.provider.Settings;import android.net.Uri;import android.graphics.Color;import android.view.*;import android.widget.*;import java.text.*;import java.util.*;

public class MainActivity extends Activity {
 static final String P="discipline"; LinearLayout root; SharedPreferences sp; TextView stats; String[] tasks={"Wake up on time","Study / learning","Workout or active recovery","Eat planned meals","No-phone block","Night review + prepare tomorrow"};
 @Override public void onCreate(Bundle b){super.onCreate(b); sp=getSharedPreferences(P,0); build(); if(Build.VERSION.SDK_INT>=33 && checkSelfPermission("android.permission.POST_NOTIFICATIONS")!=PackageManager.PERMISSION_GRANTED) requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"},7);}
 TextView tv(String s,int z){TextView v=new TextView(this);v.setText(s);v.setTextColor(Color.WHITE);v.setTextSize(z);v.setPadding(8,10,8,10);return v;}
 Button btn(String s){Button b=new Button(this);b.setText(s);return b;}
 void build(){ ScrollView sv=new ScrollView(this); root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(18,18,18,30);root.setBackgroundColor(Color.rgb(13,15,20));sv.addView(root);setContentView(sv);
  root.addView(tv("309 DAY DISCIPLINE 🔥",26)); stats=tv("",14);root.addView(stats); refreshStats();
  root.addView(tv("Today's checklist",20));
  for(int x=0;x<tasks.length;x++){final int n=x;CheckBox cb=new CheckBox(this);cb.setText(tasks[x]);cb.setTextColor(Color.WHITE);cb.setTextSize(16);cb.setChecked(sp.getBoolean("t"+n+dateKey(),false));cb.setOnCheckedChangeListener((v,c)->{sp.edit().putBoolean("t"+n+dateKey(),c).apply();refreshStats();});root.addView(cb);}
  Button complete=btn("COMPLETE DAY");complete.setOnClickListener(v->completeDay());root.addView(complete);
  Button alarms=btn("⏰ Alarms / reminders");alarms.setOnClickListener(v->alarmDialog());root.addView(alarms);
  Button ai=btn("💬 AI Discipline Assistant");ai.setOnClickListener(v->chatDialog());root.addView(ai);
  Button settings=btn("⚙️ Settings / voice");settings.setOnClickListener(v->settingsDialog());root.addView(settings);
 }
 String dateKey(){return new SimpleDateFormat("yyyyMMdd",Locale.US).format(new Date());}
 void refreshStats(){int d=0;for(int n=0;n<tasks.length;n++)if(sp.getBoolean("t"+n+dateKey(),false))d++;int pct=Math.round(d*100f/tasks.length);stats.setText("Today: "+pct+"%  •  "+d+"/"+tasks.length+" tasks\nStreak: "+sp.getInt("streak",0)+" 🔥   Best: "+sp.getInt("best",0)+" 🏆");}
 void completeDay(){for(int n=0;n<tasks.length;n++)if(!sp.getBoolean("t"+n+dateKey(),false)){toast("Finish all tasks first.");return;}int s=sp.getInt("streak",0)+1,best=Math.max(sp.getInt("best",0),s);sp.edit().putInt("streak",s).putInt("best",best).putBoolean("done"+dateKey(),true).apply();toast("Day completed! 🔥");refreshStats();}
 void alarmDialog(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);TimePicker tp=new TimePicker(this);tp.setIs24HourView(true);EditText name=new EditText(this);name.setHint("Alarm name");l.addView(name);l.addView(tp);new AlertDialog.Builder(this).setTitle("Add daily alarm").setView(l).setPositiveButton("SAVE",(d,w)->{schedule(this,name.getText().toString(),tp.getHour(),tp.getMinute());toast("Daily alarm saved");}).setNegativeButton("CANCEL",null).show();}
 void schedule(Context c,String name,int h,int m){int id=(name+h+m).hashCode();Calendar cal=Calendar.getInstance();cal.set(Calendar.HOUR_OF_DAY,h);cal.set(Calendar.MINUTE,m);cal.set(Calendar.SECOND,0);if(cal.getTimeInMillis()<=System.currentTimeMillis())cal.add(Calendar.DAY_OF_YEAR,1);Intent in=new Intent(c,AlarmReceiver.class);in.putExtra("title",name.length()>0?name:"Discipline reminder");in.putExtra("msg","නැගිටින්න! දැන්ම වැඩේ පටන් ගන්න. Do it! Do it! 🔥");PendingIntent pi=PendingIntent.getBroadcast(c,id,in,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);AlarmManager am=(AlarmManager)c.getSystemService(ALARM_SERVICE);if(Build.VERSION.SDK_INT>=31 && !am.canScheduleExactAlarms()){c.startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:"+c.getPackageName())));return;}am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pi);getSharedPreferences(P,0).edit().putString("alarm"+id,name+"|"+h+"|"+m).apply();}
 public static void scheduleAll(Context c){
  SharedPreferences p=c.getSharedPreferences(P,0);
  for(String k:p.getAll().keySet()){
   if(!k.startsWith("alarm")) continue;
   String v=p.getString(k,""); if(v==null) continue; String[] a=v.split("\\|",-1);
   if(a.length!=3) continue; try{ new MainActivity().scheduleStatic(c,a[0],Integer.parseInt(a[1]),Integer.parseInt(a[2]),k.substring(5).hashCode()); }catch(Exception ignored){}
  }
 }
 static void scheduleStatic(Context c,String name,int h,int m,int id){
  Calendar cal=Calendar.getInstance(); cal.set(Calendar.HOUR_OF_DAY,h);cal.set(Calendar.MINUTE,m);cal.set(Calendar.SECOND,0);cal.set(Calendar.MILLISECOND,0);if(cal.getTimeInMillis()<=System.currentTimeMillis())cal.add(Calendar.DAY_OF_YEAR,1);
  Intent in=new Intent(c,AlarmReceiver.class);in.putExtra("title",name.length()>0?name:"Discipline reminder");in.putExtra("msg","නැගිටින්න! දැන්ම වැඩේ පටන් ගන්න. Do it! Do it! 🔥");
  PendingIntent pi=PendingIntent.getBroadcast(c,id,in,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT); AlarmManager am=(AlarmManager)c.getSystemService(ALARM_SERVICE);
  if(Build.VERSION.SDK_INT>=31 && !am.canScheduleExactAlarms()) return; am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pi);
 }
 void chatDialog(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);TextView chat=tv("Assistant: ආයුබෝවන්! අද වැඩේ පටන් ගමු. 💪\n",15);EditText input=new EditText(this);input.setHint("සිංහලෙන් හෝ English වලින් type කරන්න...");l.addView(chat);l.addView(input);new AlertDialog.Builder(this).setTitle("AI Discipline Assistant").setView(l).setPositiveButton("SEND",(d,w)->{String q=input.getText().toString();chat.setText("Assistant: "+localReply(q));}).setNegativeButton("CLOSE",null).show();}
 String localReply(String q){q=q.toLowerCase();if(q.contains("නින්ද")||q.contains("sleep"))return "දැන් phone එක පැත්තකින් තියලා නිදාගන්න. හෙට වෙලාවට නැගිටින්න. 💪";if(q.contains("බැහැ")||q.contains("can't"))return "එකපාරටම හැමදේම කරන්න ඕනේ නෑ. එක task එකක් දැන්ම පටන් ගන්න. Do it! 🔥";return "හරි. දැන්ම පොඩි step එකක් ගන්න. ඔයාට මේක complete කරන්න පුළුවන්. Do it! Do it! 🔥";}
 void settingsDialog(){new AlertDialog.Builder(this).setTitle("Voice & permissions").setMessage("Sinhala female voice depends on the Sinhala voice installed in your Android Text-to-Speech engine.\n\nFor reliable alarms, allow Notifications and Exact alarms when Android asks.\n\nThe assistant can work offline with built-in coaching. A real cloud AI needs an API connection/key configured in a future AI provider screen.").setPositiveButton("OK",null).show();}
 void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
}
