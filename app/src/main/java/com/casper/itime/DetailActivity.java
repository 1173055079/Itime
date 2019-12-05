package com.casper.itime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.casper.itime.Data.DateHandle;
import com.casper.itime.Data.ListViewData;
import com.casper.itime.listViewAdapter.ListViewDetailAdapter;

import java.util.ArrayList;

import static com.casper.itime.MainActivity.dateHandles; //全局变量
import static com.casper.itime.MainActivity.listViewData; //全局变量

public class DetailActivity extends AppCompatActivity {

    private ListViewData Data = new ListViewData(); //用来记录新建时的配置，给显示用
    private DateHandle dateHandle = new DateHandle(); //列表中所有子项的具体倒计时数据
    private Handler mHandler;
    private int position;
    private ImageButton Edit,Back,Delete;
    private ListView listViewDatail;
    private ImageView imageView;


    TextView Title,Date,countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);
        dateHandle = dateHandles.get(position); //获得被点击项的当前倒计时信息。
        Data = listViewData.get(position);

        Title = (TextView)findViewById(R.id.textView_detail_title);
        Date = (TextView)findViewById(R.id.textView_detail_date);
        countDown = (TextView)findViewById(R.id.textView_detail_countdown);
        imageView = (ImageView)findViewById(R.id.image_detail);


        Title.setText(Data.getTitle());
        Date.setText(Data.getYear()+"年 "+Data.getMonth()+"月 "+Data.getMonthOfDay()+"天 "+Data.getHour()+"小时 "+Data.getMin()+"分钟 "+Data.getS()+"秒");
        if(Data.getImageUri() != null) //如果添加了默认图片
        {
            imageView.setImageURI(Data.getImageUri());
        }

        mHandler = new Handler();
        mHandler.post(mUpdate); //启动更新文本的子线程，当文本数据源改变时，可以修改文本显示

        Edit = (ImageButton)findViewById(R.id.button_detail_changedata);
        Edit.setOnClickListener(new View.OnClickListener() { //点击铅笔按钮，进入“编辑修改”功能
            @Override
            public void onClick(View v) {
                Intent toEdit = new Intent(DetailActivity.this,newBuiltActivity.class);
                toEdit.putExtra("position",position);
                toEdit.putExtra("Function","Edit");
                startActivityForResult(toEdit,3);
            }
        });
        Back = (ImageButton)findViewById(R.id.button_detail_back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); //点击按钮后，就执行返回键的操作。
            }
        });

        Delete = (ImageButton)findViewById(R.id.button_detail_delete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateHandles.remove(position);
                listViewData.remove(position);
                setResult(RESULT_OK);
                DetailActivity.this.finish();
            }
        });

        ArrayList<String> switchTitle = new ArrayList<>();
        switchTitle.add("Notification");
        switchTitle.add("Show in Calendar");
        switchTitle.add("IconShortcut");

        listViewDatail = (ListView)findViewById(R.id.listview_detail);
        final ListViewDetailAdapter mAdapter = new ListViewDetailAdapter(getApplicationContext(),switchTitle);
        listViewDatail.setAdapter(mAdapter); //配置一个switch的列表。
        listViewDatail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Switch aSwitch=mAdapter.getaSwitch(position);
                if(aSwitch.isChecked()){
                    aSwitch.setChecked(false);
                    //进行业务处理
                }else {
                    aSwitch.setChecked(true);
                    //进行业务处理
                }
            }
        });
    }

    private Runnable mUpdate = new Runnable() { //用子线程更新文本的倒计时信息
        public void run() {
            countDown.setText("倒计时：" + dateHandle.getDay() + "天" + dateHandle.getHour() + "小时" + dateHandle.getMin() + "分钟" + dateHandle.getS() + "秒");

            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdate); //Activity结束时，终止子线程，防止占用空间或者重复启动
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3){ //修改后的处理事件，在这里重新设置显示的内容
            if(resultCode == RESULT_OK) {
                ListViewData endDate = (ListViewData) data.getSerializableExtra("endDate");
                Title.setText(endDate.getTitle());
                Date.setText(Data.getYear()+"年 "+Data.getMonth()+"月 "+Data.getMonthOfDay()+"天 "+Data.getHour()+"小时 "+Data.getMin()+"分钟 "+Data.getS()+"秒");
                listViewData.set(position,endDate);
            }
        }
    }
}
