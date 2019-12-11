package com.example.expensetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class IntroActivity extends AppCompatActivity {

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList entries,entryDesc;
    String username;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        pieChart = findViewById(R.id.pieChart);

        Intent intent = getIntent();

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            username= null;
        } else {
            username= extras.getString("username");
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IntroActivity.this,ExpenseTrack.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

        plotGraph();
    }

    private void plotGraph() {
        entries = new ArrayList();
        entryDesc = new ArrayList();

        entries.add(new PieEntry(550f,"Food"));
        entries.add(new PieEntry(400f,"Travel"));
        entries.add(new PieEntry(200f,"Medical"));
        entries.add(new PieEntry(150f,"Others"));

        pieDataSet = new PieDataSet(entries,"Expenses");

        entryDesc.add("FOOD");
        entryDesc.add("TRAVEL");
        entryDesc.add("MEDICAL");
        entryDesc.add("OTHERS");

        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15f);
        pieDataSet.setSliceSpace(5f);
        pieChart.animateXY(1000,1000);
    }

    //To show Alert dialog when back button is pressed.
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch(id){
            case R.id.logout:
                Intent intent=new Intent(IntroActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.Help:
                Toast.makeText(IntroActivity.this, "Help", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void on_approved_click(View view) {

        // Intent submitter = new Intent(IntroActivity.this, AcceptedActivity.class);
        //startActivity(submitter);

        databaseHelper = new DatabaseHelper(this);

        System.out.println(username);

        Log.d("onapproved", "on_approved_click: ");

        Cursor c1 = databaseHelper.gethead(username);
        Cursor c2 = databaseHelper.getcategory(username);
        Log.d("onapproved2", "on_approved_click: ");
        Cursor c3 = databaseHelper.getinfo(username);
        Log.d("onapproved3", "on_approved_click: ");
        Cursor c4 = databaseHelper.getdetails(username);

        Log.d("onapproved4", "on_approved_click: ");

        StringBuffer stringBuffer = new StringBuffer();




        System.out.println("c1 : " + c1.getCount() + "\n");
        System.out.println("c2 : " + c2.getCount() + "\n");
        System.out.println("c3 : " + c3.getCount() + "\n");
        System.out.println("c4 : " + c4.getCount() + "\n");

        if(c1.getCount() == 0 || c2.getCount() == 0 || c3.getCount() == 0 || c4.getCount() == 0){

            System.out.println("c1 : " + c1.getCount() + "\n");
            System.out.println("c2 : " + c2.getCount() + "\n");
            System.out.println("c3 : " + c3.getCount() + "\n");
            System.out.println("c4 : " + c4.getCount() + "\n");


            showmessage("Error","Nothing found");
            return;
        }



        while(c1.moveToNext() && c2.moveToNext() && c3.moveToNext() && c4.moveToNext()){
            stringBuffer.append("id : " + c1.getString(0) + "\n");
            stringBuffer.append(("head_name : " + c1.getString(1) + "\n"));
            stringBuffer.append("category_name : " + c2.getString(3) + "\n");
            stringBuffer.append("bill no : " + c3.getString(3) + "\n");
            stringBuffer.append("biller name : " + c3.getString(4) + "\n");
            stringBuffer.append("date : " + c4.getString(2) + "\n");
            stringBuffer.append("time : " + c4.getString(3) + "\n\n");


        }
        showmessage("Data",stringBuffer.toString());
    }

    public void showmessage(String title , String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
