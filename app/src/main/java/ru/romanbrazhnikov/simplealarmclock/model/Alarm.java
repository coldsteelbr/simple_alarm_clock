package ru.romanbrazhnikov.simplealarmclock.model;

/**
 * Created by roman on 30.09.17.
 */

public class Alarm {

    public String mTime;
    public OnOff mState = OnOff.OFF;
    public enum OnOff{
        ON,
        OFF
    }
}
