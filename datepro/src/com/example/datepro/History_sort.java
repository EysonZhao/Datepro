package com.example.datepro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class History_sort extends Activity{
	private final int item1=Menu.FIRST;
	private final int item2=Menu.FIRST+1;
	private final int item3=Menu.FIRST+2;
	private final int item4=Menu.FIRST+3;
	private final int item5=Menu.FIRST+4;
	private final int item6=Menu.FIRST+5;
	
	private final int sub1=Menu.FIRST+6;
	private final int sub2=Menu.FIRST+7;
	private final int sub3=Menu.FIRST+8;
	private final int sub4=Menu.FIRST+9;
	
	Time t=new Time();
	
	
	
	DateproDB db;
	ListView listView;
	Cursor cursor;
	int primary_key[];
	
	int begindate=0;
	int enddate=20999999;
	
	int NowDate;
	int NowTime;
	
	String s1,s2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sortdisplay);
		
		t.setToNow();
		NowDate=getNowDate();
		NowTime=getNowTime();
		
		Toast.makeText(History_sort.this, "向左滑动表项删除\n向右滑动表项修改\n", Toast.LENGTH_SHORT).show();
		
		//获取当前主题
				SharedPreferences sp=getSharedPreferences("user_ifo", 0);
				int back=sp.getInt("background", 0);
				LinearLayout layout=(LinearLayout) findViewById(R.id.sort_list_layout);
				switch(back){
				case 0:break;//默认设置，无需设置
				case 1:layout.setBackgroundResource(R.drawable.blue4);break;
				case 2:layout.setBackgroundResource(R.drawable.pink4);break;
				case 3:layout.setBackgroundResource(R.drawable.yellow4);break;
				default:break;
				}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
				
		//使用数据库数据初始化表单
		db=new DateproDB(this);
		listView=(ListView) findViewById(R.id.sortlist);
		cursor=db.select();
		cursor=db.getReadableDatabase()
				.rawQuery("select * from dict", null);
		//声明一个list类去适应当前listview
		List<Map<String, Object>>listItems=new ArrayList<Map<String, Object>>();
		
		//循环为新的容器添加内容，通过容器去适应listview
		int count=0;
		primary_key=new int[100];
		cursor.moveToFirst();
		for(int i=0;i<cursor.getCount();i++){
			Map<String, Object>item=new HashMap<String, Object>();
			primary_key[count]=cursor.getInt(0);
			item.put("item_time", cursor.getString(2));
			item.put("item_context", cursor.getString(3));
			switch (cursor.getInt(1)) {
			case 1://重要度为1，不更改
				item.put("item_importance", R.drawable.b1);
				break;
			case 2:
				item.put("item_importance", R.drawable.b2);
				break;
			case 3:
				item.put("item_importance", R.drawable.b3);
				break;

			default:
				item.put("item_importance", R.drawable.b1);
				break;
			}
			
			listItems.add(item);//循环加入新的数据
			cursor.moveToNext();
			count++;
		}

		
		 SimpleAdapter sa=new SimpleAdapter(this,listItems,R.layout.image_items,
					new String[]{"item_importance","item_time","item_context"},new int[]{
					R.id.image_items_list_importace,R.id.image_item_time,
					R.id.image_item_context});
			
			listView.setAdapter(sa);

			
			touch();
		
	}//onresume
	//定义刷新屏幕
	
	public void touch(){
		//*****************为表项添加滑屏事件监听
		ListView li=(ListView) findViewById(R.id.sortlist);
		
		 li.setOnTouchListener(new OnTouchListener() {
				float oldX = 0;
               float oldY = 0;//这个一定要声明称这个监听器的全局变量
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
			
					 switch (event.getAction()) {
	                 case MotionEvent.ACTION_DOWN:
	                         //保存按下时的X,Y坐标

	                         oldX = event.getX();
	                         oldY = event.getY();
	                         break;
	                 case MotionEvent.ACTION_UP:

	                         //获取按起时的X,Y坐标
	                	 float newX = event.getX();
	                	 float newY = event.getY();
	                 //通过ListView中的pointToPosition方法获取点击ListView中的位置position
	                	 final int oldPosition = ((ListView)v).pointToPosition((int)oldX, (int)oldY);

	                	 int newPosition = ((ListView)v).pointToPosition((int)newX, (int)newY);
//********************向左滑屏删除事件
	            if(oldPosition!=-1){//############    	 
	            if(oldX - newX > 80  && oldPosition == newPosition) {
	                                 //获取ListView中点击是的View
	                		 final int temp=primary_key[oldPosition];
	                		 ///mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
	                		 AlertDialog.Builder dialog=new AlertDialog.Builder(History_sort.this)
	                		 .setTitle("确认删除")
	                		 .setMessage("删除后无法恢复记录，是否删除？")
	                		 .setPositiveButton("确认", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
			                		db.delete(temp);
			                		refresh();//刷新视图
			                		dialog.dismiss();
			                		Toast.makeText(History_sort.this, "删除成功！", 5000).show();
			                		
								}
							})
							.setNegativeButton("取消", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
	                		 dialog.create().show();
	                	 }//if
//**********************向右滑屏修改记录
	            else  if(newX - oldX > 80  && oldPosition == newPosition){
	            	final int temp=primary_key[oldPosition];
	            	
	            	 AlertDialog.Builder dialog=new AlertDialog.Builder(History_sort.this)
           		 .setTitle("提示")
           		 .setMessage("修改该条记录？")
           		 .setPositiveButton("确认", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							
							for(cursor.moveToFirst();cursor.getInt(0)!=temp;cursor.moveToNext()){						
							}
							
							int send_id=cursor.getInt(0);
			            	int send_importance=cursor.getInt(1);
			            	String send_time=cursor.getString(2);
			            	String send_context=cursor.getString(3); 
			            	
			            	Intent i=new Intent(History_sort.this,new_content.class);
							
							Bundle bundle=new Bundle();
							
							//传递得到的数据库数据
							bundle.putInt("send_id", send_id);
							bundle.putInt("send_imp", send_importance);
							bundle.putString("send_time", send_time);
							bundle.putString("send_context", send_context);
							bundle.putBoolean("modifying", true);
							i.putExtras(bundle);
							
							startActivity(i);
							dialog.dismiss();	                		
	                		
						}
					})
					.setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
           		 dialog.create().show();
	            	
	            }//if
	            
	            
	            
//***********************其余情况相当于点击表项进入	   
	            else if(oldPosition == newPosition) {
	            	final int temp=primary_key[oldPosition];
	            	for(cursor.moveToFirst();cursor.getInt(0)!=temp;cursor.moveToNext()){						
					}
	            int id=cursor.getInt(0);	
   				int imp=cursor.getInt(1);
   				String time=cursor.getString(2);
   				String con=cursor.getString(3);
   				
   				Intent io=new Intent(History_sort.this,Event_show.class);
   				
   				Bundle bundle=new Bundle();
   				
   				//传递得到的数据库数据
   				bundle.putInt("send_id", id);
   				bundle.putInt("send_imp", imp);
   				bundle.putString("send_time", time);
   				bundle.putString("send_con", con);
   				io.putExtras(bundle);
   				
   				startActivity(io);
	           }
	            }//oldposition!=0
	            
	                	 break;
	                	 
	                 default:	       	             				
	             			
	             		break;
	                 
					 }//switch
					return true;
				}
			});
	}
	
	public void refresh(){
		Cursor cl;
		
		cl=db.getReadableDatabase().rawQuery("select * from dict", null);
		
		cl.moveToFirst();
		List<Map<String, Object>>listItems2=new ArrayList<Map<String, Object>>();
		int count=0;
		for(int i=0;i<cl.getCount();i++){
			Map<String, Object>item=new HashMap<String, Object>();
			primary_key[count]=cl.getInt(0);
			item.put("item_time", cl.getString(2));
			item.put("item_context", cl.getString(3));
			switch (cl.getInt(1)) {
			case 1://重要度为1，不更改
				item.put("item_importance", R.drawable.b1);
				break;
			case 2:
				item.put("item_importance", R.drawable.b2);
				break;
			case 3:
				item.put("item_importance", R.drawable.b3);
				break;

			default:
				item.put("item_importance", R.drawable.b1);
				break;
			}
			
			listItems2.add(item);//循环加入新的数据
			cl.moveToNext();
			count++;
		}
		
		SimpleAdapter sa=new SimpleAdapter(this,listItems2,R.layout.image_items,
				new String[]{"item_importance","item_time","item_context"},new int[]{
				R.id.image_items_list_importace,R.id.image_item_time,
				R.id.image_item_context});
		
		listView.setAdapter(sa);
		touch();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0,item6,0,"新建");
		menu.add(1,item1,0,"屏蔽过期事件");
		menu.add(2,item2,0,"按重要度排序");
		menu.add(0,item3,0,"按照时间排序");
		menu.add(0,item4,0,"查看全部备忘"); 
		menu.add(0,item5,0,"选择起止时间");
		
		
		SubMenu time_event =menu.addSubMenu("查看固定时间内待办事项");
		time_event.setHeaderTitle("选择查看的事件");
		time_event.add(0, sub1, 0, "查看3天内事件");
		time_event.add(0, sub2, 0, "查看一周内事件");
		time_event.add(0, sub3, 0, "查看一个月内事件");
		time_event.add(0, sub4, 0, "查看过去事件");
		
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case item1://####################屏蔽过期事件######################
				passed_delete();
				break;
		case item2://#######################按照重要度排序
				importance_sort();
				break;
		case item3://################ 时间排序 ##################	
				sortTime();
				break;
		case item4://#########################刷新即可
				refresh();
				break;
		case item5://###############自定义查看时间段（仅能查看有效记事）		
				time_find();		
				break;
		case item6:
				Bundle bundle=new Bundle();
				Intent i1=new Intent(History_sort.this,new_time.class);
				bundle.putBoolean("modifying", false);
				i1.putExtras(bundle);
				startActivity(i1);
				break;
		case sub1://#######3天
				threeday();
				break;	
		case sub2://######1周
				week();
				break;
		case sub3://######1月
				amonth();
				break;
		case sub4://######过期
				inhistory();
				break;
			
		}
		return true;
	}
	
	
	
	
	//**********获取系统日期时间
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
	//时间转换函数
	public int SplitStirngDate(String s){
		
		String m1[]= s.split("年"); 
		String m2[]=m1[1].split("月");
		String m3[]=m2[1].split("日    ");
		String m4[]=m3[1].split(" : ");
		int a[]=new int[5];
		a[0]=Integer.parseInt(m1[0]);//年
		a[1]=Integer.parseInt(m2[0]);//月
		a[2]=Integer.parseInt(m3[0]);//日
		a[3]=Integer.parseInt(m4[0]);//时
		a[4]=Integer.parseInt(m4[1]);//分

		int b=a[0]*10000+a[1]*100+a[2];
		
		return b;
	}
	public int SplitStirngTime(String s){
		
		String m1[]= s.split("年"); 
		String m2[]=m1[1].split("月");
		String m3[]=m2[1].split("日    ");
		String m4[]=m3[1].split(" : ");
		int a[]=new int[5];
		a[0]=Integer.parseInt(m1[0]);//年
		a[1]=Integer.parseInt(m2[0]);//月
		a[2]=Integer.parseInt(m3[0]);//日
		a[3]=Integer.parseInt(m4[0]);//时
		a[4]=Integer.parseInt(m4[1]);//分

		int b=a[3]*100+a[4];
		
		return b;
	}
