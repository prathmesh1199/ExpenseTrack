package com.example.expensetrack;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username_text,password_text ;
    Button button_login ;
    String password_edit,username_edit ;
    TextView textView;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.new_user);
        username_text = findViewById(R.id.login_name) ;
        password_text = findViewById(R.id.login_password) ;
        button_login = findViewById(R.id.login_button) ;
        username_text.setText("");
        password_text.setText("");

        db=openOrCreateDatabase("Expense_Database", Context.MODE_PRIVATE,null);
//        db.execSQL("drop table Users;");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("onclick1", "onClick: ");

                Intent intent=new Intent(MainActivity.this, RegistrationActivity.class);

                Log.d("onclick2", "onClick: ");
                startActivity(intent);
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password_edit = password_text.getText().toString().trim();
                username_edit = username_text.getText().toString().trim();

                int check = databaseHelper.validate_user_login(username_edit,password_edit);



                if(check == 0){
                    username_text.setText("");
                    password_text.setText("");
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();

                }

                else if(check == 1){
                    Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                    intent.putExtra("username" , username_edit);
                    startActivity(intent);
                }

                else{
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();
                        //close();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }
}
