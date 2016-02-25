package com.example.jtdavy.gradletest;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {


    private ImageButton Sign, Quit;
    private TextView mEditText01;
    private static final String SERVERIP = "192.168.1.17";
    //private static final int SERVERPORT = 30000;
    private static final int SERVERPORT = 8000;
    private Thread mThread = null;
    private Thread mThread2 = null;
    private Thread mThread3 = null;
    private Socket mSocket = null;
    private BufferedReader mBufferedReader = null;
    private PrintWriter mPrintWriter = null;
    private BufferedReader br=null ;
    private String mStrMSG = "nihao";
    private static String ETAG = "Exception";
    private String macaddress = "";
    private String name = "huangwei";
    private DataInputStream in;
    private TextView minText;       //分
    private TextView secText;       //秒
    private TextView synaText;
    private  String result;

    // ////////////////////////////////////////////////////////////////////////////////////

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mEditText01.setText(mStrMSG);//
                    synaText.setText(result);
                    break;
                case 2:
                    mEditText01.setText(mStrMSG);//
                    break;
                case 3:
                    mEditText01.append(mStrMSG);//
                    break;
                case 100:
                    synaText.setText((String) msg.obj);
                    //secText.setText(result);
                    break;
            }
        }

        ;
    };

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);

        //Sign = (ImageButton) findViewById(R.id.signin);
        //Quit = (ImageButton) findViewById(R.id.quitout);
        //mEditText01 = (TextView) findViewById(R.id.textView2);
       // minText = (TextView) findViewById(R.id.mint);
        //secText = (TextView) findViewById(R.id.sec);
        //synaText = (TextView) findViewById(R.id.textView3);


        //mEditText02 = (EditText) findViewById(R.id.myinternet_tcpclient_EditText02);
        // ////////////////////////////////////////////////////////////////////////////////////

        final ScaleAnimation scaleanimation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleanimation.setDuration(500);

        //
        Sign.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // mEditText01.append(mStrMSG);//
                Sign.startAnimation(scaleanimation);
                mThread2 = new Thread(mRunnableC);
                mThread2.start();

            }
        });
        // ////////////////////////////////////////////////////////////////////////////////////
        //
        Quit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Quit.startAnimation(scaleanimation);
              /*  try
                {
                    mSocket.close();
                } catch (Exception e)
                {
                    // TODO: handle exception
                    Log.e(TAG, e.toString());
                }*/

                mThread = new Thread(mRunnable);
                mThread.start();

            }
        });

        mThread2 = new Thread(mRunnableC);
        mThread2.start();
       /* mThread = new Thread(mRunnable);
        mThread.start();*/
        mThread3 = new Thread(mRunnableS);
        mThread3.start();

    }

    // ////////////////////////////////////////////////////////////////////////////////////
    //
    private Runnable mRunnable = new Runnable() {
        public void run() {
                try {
                    /*if ((mStrMSG = in.readUTF()) != null)
                    {
                        System.out.println("����readline true");
                        mStrMSG += "\n";//
                        mHandler.sendMessage(mHandler.obtainMessage(1));//
                    }*/

                   /* while((mStrMSG = br.readLine())!=null) {
                        mStrMSG = br.readLine();
                    mHandler.sendMessage(mHandler.obtainMessage(1));//
                    br.close();*/
                    while((mStrMSG = mBufferedReader.readLine()) != null)
                        {
                            mStrMSG += "\n";// 消息换行
                            System.out.println(mStrMSG);
                            //mStrMSG = mStrMSG.substring(13, 23);

                            mHandler.sendMessage(mHandler.obtainMessage(1));// 发送消息
                        }
                } catch (Exception e) {
                    mStrMSG = "Please try again later ";
                    mHandler.sendMessage(mHandler.obtainMessage(2));//
                    Log.e(ETAG, e.toString());
                    mThread2 = new Thread(mRunnableC);
                    mThread2.start();
                }
            }


    };
    //
    private Runnable mRunnableC = new Runnable() {
        public void run() {
            try {
                mSocket = new Socket(SERVERIP, SERVERPORT);

                mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
                in = new DataInputStream(mSocket.getInputStream());
                //String s = in.readUTF();
                InputStream is =mSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br =new BufferedReader(isr);


                macaddress = getLocalMacAddress();
                //String str = mEditText02.getText().toString() + macaddress;
                String str = "Mac Address # " + macaddress + "Client Name # " + name;

                mPrintWriter.print(str);
                mPrintWriter.flush();

                System.out.println("step int login");
                mStrMSG = "step in login";
                mHandler.sendMessage(mHandler.obtainMessage(3));

            } catch (Exception e) {
                mStrMSG = "  Failed to connect...";
                mHandler.sendMessage(mHandler.obtainMessage(3));
                Log.e(ETAG, e.toString());
            }
        }
    };

    //
    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    private Runnable mRunnableS = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            try {
                while (true) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String str = sdf.format(new Date());
                    //String result = str.substring(6, str.indexOf("0"));
                    result = str.substring(6, 8);
                    mHandler.sendMessage(mHandler.obtainMessage(100, str));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };



 }
