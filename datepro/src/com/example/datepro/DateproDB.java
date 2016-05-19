package com.example.datepro; 

import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteDatabase.CursorFactory;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.widget.Toast;
  
public class DateproDB extends SQLiteOpenHelper {  

	private final static String DATABASE_NAME = "datepro.db";   
    public final static String TABLE_NAME = "dict";  
    public final static String IMPORTANCE="importance";
    public final static String CONTEXT="context" ;
    
	public DateproDB(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	public DateproDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
    @Override  
    public void onCreate(SQLiteDatabase db) {  
        // TODO Auto-generated method stub  
    	String sql = "CREATE TABLE dict (_id integer primary key autoincrement, importance integer,time text, context text);";
        db.execSQL(sql);  
    }  
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        // TODO Auto-generated method stub  
        String sql = "DROP TABLE IF EXISTS" + TABLE_NAME;  
        db.execSQL(sql);  
        onCreate(db);  
    }  
  
    public Cursor select() {  
        SQLiteDatabase db = this.getReadableDatabase();  
        Cursor cursor = db  
                .query(TABLE_NAME, null, null, null, null, null, null);  
        return cursor;  
    }  
  
    public long insert(int importance,String time,String context) {  
        SQLiteDatabase db = this.getWritableDatabase();  
        ContentValues cv = new ContentValues();  
        cv.put("importance", importance);  
        cv.put("context", context);
        cv.put("time", time);
        long row = db.insert(TABLE_NAME, null, cv);  
        return row;  
    }  
  
    public void delete(int id) {  
        SQLiteDatabase db = this.getWritableDatabase();  
        String where ="_id =?";  
        String[] whereValue = { Integer.toString(id) };  
        db.delete(TABLE_NAME, where, whereValue);  
    }  
  
        public void update(int id, int importance,String time, String context) {  
        SQLiteDatabase db = this.getWritableDatabase();  
        String where = "_id =?";  
        String[] whereValue = { Integer.toString(id) };  
  
        ContentValues cv = new ContentValues();  
        cv.put("importance",importance );  
        cv.put("time", time); 
        cv.put("context", context);
        db.update(TABLE_NAME, cv, where, whereValue);  
        
    }  

}  
