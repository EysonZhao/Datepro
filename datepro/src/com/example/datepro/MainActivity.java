package com.example.datepro;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.format.Time;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGestureListener{

	private final int item1=Menu.FIRST;
	private final int item2=Menu.FIRST+1;
	private final int item3=Menu.FIRST+2;
	private final int item4=Menu.FIRST+3;
	
	DateproDB db;
	Cursor cursor;
	Cursor cur;
	GestureDetector detector;
	Time t=new Time();
	EditText rt1;
	EditText rt2;
	EditText rt3;
	ImageView im;
	String s1;
	String s2;
	int i1;
	int i0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
	}//oncreate	
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		db=new DateproDB(this);
		cursor=db.select();
		detector = new GestureDetector(MainActivity.this, MainActivity.this);
		//��ȡ��ǰ����
		SharedPreferences sp=getSharedPreferences("user_ifo", 0);
		int back=sp.getInt("background", 0);
		RelativeLayout layout=(RelativeLayout) findViewById(R.id.main_layout);
		switch(back){
		case 0:break;//Ĭ�����ã���������
		case 1:layout.setBackgroundResource(R.drawable.blue3);break;
		case 2:layout.setBackgroundResource(R.drawable.pink3);break;
		case 3:layout.setBackgroundResource(R.drawable.yellow3);break;
		default:break;
		}

		//���3��edittext
		rt1=(EditText) findViewById(R.id.main_time_show);
		rt2=(EditText) findViewById(R.id.main_content_show);
		rt3=(EditText) findViewById(R.id.now_hint);
		im=(ImageView) findViewById(R.id.main_importance_show);
		//��ȡ��ǰ�����С
		int text_size=sp.getInt("textsize", 0);
		switch(text_size){
		case 0:break;//Ĭ�����ã���������
		case 1:rt1.setTextSize(16);rt2.setTextSize(16);break;
		case 2:rt1.setTextSize(19);rt2.setTextSize(19);break;
		case 3:rt1.setTextSize(22);rt2.setTextSize(22);break;
		default:break;
		}
		
		//�趨��ǰʱ��
		t.setToNow();
		int NowDate=getNowDate();
		int NowTime=getNowTime();
		
		//�������¼�����ʾ
		
		int i=getRecent(NowDate,NowTime);
		
		if(i==0){//���ݿ�Ϊ�գ���Ҫ��ʼ������
			i1=0;
		}
		else{
		cursor=db.getReadableDatabase().rawQuery("select _id,importance,time,context from dict where _id=?",new String[]{i+""} );
		cursor.moveToFirst();
		cur=db.getReadableDatabase().rawQuery("select * from dict",null);		
		cur.moveToFirst();
		while(cur.getInt(0)!=cursor.getInt(0)){cur.moveToNext();}
		
		s1=cursor.getString(2);
		s2=cursor.getString(3);
		i1=cursor.getInt(1);
		i0=cursor.getInt(0);
		
		main_show(s1, s2, i1);
		//�趨��ʾ����
	}
	}//onResume
	
	private void main_show(String s1,String s2,int i){
		
			rt1.setText(s1);
			rt2.setText(s2);
			
			
			switch(i1){
			case 1:im.setImageResource(R.drawable.b1);break;
			case 2:im.setImageResource(R.drawable.b2);break;
			case 3:im.setImageResource(R.drawable.b3);break;
			default:im.setImageResource(R.drawable.b1);break;
			
		}
	}
	//***********************�ı���ļ����¼�
	public void main_text_click(View v){
		if(i1==0){//û���κμ�¼
			AlertDialog.Builder a=new AlertDialog.Builder(MainActivity.this)
			.setMessage("����½������¼��¡�")
			.setIcon(R.drawable.l1)
			.setPositiveButton("��֪����", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
				}
				
			});
			a.create().show();
		}
		else{
		Intent i=new Intent(MainActivity.this,Event_show.class);
		
		Bundle bundle=new Bundle();
		
		//���ݵõ������ݿ�����
		bundle.putInt("send_id", cur.getInt(0));
		bundle.putInt("send_imp",cur.getInt(1));
		bundle.putString("send_time", cur.getString(2));
		bundle.putString("send_con", cur.getString(3));
		i.putExtras(bundle);
		
		startActivity(i);
		}
	}


	//��ʾ��ҳ�ϵ����������
	private int getRecent(int Ndate,int Ntime){
		int Gdate;
		int Gtime;
		int Rdate=20991231;
		int Rtime=2400;
		int subtime=2400;
		int i=0;
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			String time=cursor.getString(2);
			Gdate=SplitStirngDate(time);
			Gtime=SplitStirngTime(time);
			
			if(Gdate>Ndate&&Gdate<Rdate){
				Rdate=Gdate;				
				i=cursor.getInt(0);
			}
			else if((Gdate==Rdate)&&(Gtime<Rtime)){
				Rtime=Gtime;
				i=cursor.getInt(0);
			}
			if(Gdate==Ndate&&Gtime-Ntime>0&&Gtime-Ntime<subtime){
				subtime=Gtime-Ntime;
				Rdate=Gdate;
				Rtime=Gtime;
				i=cursor.getInt(0);
			}
		}//end for
		int subdate=Rdate-Ndate;
		switch (subdate) {
		case 0:
			rt3.setText("�������д������");
			break;
		case 1:
			rt3.setText("�������д������");
			break;
		case 2:
			rt3.setText("�������д������");
			break;
		default:
			rt3.setText("��������û�д������");
			break;
		}
		return i;
		
	}
	//**********��ȡϵͳ����ʱ��
	public int getNowDate(){

		int date;
		date=t.year*10000+(t.month+1)*100+t.monthDay;
		return date;
		
	}
	public int getNowTime(){

		int time;
		time=t.hour*100+t.minute;
		return time;
		
	}
	//**********
	//ʱ��ת������
	public int SplitStirngDate(String s){
		
		String m1[]= s.split("��"); 
		String m2[]=m1[1].split("��");
		String m3[]=m2[1].split("��    ");
		String m4[]=m3[1].split(" : ");
		int a[]=new int[5];
		a[0]=Integer.parseInt(m1[0]);//��
		a[1]=Integer.parseInt(m2[0]);//��
		a[2]=Integer.parseInt(m3[0]);//��
		a[3]=Integer.parseInt(m4[0]);//ʱ
		a[4]=Integer.parseInt(m4[1]);//��

		int b=a[0]*10000+a[1]*100+a[2];
		
		return b;
	}
	public int SplitStirngTime(String s){
		
		String m1[]= s.split("��"); 
		String m2[]=m1[1].split("��");
		String m3[]=m2[1].split("��    ");
		String m4[]=m3[1].split(" : ");
		int a[]=new int[5];
		a[0]=Integer.parseInt(m1[0]);//��
		a[1]=Integer.parseInt(m2[0]);//��
		a[2]=Integer.parseInt(m3[0]);//��
		a[3]=Integer.parseInt(m4[0]);//ʱ
		a[4]=Integer.parseInt(m4[1]);//��

		int b=a[3]*100+a[4];
		
		return b;
	}

