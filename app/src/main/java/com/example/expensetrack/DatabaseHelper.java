package com.example.expensetrack;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

import static android.content.ContentValues.TAG;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String login_name = "login_details";
    private static final String head_name = "head_expense";
    private static final String category_name = "expense_category";
    private static final String info_name = "expense_info";
    private static final String details_name = "expense_details";
    private static final String DB_NAME = "expense_tracker";
    private static final String details_user = "user_details";

    String sqlhead = "CREATE TABLE IF NOT EXISTS head_expense(id int , head_name VARCHAR , description VARCHAR , isActive varchar(2) , username VARCHAR);";
    String sqlcategory = "CREATE TABLE IF NOT EXISTS expense_category(username VARCHAR , id int , head_name VARCHAR , category_name VARCHAR , description VARCHAR);";
    String sqlinfo = "CREATE TABLE IF NOT EXISTS expense_info(username VARCHAR , emp_id int , id int , bill_no varchar , biler_name varchar , address varchar , city varchar , amount long);";
    String sqldetails = "CREATE TABLE IF NOT EXISTS expense_details(username VARCHAR , id int , date varchar , time varchar , particular varchar , remarks varchar , status varchar , image blob);";
    String sqluser = "CREATE TABLE IF NOT EXISTS user_details(username varchar , id int);";
    String sqllogin = "CREATE TABLE IF NOT EXISTS login_details(name VARCHAR , surname VARCHAR , username varchar , password VARCHAR , isadmin int);";



    SQLiteDatabase db;

    private static final int db_version = 4;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, db_version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //db = sqLiteDatabase;

    /*
        sqLiteDatabase.execSQL(sqlhead);
        sqLiteDatabase.execSQL(sqllogin);
        Log.d("create1", "onCreate: ");
        sqLiteDatabase.execSQL(sqlcategory);
        Log.d("create2", "onCreate: ");
        sqLiteDatabase.execSQL(sqlinfo);
        sqLiteDatabase.execSQL(sqldetails);
        sqLiteDatabase.execSQL(sqluser);
    */
    }


    public int add_login(String name, String surname, String username, String password, int isadmin) {



        db = getWritableDatabase();
        db.execSQL(sqllogin);





        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("surname", surname);
        values.put("username", username);
        values.put("password", password);
        values.put("isadmin", isadmin);
        //this.db.insert("login_details", null, values);

        // Insert into login_details tables
        db.execSQL("Insert into login_details values('"+name+"','"+surname+"','"+username+"','"+password+"','"+isadmin+"');");


        return 1;
    }

    public int validate_user_login(String username_edit , String password_edit){

        db = getWritableDatabase();
        db.execSQL(sqllogin);

        db.execSQL(sqlhead);
        Log.d("create1", "onCreate: ");
        db.execSQL(sqlcategory);
        Log.d("create2", "onCreate: ");
        db.execSQL(sqlinfo);
        db.execSQL(sqldetails);
        db.execSQL(sqluser);


        // db.execSQL("delete from login_details");

        int check = 0;

        //check = 0 : To indicate either username or paswword is wrong or username is not yet registered.
        //check = 1 : To indicate username and password is correct and user is not an admin.
        //check = 2 : To indicate username and password is correct and user is an admin.

        Log.d("validate0", "validate_user_login: ");
        Cursor cursor=db.rawQuery("Select DISTINCT username from login_details where username= '" + username_edit + "'",null);
        Log.d("validate1", "validate_user_login: ");
        if (cursor.getCount()!=0) {
            Log.d("validate2", "validate_user_login: ");
            cursor = db.rawQuery("Select password,isadmin from login_details where username = '" + username_edit + "'", null);
            Log.d("validate3", "validate_user_login: ");
            if (cursor.moveToFirst()) {
                Log.d("validate4", "validate_user_login: ");
                // Validates the password
                if (password_edit.equals(cursor.getString(0))) {
                    Log.d("validate5", "validate_user_login: ");
                    // Checks if user is admin or not
                    if (cursor.getInt(1) == 0) {
                        // not an admin
                        check = 1;
                        Log.d("validate6", "validate_user_login: ");

                    } else if (cursor.getInt(1) == (1)) {
                        // admin
                        check = 2;
                        Log.d("validate7", "validate_user_login: ");

                    }
                }
            }
            else{Log.d("validate8", "validate_user_login: ");
                // Wrong Password.
                check = 0;
            }
        }
        else
        {
            // Username not registered.
            Log.d("validate9", "validate_user_login: ");
            check = 0;
        }


        return check;
        //return 1;
    }

    public int add_expense_head(String name_head, String  description, int isActive,String username) {

        db = getWritableDatabase();

        Log.d("head1", "add_expense_head: ");
        db.execSQL(sqlhead);

        // To count number of entries in expense_head table.
        //Used to assign id to a transaction.
        //It is assumed to be 1 + number of entries in the table.

        Log.d("here_33", "add_expense_head: ");

        //db.execSQL("DROP TABLE IF EXISTS head_expense");

        Log.d("here_444", "add_expense_head: ");


        String countQuery = "SELECT  * FROM " + head_name + " where username = '" + username + "'";
        SQLiteDatabase db1 = this.getReadableDatabase();
        Log.d("here_44", "add_expense_head: ");
        Cursor cursor = db1.rawQuery(countQuery, null);
        Log.d("here_55", "add_expense_head: ");
        int count = cursor.getCount();
        cursor.close();

        int id = count + 1;

        System.out.println(id);

        ContentValues contentValues = new ContentValues();

        contentValues.put("id",id);
        contentValues.put("head_name", name_head);
        contentValues.put("description", description);
        contentValues.put("isActive", isActive);
        contentValues.put("username" , username);


        System.out.println("head :- " + id + " " + name_head + " " + description + " " +isActive + " " + username  );

        db.insert("head_expense", null, contentValues);
        db.close();


        Log.d("print_here7", "here");

        return id;


        // db.execSQL("delete from head_expense");
        //Log.d("head2", "add_expense_head: ");
        //return 1;

    }

    public boolean add_expense_category(String username , int id, String head_name, String category_name, String description) {

        db = getWritableDatabase();
        db.execSQL(sqlcategory);


        ContentValues contentValues = new ContentValues();

        contentValues.put("username" , username);
        contentValues.put("id", id);
        contentValues.put("head_name", head_name);
        contentValues.put("category_name", category_name);
        contentValues.put("description", description);

        System.out.println("Category :- " + username + " " + id + " " + head_name + " " + category_name + " " + description );

        db.insert("expense_category", null, contentValues);
        db.close();

        Log.d("category_exp", "add_expense_category: ");


        //db.execSQL("delete from expense_category");
        return true;
    }

    public boolean add_expense_info(String username , int id, String bill_no, String biler_name, String address, String city, long amount) {

        db = getWritableDatabase();
        db.execSQL(sqlinfo);
        ContentValues contentValues = new ContentValues();

        contentValues.put("username" , username);
        contentValues.put("id", id);
        contentValues.put("bill_no", bill_no);
        contentValues.put("biler_name", biler_name);
        contentValues.put("address", address);
        contentValues.put("city", city);
        contentValues.put("amount", amount);

        Log.d("here_11", "add_expense_info: ");

        System.out.println("info :- " + username + " " + id + " " + bill_no + " " + biler_name + " " + address + " " + city + " " + amount);

        db.insert("expense_info", null, contentValues);

        Log.d("here_12", "add_expense_info: ");

        db.close();

        Log.d("here_13", "add_expense_info: ");


        //db.execSQL("delete from expense_info");
        return true;

    }

    public boolean display_data(){

        db = getWritableDatabase();

        List<String> expense_head,expense_category,billno,billername,address,city,date,time,particulars,remarks;
        List<String>amount;

        Cursor c1 = db.rawQuery("SELECT * FROM head_expense", null);
        Cursor c2 = db.rawQuery("SELECT * FROM expense_category", null);
        Cursor c3 = db.rawQuery("SELECT * FROM expense_info", null);
        Cursor c4 = db.rawQuery("SELECT * FROM expense_details", null);


        if (c1.getCount() == 0) {
            Log.d("here", "display_data: ");
        }
       /* StringBuffer buffer = new StringBuffer();
        while (c1.moveToNext() && c2.move) {
            buffer.append("Rollno: " + c.getString(0) + "\n");
            buffer.append("Name: " + c.getString(1) + "\n");
            buffer.append("Marks: " + c.getString(2) + "\n\n");
        }*/

        Log.d("display_data", "display_data: ");



        return true;
    }

    public boolean add_expense_details(String username , int id, String date, String time, String particular, String remarks, int status, byte[] image) {

        db = getWritableDatabase();
        db.execSQL(sqldetails);
        ContentValues contentValues = new ContentValues();

        contentValues.put("username" , username);
        contentValues.put("id", id);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("particular", particular);
        contentValues.put("remarks", remarks);
        contentValues.put("status", status);
        contentValues.put("image", image);

        System.out.println("details :- " + username + " " + id + " " + date + " " + time + " " + particular + " " + remarks + " " + status);

        db.insert("expense_details", null, contentValues);
        db.close();

        Log.d("added", "add_expense_details: ");



        //db.execSQL("delete from expense_details");
        return true;

    }

    public boolean add_user_details(String username, int id) {

        db = getWritableDatabase();

        db.execSQL(sqluser);
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("id", id);

        db.insert("user_details", null, contentValues);
        db.close();

        Log.d("success", "add_user_details: ");

        //db.execSQL("delete from user_details");
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.d("here_111", "onUpgrade: ");

        if (i1 >= i) {
            // db.execSQL("ALTER TABLE head_expense ADD COLUMN username VARCHAR DEFAULT 0");

            Log.d("here_11", "onUpgrade: ");

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + head_name);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + category_name);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + info_name);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + details_name);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + details_user);


            Log.d("here_22", "onUpgrade: ");
            //onCreate(sqLiteDatabase);


        }
    }

    public Cursor gethead(String username) {
        db = getWritableDatabase();
        Log.d("head1", "gethead: ");
        Cursor c = db.rawQuery("select * from " + head_name + " where username = '" + username + "'", null);
        Log.d("head2", "gethead: ");
        return c;

    }

    public Cursor getcategory(String username) {
        db = getWritableDatabase();
        Log.d("category1", "gethead: ");
        // String query = "select * from info_name where username = '" + username + "'";
        //Cursor c = db.rawQuery(query, null);

        // Cursor c = db.rawQuery("select * from " + category_name + " where username = '" + username + "'", null);
        Cursor c = db.rawQuery("select * from " + category_name + " where username = '" + username + "'", null);
        Log.d("category2", "getcategory: ");
        return c;
    }

    public Cursor getinfo(String username) {

/*        System.out.println("username : " + username);

        Log.d("getinfo1", "getinfo: ");
        db = getWritableDatabase();
        Log.d("getinfo2", "getinfo: ");
       // String query = "select * from expense_info where username = '" + username + "'";
        Log.d("getinfo3", "getinfo: ");
        //Cursor c = db.rawQuery(query, null);

            Cursor c = db.rawQuery("select * from expense_info where username = '" + username + "'", null);

            Log.d("getinfo4", "getinfo: ");
        return c;
*/

        db = getWritableDatabase();
        Log.d("head1", "gethead: ");
        Cursor c = db.rawQuery("select * from " + info_name + " where username = '" + username + "'", null);
        Log.d("head2", "gethead: ");
        return c;

    }

    public Cursor getdetails(String username) {

        db = getWritableDatabase();
        Log.d("getdetails1", "getdetails: ");
        // String query = "select * from expense_details where username = '" + username + "'";
        Log.d("getdeatils2", "getdetails: ");
        //Cursor c = db.rawQuery(query, null);

        Cursor c = db.rawQuery("select * from " + details_name + " where username = '" + username + "'", null);

        Log.d("getdetails3", "getinfo: ");
        return c;

    }

    public Bitmap getimage(String username , String id){
        db = getWritableDatabase();
        Bitmap bitmap = null;

        Cursor cursor = db.rawQuery("select image from where username = ' " + username + "' and id = '" + id + "'",null);
        if(cursor.moveToNext()){
            byte[] img = cursor.getBlob(0);
            bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
        }

        return bitmap;
    }

}