//************************排序函数
	public int findmin(int a[][]){
		int m=0,min=20991231;
		for(int i=0;i<a.length;i++){
			if(a[i][0]<min){
				min=a[i][0];				
				m=i;
			}
		}
		a[m][0]=20991231;
		return a[m][1];
	}//end sortArray
//***********************按时间排序
	public void sortTime(){
		Cursor cl11;
		
		cl11=db.getReadableDatabase().rawQuery("select * from dict", null);
		
		cl11.moveToFirst();
		List<Map<String, Object>>listItems11=new ArrayList<Map<String, Object>>();
		int n=cl11.getCount();
		//新建一个二维数组保存时间和主键
		int a[][]=new int[n][2];
		for(int i=0;i<n;i++){
			int Gdate=SplitStirngDate(cl11.getString(2));
			a[i][0]=Gdate;
			a[i][1]=cl11.getInt(0);
			cl11.moveToNext();
			
		}//end for
		cl11.close();
		Cursor c;
		
		int count=0;
		
		for(int m=0;m<a.length;m++){
			int t;
			t=findmin(a);

			
			c=db.getReadableDatabase().rawQuery("select * from dict where _id=?", new String[] {t+""});
			c.moveToFirst();
			Map<String, Object>item2=new HashMap<String, Object>();
			primary_key[count]=c.getInt(0);
			item2.put("item_time", c.getString(2));
			item2.put("item_context", c.getString(3));
			switch (c.getInt(1)) {
			case 1://重要度为1，不更改
				item2.put("item_importance", R.drawable.b1);
				break;
			case 2:
				item2.put("item_importance", R.drawable.b2);
				break;
			case 3:
				item2.put("item_importance", R.drawable.b3);
				break;

			default:
				item2.put("item_importance", R.drawable.b1);
				break;
			}
			
			listItems11.add(item2);
			count++;
			
		}
		
		SimpleAdapter sc=new SimpleAdapter(this,listItems11,R.layout.image_items,
				new String[]{"item_importance","item_time","item_context"},new int[]{
				R.id.image_items_list_importace,R.id.image_item_time,
				R.id.image_item_context});
		
		listView.setAdapter(sc);
		touch();
		
	}
	//顺便写一个通用的对LIST排序的函数
	private void sortlist(List<Map<String, Object>> list){
		
		int n=list.size();
		int a[][]=new int[n][2];
		for(int i=0;i<n;i++){
			String t1=list.get(i).get("item_time").toString();
			Cursor ci;
			ci=db.getReadableDatabase()
					.rawQuery("select * from dict where time=?", new String[] {t1});
			ci.moveToFirst();
			int t2=ci.getInt(0);
			a[i][0]=SplitStirngDate(t1);
			a[i][1]=t2;						
		}
		
		list.clear();
		int count=0;
		
		for(int m=0;m<a.length;m++){
			int t;
			t=findmin(a);
			Cursor c;
			
			c=db.getReadableDatabase().rawQuery("select * from dict where _id=?", new String[] {t+""});
			c.moveToFirst();
			
			primary_key[count]=c.getInt(0);
			Map<String, Object>item2=new HashMap<String, Object>();
			item2.put("item_time", c.getString(2));
			item2.put("item_context", c.getString(3));
			switch (c.getInt(1)) {
			case 1://重要度为1，不更改
				item2.put("item_importance", R.drawable.b1);
				break;
			case 2:
				item2.put("item_importance", R.drawable.b2);
				break;
			case 3:
				item2.put("item_importance", R.drawable.b3);
				break;

			default:
				item2.put("item_importance", R.drawable.b1);
				break;
			}
			c.moveToNext();
			
			list.add(item2);
			count++;
			
		}		
		
	}
	
