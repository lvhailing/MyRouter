package com.ziroom.myrouter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ziroom.routermiddleware.RouterMiddleware;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    TextView mFragment1Tv;
    TextView mFragment2Tv;
    TextView mFragment3Tv;

    FrameLayout mainFl;

    FragmentManager manager;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        switchContent(1);

        Log.i(TAG, "onCreate");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            Log.i(TAG, "MainActivity 的 onActivityResult 执行了");
        }else if (requestCode == 300) {
            Log.i(TAG, "MainActivity 的 onActivityResult 执行了 IModuleUri");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initListener() {
        mFragment1Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchContent(1);
            }
        });
        mFragment2Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchContent(2);
            }
        });
        mFragment3Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchContent(3);
            }
        });
    }

    public void switchContent(int index) {
        FragmentTransaction transaction = manager.beginTransaction();
        hideAll(transaction);
        if (1 == index) {
            if (!fragment1.isAdded()) {
                transaction.add(R.id.main_fl, fragment1);
            }
            transaction.show(fragment1);
        } else if (2 == index) {
            if (!fragment2.isAdded()) {
                transaction.add(R.id.main_fl, fragment2);
            }
            transaction.show(fragment2);
        } else if (3 == index) {
            if (!fragment3.isAdded()) {
                transaction.add(R.id.main_fl, fragment3);
            }
            transaction.show(fragment3);
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideAll(FragmentTransaction transaction) {
        if (fragment1 != null && fragment1.isVisible()) {
            transaction.hide(fragment1);
            Log.i(TAG, "fragment1 hide");
        }
        if (fragment2 != null && fragment2.isVisible()) {
            transaction.hide(fragment2);
            Log.i(TAG, "fragment2 hide");
        }
        if (fragment3 != null && fragment3.isVisible()) {
            transaction.hide(fragment3);
            Log.i(TAG, "fragment3 hide");
        }
    }

    private void initView() {
        mFragment1Tv = (TextView) findViewById(R.id.fragment1_tv);
        mFragment2Tv = (TextView) findViewById(R.id.fragment2_tv);
        mFragment3Tv = (TextView) findViewById(R.id.fragment3_tv);
        mainFl = (FrameLayout) findViewById(R.id.main_fl);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();

        Bundle bundle = new Bundle();
        bundle.putString("aaa", "aaa");
        Object object = RouterMiddleware.getInstance().getPathFragmentRouter().build("/ModuleWeiXiao/weixiao/WXFragment").setBundle(bundle).getFragment();
        if (object != null && object instanceof Fragment) {
            fragment3 = (Fragment) object;
        }else {
            fragment3 = new Fragment();
        }

        manager = getSupportFragmentManager();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
