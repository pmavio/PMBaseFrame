package com.strongit.framedemo.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pmavio.pmbaseframe.test.R;

/**
 * 检查更新类，使用checkForUpdates方法检查更新
 */
public class UpdateChecker{
    private static final String TAG = "UpdateChecker";
    private static final int DOWNLOAD_NOTIFICATION_ID = "AppUpdate".hashCode();


    private Context mContext;
    //检查版本信息的线程
    private Thread mThread;
    //版本对比地址
    private String mCheckUrl;
    private AppVersion mAppVersion;
    //下载apk的对话框
    private ProgressDialog mProgressDialog;
    //通知栏进度
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder ntfBuilder;


    private Handler handler;

    private File apkFile;
    public void setCheckUrl(String url) {
        mCheckUrl = url;
    }

    public UpdateChecker(Context context) {
        mContext = context;
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在下载");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }
        });

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 检查更新
     */
    public void checkForUpdates() {
        if(mCheckUrl == null) {
            //throw new Exception("checkUrl can not be null");
            return;
        }

        mThread = new Thread() {
            @Override
            public void run() {
                //if (isNetworkAvailable(mContext)) {
                String json = sendPost();
                Log.i("jianghejie","json = "+json);
                if(json!=null){
                    mAppVersion = parseJson(json);
                    update();
                }else{
                    Log.e(TAG, "can't get app update json");
                }
            }
        };
        mThread.start();
    }

    private void update(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
                    if (mAppVersion.getApkCode() > versionCode) {
                        showUpdateDialog();
                    } else {
                        //Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    }
                }catch (PackageManager.NameNotFoundException ignored) {
                    //
                }
            }
        });
    }


    /**
     * 通知栏弹出下载提示进度
     *
     * @param progress
     */
    private void showDownloadNotificationUI(final int progress) {
        if (mContext != null) {
            String contentText = new StringBuffer().append(progress)
                    .append("%").toString();
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                    0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
            if (mNotificationManager == null) {
                mNotificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (ntfBuilder == null) {
                String title = mContext.getResources().getString(R.string.app_name);
                ntfBuilder = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(mContext.getApplicationInfo().icon)
                        .setTicker("开始下载...")
                        .setContentTitle(title)
                        .setContentIntent(contentIntent);
            }
            if(progress == 100){
                ntfBuilder.setTicker("下载完成");
            }
            ntfBuilder.setContentText(contentText);
            ntfBuilder.setProgress(100, progress, false);
            mNotificationManager.notify(DOWNLOAD_NOTIFICATION_ID, ntfBuilder.build());
        }
    }

    protected String sendPost() {
        HttpURLConnection uRLConnection = null;
        InputStream is = null;
        BufferedReader buffer = null;
        String result = null;
        try {
            URL url = new URL(mCheckUrl);
            uRLConnection = (HttpURLConnection) url.openConnection();
            uRLConnection.setDoInput(true);
            uRLConnection.setDoOutput(true);
            uRLConnection.setRequestMethod("POST");
            uRLConnection.setUseCaches(false);
            uRLConnection.setConnectTimeout(10 * 1000);
            uRLConnection.setReadTimeout(10 * 1000);
            uRLConnection.setInstanceFollowRedirects(false);
            uRLConnection.setRequestProperty("Connection", "Keep-Alive");
            uRLConnection.setRequestProperty("Charset", "UTF-8");
            uRLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            uRLConnection.setRequestProperty("Content-Type", "application/json");
            uRLConnection.connect();
            is = uRLConnection.getInputStream();
            String content_encode = uRLConnection.getContentEncoding();
            if (null != content_encode && !"".equals(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }
            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            result = strBuilder.toString();
        } catch (Exception e) {
            Log.e(TAG, "http post error", e);
        } finally {
            if(buffer!=null){
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(uRLConnection!=null){
                uRLConnection.disconnect();
            }
        }
        return result;
    }
    private AppVersion parseJson(String json) {
        AppVersion appVersion = new AppVersion();
        try {
            JSONObject obj = new JSONObject(json);
            String updateMessage = obj.getString(AppVersion.APK_UPDATE_CONTENT);
            String apkUrl = obj.getString(AppVersion.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(AppVersion.APK_VERSION_CODE);
            appVersion.setApkCode(apkCode);
            appVersion.setApkUrl(apkUrl);
            appVersion.setUpdateMessage(updateMessage);
        } catch (JSONException e) {
            Log.e(TAG, "parse json error", e);
        }
        return appVersion;
    }

    public void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //builder.setIcon(R.drawable.icon);
        builder.setTitle("有新版本");
        builder.setMessage(mAppVersion.getUpdateMessage());
        builder.setPositiveButton("下载",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        downLoadApk();
                    }
                });
        builder.setNegativeButton("忽略",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        builder.show();
    }

    public void downLoadApk() {
        String apkUrl = mAppVersion.getApkUrl();
        String dir = mContext.getExternalFilesDir( "apk").getAbsolutePath();
        File folder = Environment.getExternalStoragePublicDirectory(dir);
        if(folder.exists() && folder.isDirectory()) {
            //do nothing
        }else {
            folder.mkdirs();
        }
        String filename = apkUrl.substring(apkUrl.lastIndexOf("/"),apkUrl.length());
        String destinationFilePath =  dir + "/" + filename;
        apkFile = new File(destinationFilePath);
        mProgressDialog.show();
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra("url", apkUrl);
        intent.putExtra("dest", destinationFilePath);
        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
        mContext.startService(intent);
    }

    private class DownloadReceiver extends ResultReceiver{
        public DownloadReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                showDownloadNotificationUI(progress);
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                    //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
                    String[] command = {"chmod","777",apkFile.toString()};
                    try{
                        ProcessBuilder builder = new ProcessBuilder(command);
                        builder.start();
                        installApk(Uri.fromFile(apkFile));
                    }catch (Exception e){

                    }
                }
            }
        }
    }

    private void installApk(Uri data) {
        if (mContext != null) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(data, "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
            if (mNotificationManager != null) {
                mNotificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
            }
        } else {
            Log.e("NullPointerException", "The context must not be null.");
        }

    }

}