package com.quick.core.ui.widget.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;

import quick.com.core.R;

/**
 *  列表选择对话框适配器
 */
public class DialogSelectAdapter extends BaseAdapter {

    private List<HashMap<String, Object>> meumList;

    private Context con;

    private LayoutInflater inflater;

    /**
     * 是否多选
     */
    private boolean isMulti;

    public DialogSelectAdapter(Context con, List<HashMap<String, Object>> meumList) {
        this(con, meumList, false);
    }

    public DialogSelectAdapter(Context con, List<HashMap<String, Object>> meumList, boolean isMulti) {
        this.meumList = meumList;
        this.con = con;
        this.isMulti = isMulti;
        this.inflater = LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return meumList.size();
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        return meumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.frm_select_adapter, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, Object> map = meumList.get(position);
        holder.tv.setText(map.get("text").toString());
        if (isMulti) {
            holder.iv.setVisibility(View.VISIBLE);
            setCheckBg(map, holder.iv, holder.tv);
        } else {
            holder.iv.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void onItemClick(View view, int position) {
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        setCheckBg(meumList.get(position), iv, tv);
    }

    private void setCheckBg(HashMap<String, Object> map, ImageView iv, TextView tv) {
        if ("1".equals(map.get("isChecked"))) {
            map.put("isChecked", "0");
            iv.setImageResource(R.mipmap.img_unchecked_btn);
            tv.setTextColor(con.getResources().getColor(R.color.text_black));
        } else {
            map.put("isChecked", "1");
            iv.setImageResource(R.mipmap.img_checked_btn);
            tv.setTextColor(con.getResources().getColor(R.color.text_blue));
        }
    }

    public class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }
}
