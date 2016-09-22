package hhh.appstudy.entities;

/**
 * Created by hhh on 2016/4/22.
 */
public class Result<T> {
    public static final int STATE_SUC=0;
    public static final int STATE_ERR=1;

    public int code;
    public String message;
    public T data;
}
