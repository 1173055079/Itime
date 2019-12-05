package com.casper.itime;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;

import com.casper.itime.Data.DateHandle;
import com.casper.itime.Data.ListViewData;
import com.casper.itime.listViewAdapter.ListViewMainAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar; //标题栏
    private FloatingActionButton fab; //悬浮菜单按钮
    private DrawerLayout drawer; //抽屉布局
    private NavigationView navigationView; //侧滑菜单

    public static ArrayList<ListViewData> listViewData = new ArrayList<ListViewData>(); //用来记录新建时的配置，给listView显示用

    private ListViewMainAdapter theAdapter;
    private ListView listViewShow;

    private AlertDialog dialog;

    public static ArrayList<DateHandle> dateHandles = new ArrayList<DateHandle>(); //列表中所有子项的具体倒计时数据
    //private Calendar calendar;

    public static Boolean beCountDown = false; //用来判断倒计时线程是否已经启动的变量，全局变量是担心被销毁
    public static int backgroundColor = R.color.cyan; //通过getResources().getCoror(backgroundColor)方法可以设置组件的背景颜色
    private Drawable color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar); //获取并设置标题栏
        setSupportActionBar(toolbar); //设置按钮菜单
        toolbar.setBackgroundColor(getResources().getColor(backgroundColor));




        fab = findViewById(R.id.fab);  //获取悬浮按钮并设置监听,进行“新建”功能
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,newBuiltActivity.class); //点击悬浮按钮，进入新建倒计时页面
                intent.putExtra("Function","new_built"); //因为有两个Activity能打开新建界面，所以这里加上一个标识符用来识别
                startActivityForResult(intent,1); // 1作为结果返回处理时的识别数字
            }
        });

        drawer = findViewById(R.id.drawer_layout);  //获取抽屉布局并设置监听
        //ActionBarDrawerToggle  是 DrawerLayout.DrawerListener实现，和 NavigationDrawer搭配使用，推荐用这个方法，符合Android design规范。
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); //这里设置了标题栏左侧按钮的响应事件
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);//获取导航视图并设置菜单监听，对应上面实现的接口
        navigationView.setNavigationItemSelectedListener(this); //设置侧滑菜单

        listViewShow= (ListView) this.findViewById(R.id.content_list_view); //找到列表
        listViewShow.setOnItemClickListener(new AdapterView.OnItemClickListener() { //列表子项的点击事件,点击进入详细页面
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Log.v("点击的位置是：",position+" ");
                intent.putExtra("position",position);
                intent.putExtra("Function","Detail");
                startActivityForResult(intent,2); //2表示开启的是倒计时详细页面
            }
        });


    }

    @Override
    public void onBackPressed() { //当按下返回键的时候，对抽屉进行设置，这个地方要知道如何关闭和打开抽屉
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        /**
         *  drawer.closeDrawer(GravityCompat.START);从开始关闭抽屉
         *  drawer.openDrawer();打开抽屉，方向自选
         */
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //menu是指标题栏所点击的按钮,这里实现右上角按钮的菜单
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu); //为右上角三个点的按钮设置一个菜单
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //右上角三个点的按钮出现的菜单项的响应事件
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//当导时航栏菜单被单击时，根据ID判断并给出响应
        // Handle navigation view item clicks here.
        int id = item.getItemId(); //获取被点击菜单项的ID，以便于进行判断响应事件

        if (id == R.id.nav_home) {
            // Handle the camera action
        }
        if(id == R.id.set_color){
            showSetDeBugDialog(); //显示弹窗
        }

        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ //处理新建页面操作
            if(resultCode == RESULT_OK) {
                String startDate = getSystemTime();

                try {
                    DateHandle dateHandle = new DateHandle(); //用来接收数字化的倒计时时间。以便于进行计算和比较,作用域要限制在这个响应时间内，
                    //否则会造成Arraylist<>中存储的数据的指针都指向同一个内存地址，导致数组dateHandles内的数据都是变成一样的。
                    ListViewData listdata = (ListViewData) data.getSerializableExtra("endDate");
                    String endDate = listdata.getDate()+listdata.getTime();
                    dateHandle = computeDay(startDate, endDate, dateHandle); //先计算一次倒计时，以便于设置数据显示到listView上

                    /*listdata.setDate(data.getStringExtra("Date"));
                    listdata.setNote(data.getStringExtra("Note"));
                    listdata.setTime(data.getStringExtra("Time"));
                    listdata.setTitle(data.getStringExtra("Title"));*/

                    listdata.setCountDown(dateHandle.getCountdown()); //取得新建Activity中设置的日期，以便于进行倒计时计算
                    Toast.makeText(this, dateHandle.getCountdown() + "天", Toast.LENGTH_SHORT).show();
                    dateHandles.add(dateHandle); //将要进行倒计时计算的数据放入

                    /*
                    获得新建的数据后，配置listView
                    */
                    listViewData.add(listdata); //将设置好的一个日期添加到队列中，作为listView适配器的数据源
                    //同时作为用户的配置日期的信息存储起来，之后页面的显示都要从其中调用数据

                    theAdapter = new ListViewMainAdapter(this, R.layout.listview_item, listViewData); //设置适配器
                    listViewShow.setAdapter(theAdapter); //给listView绑定上适配器，之后就可以显示数据了,每个项的布局都是R.layout.listview_item的样式

                    //this.registerForContextMenu(listViewSuper); //为listView组件注册一个上下文菜单
                   /* Uri imageuri;
                    if (( imageuri = Uri.parse(data.getStringExtra("Imageuri")))!= null){
                        ImageView imageView = (ImageView) findViewById(R.id.listViewItem_image); //设置好默认图片
                        imageView.setImageURI(imageuri);
                     }*/
                    if(!beCountDown) //防止倒计时线程重复运行,所以加个变量进行判断
                        countDownTime();

                }
                catch (Exception e){
                }
            }

        }
        if(requestCode == 2){ //处理倒计时页面
            theAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 自定义dialog 简单自定义布局
     */
    private void showSetDeBugDialog() {
        AlertDialog.Builder setDeBugDialog = new AlertDialog.Builder(this);
        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_seekbar, null);
        //将界面填充到AlertDiaLog容器
        setDeBugDialog.setView(dialogView);
        //初始化控件
        SeekBar seekBar = (SeekBar)dialogView.findViewById(R.id.seekBar);
        Button seekBarOk = (Button)dialogView.findViewById(R.id.seekbar_ok);
        Button seekBarCancel = (Button)dialogView.findViewById(R.id.seekbar_cancel);
        LinearGradient test = new LinearGradient(0.f, 0.f, 900.f, 0.0f,
                new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF,
                        0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF},
                null, Shader.TileMode.CLAMP);

        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);
        seekBar.setProgressDrawable((Drawable)shape);
        //点击外部消失弹窗
        setDeBugDialog.setCancelable(true);
        //创建AlertDiaLog
        setDeBugDialog.create();
        //AlertDiaLog显示
        final AlertDialog customAlert = setDeBugDialog.show();

    }


    public String getSystemTime() { //获取系统时间并格式化为字符串
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d1=new Date(time);
        String startDate=format.format(d1);
        return  startDate;
    }

    public DateHandle computeDay(String startDate,String endDate,DateHandle data) throws ParseException { //传进来两个日期，然后计算之间相差的时间（精确到秒）
        String s1 = endDate;
        String s2 = startDate;
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss"); //精确到秒的格式化字符串,不足十位数的会补0，所以字符串也不能少0
        Calendar calendar = new GregorianCalendar();
        Date d1 = df.parse(s1);
        Date d2 = df.parse(s2);

        long countdown = ((d1.getTime() - d2.getTime()) / (60 * 60 * 1000 * 24)); //这里也是天数
        long between = d1.getTime() - d2.getTime(); //终止时间减去现在的时间,这里毫秒数
        long day = between / (24 * 60 * 60 * 1000); //这里是天数
        long hour = (between / (60 * 60 * 1000) - day * 24); //这里是小时
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60); //分钟
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60); //秒

        data.setCountdown(countdown); //将计算结果返回
        data.setBetween(between);
        data.setDay(day);
        data.setHour(hour);
        data.setMin(min);
        data.setS(s);

        return data;
    }

    public void countDownTime(){ //以下为循环倒计时，只要这个Acitivity没有结束，这个线程就会继续执行

        final Handler handler = new Handler();
        Runnable task = new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                handler.postDelayed(this,1*1000);//设置循环时间，此处是1秒
                //需要执行的代码
                beCountDown = true;

                String startDate = getSystemTime(); //获取当前时间的字符串
                for(int i = 0;i<listViewData.size();i++){ //对设置好的终止日期的队列遍历，循环计算倒计时
                    try {
                        ListViewData data = listViewData.get(i);
                        DateHandle dateThread = dateHandles.get(i); //每个设置好的时间都有独立的数组用来存储其倒计时,这里get出来的是指针，
                                                        // 赋值给一个变量后，这个变量的值改变，数据Arraylist内的数值也会跟着改变。即Arraylist使动态变化的。

                        dateThread = computeDay(startDate,data.getDate() + data.getTime(), dateThread); //传入时间字符串计算时间
                        Log.v("倒计时",dateThread.getDay()+"天"+dateThread.getHour()+"小时"+dateThread.getMin()+"分钟"+dateThread.getS()+"秒");
                        long y = data.getCountDown();
                        long x = dateThread.getCountdown();
                        if(x != y) { //当天数变化时，对列表进行更新
                            data.setCountDown(x);
                            listViewData.set(i,data);
                            theAdapter.notifyDataSetChanged();
                        }
                        //dateHandles.set(i,dateThread); //更新数据,这里有个问题，数据全变了，以后再改
                    }catch (Exception e){
                    }
                }
            }
        };
        handler.post(task);//立即调用,将Runnable 这个对象传递给Handle,然后Handler会在looper中调用这个对象的run方法
    }
}
