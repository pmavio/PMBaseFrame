package com.strongit.framedemo.bean;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public class BaseResponse<T> extends BaseBean {
    String flag;
    String message;
    T result;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
