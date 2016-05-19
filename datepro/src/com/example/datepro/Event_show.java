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
		//��õ�ǰ����
		
		SharedPreferences sp=getSharedPreferences("user_ifo", 0);
		int back=sp.getInt("background", 0);
		RelativeLayout layout=(RelativeLayout) findViewById(R.id.show_layout);
		switch(back){
		case 0:break;//Ĭ�����ã���������
		case 1:layout.setBackgroundResource(R.drawable.blue2);break;
		case 2:layout.setBackgroundResource(R.drawable.pink2);break;
		case 3:layout.setBackgroundResource(R.drawable.yellow2);break;
		default:break;
		}
		//���editview������
		get_time=this.getIntent().getExtras().getString("send_time");
		get_con=this.getIntent().getExtras().getString("send_con");
		//���id��importance
		get_id=this.getIntent().getExtras().getInt("send_id");
		get_imp=this.getIntent().getExtras().getInt("send_imp");
		
		et1=(EditText) findViewById(R.id.show_Time_show);
		et2=(EditText) findViewById(R.id.show_Text_show);
		iv=(ImageView) findViewById(R.id.show_Importance);
		
		//��ȡ��ǰ�����С
		int text_size=sp.getInt("textsize", 0);
		switch(text_size){
		case 0:break;//Ĭ�����ã���������
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
	
	//Ϊ����ı��󶨼�������
	public void show_content_click(View v){
		Intent i=new Intent(Event_show.this,new_content.class);
		//����������Ϣ
		Bundle bundle=new Bundle();
		bundle.putInt("send_id", get_id);
		bundle.putString("send_time", get_time);
		bundle.putString("send_context", get_con);
		
		bundle.putBoolean("modifying", true);
		i.putExtras(bundle);
		
		startActivity(i);
		finish();
	}
	
	
	
	
	
	//Ϊɾ����ť�󶨼����¼�
	public void deletebutton(View v){
		
		//ֱ�ӹ����Ի���
		AlertDialog.Builder builder=new AlertDialog.Builder(this)
		.setMessage("ɾ�����޷��ظ��ñ�����¼��ȷ��ɾ����")
		.setTitle("����")
		.setIcon(R.drawable.l1)
		.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				db.delete(get_id);
				Toast.makeText(Event_show.this, "ɾ���ɹ���", 3000).show();
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
	//Ϊ�޸İ�ť�󶨼���
	public void notifybutton(View v){
		Intent i=new Intent(Event_show.this,new_time.class);
		//����������Ϣ
		Bundle bundle=new Bundle();
		bundle.putInt("send_id", get_id);
		bundle.putString("send_time", get_time);
		bundle.putString("send_context", get_con);
		bundle.putBoolean("modifying", true);
		i.putExtras(bundle);
		
		startActivity(i);
		finish();
	}
	//Ϊ���ذ�ť�󶨼����¼�
	public void returnbutton(View v){
		finish();
	}

}
