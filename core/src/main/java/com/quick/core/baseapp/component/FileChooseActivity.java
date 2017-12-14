package com.quick.core.baseapp.component;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quick.core.baseapp.baseactivity.FrmBaseActivity;
import com.quick.core.util.common.QuickUtil;
import com.quick.core.util.io.FileSorter;
import com.quick.core.util.io.FileUtil;
import com.quick.core.util.reflect.ResManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import quick.com.core.R;

/**
 * Created by dailichun on 2017/12/7.
 * 选择本地文件
 */
public class FileChooseActivity extends FrmBaseActivity {

    public static final String FILE_TYPE_BACK = "back";
    public static final String FILE_TYPE_DIR = "dir";
    public static final String FILE_TYPE_FILE = "file";

    private ListView lv;

    private List<Map<String, Object>> mData;

    /**
     * 用作返回按钮作标记
     */
    private ArrayList<String> backflaglist;

    private MyAdapter adapter;

    private String rootPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.frm_localfilelist_activity);

        setTitle(getString(R.string.file_local));

        backflaglist = new ArrayList<>();

        rootPath = getIntent().getStringExtra("rootPath");
        if (TextUtils.isEmpty(rootPath)) {
            rootPath = Environment.getExternalStorageDirectory().getPath();
        }
        if (!TextUtils.isEmpty(rootPath)) {
            mData = getData(rootPath);
        } else {
            mData = new ArrayList<>();
            toast(getString(R.string.file_no_sdcard));
        }

        lv = (ListView) findViewById(ResManager.getIdInt("lv"));
        adapter = new MyAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (mData.get(position).get("type").equals(FILE_TYPE_DIR)) {
                        String path = mData.get(position).get("path").toString();
                        backflaglist.add(path);
                        mData = getData(path);
                        adapter.notifyDataSetChanged();
                    } else if (mData.get(position).get("type").equals(FILE_TYPE_BACK)) {
                        backLogic();
                    } else {
                        String path = mData.get(position).get("path").toString();
                        QuickUtil.quickResult(pageControl.getActivity(), path);
                        finish();
                    }
                } catch (Exception e) {
                    finish();
                }
            }
        });
    }

    /**
     * 返回按钮操作逻辑
     */
    public void backLogic() {
        if (backflaglist.size() == 0) {
            finish();
            return;
        } else if (backflaglist.size() == 1) {
            mData = getData(rootPath);
        } else {
            mData = getData(backflaglist.get(backflaglist.size() - 2));
        }
        if (backflaglist.size() > 0) {
            backflaglist.remove(backflaglist.size() - 1);
        }
        adapter.notifyDataSetChanged();
    }

    private List<Map<String, Object>> getData(String dir) {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map;

        List<File> files = FileSorter.sortByName(dir);

        if (dir.equals("")) {
            map = new HashMap<>();
            map.put("type", FILE_TYPE_DIR);
            map.put("path", rootPath);
            map.put("name", getString(R.string.file_sdcard));
            map.put("icon", R.mipmap.img_fc_sdcard);
            list.add(map);

        } else {
            map = new HashMap<>();
            map.put("type", FILE_TYPE_BACK);
            map.put("path", "");
            map.put("name", dir.equals(rootPath) ? getString(R.string.file_sdcard) : new File(dir).getName());
            map.put("icon", R.mipmap.img_fc_back);
            list.add(map);

            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    map = new HashMap<>();
                    map.put("type", FILE_TYPE_DIR);
                    map.put("path", currentFile.getAbsolutePath());
                    map.put("name", currentFile.getName());
                    map.put("icon", R.mipmap.img_file);
                    list.add(map);
                } else {
                    map = new HashMap<>();
                    map.put("type", FILE_TYPE_FILE);
                    map.put("path", currentFile.getAbsolutePath());
                    map.put("name", currentFile.getName());
                    list.add(map);
                }
            }
        }
        return list;
    }

    public final class ViewHolder {
        public ImageView fc_img;
        public TextView fc_title;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.frm_localfilelist_adapter, null);
                holder.fc_img = (ImageView) convertView.findViewById(R.id.fc_img);
                holder.fc_title = (TextView) convertView.findViewById(R.id.fc_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String fileName = (String) mData.get(position).get("name");
            if (mData.get(position).containsKey("icon")) {
                holder.fc_img.setImageResource((int) mData.get(position).get("icon"));
            } else {
                holder.fc_img.setImageBitmap(FileUtil.getImgByFileName(FileChooseActivity.this, fileName));
            }
            holder.fc_title.setText(fileName);
            return convertView;
        }
    }

    /**
     * 进入文件选择界面
     *
     * @param activity
     * @param requestCode
     */
    public static void goFileChooseActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, FileChooseActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 进入文件选择界面
     *
     * @param fragment
     * @param requestCode
     */
    public static void goFileChooseActivity(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), FileChooseActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 进入文件选择界面
     *
     * @param fragment
     * @param requestCode
     */
    public static void goFileChooseActivity(Object fragment, int requestCode) {
        if (fragment instanceof Fragment) {
            goFileChooseActivity((Fragment) fragment, requestCode);
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            goFileChooseActivity((android.support.v4.app.Fragment) fragment, requestCode);
        }
    }

    /**
     * 进入文件选择界面
     *
     * @param fragment
     * @param requestCode
     */
    public static void goFileChooseActivity(android.support.v4.app.Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), FileChooseActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }
}
