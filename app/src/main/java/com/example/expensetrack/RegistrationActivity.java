package com.example.expensetrack;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    RadioButton radioButton1,radioButton2;
    Button button;
    EditText Fname,Lname,Username,Password;
    SQLiteDatabase db;
    int flag_isadmin;

    DatabaseHelper login = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Fname=findViewById(R.id.fname);
        Lname=findViewById(R.id.lname);
        Username=findViewById(R.id.userid);
        Password=findViewById(R.id.pass);
        button=findViewById(R.id.submit_regis);
        radioButton1=findViewById(R.id.admin_rb);
        radioButton2=findViewById(R.id.emp_rb);
        db=openOrCreateDatabase("Expense_Database", Context.MODE_PRIVATE,null);



        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("radio1", "onClick: ");
                Toast.makeText(RegistrationActivity.this, "Admin Selected", Toast.LENGTH_SHORT).show();
                flag_isadmin=1;
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onclick2", "onClick: ");
                Toast.makeText(RegistrationActivity.this, "Employee Selected", Toast.LENGTH_SHORT).show();
                flag_isadmin=0;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Fname.getText().toString().trim().length()==0||Lname.getText().toString().trim().length()==0||Username.getText().toString().trim().length()==0||Password.getText().toString().trim().length()==0||(!radioButton1.isChecked()&&!radioButton2.isChecked())){
                    Toast.makeText(RegistrationActivity.this,"Please Fill up all the fields.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("hh123", "onClick: ");
                    int res = login.add_login(Fname.getText().toString(), Lname.getText().toString(), Username.getText().toString(), Password.getText().toString(), flag_isadmin);
                    if (res == 1) {
                        Log.d("hh2", "onClick: ");
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        Log.d("hh3", "onClick: ");
                        startActivity(intent);
                    } else {
                        showMessage("Username EXISTS!!!", "Please use a different Username");
                        Lname.setText("");
                        Username.setText("");
                        Password.setText("");
                        Fname.setText("");
                        //username exists : dialog box
                    }
                }
            }
        });
    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    //    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.admin_rb:
//                if (checked)
//                    flag_isadmin=1;
//                    break;
//            case R.id.emp_rb:
//                if (checked)
//                    flag_isadmin=0;
//                    break;
//        }
//    }
    }

