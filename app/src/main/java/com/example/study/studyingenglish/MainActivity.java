package com.example.study.studyingenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
//import androidx.core.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String DATA_LIST_KEY = "dataList";

    private static final String QUES_NUM = "_ques_num";
    private static final String CORRECT_NUM = "_correct_num";
    private static final String NEW_FLAG = "_new_flag";
    private static final String COUNT = "_count";
    private static final String DIR_NAME = "english_txt_data/";

    private ArrayList<MyData> mDataList;

    private String title = null;
    private int ques_num;
    private int correct_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mainIntent = getIntent();
        // 結果画面から遷移してきた場合、インテントから直線の単語問題の正答数などを取得
        if (mainIntent != null) {
            title = mainIntent.getStringExtra(RecordActivity.RECORD_TITLE_EXTRA_KEY);
            ques_num = mainIntent.getIntExtra(RecordActivity.RECORD_QUES_EXTRA_KEY, 72);
            correct_num = mainIntent.getIntExtra(RecordActivity.RECORD_CORRECT_EXTRA_KEY, 72);
        }

        initData();

        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA_LIST_KEY, mDataList);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        transaction.replace(R.id.content_fragment, fragment).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private void initData() {
        // 単語問題ファイル一つにつき、MyDataを一つ作成し、リストに追加
        AssetManager assetManager = this.getAssets();
        mDataList = new ArrayList<>();
        try {
            for (String filename : assetManager.list(DIR_NAME)) {
                if (filename.endsWith(".txt")) {
                    filename = filename.substring(0, filename.lastIndexOf("."));
                    mDataList.add(new MyData(filename, DIR_NAME));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 正答数などをMyDataにセット
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        for (MyData data : mDataList) {
            String title = data.getTitle();
            if(this.title != null & title.equals(this.title)) {
                // 直前に行った単語問題の場合
                data.setQuesNum(this.ques_num);
                data.setCorrectNum(this.correct_num);
                data.setNewFlag(false);
                data.setCount(data.getCount() + 1);
            } else {
                data.setQuesNum(pref.getInt(title + QUES_NUM, 0));
                data.setCorrectNum(pref.getInt(title + CORRECT_NUM, 0));
                data.setNewFlag(pref.getBoolean(title + NEW_FLAG, true));
                data.setCount(pref.getInt(title + COUNT, 0));
            }
        }
    }

    private void saveData() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for (MyData data: mDataList) {
            String title = data.getTitle();
            editor.putInt(title + QUES_NUM, data.getQuesNum());
            editor.putInt(title + CORRECT_NUM, data.getCorrectNum());
            editor.putBoolean(title + NEW_FLAG, data.isNewFlag());
            editor.putInt(title + COUNT, data.getCount());
        }
        editor.apply();
    }
}
