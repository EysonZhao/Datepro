package com.example.datepro;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;


public class Event_display extends Activity{
	final private int item1=Menu.FIRST;
	
	DateproDB db;
	ListView listView;
	int number;
	Cursor cursor;
	private View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sortdisplay);
		//��ʼ�����ݿ����
		db=new DateproDB(this);
		listView=(ListView) findViewById(R.id.sortlist);
		cursor=db.select();

		Cursor cu=db.getReadableDatabase()
				.rawQuery("select _id,time , context from dict", null);
		inflateList(cu);
		
		
	}//----------------------------oncreate
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		//��ȡ��ǰ����
		SharedPreferences sp=getSharedPreferences("user_ifo", 0);
		int back=sp.getInt("background", 0);
		LinearLayout layout=(LinearLayout) findViewById(R.id.sort_list_layout);
		switch(back){
		case 0:break;//Ĭ�����ã���������
		case 1:layout.setBackgroundResource(R.drawable.blue4);break;
		case 2:layout.setBackgroundResource(R.drawable.pink4);break;
		case 3:layout.setBackgroundResource(R.drawable.yellow4);break;
		default:break;
		}
		
		db=new DateproDB(this);
		listView=(ListView) findViewById(R.id.sortlist);
		cursor=db.select();

		

        listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				float oldX = 0;
                float oldY = 0;
				 switch (event.getAction()) {
                 case MotionEvent.ACTION_DOWN:
                         //���水��ʱ��X,Y����

                         oldX = event.getX();
                         oldY = event.getY();
                         break;
                 case MotionEvent.ACTION_UP:

                         //��ȡ����ʱ��X,Y����
                 float newX = event.getX();
                 float newY = event.getY();
                 //ͨ��ListView�е�pointToPosition������ȡ���ListView�е�λ��position
                 final int oldPosition = ((ListView)v).pointToPosition((int)oldX, (int)oldY);

                 int newPosition = ((ListView)v).pointToPosition((int)newX, (int)newY);

                        
                 if( newX - oldX > 20  && oldPosition == newPosition) {
                                 //��ȡListView�е���ǵ�View
                Toast.makeText(Event_display.this, "�������ɹ�", 5000).show();
                System.out.println("!!!");
               //  view = ((ListView)v).getChildAt(oldPosition);
				
                  }
                 
				 }//switch
				return false;
			}
		});

		//onclick����
		//��onResume�����onclick��������Ϊ�˸��º���Բ鿴���º�ı���
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {				
				cursor.moveToPosition(position);
				int id=cursor.getInt(0)	;
				int imp=cursor.getInt(1);
				String time=cursor.getString(2);
				String con=cursor.getString(3);				
				Intent i=new Intent(Event_display.this,Event_show.class);				
				Bundle bundle=new Bundle();				
				//���ݵõ������ݿ�����
				bundle.putInt("send_id", id);
				bundle.putInt("send_imp", imp);
				bundle.putString("send_time", time);
				bundle.putString("send_con", con);
				i.putExtras(bundle);
				
				startActivity(i);					
			}
		});
		
		Cursor cu=db.getReadableDatabase()
				.rawQuery("select _id,time , context from dict", null);
		inflateList(cu);//���±���ͼ��
		
	}//---------------------------------onResume

	@SuppressLint("NewApi")
	private void inflateList(Cursor cursor){
		SimpleCursorAdapter adapter =new SimpleCursorAdapter
				(Event_display.this, 
			R.layout.items, 
			cursor, 
			new String[]{"time","context"},
			new int[]{R.id.time,R.id.context},
		CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		listView.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,item1,0,"�½�");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case item1://####################�½�######################
			Bundle bundle=new Bundle();
			Intent i1=new Intent(Event_display.this,new_time.class);
			bundle.putBoolean("modifying", false);
			i1.putExtras(bundle);
			startActivity(i1);
			break;
		default:	break;
		}
		return true;
	}

	
}