//*******************������Ӧ����*******************************//
//�½���������	
	public void new_button(View source){
		Bundle bundle=new Bundle();
		Intent i1=new Intent(MainActivity.this,new_time.class);
		bundle.putBoolean("modifying", false);
		i1.putExtras(bundle);
		startActivity(i1);
	}
//�鿴������Ŀ(�������)
	public void event_recent_show(View source) {
		Intent i1=new Intent(MainActivity.this,Event_display.class);
		startActivity(i1);
	}
	
//��ʷ����
	public void event_sort_show(View v){
		Intent i1=new Intent(MainActivity.this,History_sort.class);
		startActivity(i1);
	}
//ѡ��˵�
	public void option_click(View v){
		Intent i1=new Intent(MainActivity.this,Options.class);
		startActivity(i1);
	}
	
//�����˳��¼���Ӧ	
	public void exit_button(View source){
		showExitDialog();	
	}

	public void showExitDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this)
		.setMessage("ȷ���˳���")
		.setTitle("��ʾ")
		.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
				System.exit(0);
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
	
/*************************���������¼�********************************/
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(event);
	}
	



	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	



	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		if((e2.getX()-e1.getX()>80)){
			cur.moveToPrevious();
			if(!cur.isBeforeFirst()){//���ǵ�һ������
				
				String ss1=cur.getString(2);
				String ss2=cur.getString(3);
				int ii1=cur.getInt(1);
				main_show(ss1, ss2, ii1);
				
			}
			else{
				Toast.makeText(MainActivity.this, "���ǵ�һ������", 5000).show();
			}

		}
		else if((e1.getX()-e2.getX()>80)){
			// Toast.makeText(MainActivity.this, "����", 5000).show();			
			cur.moveToNext();
			if(!cur.isAfterLast()){//�������һ������
				
				String ss1=cur.getString(2);
				String ss2=cur.getString(3);
				int ii1=cur.getInt(1);
				main_show(ss1, ss2, ii1);
				
			}
			else{
				Toast.makeText(MainActivity.this, "�������һ������", 5000).show();
			}
		}
		
		/*if((e1.getY()-e2.getY()>80)&&(Math.abs(e1.getX()-e2.getX())<50)){
			Toast.makeText(MainActivity.this, "���ϻ�", 5000).show();
		}else if((e2.getY()-e1.getY()>80)&&(Math.abs(e1.getX()-e2.getX())<50)){
			Toast.makeText(MainActivity.this, "���»�", 5000).show();
		}*/
		
		return true;
	}
	
	
	//���õ��������½���
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Bundle bundle=new Bundle();
		Intent i1=new Intent(MainActivity.this,new_time.class);
		bundle.putBoolean("modifying", false);
		i1.putExtras(bundle);
		startActivity(i1);
		
	}



	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub	
	}



	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}


//***************************�˵�����Ӧ*******************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,item1,0,"�½�");
		menu.add(0,item2,0,"�鿴");
		menu.add(0,item3,0,"ѡ��");
		menu.add(0,item4,0,"�˳�");    
		  return true;  
		//return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) { 
		  case Menu.FIRST: 
			  Bundle bundle=new Bundle();
				Intent i1=new Intent(MainActivity.this,new_time.class);
				bundle.putBoolean("modifying", false);
				i1.putExtras(bundle);
				startActivity(i1);
		   
		   break; 
		  case Menu.FIRST+1: 
			  Intent i2=new Intent(MainActivity.this,History_sort.class);
			startActivity(i2);
		   break; 
		  case Menu.FIRST+2: 
			Intent i3=new Intent(MainActivity.this,Options.class);
			startActivity(i3);
		   break; 
		  case Menu.FIRST+3: 
		   finish();
		   break; 

		  } 
		return true;
		//return super.onOptionsItemSelected(item);
	}

}