//***********************重要度排序
	public void importance_sort(){
		List<Map<String, Object>>listItems=new ArrayList<Map<String, Object>>();
		Cursor cu=db.getReadableDatabase()
				.rawQuery("select * from dict", null);
		cu.moveToFirst();
		int count=0;
		for(int j=3;j>0;j--){
			for(int i=0;i<cu.getCount();i++){					
				if(cu.getInt(1)==j){
					Map<String, Object>item1=new HashMap<String, Object>();
					primary_key[count++]=cu.getInt(0);
					item1.put("item_time", cu.getString(2));
					item1.put("item_context", cu.getString(3));
					switch (j) {
				case 1://重要度为1，不更改
				item1.put("item_importance", R.drawable.b1);
				break;
				case 2:
				item1.put("item_importance", R.drawable.b2);
				break;
				case 3:
				item1.put("item_importance", R.drawable.b3);
				break;

				default:
				item1.put("item_importance", R.drawable.b1);
				break;
				}
					listItems.add(item1);//循环加入新的数据
				}//if							
			cu.moveToNext();
		}
		cu.moveToFirst();
		}//end j for
		
		 SimpleAdapter sa1=new SimpleAdapter(this,listItems,R.layout.image_items,
					new String[]{"item_importance","item_time","item_context"},new int[]{
					R.id.image_items_list_importace,R.id.image_item_time,
					R.id.image_item_context});
			
			listView.setAdapter(sa1);
			touch();
		
	}
