package com.quick.quickhybrid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.quick.core.baseapp.baseactivity.FrmBaseActivity;
import com.quick.core.baseapp.component.scan.ScanCaptureActivity;
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

    @OnClick(R.id.btn_scan)
    public void scan() {
        IntentIntegrator integrator = null;
        Activity activity = MainActivity.this;

        integrator = new IntentIntegrator(activity);

        if (integrator != null) {
            integrator.setCaptureActivity(ScanCaptureActivity.class);
            integrator.initiateScan();
        }
    }

    @OnClick(R.id.btn_about)
    public void about() {
        AboutActivity.go(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == IntentIntegrator.REQUEST_CODE) {
                // 扫描二维码回传值
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                String ewmString = result.getContents();

                Intent mintent = new Intent(MainActivity.this, QuickWebLoader.class);
                QuickBean bean = new QuickBean(ewmString);
                mintent.putExtra("bean", bean);
                startActivity(mintent);
            }
        }
    }

}
