package com.quick.core.baseapp.baseactivity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import quick.com.core.R;

/**
 * Created by dailichun on 2017/12/8.
 * Fragment的Activity容器
 */
public class FrmFragmentActivity extends FrmBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageControl.getNbBar().hide();

        try {
            String fragmentName = getIntent().getStringExtra("fragment");
            Bundle bundle = getIntent().getBundleExtra("data");
            if (!TextUtils.isEmpty(fragmentName)) {
                Fragment fragment = (Fragment) Class.forName(fragmentName).newInstance();
                if (bundle!=null){
                    fragment.setArguments(bundle);
                }
                getFragmentManager().beginTransaction().add(R.id.baseContent,fragment).commit();
            }else{
                Log.e("FrmFragmentActivity",fragmentName+"未找到");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void go(Context context, Class fragmentClass){
        go(context,fragmentClass,null);
    }

    public static void go(Context context, Class fragmentClass, Bundle bundle){
        Intent intent = new Intent(context,FrmV4FragmentActivity.class);
        intent.putExtra("fragment",fragmentClass.getName());
        intent.putExtra("data",bundle);
        context.startActivity(intent);
    }
}