//*******************自定义时间段查看
	public void time_find(){
		LayoutInflater factory = LayoutInflater.from(this);

		final View textEntryView = factory.inflate(R.layout.riqiguolv, null);


		AlertDialog.Builder b=new AlertDialog.Builder(History_sort.this);
		b.setTitle("自定义查看时间段");
		b.setView(textEntryView);
				
		DatePicker begin=(DatePicker) textEntryView.findViewById(R.id.datepicker2);
		DatePicker end=(DatePicker) textEntryView.findViewById(R.id.datepicker3);						
		
		t.setToNow();
		int y=t.year;
		int m=t.month;
		int d=t.monthDay;
		s1=y+"-"+(m+1)+"-"+d;
		s2=y+"-"+(m+1)+"-"+d;
		begindate=y*10000+m*100+d;
		enddate=y*10000+m*100+d;
		
		begin.init(y, m, d, new OnDateChangedListener(){

			@Override
			public void onDateChanged(DatePicker arg0, int year, int month,
					int day) {
				// TODO Auto-generated method stub
				s1=year+"-"+(month+1)+"-"+day;
				begindate=year*10000+(month+1)*100+day;
			}
			
		});
		end.init(y, m, d, new OnDateChangedListener(){

			@Override
			public void onDateChanged(DatePicker arg0, int year, int month,
					int day) {
				// TODO Auto-generated method stub
				s2=year+"-"+(month+1)+"-"+day;
				enddate=year*10000+(month+1)*100+day;
			}
			
		});
		b.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(begindate>enddate)
				{
					Toast.makeText(History_sort.this, "输入错误！开始时间应小于结束时间！", 8000).show();
				}
				else{Toast.makeText(History_sort.this, s1+" —— "+s2, 8000).show();
				Cursor cl111;
				
				cl111=db.getReadableDatabase().rawQuery("select * from dict", null);
				
				int count=0;
				cl111.moveToFirst();
				List<Map<String, Object>>listItems111=new ArrayList<Map<String, Object>>();
				
				for(int i=0;i<cl111.getCount();i++){
					int Gdate=SplitStirngDate(cl111.getString(2));
					int Gtime=SplitStirngTime(cl111.getString(2));
					
					if((Gdate>=begindate)&&(Gdate<=enddate)){
					
					
					Map<String, Object>item2=new HashMap<String, Object>();
					primary_key[count++]=cl111.getInt(0);
					item2.put("item_time", cl111.getString(2));
					item2.put("item_context", cl111.getString(3));
					switch (cl111.getInt(1)) {
					case 1://重要度为1，不更改
						item2.put("item_importance", R.drawable.b1);
						break;
					case 2:
						item2.put("item_importance", R.drawable.b2);
						break;
					case 3:
						item2.put("item_importance", R.drawable.b3);
						break;

					default:
						item2.put("item_importance", R.drawable.b1);
						break;
					}
					
					listItems111.add(item2);}//循环加入新的数据
					cl111.moveToNext();
				}//end for
				
				SimpleAdapter sc1=new SimpleAdapter(History_sort.this,listItems111,R.layout.image_items,
						new String[]{"item_importance","item_time","item_context"},new int[]{
						R.id.image_items_list_importace,R.id.image_item_time,
						R.id.image_item_context});
				
				listView.setAdapter(sc1);
				
				}
				dialog.dismiss();
				
			}
		});
		b.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
			}
		});
		b.create().show();
		touch();
		

		
	}//time_find
