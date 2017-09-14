package com.ihealth.learnaidl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShenZhenWei on 17/9/14.
 */

public class MyService extends Service{
    private static final String TAG = "MyService";
    private static final String PACKAGE_SAYHI="com.ihealth.learnaidl";

    private NotificationManager mNotificationManager;
    private boolean mCanRun=true;
    private List<Student> mStudents=new ArrayList<>();

    private final IMyService.Stub mBinder=new IMyService.Stub(){

        @Override
        public List<Student> getStudent() throws RemoteException {
            synchronized (mStudents){
                return mStudents;
            }
        }

        @Override
        public void addStudent(Student student) throws RemoteException {

            synchronized (mStudents){
                if (!mStudents.contains(student)){
                    mStudents.add(student);
                }
            }
        }

        //在这里做权限认证,return false 意味着客户端调用就会失败,比如下面,只允许包名为com.ihealth.learnaidl的客户端通过,
        //其他的apk将无法完成调用过程
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            String packageName=null;
            String [] packages=MyService.this.getPackageManager().getPackagesForUid(getCallingUid());
            if (packages!=null && packages.length>0){
                packageName=packages[0];
            }
            Log.d(TAG,"onTransact:"+packageName);
            if (!PACKAGE_SAYHI.equals(packageName)){
                return false;
            }
            return super.onTransact(code,data,reply,flags);
        }
    };

    @Override
    public void onCreate() {
        Thread thread=new Thread(null,new ServiceWorker(),"BackgroundService");
        thread.start();
        synchronized (mStudents){
            for (int i=1;i<6;i++){
                Student student=new Student();
                student.name="student#"+i;
                student.age=i;
                mStudents.add(student);
            }
        }
        mNotificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        super.onCreate();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,String.format("on bind intent=%s",intent.toString()));
        displayNotificationMessage("服务已启动");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayNotificationMessage(String message) {
        Notification notification=new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("通知来了")
                .setContentTitle("这是一个通知标题")
                .setContentText(message)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
//                .setContentIntent(PendingIntent.getActivity(this,1,new Intent(this,MainActivity.class),PendingIntent.FLAG_CANCEL_CURRENT))
                .setContentIntent(PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0))
                .build();
        /**发起通知**/
        mNotificationManager.notify(0, notification);
    }

    class ServiceWorker implements Runnable{
        long counter=0;

        @Override
        public void run() {
            while (mCanRun){
                counter++;
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
