package com.casper.itime.listViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.casper.itime.Data.ListViewData;
import com.casper.itime.R;

import java.util.List;

public class ListViewMainAdapter extends ArrayAdapter<ListViewData> {

    private  int resourceId;
    public ListViewMainAdapter(Context context, int resource, List<ListViewData> objects) {
        super(context, resource, objects);
        resourceId=resource; //获得列表项的布局的ID
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater= LayoutInflater.from(this.getContext());
        View item = mInflater.inflate(this.resourceId,null);  //以resourceId的布局作为数据项显示的格式,所以变量item即为列表项的视图

        ImageView img = (ImageView)item.findViewById(R.id.listViewItem_image); //从列表项的视图中获得组件的ID并赋值给某变量，以便于将数据给组件
        TextView title = (TextView)item.findViewById(R.id.listViewItem_title);
        TextView date = (TextView)item.findViewById(R.id.listViewItem_Date);
        TextView countDown = (TextView)item.findViewById(R.id.countdownText);

        ListViewData list_item= this.getItem(position); //从数据项中获得数据源,this指代的是这个适配器

        title.setText(list_item.getTitle());
        date.setText(list_item.getDate()); //从list_item数据源中取出数据，放在视图View的组件上
        countDown.setText(String.valueOf(list_item.getCountDown())+"天");

        return item;
    }
}
