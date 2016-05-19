package com.example.datepro;

import java.util.Random;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class new_content extends Activity {
//声明一个数据库实例
	DateproDB db;
	RadioGroup rg;
	EditText et;
	EditText et2;
	private boolean modifying;
	
	int get_importance=1;
	String get_time="2014年1月1日";
	int get_id;
	int get_imp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		modifying=this.getIntent().getExtras().getBoolean("modifying");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtips);	
		
		//获取主题
		SharedPreferences sp=getSharedPreferences("user_ifo", 0);
		int back=sp.getInt("background", 0);
		RelativeLayout layout=(RelativeLayout) findViewById(R.id.newtips_layout);
		switch(back){
		case 0:break;//默认设置，无需设置
		case 1:layout.setBackgroundResource(R.drawable.blue3);break;
		case 2:layout.setBackgroundResource(R.drawable.pink3);break;
		case 3:layout.setBackgroundResource(R.drawable.yellow3);break;
		default:break;
		}
		
		rg=(RadioGroup) findViewById(R.id.importance);
		et=(EditText) findViewById(R.id.showimportance);
		et2=(EditText) findViewById(R.id.showtime);
		EditText et3=(EditText) findViewById(R.id.newcontent);
		
		//获取传递的时间
		get_time=this.getIntent().getExtras().getString("send_time");
		et2.setText(get_time);
		//初始化数据库open
		db=new DateproDB(this);
		if(modifying==true){
			et3.setText(this.getIntent().getExtras().getString("send_context"));
		}
		rg.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.i1:get_importance=1;et.setText("正常提醒。");break;
				case R.id.i2:get_importance=2;et.setText("日常记事~");break;
				case R.id.i3:get_importance=3;et.setText("必须要办！");break;
				}
				
			}
		});
		
		
	
	}//oncreate

	public void new_done_click(View source){

		//+++++++++++++++++++++新建状态
		if(modifying==false){
		//插入数据
			String ss=new String();
			EditText content = (EditText) findViewById(R.id.newcontent);
			ss=content.getText().toString();
			
			if(ss.length()!=0){
				
				db.insert(get_importance, get_time, ss);
		
				Toast.makeText(new_content.this, "新建完成。", 8000).show();
				finish();
			}
			else{
				AlertDialog.Builder builder=new AlertDialog.Builder(this)
				.setMessage("请输入记录内容！")
				.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub					
					arg0.dismiss();
				}
				});
				builder.create().show();
			}

		}
		//+++++++++++++++++++++修改状态
		else{
			String ss=new String();
			EditText content = (EditText) findViewById(R.id.newcontent);
			ss=content.getText().toString();			
			get_id=this.getIntent().getExtras().getInt("send_id");
			//输入为空提示
			if(ss.length()==0){
				AlertDialog.Builder builder=new AlertDialog.Builder(this)
				.setMessage("请输入记录内容！")
				.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub					
						arg0.dismiss();
					}
				});
				builder.create().show();
			}
			else{
				
			db.update(get_id, get_importance, get_time, ss);
			
			Toast.makeText(new_content.this, "记录更改完毕！", 8000).show();
			finish();
			}
		}
		
	}
	//取消编辑键按下
	public void new_cancel_click(View source){
		Builder builder=new AlertDialog.Builder(this)
		.setTitle("确认放弃")
		.setMessage("放弃当前编辑内容？")
		.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
				finish();
				
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	
	}
	
}



