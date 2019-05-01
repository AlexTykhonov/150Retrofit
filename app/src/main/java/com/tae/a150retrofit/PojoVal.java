package com.tae.a150retrofit;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoVal implements Parcelable, Comparable <PojoVal> {

@SerializedName("r030")
@Expose
private Integer r030;

@SerializedName("txt")
@Expose
private String txt;

@SerializedName("rate")
@Expose
private Double rate;

@SerializedName("cc")
@Expose
private String cc;

@SerializedName("exchangedate")
@Expose
private String exchangedate;

    private int priority;

    public PojoVal(Integer r030, String txt, Double rate, String cc, String exchangedate) {
        this.r030 = r030;
        this.txt = txt;
        this.rate = rate;
        this.cc = cc;
        this.exchangedate = exchangedate;
    }

    public PojoVal() {
    }

    public PojoVal(Double rate, String cc) {
        this.rate = rate;
        this.cc = cc;
        }

    public PojoVal(Double rate, String cc, String date) {
        this.rate = rate;
        this.cc = cc;
        this.exchangedate = date;
    }


    public static final Creator<PojoVal> CREATOR = new Creator<PojoVal>() {
        @Override
        public PojoVal createFromParcel(Parcel in) {
            String val = in.readString();
            Double rate = in.readDouble();
            String exchangedate = in.readString();
            return new PojoVal(rate, val, exchangedate);
        }

        @Override
        public PojoVal[] newArray(int size) {
            return new PojoVal[size];
        }
    };

    public Integer getR030() {
        return r030;
        }

public void setR030(Integer r030) {
        this.r030 = r030;
        }

public String getTxt() {
        return txt;
        }

public void setTxt(String txt) {
        this.txt = txt;
        }

public Double getRate() {
        return rate;
        }

public void setRate(Double rate) {
        this.rate = rate;
        }

public String getCc() {
        return cc;
        }

public void setCc(String cc) {
        this.cc = cc;
        }

public String getExchangedate() {
        return exchangedate;
        }

public void setExchangedate(String exchangedate) {
        this.exchangedate = exchangedate;
        }

        public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getCc());
        dest.writeDouble(getRate());
        dest.writeString(getExchangedate());
    }

    @Override
    public String toString() {
        return "PojoVal{" +
                "r030=" + r030 +
                ", txt='" + txt + '\'' +
                ", rate=" + rate +
                ", cc='" + cc + '\'' +
                ", exchangedate='" + exchangedate + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull PojoVal o) {
        return this.getPriority()-o.getPriority();
    }
}