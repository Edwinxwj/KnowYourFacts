package sg.edu.rp.c346.knowyourfacts;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Random;
import java.util.Calendar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList <Fragment> al;
    MyFragmentPagerAdapter adapter;
    ViewPager vPager;
    Button btnRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vPager = (ViewPager) findViewById(R.id.viewpager1);
        btnRead = (Button) findViewById(R.id.btnRead);

        FragmentManager fm = getSupportFragmentManager();

        al = new ArrayList <Fragment> ();
        al.add(new Frag1());
        al.add(new Frag2());
        al.add(new Frag3());

        adapter = new MyFragmentPagerAdapter(fm, al);
        vPager.setAdapter(adapter);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 20);

                Intent intent = new Intent(MainActivity.this,
                        ScheduleNotificationReceiver.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this, 123,
                        intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager)
                        getSystemService(Activity.ALARM_SERVICE);

                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        pendingIntent);

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();


            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_Next) {
            int max = vPager.getChildCount();
            if (vPager.getCurrentItem() < max - 1) {
                int nextPage = vPager.getCurrentItem() + 1;
                vPager.setCurrentItem(nextPage, true);

            }
        }
        else if(item.getItemId() == R.id.action_Random){
            Random random = new Random();
            int randomPage = random.nextInt(al.size());
            vPager.setCurrentItem(randomPage, true);

        } else{
            if (vPager.getCurrentItem() > 0) {
                int previousPage = vPager.getCurrentItem() - 1;
                vPager.setCurrentItem(previousPage, true);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("content", vPager.getCurrentItem());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("saved", MODE_PRIVATE);
        int content = preferences.getInt("content", 1);
        vPager.setCurrentItem(content, true);
    }





}
