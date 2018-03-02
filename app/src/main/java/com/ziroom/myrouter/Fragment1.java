package com.ziroom.myrouter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziroom.routermiddleware.IModuleUri;
import com.ziroom.routermiddleware.RouterMiddleware;

/**
 * Created by lvhl3 on 2017/12/12.
 * 类简单说明:
 */

public class Fragment1 extends Fragment {
    private static final String TAG = "WXActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_layout, null);

        view.findViewById(R.id.btn_towx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //方式一 点击按钮直接跳转到对应activity
//                RouterMiddleware.getInstance().getPathActivityRouter().build("/ModuleWeiXiao/weixiao/WXActivity").setRequestCode(100, Fragment1.this).openActivity();

                //方式一 点击按先获取到对应activity，再跳转
                Class clazz = RouterMiddleware.getInstance().getClassRouter().build("/ModuleWeiXiao/weixiao/WXActivity").getTheClass();
                Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_toks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouterMiddleware.getInstance().getUriActivityRouter().create(IModuleUri.class, 300, getActivity()).startKSActivity("20090001");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            Log.i(TAG, "Fragment1 的 onActivityResult 执行了");
        } else if (requestCode == 300) {
            Log.i(TAG, "Fragment1 的 onActivityResult 执行了 IModuleUri");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