//屏蔽过期事件
	public void passed_delete(){
		Cursor cl;
		
		cl=db.getReadableDatabase().rawQuery("select * from dict", null);
		
		cl.moveToFirst();
		List<Map<String, Object>>listItems2=new ArrayList<Map<String, Object>>();
		
		for(int i=0;i<cl.getCount();i++){
			int Gdate=SplitStirngDate(cl.getString(2));
			int Gtime=SplitStirngTime(cl.getString(2));
			if(Gdate>NowDate||(Gdate==NowDate&&Gtime>NowTime)){
			
			
			Map<String, Object>item2=new HashMap<String, Object>();
			item2.put("item_time", cl.getString(2));
			item2.put("item_context", cl.getString(3));
			switch (cl.getInt(1)) {
			case 1://重要度为1，不更改
				item2.put("item_importance", R.drawable.b1);
				break;
			case 2:
				item2.put("item_importance", R.drawable.b2);
				break;
			case 3:
				item2.put("item_importance", R.drawable.b3);
				break;

			default:
				item2.put("item_importance", R.drawable.b1);
				break;
			}
			
			listItems2.add(item2);}//循环加入新的数据
			cl.moveToNext();
		}//end for
		
		sortlist(listItems2);
		
		SimpleAdapter sa=new SimpleAdapter(this,listItems2,R.layout.image_items,
				new String[]{"item_importance","item_time","item_context"},new int[]{
				R.id.image_items_list_importace,R.id.image_item_time,
				R.id.image_item_context});
		
		listView.setAdapter(sa);
		touch();
	}
