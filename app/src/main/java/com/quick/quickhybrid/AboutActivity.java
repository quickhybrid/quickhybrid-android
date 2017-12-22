package com.quick.quickhybrid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.quick.core.baseapp.baseactivity.FrmBaseActivity;

public class AboutActivity extends FrmBaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.about_activity);

        setTitle(getString(R.string.about_title));

    }

    public static void go(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

}
