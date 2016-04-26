package com.pmavio.pmbaseframe.bean;

import com.activeandroid.Model;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public abstract class BaseBean extends Model implements Serializable{
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