//1周
	public void week(){
		Cursor cl11;
		
		cl11=db.getReadableDatabase().rawQuery("select * from dict", null);
		
		cl11.moveToFirst();
		List<Map<String, Object>>listItems11=new ArrayList<Map<String, Object>>();
		
		for(int i=0;i<cl11.getCount();i++){
			int Gdate=SplitStirngDate(cl11.getString(2));
			int Gtime=SplitStirngTime(cl11.getString(2));
			
			if((Gdate>NowDate&&Gdate-NowDate<=6)||(Gdate==NowDate&&Gtime>NowTime)){
			
			
			Map<String, Object>item2=new HashMap<String, Object>();
			item2.put("item_time", cl11.getString(2));
			item2.put("item_context", cl11.getString(3));
			switch (cl11.getInt(1)) {
			case 1://重要度为1，不更改
				item2.put("item_importance", R.drawable.b1);
				break;
			case 2:
				item2.put("item_importance", R.drawable.b2);
				break;
			case 3:
				item2.put("item_importance", R.drawable.b3);
				break;

			default:
				item2.put("item_importance", R.drawable.b1);
				break;
			}
			
			listItems11.add(item2);}//循环加入新的数据
			cl11.moveToNext();
		}//end for

		sortlist(listItems11);
		
		SimpleAdapter sc=new SimpleAdapter(this,listItems11,R.layout.image_items,
				new String[]{"item_importance","item_time","item_context"},new int[]{
				R.id.image_items_list_importace,R.id.image_item_time,
				R.id.image_item_context});
		
		listView.setAdapter(sc);
		touch();
		if(listItems11.size()==0){Toast.makeText(History_sort.this, "您一周内没有日程", 5000).show();}
		if(listItems11.size()>=5){Toast.makeText(History_sort.this, "您将过忙碌的一周", 5000).show();}
	}
