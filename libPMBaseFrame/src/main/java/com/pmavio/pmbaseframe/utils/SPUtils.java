package com.pmavio.pmbaseframe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pmavio.pmbaseframe.BuildConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * SharedPreferenced工具，可以自动赋值或保存所有标记了@{@link com.pmavio.pmbaseframe.utils.SPUtils.SpContent}注解的参数<br />
 * 但类型仅限于String、boolean、int、float、long、Set<String>
 * 作者：Mavio
 * 日期：2016/3/29.
 */
public class SPUtils {

    private SharedPreferences sp;
    private Context mContext;

    public SPUtils(Context context) {
        initSP(context);
    }

    private void initSP(Context context){
        if(context == null) throw new NullPointerException("context can't be null");
        if(sp == null || context != mContext) {
            sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            mContext = context;
        }
    }

    public String getString(String key){
        return getString(key, null);
    }

    public String getString(String key, String defaultValue){
        return sp.getString(key, defaultValue);
    }

    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue){
        return sp.getBoolean(key, defaultValue);
    }

    public int getInt(String key){
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue){
        return sp.getInt(key, defaultValue);
    }

    public float getFloat(String key){
        return getFloat(key, 0f);
    }

    public float getFloat(String key, float defaultValue){
        return sp.getFloat(key, defaultValue);
    }

    public long getLong(String key){
        return getLong(key, 0l);
    }

    public long getLong(String key, long defaultValue){
        return sp.getLong(key, defaultValue);
    }

    public Set<String> getStringSet(String key){
        return getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue){
        return sp.getStringSet(key, defaultValue);
    }

    /**
     * 存储一个键值对
     * 本方法会根据value类型来做相应存储，但类型仅限于String、boolean、int、float、long、Set<String>
     * @param key
     * @param value
     */
    public void set(String key, Object value){
        if(key == null || key.length() == 0 | value == null) return;

        Class valueClass = value.getClass();

        if(String.class.isAssignableFrom(valueClass)) {
            sp.edit().putString(key, (String) value).apply();
        }else if(Boolean.class.isAssignableFrom(valueClass) || boolean.class.isAssignableFrom(valueClass)){
            sp.edit().putBoolean(key, (boolean) value).apply();
        }else if(Integer.class.isAssignableFrom(valueClass) || int.class.isAssignableFrom(valueClass)){
            sp.edit().putInt(key, (int) value).apply();
        }else if(Float.class.isAssignableFrom(valueClass) || float.class.isAssignableFrom(valueClass)){
            sp.edit().putFloat(key, (float) value).apply();
        }else if(Long.class.isAssignableFrom(valueClass) || long.class.isAssignableFrom(valueClass)) {
            sp.edit().putLong(key, (long) value).apply();
        }else if(Set.class.isAssignableFrom(valueClass)) {
            sp.edit().putStringSet(key, (Set) value).apply();
        }
    }


    /**
     * 自动填充所有带有@{@link com.pmavio.pmbaseframe.utils.SPUtils.SpContent}注解的参数
     * @param target
     */
    public void getValues(Object target){
        if(target == null) return;

        Field[] fields = target.getClass().getDeclaredFields();
        for(Field f : fields){
            f.setAccessible(true);
            SpContent spContent = f.getAnnotation(SpContent.class);
            if(spContent == null) continue;
            String key = spContent.value();
            if(key == null) continue;

            Class fClass = f.getType();
            Object value = null;
            if(String.class.isAssignableFrom(fClass)) {
                value = sp.getString(key, spContent.defaultString());
            }else if(Boolean.class.isAssignableFrom(fClass) || boolean.class.isAssignableFrom(fClass)){
                value = sp.getBoolean(key, spContent.defaultBoolean());
            }else if(Integer.class.isAssignableFrom(fClass) || int.class.isAssignableFrom(fClass)){
                value = sp.getInt(key, spContent.defaultInt());
            }else if(Float.class.isAssignableFrom(fClass) || float.class.isAssignableFrom(fClass)){
                value = sp.getFloat(key, spContent.defaultFloat());
            }else if(Long.class.isAssignableFrom(fClass) || long.class.isAssignableFrom(fClass)){
                value = sp.getLong(key, spContent.defaultLong());
            }else if(Set.class.isAssignableFrom(fClass)){
                value = sp.getStringSet(key, null);
            }
            if(value != null){
                try {
                    f.set(target, value);
                } catch (IllegalAccessException e) {
                    if(BuildConfig.DEBUG)
                        e.printStackTrace();
                }
            }
        }
    }

    /**
     * 自动保存所有带有@{@link com.pmavio.pmbaseframe.utils.SPUtils.SpContent}注解的参数
     * @param target
     */
    public void setValues(Object target){
        if(target == null) return;

        Field[] fields = target.getClass().getDeclaredFields();
        for(Field f : fields){
            f.setAccessible(true);
            SpContent spContent = f.getAnnotation(SpContent.class);
            if(spContent == null) continue;
            String key = spContent.value();
            if(key == null) continue;
            try{
                Object value = f.get(target);
                set(key, value);
            } catch (IllegalAccessException e) {
                continue;
            }
        }
    }


    /**
     * 用于标记需要自动赋值或保存的参数
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface SpContent{

        /**
         * key值
         * @return
         */
        String value();

        /**
         * 默认值
         * @return
         */
        String defaultString() default "";

        /**
         * 默认值
         * @return
         */
        int defaultInt() default 0;

        /**
         * 默认值
         * @return
         */
        boolean defaultBoolean() default false;

        /**
         * 默认值
         * @return
         */
        long defaultLong() default 0l;

        /**
         * 默认值
         * @return
         */
        float defaultFloat() default 0f;

    }
}
