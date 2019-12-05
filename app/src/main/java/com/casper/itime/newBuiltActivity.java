package com.casper.itime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.casper.itime.Data.DateHandle;
import com.casper.itime.Data.ListViewData;

import static com.casper.itime.MainActivity.backgroundColor;
import static com.casper.itime.MainActivity.dateHandles; //全局变量
import static com.casper.itime.MainActivity.listViewData; //全局变量

import java.util.ArrayList;
import java.util.Calendar;

public class newBuiltActivity extends AppCompatActivity {

    private Button chooseDate;
    private ImageButton ok,back;
    private Button addPicture,cyclebutton;
    private EditText Title,Note;
    private String date,time;
    private RelativeLayout toolbar_newbuild;
    private Uri Imageuri = null;

    private Calendar calendar; //获得系统时间的类
    //选择日期Dialog
    private DatePickerDialog datePickerDialog;
    //选择时间Dialog
    private TimePickerDialog timePickerDialog;

    private ListViewData endDate = new ListViewData(); //用来存放设置好的截止日期。


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar(); //隐藏这个Activity的标题栏，让xml中inlcude的标题栏显示出来
        if(actionBar!=null){
            actionBar.hide();
        }

        setContentView(R.layout.activity_new_built);


        chooseDate = (Button)findViewById(R.id.date_button);
        ok = (ImageButton) findViewById(R.id.okbutton);
        back = (ImageButton)findViewById(R.id.backbutton);
        addPicture = (Button)findViewById(R.id.button_picture_newbuild) ;
        Title = (EditText)findViewById(R.id.title_input);
        Note = (EditText)findViewById(R.id.note_input);
        cyclebutton = (Button)findViewById(R.id.cycle_button);
        toolbar_newbuild = (RelativeLayout) findViewById(R.id.toolbar_newbuild);

        toolbar_newbuild.setBackgroundColor(getResources().getColor(backgroundColor)); //设置上面可编辑部分的背景颜色,下面的按键不用设置背景色

        calendar = Calendar.getInstance();
        chooseDate.setOnClickListener(new ChooseDate());  //点击日期按钮后的响应事件，会弹出一个日历和时钟。

        Intent toEdit  = getIntent();
        String function = toEdit.getStringExtra("Function"); //判断是新建还是修改原有的
        if(function.equals("Edit")){
            int position = toEdit.getIntExtra("position",0);
            Title.setText(listViewData.get(position).getTitle());
            Note.setText(listViewData.get(position).getNote());
        }

        ok.setOnClickListener(new View.OnClickListener() { //点击按钮后，将设置好的日期信息传回去
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();


                /*intent.putExtra("Date",date);
                intent.putExtra("Time",time);
                intent.putExtra("Title",Title.getText().toString());
                intent.putExtra("Note",Note.getText().toString());*/

                intent.putExtra("Calendar",calendar);

                //intent.putExtra("Imageuri", Imageuri.toString());

                endDate.setTitle(Title.getText().toString());
                endDate.setTime(time);
                endDate.setDate(date);
                endDate.setNote(Note.getText().toString()); //将配置信息放入，然后将该对象整个传回去
                if(Imageuri != null)
                    endDate.setImageUri(Imageuri);

                intent.putExtra("endDate",endDate);


                setResult(RESULT_OK,intent);
                newBuiltActivity.this.finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); //就调用返回键的操作。
            }
        });

        addPicture.setOnClickListener(new View.OnClickListener() { //添加详细页面中的默认图片的响应事件
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 4); //从相册获取图片
            }
        });

        registerForContextMenu(cyclebutton);

        cyclebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cyclebutton.showContextMenu();
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Period");
        menu.add(0,1,0,"Week");

        menu.add(0,2,0,"Month");

        menu.add(0,3,0,"Year");

        menu.add(0,4,0,"Custom");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Imageuri = data.getData();
                //imageView.setImageURI(uri);

            }
        }
    }

    private class ChooseDate implements View.OnClickListener {
        @Override
        public void onClick(View v) {
                showTime(); //点击事件，先显示时钟，因为其会被日历给遮住。达成先选日期再选时间的效果
                showDailog();

        }
    }

    private void showDailog() {
         datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {//根据当前时间设置日历
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                endDate.setYear(year);
                endDate.setMonth(monthOfYear+1);
                endDate.setMonthOfDay(dayOfMonth);

                String month,day;
                if(monthOfYear<8)
                    month = "0"+String.valueOf(monthOfYear+1);
                else
                    month = String.valueOf(monthOfYear + 1);
                if(dayOfMonth<10)
                    day = "0"+String.valueOf(dayOfMonth);
                else
                    day = String.valueOf(dayOfMonth);

                //monthOfYear 得到的月份会减1所以我们要加1
                date = String.valueOf(year)  + month + day;
                //date = String.valueOf(calendar.get(Calendar.YEAR))+String.valueOf(calendar.get(Calendar.MONTH))+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                Log.d("测试", date); // 获得点击OK后的所获得的时间
                Toast.makeText(view.getContext(),date,Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
        //自动弹出键盘问题解决
        datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void showTime() {
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() { //响应时间，后面的参数是系统时间
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                String min;
                String hour;
                String second;
                if(hourOfDay<10){
                     hour = "0"+String.valueOf(hourOfDay); //这里是为了匹配主Activity中计算倒计时的格式化字符串,所以不足10要补个“0”
                }
                else
                    hour = String.valueOf(hourOfDay);
                if(minute<10){
                     min = "0"+String.valueOf(minute);
                }
                else
                    min = String.valueOf(minute);
                if(Calendar.SECOND < 10){
                    second = "0"+String.valueOf(Calendar.SECOND);
                }
                else
                    second = String.valueOf(Calendar.SECOND);

                time =hour+min+second;
                              endDate.setHour(hourOfDay);
                endDate.setMin(minute);
                endDate.setS(Calendar.SECOND);

                Log.d("测试", Integer.toString(hourOfDay)); //点击OK后获得时间具体数值
                Log.d("测试", Integer.toString(minute));
                Toast.makeText(view.getContext(),time,Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true); //设置时间滚动器

        timePickerDialog.show();
        timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}
