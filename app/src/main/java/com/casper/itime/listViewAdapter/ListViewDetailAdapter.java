package com.casper.itime.listViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;

import com.casper.itime.R;

import java.util.ArrayList;

public class ListViewDetailAdapter extends BaseAdapter {

    private ArrayList<String> switchTitle = new ArrayList<String>();
    private ArrayList<Switch> list_switch = new ArrayList<>();
    private Context mContext;
    private Switch aSwitch;

    public ListViewDetailAdapter(Context context,ArrayList<String> Title) {
            mContext = context;
            switchTitle = Title;
    }

    @Override
    public int getCount() {
        return switchTitle.size();
    }

    @Override
    public Object getItem(int position) {
        return switchTitle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Switch getaSwitch(int position){ //获得相应位置的switch组件

        return list_switch.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_detail_item,null); //获取布局中的视图

        aSwitch=(Switch)convertView.findViewById(R.id.switchDetail);

        aSwitch.setText(switchTitle.get(position)); //设置switch开关的标题
        list_switch.add(aSwitch);

        return convertView;

    }
}
