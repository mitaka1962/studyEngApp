package com.example.study.studyingenglish;

import android.content.Intent;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.ButtonBarLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class RecordActivity extends AppCompatActivity {

    public static final String RECORD_TITLE_EXTRA_KEY = "title";
    public static final String RECORD_QUES_EXTRA_KEY = "ques_num";
    public static final String RECORD_CORRECT_EXTRA_KEY = "correct_num";

    private String title;
    private int ques_num;
    private int ques_max;
    private int correct_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent recordIntent = getIntent();
        if (recordIntent != null) {
            title = recordIntent.getStringExtra(StudyActivity.STUDY_TITLE_EXTRA_KEY);
            ques_num = recordIntent.getIntExtra(StudyActivity.STUDY_QUES_EXTRA_KEY, 1);
            ques_max = recordIntent.getIntExtra(StudyActivity.STUDY_MAX_EXTRA_KEY, 1);
            correct_num = recordIntent.getIntExtra(StudyActivity.STUDY_CORRECT_EXTRA_KEY, 0);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.record_action_bar_text);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView title_view = (TextView) findViewById(R.id.record_title);
        title_view.setText(title);

        TextView rate_view = (TextView) findViewById(R.id.record_rate_number);
        rate_view.setText(String.valueOf(correct_num * 100 / ques_max));

        Button button_ok = (Button) findViewById(R.id.record_button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record2main();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                record2main();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            record2main();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void record2main() {
        Intent mainIntent = new Intent(getApplication(), MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra(RECORD_TITLE_EXTRA_KEY, title);
        mainIntent.putExtra(RECORD_QUES_EXTRA_KEY, ques_num);
        mainIntent.putExtra(RECORD_CORRECT_EXTRA_KEY, correct_num);
        startActivity(mainIntent);
    }
}