//3天
	public void threeday(){
		Cursor cl11;
		
		cl11=db.getReadableDatabase().rawQuery("select * from dict", null);
		
		cl11.moveToFirst();
		List<Map<String, Object>>listItems11=new ArrayList<Map<String, Object>>();
		
		for(int i=0;i<cl11.getCount();i++){
			int Gdate=SplitStirngDate(cl11.getString(2));
			int Gtime=SplitStirngTime(cl11.getString(2));
			
			if((Gdate>NowDate&&Gdate-NowDate<=2)||(Gdate==NowDate&&Gtime>NowTime)){
			
			
			Map<String, Object>item2=new HashMap<String, Object>();
			item2.put("item_time", cl11.getString(2));
			item2.put("item_context", cl11.getString(3));
			switch (cl11.getInt(1)) {
			case 1://重要度为1，不更改
				item2.put("item_importance", R.drawable.b1);
				break;
			case 2:
				item2.put("item_importance", R.drawable.b2);
				break;
			case 3:
				item2.put("item_importance", R.drawable.b3);
				break;

			default:
				item2.put("item_importance", R.drawable.b1);
				break;
			}
			
			listItems11.add(item2);}//循环加入新的数据
			cl11.moveToNext();
		}//end for

		sortlist(listItems11);
		
		SimpleAdapter sc=new SimpleAdapter(this,listItems11,R.layout.image_items,
				new String[]{"item_importance","item_time","item_context"},new int[]{
				R.id.image_items_list_importace,R.id.image_item_time,
				R.id.image_item_context});
		
		listView.setAdapter(sc);
		touch();
		if(listItems11.size()==0){Toast.makeText(History_sort.this, "您3天内无事件", 5000).show();}
		if(listItems11.size()>=4){Toast.makeText(History_sort.this, "您这三天有较多事件", 5000).show();}
	}
//1月
	public void amonth(){
			Cursor cl11;
			
			cl11=db.getReadableDatabase().rawQuery("select * from dict", null);
			
			cl11.moveToFirst();
			List<Map<String, Object>>listItems11=new ArrayList<Map<String, Object>>();
			
			for(int i=0;i<cl11.getCount();i++){
				int Gdate=SplitStirngDate(cl11.getString(2));
				int Gtime=SplitStirngTime(cl11.getString(2));
				
				if(Gdate/100==NowDate/100){
				
				
				Map<String, Object>item2=new HashMap<String, Object>();
				item2.put("item_time", cl11.getString(2));
				item2.put("item_context", cl11.getString(3));
				switch (cl11.getInt(1)) {
				case 1://重要度为1，不更改
					item2.put("item_importance", R.drawable.b1);
					break;
				case 2:
					item2.put("item_importance", R.drawable.b2);
					break;
				case 3:
					item2.put("item_importance", R.drawable.b3);
					break;

				default:
					item2.put("item_importance", R.drawable.b1);
					break;
				}
				
				listItems11.add(item2);}//循环加入新的数据
				cl11.moveToNext();
			}//end for
			
			sortlist(listItems11);
			
			SimpleAdapter sc=new SimpleAdapter(this,listItems11,R.layout.image_items,
					new String[]{"item_importance","item_time","item_context"},new int[]{
					R.id.image_items_list_importace,R.id.image_item_time,
					R.id.image_item_context});
			
			listView.setAdapter(sc);
			touch();
	}
//过期
	public void inhistory(){
		Cursor cl11;
		
		cl11=db.getReadableDatabase().rawQuery("select * from dict", null);
		
		cl11.moveToFirst();
		List<Map<String, Object>>listItems11=new ArrayList<Map<String, Object>>();
		
		for(int i=0;i<cl11.getCount();i++){
			int Gdate=SplitStirngDate(cl11.getString(2));
			int Gtime=SplitStirngTime(cl11.getString(2));
			
			if(Gdate<NowDate){
			
			
			Map<String, Object>item2=new HashMap<String, Object>();
			item2.put("item_time", cl11.getString(2));
			item2.put("item_context", cl11.getString(3));
			switch (cl11.getInt(1)) {
			case 1://重要度为1，不更改
				item2.put("item_importance", R.drawable.b1);
				break;
			case 2:
				item2.put("item_importance", R.drawable.b2);
				break;
			case 3:
				item2.put("item_importance", R.drawable.b3);
				break;

			default:
				item2.put("item_importance", R.drawable.b1);
				break;
			}
			
			listItems11.add(item2);}//循环加入新的数据
			cl11.moveToNext();
		}//end for
		
		sortlist(listItems11);
		
		SimpleAdapter sc=new SimpleAdapter(this,listItems11,R.layout.image_items,
				new String[]{"item_importance","item_time","item_context"},new int[]{
				R.id.image_items_list_importace,R.id.image_item_time,
				R.id.image_item_context});
		
		listView.setAdapter(sc);
		touch();
	}
}
