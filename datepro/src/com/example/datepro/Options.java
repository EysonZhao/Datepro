package com.example.datepro;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Options extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		 ListView listView = (ListView) findViewById(R.id.optionlist);
	     listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData())); 
	      
	     listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				switch(position){
				case 0://设置字号
					final SharedPreferences sp2=getSharedPreferences("user_ifo",0);
					final Editor texteditor=sp2.edit();
					
					AlertDialog.Builder builder2 = new AlertDialog.Builder(Options.this);  		      
	                builder2.setTitle("选择字体大小");  
	                final ChoiceOnClickListener textchoice=new ChoiceOnClickListener();  
	                builder2.setSingleChoiceItems(R.array.word, sp2.getInt("textsize", 0),textchoice); 
	                builder2.setPositiveButton("确定", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							int choiceWhich = textchoice.getWhich();
							switch (choiceWhich) {
							case 0:
								texteditor.putInt("textsize", 0);//默认设置不操作
								break;
							case 1:
								texteditor.putInt("textsize", 1);
								break;
							case 2:
								texteditor.putInt("textsize", 2);
								break;
							case 3:
								texteditor.putInt("textsize", 3);
								break;
							default:
								texteditor.putInt("textsize", 0);
								break;
							}
							texteditor.commit();

							Toast.makeText(Options.this, "字体设置完成  ", 5000).show();
							
							arg0.dismiss();
						}
					});
	                
	                
	                builder2.create().show();
			texteditor.commit();
					break;
				case 1://设置背景主题
					final SharedPreferences sp=getSharedPreferences("user_ifo",0);
						final Editor editor=sp.edit();
						AlertDialog.Builder builder = new AlertDialog.Builder(Options.this);  		      
		                builder.setTitle("选择皮肤");  
		                final ChoiceOnClickListener choice=new ChoiceOnClickListener();  
		                builder.setSingleChoiceItems(R.array.hobby, sp.getInt("background", 0),choice); 
		                builder.setPositiveButton("确定", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								int choiceWhich = choice.getWhich();
								switch (choiceWhich) {
								case 0:
									editor.putInt("background", 0);//默认设置不操作
									break;
								case 1:
									editor.putInt("background", 1);
									break;
								case 2:
									editor.putInt("background", 2);
									break;
								case 3:
									editor.putInt("background", 3);
									break;
								default:
									editor.putInt("background", 0);
									break;
								}
								editor.commit();

								Toast.makeText(Options.this, "主题设置完成  ", 5000).show();
								
								arg0.dismiss();
							}
						});
		                
		                
		                builder.create().show();
				editor.commit();			
					
					break;
				case 2://设置提醒
					final SharedPreferences sp1=getSharedPreferences("user_ifo",0);
					final Editor editor1=sp1.edit();
					AlertDialog.Builder builder1 = new AlertDialog.Builder(Options.this);  		      
	                builder1.setTitle("选择提醒方式");  
	                final ChoiceOnClickListener choice1=new ChoiceOnClickListener();  
	                builder1.setSingleChoiceItems(R.array.remind_method, 
	                		sp1.getInt("remind", 0),choice1); 
	                builder1.setPositiveButton("确定", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							int choiceWhich = choice1.getWhich();
							switch (choiceWhich) {
							case 0:
								editor1.putInt("remind", 0);
								break;
							case 1:
								editor1.putInt("remind", 1);
								break;
							case 2:
								editor1.putInt("remind", 2);
								break;
							default:
								editor1.putInt("remind", 0);
								break;
							}
							editor1.commit();
							String s="提醒方式为：";
							switch(sp1.getInt("remind", 0)){
							case 0:Toast.makeText(Options.this, s+"铃声  ", 5000).show();
							break;
							case 1:Toast.makeText(Options.this, s+"震动  ", 5000).show();
							break;
							case 2:Toast.makeText(Options.this, s+"不提醒  ", 5000).show();
							break;
							}

							
							
							arg0.dismiss();
						}
					});
	                
	                
	                builder1.create().show();
					break;
				case 3://版本信息
					AlertDialog.Builder b=new AlertDialog.Builder(Options.this)
						.setMessage("版本： datepro 1.0\n\n发布时间：2014年5月15日\n\n" +
								"作者：赵延松 EyesonG\n联系我：QQ 737740369\n" +
								"导师：谷方明    许志闻\n\n版权所有©~~赵延松~~")
						.setTitle("关于datepro")
						.setPositiveButton("确定", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
								
							}
						});
				b.create().show();
					break;
				case 4://设置帮助
					AlertDialog.Builder b2=new AlertDialog.Builder(Options.this)
					.setMessage("Q1.如何新建备忘？\n" +
							"A：在主界面上点击【新建备忘】按钮，或者<长按屏幕>即可新建" +
							"备忘。在查看界面中点击菜单键，选择新建也可以新建备忘。\n\n" +
							"Q2.如何查看我的备忘事项？\n    " +
							"A：点击主界面【快速浏览】即可查看您的全部备忘事项，您也可以在【历史排序】" +
							"中浏览您的备忘。若您想浏览固定备忘事项，单击该事件即可。\n\n" +
							"Q3.如何修改和删除事项？\n" +
							"A：单击您想查看的事项，进入查看视图，单击下方【修改】按钮即可修改，单击" +
							"下方【X】按钮可删除，在历史排序查看视图下，在您想删除的记录上<向左滑动>" +
							"即可删除，<像右滑动>修改。\n\n" +
							"Q4.本软件支持那些手势？\n" +
							"A：主界面<长按><左右滑动>均是支持的，在查看界面上对表项<左右滑动>实现修改和" +
							"删除的效果\n\n" +
							"Q5.如何设置背景字号？\n" +
							"A：在主界面点击【设置】或者在菜单中点击【设置】进入设置界面，在设置菜单中您可以" +
							"找到相关设置选项。\n\n" +
							"Q6.本软件有何特色？\n" +
							"A：本软件支持用户个性化设置，同时支持备忘事项的分类别查看功能，这些功能帮助您" +
							"更好地了解最近的备忘事项。")
					.setTitle("使用帮助")
					.setPositiveButton("好的，我知道了", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
							
						}
					})
					;
					b2.create().show();
					break;
				case 5:
				AlertDialog.Builder b21=new AlertDialog.Builder(Options.this)
				.setMessage("您对这款软件有什么建议")
				.setView(new EditText(Options.this))
				.setTitle("关于软件")
				.setNegativeButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(Options.this, "您的意见已经提交。", 5000).show();
						arg0.dismiss();
						
					}
				})
				.setPositiveButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
						
					}
				})
				;
		b21.create().show();break;
				}//switch
				
			}
		});
	}
	 private class ChoiceOnClickListener implements DialogInterface.OnClickListener {  
		  
	        private int which = 0;  
	        @Override  
	        public void onClick(DialogInterface dialogInterface, int which) {  
	            this.which = which;  
	        }  
	          
	        public int getWhich() {  
	            return which;  
	        }  
	    }  
	private List<String> getData(){ 
        
        List<String> data = new ArrayList<String>(); 
        data.add("字号大小"); 
        data.add("背景主题"); 
        data.add("提醒设置"); 
        data.add("关于"); 
        data.add("帮助");
        data.add("反馈建议"); 
          
        return data; 
    } 

	
}
