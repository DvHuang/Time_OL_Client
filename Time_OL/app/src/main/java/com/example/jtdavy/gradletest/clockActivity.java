package com.example.jtdavy.gradletest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JTdavy on 2015/8/27.
 */
public class clockActivity extends Activity {

    private ImageButton userbutton;
    private ImageButton modebutton;

    private TextView hourText;       //秒
    private TextView minText;       //分
    private TextView secText;       //秒
    private TextView userName;
    private TextView mode;
    private TextView onlineText;

    private Thread mThread3 = null;

    private String hourString;
    private String minString;
    private String secString;
    private String userString;
    private String modeString;
    private String onlinString;


    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mode.setText(modeString);
                    break;
                case 2:
                    hourText.setText(hourString);
                    minText.setText(minString);
                    secText.setText(secString);
                    break;
                case 3:
                    onlineText.setText(onlinString);
                    break;
            }
        }

        ;
    };


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);

        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String name = bundle.getString("name");
        Log.i("获取到的name值为", name);
        //System.out.println("bundle" + name);

        userbutton = (ImageButton) findViewById(R.id.userbutton);
        modebutton = (ImageButton) findViewById(R.id.modebutton);

        hourText = (TextView) findViewById(R.id.hourText);
        minText = (TextView) findViewById(R.id.minText);
        secText = (TextView) findViewById(R.id.secText);
        userName = (TextView) findViewById(R.id.UserName);
        mode = (TextView) findViewById(R.id.mode);
        onlineText = (TextView) findViewById(R.id.online);

        userString=name.substring(name.indexOf("! "), name.indexOf("@"));
        userString=userString.substring(1);

        hourString=name.substring(name.indexOf("# "), name.indexOf("$"));
        hourString=hourString.substring(2);

        minString=name.substring(name.indexOf("^ "), name.indexOf("&"));
        minString=minString.substring(2);
        //userString="1";
        //hourString="2";
        //minString="3";

        hourText.setText(hourString);
        minText.setText(minString);
        userName.append(userString);

        mThread3 = new Thread(mRunnableS);
        mThread3.start();



        //mEditText02 = (EditText) findViewById(R.id.myinternet_tcpclient_EditText02);
        // ////////////////////////////////////////////////////////////////////////////////////

        final ScaleAnimation scaleanimation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleanimation.setDuration(500);

        //
        userbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // mEditText01.append(mStrMSG);//
                userbutton.startAnimation(scaleanimation);
                modeString ="Reserved function is not enabled";
                mHandler.sendMessage(mHandler.obtainMessage(1));
            }
        });
        // ////////////////////////////////////////////////////////////////////////////////////
        //
        modebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modebutton.startAnimation(scaleanimation);
                modeString = "Mode change is not enabled";
                mHandler.sendMessage(mHandler.obtainMessage(1));


            }
        });

    }

    private Runnable mRunnableS = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            try {
                while (true) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String str = sdf.format(new Date());
                    secString = str.substring(6, 8);
                    int minInt,hourInt,secInt;
                    minInt=Integer.parseInt(minString, 10);
                    hourInt=Integer.parseInt(hourString, 10);
                    secInt=Integer.parseInt(secString,10);
                    if(secInt>=59){
                        minInt+=1;
                        minString=Integer.toString(minInt);
                        if (isWifiConnected(clockActivity.this))
                        {onlinString="no ";}
                        else {
                            onlinString="Link Broken ,Is not in service area";
                            mHandler.sendMessage(mHandler.obtainMessage(3));
                            break;
                        }

                        if(minInt>=59){
                            minInt=0;
                            minString=Integer.toString(minInt);
                            hourInt+=1;
                            hourString=Integer.toString(hourInt);
                        }
                    }
                    mHandler.sendMessage(mHandler.obtainMessage(2));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
   /* public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Are you Sure  offline now")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                    }
                })
                .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作

                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.MAIN");
                        intent.addCategory("android.intent.category.HOME");
                        startActivity(intent);
                    }
                }).show();
    }*/
    public void onBackPressed() {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.MAIN");
                        intent.addCategory("android.intent.category.HOME");
                        startActivity(intent);

    }

    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
