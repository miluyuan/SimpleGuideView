package com.example.a80002584.userguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylibrary.SimpleGuideView;

public class MainActivity extends AppCompatActivity {

    private Button mBtn;
    private TextView mTv;
    private SimpleGuideView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button) findViewById(R.id.btn1);
        mTv = findViewById(R.id.text_view);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGuideView(mTv);
            }
        });

        mView = new SimpleGuideView.Build(this)
                .setHighLightView(mBtn)
                .setHighLightPadding(0)
                .setGuildView(R.layout.view_guide_simple)
                .setClickableView(R.id.button, new SimpleGuideView.OnViewGoneListener() {
                    @Override
                    public void onViewGone() {
//                        Toast.makeText(MainActivity.this, "view gone", Toast.LENGTH_SHORT).show();
                    }
                })
                .setDirection(SimpleGuideView.Direction.BOTTOM)
                .setRadius(0)
                .setAlphaAnimation(true)
//                .setBgColor( 0x55000000)
                .build();

        FrameLayout root = (FrameLayout) findViewById(android.R.id.content);
//        GuideView guideView = (GuideView) findViewById(R.id.guide_view);
//        myGuideView(btn);
        myGuideView(mTv);
//        threeGuideView(btn);

//        HttpsVerifyHelper.init(this, "PRD.cer", false);


        Log.e(TAG, "onCreate: " );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent: " );
//        finish();
//        startActivity(new Intent(this, MainActivity.class));
    }

    private static final String TAG = "MainActivity";
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: " );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }

    private void threeGuideView(Button btn) {
        //文字图片
        final ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.huiyuan_cebianlan_mengban);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iv1.setLayoutParams(params1);

        //我知道啦
        final ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.right);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iv2.setLayoutParams(params2);

    }

    private void myGuideView(View btn) {
        SimpleGuideView guideView = new SimpleGuideView(this, new SimpleGuideView.Build(this));
        SimpleGuideView build = new SimpleGuideView.Build(this)
                .setHighLightView(btn)
                .setHighLightPadding(getResources().getDimensionPixelSize(R.dimen.radius))
                .setGuildView(R.layout.view_guide_simple)
                .setDirection(SimpleGuideView.Direction.BOTTOM)
                .setRadius(getResources().getDimensionPixelSize(R.dimen.radius))
                .build();


//                        .setBgColor( 0x55000000)

        build.show();
    }
}
