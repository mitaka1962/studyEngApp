package com.example.study.studyingenglish;

import android.os.Parcel;
import android.os.Parcelable;

class MyData implements Parcelable {
    private String title;
    private String dirName;
    private int ques_num;
    private int correct_num;
    private boolean new_flag;
    private int count;

    MyData(String title, String name) {
        this.title = title;
        this.dirName = name;
    }

    String getTitle() {
        return title;
    }

    String getDirName() {
        return dirName;
    }

    int getQuesNum() {
        return ques_num;
    }

    void setQuesNum(int ques_num) {
        this.ques_num = ques_num;
    }

    int getCorrectNum() {
        return correct_num;
    }

    void setCorrectNum(int correct_num) {
        this.correct_num = correct_num;
    }

    boolean isNewFlag() {
        return new_flag;
    }

    void setNewFlag(boolean new_flag) {
        this.new_flag = new_flag;
    }

    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.dirName);
        parcel.writeInt(this.ques_num);
        parcel.writeInt(this.correct_num);
        parcel.writeInt(this.new_flag ? 1 : 0);
        parcel.writeInt(this.count);
    }

    private MyData(Parcel in) {
        this.title = in.readString();
        this.dirName = in.readString();
        this.ques_num = in.readInt();
        this.correct_num = in.readInt();
        this.new_flag = in.readInt() == 1;
        this.count = in.readInt();
    }

    public static final Creator<MyData> CREATOR = new Creator<MyData>() {
        @Override
        public MyData createFromParcel(Parcel in) {
            return new MyData(in);
        }

        @Override
        public MyData[] newArray(int size) {
            return new MyData[size];
        }
    };
}
