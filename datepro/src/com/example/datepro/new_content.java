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
//����һ�����ݿ�ʵ��
	DateproDB db;
	RadioGroup rg;
	EditText et;
	EditText et2;
	private boolean modifying;
	
	int get_importance=1;
	String get_time="2014��1��1��";
	int get_id;
	int get_imp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		modifying=this.getIntent().getExtras().getBoolean("modifying");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtips);	
		
		//��ȡ����
		SharedPreferences sp=getSharedPreferences("user_ifo", 0);
		int back=sp.getInt("background", 0);
		RelativeLayout layout=(RelativeLayout) findViewById(R.id.newtips_layout);
		switch(back){
		case 0:break;//Ĭ�����ã���������
		case 1:layout.setBackgroundResource(R.drawable.blue3);break;
		case 2:layout.setBackgroundResource(R.drawable.pink3);break;
		case 3:layout.setBackgroundResource(R.drawable.yellow3);break;
		default:break;
		}
		
		rg=(RadioGroup) findViewById(R.id.importance);
		et=(EditText) findViewById(R.id.showimportance);
		et2=(EditText) findViewById(R.id.showtime);
		EditText et3=(EditText) findViewById(R.id.newcontent);
		
		//��ȡ���ݵ�ʱ��
		get_time=this.getIntent().getExtras().getString("send_time");
		et2.setText(get_time);
		//��ʼ�����ݿ�open
		db=new DateproDB(this);
		if(modifying==true){
			et3.setText(this.getIntent().getExtras().getString("send_context"));
		}
		rg.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.i1:get_importance=1;et.setText("�������ѡ�");break;
				case R.id.i2:get_importance=2;et.setText("�ճ�����~");break;
				case R.id.i3:get_importance=3;et.setText("����Ҫ�죡");break;
				}
				
			}
		});
		
		
	
	}//oncreate

	public void new_done_click(View source){

		//+++++++++++++++++++++�½�״̬
		if(modifying==false){
		//��������
			String ss=new String();
			EditText content = (EditText) findViewById(R.id.newcontent);
			ss=content.getText().toString();
			
			if(ss.length()!=0){
				
				db.insert(get_importance, get_time, ss);
		
				Toast.makeText(new_content.this, "�½���ɡ�", 8000).show();
				finish();
			}
			else{
				AlertDialog.Builder builder=new AlertDialog.Builder(this)
				.setMessage("�������¼���ݣ�")
				.setPositiveButton("ȷ��", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub					
					arg0.dismiss();
				}
				});
				builder.create().show();
			}

		}
		//+++++++++++++++++++++�޸�״̬
		else{
			String ss=new String();
			EditText content = (EditText) findViewById(R.id.newcontent);
			ss=content.getText().toString();			
			get_id=this.getIntent().getExtras().getInt("send_id");
			//����Ϊ����ʾ
			if(ss.length()==0){
				AlertDialog.Builder builder=new AlertDialog.Builder(this)
				.setMessage("�������¼���ݣ�")
				.setPositiveButton("ȷ��", new OnClickListener() {
					
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
			
			Toast.makeText(new_content.this, "��¼������ϣ�", 8000).show();
			finish();
			}
		}
		
	}
	//ȡ���༭������
	public void new_cancel_click(View source){
		Builder builder=new AlertDialog.Builder(this)
		.setTitle("ȷ�Ϸ���")
		.setMessage("������ǰ�༭���ݣ�")
		.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
				finish();
				
			}
		})
		.setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	
	}
	
}



