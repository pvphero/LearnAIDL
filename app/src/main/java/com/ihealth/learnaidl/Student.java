package com.ihealth.learnaidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShenZhenWei on 17/9/14.
 */

public class Student implements Parcelable{
    public static final int SEX_MALE=1;
    public static final int SEX_FRMALE=2;

    public int sno;
    public String name;
    public int sex;
    public int age;

    public Student() {
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    protected Student(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in){
        sno=in.readInt();
        name=in.readString();
        sex=in.readInt();
        age=in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(sno);
        dest.writeString(name);
        dest.writeInt(sex);
        dest.writeInt(age);
    }

    @Override
    public String toString() {
        return "Student{" +
                "sno=" + sno +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }
}
