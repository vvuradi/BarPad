package com.example.barpad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {
	public String userNameGlobal;
	public static final String USER_DETAILS_TABLE_NAME="USER_DETAILS";
	public static final String EMAIL = "EMAIL";
	public static final String USER_NAME = "USER_NAME";
	public static final String PWD = "PWD";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String MIDDLE_NAME = "MIDDLE_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String TELEPHONE = "TELEPHONE";
	public static final String USER_ID_USER_DETAILS = "USER_ID";
	

	public static final String CLICKER_TABLE_NAME="CLICKER_DETAILS";
	public static final String TOTAL_COUNT = "TOTAL_COUNT";
	public static final String USER_ID_COUNTER = "USER_ID";
	public static final String DATE = "DATE";
	
	public static final String DB_NAME="BARPAD_DB.db";
	public static final int DB_VERSION = 5;
	public static final String CLICKER_CREATE = " CREATE TABLE "+ CLICKER_TABLE_NAME +"( " +
			" TOTAL_COUNT REAL NOT NULL, USER_ID LONG NOT NULL, DATE DATETIME NOT NULL,PRIMARY KEY(USER_ID,DATE));";
	public static final String USER_DETAILS_CREATE = " CREATE TABLE "+ USER_DETAILS_TABLE_NAME+"( " +
			" FIRST_NAME TEXT NOT NULL, MIDDLE_NAME TEXT, LAST_NAME TEXT NOT NULL, EMAIL TEXT, USER_NAME TEXT UNIQUE NOT NULL, PWD TEXT NOT NULL, USER_ID LONG PRIMARY KEY,TELEPHONE LONG);";
	
	DataBaseHelper dbHelper;
	Context ctx;
	SQLiteDatabase db;
	public DataHandler(Context ctx){
		this.ctx = ctx;
		dbHelper = new DataBaseHelper(ctx);
	}
	
    private static class DataBaseHelper extends SQLiteOpenHelper{

    	public DataBaseHelper(Context ctx){
    		super(ctx,DB_NAME,null,DB_VERSION);
    		System.out.println("Database Created");
    	}
		@Override
		public void onCreate(SQLiteDatabase db) {
			System.out.println("On Create method called");
			// TODO Auto-generated method1 stub
			try{
				System.out.println("query to create table : "+CLICKER_CREATE);
			db.execSQL(CLICKER_CREATE);
			System.out.println("CLICKER_CREATE Table Created");
				System.out.println("query to create table : "+USER_DETAILS_CREATE);
			db.execSQL(USER_DETAILS_CREATE);
			System.out.println("Table Created");
			}catch(SQLException e){
				System.out.println("Exception occured in table creation : //////////////////");
				e.printStackTrace();
			}
		}	

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
			//db.execSQL("DROP TABLE IF EXISTS"+CLICKER_CREATE);
			//db.execSQL("DROP TABLE IF EXISTS "+USER_DETAILS_CREATE);
			onCreate(db);
		}
    	
    }
    public DataHandler open(){
    	System.out.println("In Database open() ");
		db= dbHelper.getWritableDatabase();
		/*if(db != null){
			System.out.println("DB is not null");
		}*/
		return this;
	}
    
    public void close(){
    	dbHelper.close();
    }
    
    public long getMaxID(){
    	SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
    	if(sqlDB != null){
    		String maxQuery = "SELECT MAX(USER_ID) FROM " +USER_DETAILS_TABLE_NAME;
    		Cursor c = sqlDB.rawQuery(maxQuery, null);
    		if(null != c){
    			c.moveToFirst();
    		}
    		Long maxNumber = Long.valueOf(c.getLong(0));
    		System.out.println("User ID max from db : "+maxNumber);
    		if(maxNumber == null){
    			maxNumber = 0L;
    		}
    		return maxNumber;
    	}
    	return -1;
    }
    
    
    public long insertDataUserDetails(String email, String userName, String password, String firstName, String middleName, String lastName, String telephone){
    	ContentValues content = new ContentValues();
    	content.put(EMAIL, email);
    	content.put(PWD,password);
    	content.put(USER_NAME, userName);
    	content.put(FIRST_NAME, firstName);
    	content.put(MIDDLE_NAME, middleName);
    	content.put(LAST_NAME, lastName);
    	content.put(TELEPHONE, telephone);
    	Long userIdLong = getMaxID();
    	
    	//userNameGlobal=userName;
    	
    	System.out.println("USerID after querying : "+userIdLong);
    	userIdLong++;
    	content.put(USER_ID_USER_DETAILS, String.valueOf(userIdLong)); 
    	if(db!=null){
    		long result = db.insert(USER_DETAILS_TABLE_NAME, null, content);
    		
    		return result;
    	}else{
    		return -1;
    	}
    	
    }
    
    
    public long getUserID(String userName){
    	System.out.println("Entered getUSerId");
    	System.out.println("username sent : "+userName);
    	if(userName == null || userName.length() == 0){
    		return -1;
    	}else{
    		Long userID = -1L;
    		SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
        		String userIDQuery = "SELECT USER_ID FROM " +USER_DETAILS_TABLE_NAME+" WHERE USER_NAME = '"+userName+"';";
        		System.out.println("Query to get userid : "+userIDQuery);
        		Cursor c = sqlDB1.rawQuery(userIDQuery, null);
        		if(null != c){
        			c.moveToFirst();
        		}
        		Long maxNumber = Long.valueOf(c.getLong(0));
        		System.out.println("User ID returned  from db : "+maxNumber+"for username : "+userNameGlobal);
        		if(maxNumber == null){
        			maxNumber = 0L;
        		}
    		return userID;
    	}
    }
    
    
    public long insertClickerDetails(long count, String userName, String date){
    	ContentValues content = new ContentValues();
    	System.out.println("Global username : "+userName);
    	content.put(TOTAL_COUNT, count);
    	content.put("USER_ID", String.valueOf(userName));
    	content.put(DATE, date);
    	if(db!=null){
    		return db.insert(CLICKER_TABLE_NAME, null, content);
    	}else{
    		return -1;
    	}
    	
    }
    
    
    public Cursor fetchUserDetails(){
    	String[] cols = {EMAIL,PWD,"FIRST_NAME","LAST_NAME","USER_NAME","USER_ID"};
    	return db.query(USER_DETAILS_TABLE_NAME, cols, null, null, null, null, null);
    }
    
    public Cursor fetchClickerDetails(){
    	String[] cols = {"COUNT","DATE","USER_ID"};
    	return db.query(CLICKER_TABLE_NAME, cols, null, null, null, null, null);
    }
    
}
