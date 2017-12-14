package com.quick.quickhybrid;

import android.content.Intent;
import android.os.Bundle;

import com.quick.core.baseapp.baseactivity.FrmBaseActivity;
import com.quick.core.ui.widget.ToastUtil;
import com.quick.jsbridge.bean.QuickBean;
import com.quick.jsbridge.view.QuickWebLoader;

import butterknife.OnClick;

public class MainActivity extends FrmBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_main);

        pageControl.getNbBar().hideNbBack();
        setTitle("Quick Hybrid");
    }


    @OnClick(R.id.btn)
    public void openPage() {
        // 打开一个新的混合开发页面
        ToastUtil.toastShort(MainActivity.this, "open2");

        Intent mintent = new Intent(MainActivity.this, QuickWebLoader.class);
        QuickBean bean = new QuickBean("file:///android_asset/examples/index.html");
        mintent.putExtra("bean", bean);
        startActivity(mintent);


    }

}
