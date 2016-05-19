package com.example.datepro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Event_show extends Activity{
	EditText et1;
	EditText et2;
	ImageView iv;
	String get_time;
	String get_con;
	int get_id;
	int get_imp;
	int imp_pic;
	
	DateproDB db;
	//Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
		//获得当前主题
		
		SharedPreferences sp=getSharedPreferences("user_ifo", 0);
		int back=sp.getInt("background", 0);
		RelativeLayout layout=(RelativeLayout) findViewById(R.id.show_layout);
		switch(back){
		case 0:break;//默认设置，无需设置
		case 1:layout.setBackgroundResource(R.drawable.blue2);break;
		case 2:layout.setBackgroundResource(R.drawable.pink2);break;
		case 3:layout.setBackgroundResource(R.drawable.yellow2);break;
		default:break;
		}
		//获得editview中数据
		get_time=this.getIntent().getExtras().getString("send_time");
		get_con=this.getIntent().getExtras().getString("send_con");
		//获得id和importance
		get_id=this.getIntent().getExtras().getInt("send_id");
		get_imp=this.getIntent().getExtras().getInt("send_imp");
		
		et1=(EditText) findViewById(R.id.show_Time_show);
		et2=(EditText) findViewById(R.id.show_Text_show);
		iv=(ImageView) findViewById(R.id.show_Importance);
		
		//获取当前字体大小
		int text_size=sp.getInt("textsize", 0);
		switch(text_size){
		case 0:break;//默认设置，无需设置
		case 1:et1.setTextSize(16);et2.setTextSize(16);break;
		case 2:et1.setTextSize(19);et2.setTextSize(19);break;
		case 3:et1.setTextSize(22);et2.setTextSize(22);break;
		default:break;
		
		}
		
		db=new DateproDB(this);
		
		imp_pic=R.drawable.b3;
		
		et1.setText(get_time);
		et2.setText(get_con);
				
				
		switch (get_imp) {
		case 1:
			imp_pic=R.drawable.b1;
			break;
		case 2:
			imp_pic=R.drawable.b2;
			break;
		case 3:
			imp_pic=R.drawable.b3;
			break;

		default:
			break;
		}
		iv.setImageResource(imp_pic);
		
	}
	
	//为点击文本绑定监听对象
	public void show_content_click(View v){
		Intent i=new Intent(Event_show.this,new_content.class);
		//传递主键信息
		Bundle bundle=new Bundle();
		bundle.putInt("send_id", get_id);
		bundle.putString("send_time", get_time);
		bundle.putString("send_context", get_con);
		
		bundle.putBoolean("modifying", true);
		i.putExtras(bundle);
		
		startActivity(i);
		finish();
	}
	
	
	
	
	
	//为删除按钮绑定监听事件
	public void deletebutton(View v){
		
		//直接关联对话框
		AlertDialog.Builder builder=new AlertDialog.Builder(this)
		.setMessage("删除后将无法回复该备忘记录，确认删除？")
		.setTitle("警告")
		.setIcon(R.drawable.l1)
		.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				db.delete(get_id);
				Toast.makeText(Event_show.this, "删除成功！", 3000).show();
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
	//为修改按钮绑定监听
	public void notifybutton(View v){
		Intent i=new Intent(Event_show.this,new_time.class);
		//传递主键信息
		Bundle bundle=new Bundle();
		bundle.putInt("send_id", get_id);
		bundle.putString("send_time", get_time);
		bundle.putString("send_context", get_con);
		bundle.putBoolean("modifying", true);
		i.putExtras(bundle);
		
		startActivity(i);
		finish();
	}
	//为返回按钮绑定监听事件
	public void returnbutton(View v){
		finish();
	}

}
