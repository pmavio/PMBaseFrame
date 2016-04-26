package com.strongit.framedemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.github.lazylibrary.util.FileUtils;

import java.io.File;


/**
 * 照片选择BaseActivity
 * 作者：Mavio
 * 日期：2016/3/14.
 */
public abstract class MBasePickPhotoActivity extends MBaseActivity {
    private static String DIR_CACHE;

    private final int REQ_CODE_IMAGE_FROM_CAMERA = 0;

    private final int REQ_CODE_IMAGE_FROM_GALLERY = 1;

    private final int REQ_CODE_IMAGE_CROP = 2;

    private File currentImageFile;
    private Uri currentImageUri;

    private OnPickPhotoListener listener;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if(DIR_CACHE == null){
            DIR_CACHE = getExternalCacheDir() + File.separator + "cache_image";
        }
        createDir(DIR_CACHE);
    }

    @Override
    protected void onDestroy() {
        FileUtils.deleteFile(DIR_CACHE); //清除缓存的图片
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_IMAGE_FROM_CAMERA: {
                if (Activity.RESULT_OK == resultCode) {
                    if(listener.isCrop) {
                        cropImage(currentImageUri);
                    } else{
                        onPickPhoto(currentImageFile);
                    }
                }
                break;
            }
            case REQ_CODE_IMAGE_FROM_GALLERY: {
                if (Activity.RESULT_OK == resultCode) {
                    if(listener.isCrop){
                        cropImage(data.getData());
                    }else{
                        String path = FileUtils.uriToPath(act, data.getData());
                        File file = new File(path);
                        onPickPhoto(file);
                    }
                }
                break;
            }
            case REQ_CODE_IMAGE_CROP: {
                if (Activity.RESULT_OK == resultCode) {
                    onPickPhoto(currentImageFile);
                }
                break;
            }

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void getImageFromGallery() {
        setNewFile(null);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("return-data", false);//设置true的话，则会在onActivityResult的intent里返回Bitmap，在选择大图时会出现问题，设置false的话则会在intent.getData返回uri

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQ_CODE_IMAGE_FROM_GALLERY);
        }
    }

    private void getImageFromCamera() {
        setNewFile(null);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);//设置true的话，则会在onActivityResult的intent里返回Bitmap
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);//与打开相册选择不同，上一行参数设置false时，需要传入此参数来指定拍好的照片存储的路径
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQ_CODE_IMAGE_FROM_CAMERA);
        }
    }

    private void setNewFile(Uri uri) {
        String path = DIR_CACHE + File.separator + System.currentTimeMillis() + "." + Bitmap.CompressFormat.JPEG.toString();
        currentImageFile = new File(path);
        if(uri == null){
            currentImageUri = Uri.fromFile(currentImageFile);
        }else{
            currentImageUri = uri;
        }
    }

    private boolean createDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            return true;
        }
        if (dir.exists() && !dir.isDirectory()) {
            return false;
        }
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*"); //放入图片源的uri

        intent.putExtra("crop", listener.isCrop);

        //裁剪的宽高比
        intent.putExtra("aspectX", listener.aspectX);
        intent.putExtra("aspectY", listener.aspectY);

        //裁剪的输出宽高值
        intent.putExtra("outputX", listener.outputX);
        intent.putExtra("outputY", listener.outputY);

        //去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);

        intent.putExtra("noFaceDetection", true);

        intent.putExtra("return-data", false); //设置在指定uri路径里返回裁剪好的图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, REQ_CODE_IMAGE_CROP);
    }

    protected void pickFromAlbum(OnPickPhotoListener listener){
        this.listener = listener;
        if(listener == null) return;

        getImageFromGallery();
    }

    protected void pickFromCamera(OnPickPhotoListener listener){
        this.listener = listener;
        if(listener == null) return;

        getImageFromCamera();
    }

    private void onPickPhoto(final File file){
//        runOnUiThread(new Action0() { //设置在UI线程运行
//            @Override
//            public void call() {
                listener.onPickPhoto(file);
//            }
//        });
    }

    /**
     * 选照片回调对象
     */
    public abstract class OnPickPhotoListener{
        boolean isCrop = false;
        int outputX, outputY;
        int aspectX, aspectY;

        public OnPickPhotoListener() {
        }

        public OnPickPhotoListener(boolean isCrop, int aspectX, int aspectY) {
            this.isCrop = isCrop;
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            check();
        }

        /**
         * 检查是否裁剪，如果设置为裁剪，检查宽高比，并设置输出宽高值
         */
        protected void check(){
            if(!isCrop) return;
            if(aspectX < 1) aspectX = 1;
            if(aspectY < 1) aspectY = 1;

            outputX = screenWidth;
            outputY = screenHeight;

            if(aspectX > aspectY){
                outputX = (int) (outputY * aspectX / (float)aspectY);
            }else{
                outputY = (int) (outputX * aspectY / (float)aspectX);
            }
        }

        /**
         * 图片选择完成后回调方法
         * @param file
         */
        public abstract void onPickPhoto(File file);
    }
}