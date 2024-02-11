package com.example.study.studyingenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import androidx.core.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewFragment extends Fragment implements OnRecyclerListener {

    public static final String MAIN_TITLE_EXTRA_KEY = "title";
    public static final String MAIN_DIR_EXTRA_KEY = "dirName";
    public static final String MAIN_RAND_EXTRA_KEY = "random";
    public static final String MAIN_QUES_NUM_KEY = "ques_num";
    public static final String MAIN_QUES_START_KEY = "start_num";

    private static final String RAND_FLAG = "rand_flag";
    private static final String NUM_PICK = "num_pick";
    private static final String START_PICK = "start_pick";

    private ArrayList<MyData> mDataList;

    private NumberPicker num_pick;
    private NumberPicker start_pick;
    private boolean rand_flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mDataList = args.getParcelableArrayList(MainActivity.DATA_LIST_KEY);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new CustomAdapter(mDataList, this));

        Switch rand_switch = (Switch) rootView.findViewById(R.id.main_random_switch);
        rand_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                rand_flag = isChecked;
            }
        });

        num_pick = (NumberPicker) rootView.findViewById(R.id.main_num_pick);
        num_pick.setMaxValue(100);
        num_pick.setMinValue(0);

        start_pick = (NumberPicker) rootView.findViewById(R.id.main_start_pick);
        start_pick.setMaxValue(100);
        start_pick.setMinValue(1);

        SharedPreferences pref = getActivity().getPreferences(MODE_PRIVATE);
        rand_switch.setChecked(pref.getBoolean(RAND_FLAG, false));
        num_pick.setValue(pref.getInt(NUM_PICK, 0));
        start_pick.setValue(pref.getInt(START_PICK, 1));

        TextView text_view = (TextView) rootView.findViewById(R.id.main_ques_num);
        text_view.requestFocus();

        return rootView;
    }

    @Override
    public void OnRecyclerItemClicked(View itemView, int position) {
        Intent studyIntent = new Intent(getActivity(), StudyActivity.class);
        studyIntent.putExtra(MAIN_TITLE_EXTRA_KEY, mDataList.get(position).getTitle());
        studyIntent.putExtra(MAIN_DIR_EXTRA_KEY, mDataList.get(position).getDirName());
        studyIntent.putExtra(MAIN_RAND_EXTRA_KEY, rand_flag);
        studyIntent.putExtra(MAIN_QUES_NUM_KEY, num_pick.getValue());
        studyIntent.putExtra(MAIN_QUES_START_KEY, start_pick.getValue());
        startActivity(studyIntent);
    }

    private void saveData() {
        SharedPreferences pref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(RAND_FLAG, rand_flag);
        editor.putInt(NUM_PICK, num_pick.getValue());
        editor.putInt(START_PICK, start_pick.getValue());
        editor.apply();
    }
}
