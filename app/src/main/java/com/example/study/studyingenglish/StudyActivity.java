package com.example.study.studyingenglish;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class StudyActivity extends AppCompatActivity {

    public static final String STUDY_TITLE_EXTRA_KEY = "title";
    public static final String STUDY_QUES_EXTRA_KEY = "ques_num";
    public static final String STUDY_MAX_EXTRA_KEY = "ques_max";
    public static final String STUDY_CORRECT_EXTRA_KEY = "correct";

    private String title;
    private String dir_name;
    private boolean rand_flag;
    private int ques_num;
    private int start_num;

    private ArrayList<String> mJpnList;
    private ArrayList<String> mEngList;

    private TextView mIndexText;
    private TextView mJpnText;
    private TextView mEditText;
    private FloatingActionButton mFloatButton;
    private TextView mJudgeText;

    private int mCount;
    private ArrayList<Integer> mIndex;
    private int mCountMax;
    private int mCorrectNum;
    private boolean mAnswerFlag = false;
    private InputMethodManager mIMManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        mIMManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        Intent studyIntent = getIntent();
        if (studyIntent != null) {
            title = studyIntent.getStringExtra(RecyclerViewFragment.MAIN_TITLE_EXTRA_KEY);
            dir_name = studyIntent.getStringExtra(RecyclerViewFragment.MAIN_DIR_EXTRA_KEY);
            rand_flag = studyIntent.getBooleanExtra(RecyclerViewFragment.MAIN_RAND_EXTRA_KEY, false);
            ques_num = studyIntent.getIntExtra(RecyclerViewFragment.MAIN_QUES_NUM_KEY, 0);
            start_num = studyIntent.getIntExtra(RecyclerViewFragment.MAIN_QUES_START_KEY, 1);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<String> dataList = loadTextData();

        mJpnList = new ArrayList<>();
        mEngList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (i % 2 == 0) {
                mJpnList.add(dataList.get(i));
            } else {
                mEngList.add(dataList.get(i));
            }
        }

        mIndex = new ArrayList<>();
        int start = 0;
        if (start_num != 0) {
            start = --start_num;
        }
        for (int i = start; i < mJpnList.size(); i++) {
            mIndex.add(i);
        }
        if (rand_flag) {
            Collections.shuffle(mIndex);
        }
        if (ques_num == 0 | ques_num > mIndex.size()) {
            mCountMax = mIndex.size();
        } else {
            mCountMax = ques_num;
        }
        mCount = 0;
        mCorrectNum = 0;

        mIndexText = (TextView) findViewById(R.id.study_index);
        mJpnText = (TextView) findViewById(R.id.study_japanese);
        mEditText = (EditText) findViewById(R.id.study_edit_text);
        mFloatButton = (FloatingActionButton) findViewById(R.id.float_button);
        mJudgeText = (TextView) findViewById(R.id.study_judge);

        mJpnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAnswerFlag) {
                    if (mJpnText.getText() == mEngList.get(mIndex.get(mCount))) {
                        mJpnText.setTextColor(Color.BLACK);
                        mJpnText.setText(mJpnList.get(mIndex.get(mCount)));
                    } else {
                        mJpnText.setTextColor(Color.RED);
                        mJpnText.setText(mEngList.get(mIndex.get(mCount)));
                    }
                }
            }
        });

        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerFlag) {
                    if (getCurrentFocus() != null) {
                        mIMManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    check();
                } else {
                    mCount++;
                    if (mCount < mCountMax) {
                        show();
                    } else {
                        study2record();
                    }
                }
            }
        });

        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mJudgeText.setVisibility(View.GONE);
                } else {
                    mIMManager.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    mJudgeText.setVisibility(View.VISIBLE);
                }
            }
        });

        show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private ArrayList<String> loadTextData() {
        String line;
        InputStream inputStream = null;
        ArrayList<String> data = new ArrayList<>();

        AssetManager assetManager = this.getAssets();
        try {
            inputStream = assetManager.open(dir_name + title + ".txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = in.readLine()) != null) {
                if (line.trim().length() != 0) {
                    data.add(line.trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    private void show() {
        mIndexText.setText(String.format("%d / %d", mCount + 1, mCountMax));
        mJpnText.setTextColor(Color.BLACK);
        mJpnText.setText(mJpnList.get(mIndex.get(mCount)));
        mEditText.setText("");
        mEditText.requestFocus();
        mIMManager.showSoftInput(mEditText, 0);
        mAnswerFlag = true;
    }

    private void check() {
        String answer = mEditText.getText().toString().trim();
        if (answer.equals(mEngList.get(mIndex.get(mCount)))) {
            mCorrectNum++;
            mJudgeText.setTextColor(Color.RED);
            mJudgeText.setText(R.string.study_correct_text);
        } else {
            mJudgeText.setTextColor(Color.BLUE);
            mJudgeText.setText(R.string.study_wrong_text);
        }
        mIndexText.requestFocus();
        mJpnText.setTextColor(Color.RED);
        mJpnText.setText(mEngList.get(mIndex.get(mCount)));
        mJudgeText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.judge_animation));
        mAnswerFlag = false;
    }

    private void study2record() {
        Intent recordIntent = new Intent(this, RecordActivity.class);
        recordIntent.putExtra(STUDY_TITLE_EXTRA_KEY, title);
        recordIntent.putExtra(STUDY_QUES_EXTRA_KEY, mJpnList.size());
        recordIntent.putExtra(STUDY_MAX_EXTRA_KEY, mCountMax);
        recordIntent.putExtra(STUDY_CORRECT_EXTRA_KEY, mCorrectNum);
        startActivity(recordIntent);
    }
}
