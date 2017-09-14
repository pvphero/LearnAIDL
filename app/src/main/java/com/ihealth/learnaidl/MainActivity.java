package com.ihealth.learnaidl;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String ACTION_BIND_SERVICE="com.ihealth.learnaidl.MyService";
    private IMyService mIMyService;
    private Button btn1;
    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mIMyService=IMyService.Stub.asInterface(service);
            try {
                Student student=mIMyService.getStudent().get(0);
                showDialog(student.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIMyService=null;
        }
    };

    private void showDialog(String message) {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("scott")
                .setMessage(message)
                .setPositiveButton("确定",null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1= (Button) findViewById(R.id.btn_1);
        btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_1:
                Intent intentService=new Intent();
                intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentService.setAction(ACTION_BIND_SERVICE);
                intentService.setPackage(getPackageName());
                MainActivity.this.bindService(intentService,mServiceConnection,BIND_AUTO_CREATE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mIMyService!=null){
            unbindService(mServiceConnection);
        }
        super.onDestroy();
    }
}
