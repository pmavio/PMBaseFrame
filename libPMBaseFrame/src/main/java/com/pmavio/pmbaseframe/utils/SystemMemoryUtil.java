package com.pmavio.pmbaseframe.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.text.format.Formatter;
import android.util.Log;

public class SystemMemoryUtil {
	
	
	public static String getAvailMemory(Context context) 
    {
        // 获取android当前可用内存大小 
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存 
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化 
//        return mi.availMem/(1024*1024);
    }
	
	public static long getTotalMemory(Context context) 
    {
        String str1 = "/proc/meminfo";// 系统内存信息文件 
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try 
        {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
            localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小 

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte 
            localBufferedReader.close();

        } catch (IOException e) {
        }
        //return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化 
        return initial_memory/(1024*1024);
    }
}
