package com.example.jtdavy.gradletest;

/**
 * Created by JTdavy on 2015/8/27.
 */
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.net.wifi.WifiInfo;
        import android.net.wifi.WifiManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.view.animation.Animation;
        import android.view.animation.ScaleAnimation;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.net.Socket;

public class loginActivity extends Activity {
    private ProgressBar progressBar;
    private ImageButton backButton;
    private ImageButton Sign, Quit;
    private TextView mEditText01, stateText_tv1;
    private static final String SERVERIP = "192.168.1.34";
    private static final int SERVERPORT = 8000;

    private Thread mThread2 = null;
    private Thread mThread = null;

    private Socket mSocket = null;
    private BufferedReader mBufferedReader = null;
    private PrintWriter mPrintWriter = null;
    private String stateText_string = "";
    private static String ETAG = "Exception";
    private String macaddress = "";
    private EditText userName;
    private String userNameValue;
    private String mStrMSG = "";
    private String consuccess = "";


    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    stateText_tv1.setText(stateText_string);//
                    break;
                case 2:
                    stateText_tv1.setText(mStrMSG);//
                    break;
                case 3:
                    stateText_tv1.setText(mStrMSG);//
                    break;
            }
        }

        ;
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logo);

        progressBar = (ProgressBar) findViewById(R.id.pgBar);
        backButton = (ImageButton) findViewById(R.id.btn_back);
        stateText_tv1=(TextView)findViewById(R.id.tv1);
        userName = (EditText) findViewById(R.id.et_zh);

        final ScaleAnimation scaleanimation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleanimation.setDuration(500);

        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                backButton.startAnimation(scaleanimation);

                userNameValue = userName.getText().toString();
                //System.out.println(userNameValue);

                if(userNameValue.equals(""))
                {
                    stateText_string="There was no user name\n" +"Please enter them on the line";
                    mHandler.sendMessage(mHandler.obtainMessage(1));
                }else{
                    mThread2 = new Thread(mRunnableC);
                    mThread2.start();
                }
            }
        });

    }

    private Runnable mRunnableC = new Runnable() {
        public void run() {
            try {
                mSocket = new Socket(SERVERIP, SERVERPORT);

                mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);

                //String s = in.readUTF();
                InputStream is =mSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br =new BufferedReader(isr);


                macaddress = getLocalMacAddress();
                //String str = mEditText02.getText().toString() + macaddress;
                String str = "!" + macaddress +"@"+ userNameValue+"#" ;
                //String str = macaddress + userNameValue ;
                System.out.println(str);

                mPrintWriter.print(str);
                mPrintWriter.flush();

                stateText_string = "Login successful....";
                mHandler.sendMessage(mHandler.obtainMessage(1));
                if(stateText_string.equals("Login successful...."))
                {
                    mThread = new Thread(mRunnable);
                    mThread.start();
                }else{
                    stateText_string = "Failed to connect\nMaybe the server has been closed\nor\n You are not in service area\ntry again later...";
                    mHandler.sendMessage(mHandler.obtainMessage(1));

                }
            } catch (Exception e) {
                stateText_string = "the server has been closed\nor\n You are not in service area\ntry again later...";
                mHandler.sendMessage(mHandler.obtainMessage(1));
                Log.e(ETAG, e.toString());
            }
        }
    };

    private Runnable mRunnable = new Runnable() {
        public void run() {
            try {
                while((mStrMSG = mBufferedReader.readLine()) != null)
                {
                    mStrMSG += "";// 消息换行
                    //System.out.println("readline"+mStrMSG);
                    if(mStrMSG.contains("your mac was not expected mac")) {
                        mStrMSG= "Connect successful....\nBut\n"+mStrMSG+"Please check your input";
                        mHandler.sendMessage(mHandler.obtainMessage(2));// 发送消息
                        break;
                    }
                    else{
                        consuccess="Connect successful....\nYour information \n"+mStrMSG;
                        mHandler.sendMessage(mHandler.obtainMessage(3));// 发送消息

                        Intent intent = new Intent(loginActivity.this, clockActivity.class);
                        Bundle bundle=new Bundle();
                        //传递name参数为tinyphp
                        bundle.putString("name", mStrMSG);
                        intent.putExtras(bundle);
                        loginActivity.this.startActivity(intent);
                    }
                }
                while((mStrMSG = mBufferedReader.readLine()) == null){
                    stateText_string = "The server crashed\n" + "Please contact your administrator";
                    mHandler.sendMessage(mHandler.obtainMessage(1));//

                }

            } catch (Exception e) {
                stateText_string = "  Failed to connect,try again later...";
                mHandler.sendMessage(mHandler.obtainMessage(1));//
                Log.e(ETAG, e.toString());
                mThread2 = new Thread(mRunnableC);
                mThread2.start();
            }
        }


    };


    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

}
