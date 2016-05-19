package com.example.datepro;

import java.sql.Date;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;


public class new_time extends Activity{
	String send_time;
	String p1;
	String p2;
	private boolean modifying=false;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	
	EditText show;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer);
		
		show=(EditText) findViewById(R.id.show);
		
		modifying=this.getIntent().getExtras().getBoolean("modifying");
		
		SharedPreferences sp=getSharedPreferences("user_ifo", 0);
		int back=sp.getInt("background", 0);
		RelativeLayout layout=(RelativeLayout) findViewById(R.id.timer_layout);
		switch(back){
		case 0:break;//默认设置，无需设置
		case 1:layout.setBackgroundResource(R.drawable.blue3);break;
		case 2:layout.setBackgroundResource(R.drawable.pink3);break;
		case 3:layout.setBackgroundResource(R.drawable.yellow3);break;
		default:break;
		}
		
		DatePicker dp=(DatePicker) findViewById(R.id.datepicker);
		TimePicker tp=(TimePicker) findViewById(R.id.timepicker);
		Time c=new Time();
		c.setToNow();
		
		year=c.year;
		month=c.month+1;//安卓系统的时间存储是0~11.
		day=c.monthDay;
		hour=c.hour;
		minute=c.minute;
		
		p1=year+"年"+month+"月"+day+"日    ";
		p2=hour+" : "+minute;
				
		//初始化DatePicker组件，初始化指定监听器
		dp.init(year, month, day, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int month,
					int day) {
				// TODO Auto-generated method stub
				new_time.this.year=year;
				new_time.this.month=month;
				new_time.this.day=day;
				showdate(year, month, day, hour, minute);
				
				//构造传递数据对象前半部分
				p1=year+"年"+month+"月"+day+"日    ";
			}
		}
			
		);
		tp.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker arg0, int hourofday, int minute) {
				// TODO Auto-generated method stub
				new_time.this.hour=hourofday;
				new_time.this.minute=minute;
				showdate(year, month, day, hour, minute);
				
				p2=hour+" : "+minute;
				
			}
		});
		if(modifying==true){show.setText(this.getIntent().getExtras().getString("send_time"));}
		else{showdate(year,month,day,hour,minute);}
	}
	
	//定义显示函数
	private void showdate (int year,int month,int day,int hour,int minute) {
		show.setText(year+"年"+month+"月"+day+"日    "+hour+" : "+minute);
		
	}
	
	//************************ 为按钮绑定监听 *******************************
	public void timer_yes(View source){

			Intent i=new Intent(new_time.this,new_content.class);
			Bundle bundle=new Bundle();
			//传递时间数据
			send_time= show.getText().toString();
			if(modifying==true){
				int get_id=this.getIntent().getExtras().getInt("send_id");
				bundle.putInt("send_id", get_id);
			}
			bundle.putString("send_context", this.getIntent().getExtras().getString("send_context"));
			bundle.putString("send_time", send_time);
			bundle.putBoolean("modifying",modifying );
			i.putExtras(bundle);
			finish();
			startActivity(i);

	}
	
}
