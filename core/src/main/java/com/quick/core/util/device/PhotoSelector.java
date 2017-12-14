package com.quick.core.util.device;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;


import com.quick.core.util.common.DateUtil;
import com.quick.core.util.io.FileSavePath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by dailichun on 2017/12/6.
 * 调用系统的相册或者摄像头进行照片选取，并压缩保存
 */

public class PhotoSelector {

    /**
     * 保存的相册宽度，超过该宽度会等比例缩小
     */
    private int dWidth = 720;

    /**
     * 保存相册质量。0-100
     */
    private int dQuality = 70;

    private String desPath;

    private String requestCamaraPath;

    /*
     * 保存的文件路径，默认为/sdcard/quickapp/cache/
     */
    private String dirPath;

    /*
     * 是否需要删除原始文件
     */
    private boolean isDelOriginalFile = true;

    private CompressResult result;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1001 && result != null) {
                result.onCompelete(desPath);
            }
        }
    };

    public PhotoSelector() {

    }

    public PhotoSelector(int dWidth, int dQuality) {
        this.dWidth = dWidth;
        this.dQuality = dQuality;
    }

    public void setWidth(int width) {
        this.dWidth = width;
    }

    public void setdQuality(int quality) {
        this.dQuality = quality;
    }

    /**
     * Activity调用系统拍照
     *
     * @param activity
     * @param requestCode
     */
    public void requestSysCamera(Activity activity, int requestCode) {
        requestCamaraPath = getPhotoTmpPath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(requestCamaraPath)));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Fragment调用系统拍照
     *
     * @param fragment
     * @param requestCode
     */
    public void requestSysCamera(android.support.v4.app.Fragment fragment, int requestCode) {
        requestCamaraPath = getPhotoTmpPath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(requestCamaraPath)));
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Fragment调用系统拍照
     *
     * @param fragment
     * @param requestCode
     */
    public void requestSysCamera(Fragment fragment, int requestCode) {
        requestCamaraPath = getPhotoTmpPath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(requestCamaraPath)));
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Fragment调用系统拍照
     *
     * @param fragment
     * @param requestCode
     */
    public void requestSysCamera(Object fragment, int requestCode) {
        if (fragment instanceof Fragment) {
            requestSysCamera((Fragment) fragment, requestCode);
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            requestSysCamera((android.support.v4.app.Fragment) fragment, requestCode);
        }
    }

    /**
     * Activity调用系统相册
     *
     * @param activity
     * @param requestCode
     */
    public void requestPhotoPick(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Fragment调用系统相册
     *
     * @param fragment
     * @param requestCode
     */
    public void requestPhotoPick(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Fragment调用系统相册
     *
     * @param fragment
     * @param requestCode
     */
    public void requestPhotoPick(android.support.v4.app.Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Fragment调用系统相册
     *
     * @param fragment
     * @param requestCode
     */
    public void requestPhotoPick(Object fragment, int requestCode) {
        if (fragment instanceof Fragment) {
            requestPhotoPick((Fragment) fragment, requestCode);
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            requestPhotoPick((android.support.v4.app.Fragment) fragment, requestCode);
        }
    }

    /**
     * 压缩处理选择相册后的图片
     *
     * @param con
     * @param intent
     * @param result
     * @return
     */
    public String handlePick(final Context con, final Intent intent, CompressResult result) {
        this.result = result;
        new Thread(new Runnable() {
            @Override
            public void run() {
                handlePickBack(con, intent);
                handler.sendEmptyMessage(0x1001);
            }
        }).start();
        return desPath;
    }

    /**
     * 压缩处理拍照后的图片
     *
     * @param result
     * @return
     */
    public String handleCamera(CompressResult result) {
        this.result = result;
        new Thread(new Runnable() {
            @Override
            public void run() {
                decodeFile(requestCamaraPath, dWidth, dQuality);
                handler.sendEmptyMessage(0x1001);
            }
        }).start();
        return desPath;
    }

    /**
     * 获取图片保存路径
     *
     * @return
     */
    public String getPhotoPath() {
        return desPath;
    }

    /**
     * 设置图片保存路径
     *
     * @param dirPath
     */
    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    /**
     * 设置是否删除保存前的原始图片
     *
     * @param isDelOriginalFile
     */
    public void setIsDelOriginalFile(boolean isDelOriginalFile) {
        this.isDelOriginalFile = isDelOriginalFile;
    }

    /**
     * 获取保存路径
     *
     * @return
     */
    public String getDirPath() {
        if (TextUtils.isEmpty(dirPath)) {
            this.dirPath = FileSavePath.getTempFolder();
        }
        return this.dirPath;
    }

    /**
     * 获取图片旋转角度
     *
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ignored) {
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }
        return degree;
    }

    private void handlePickBack(Context con, Intent intent) {
        Uri uri = intent.getData();
        Cursor cursor = con.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            HashMap<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                map.put(cursor.getColumnName(i), cursor.getString(i));
            }
            String spath = map.get("_data");
            decodeFile(spath, dWidth, dQuality);
            cursor.close();
        }
    }

    private void decodeFile(String srcPath, int defaultWidth, int quality) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 获取这个图片的宽和高
        options.inJustDecodeBounds = true;

        Bitmap bitmap; // 此时返回bm为空

        int w = options.outWidth;
        int h = options.outHeight;

        if (defaultWidth >= w && defaultWidth >= h) {
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(srcPath, options);
            saveBitmap(bitmap, quality);
        } else {

            int nw;
            int nh;

            if (h > w) {
                nw = defaultWidth;
                nh = nw * h / w;
            } else {
                nh = defaultWidth;
                nw = nh * w / h;
            }

            int be = 1;// be=1表示不缩放
            if (w > h && w > nw) {// 如果宽度大的话根据宽度固定大小缩放
                be = w / nw;
            } else if (w < h && h > nh) {// 如果高度高的话根据宽度固定大小缩放
                be = h / nh;
            }
            if (be <= 0)
                be = 1;
            options.inJustDecodeBounds = false;
            options.inSampleSize = be;// 设置缩放比例
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            bitmap = BitmapFactory.decodeFile(srcPath, options);
            saveBitmap(bitmap, quality);

            if (isDelOriginalFile) {
                File file = new File(srcPath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    private void saveBitmap(Bitmap bm, int quality) {
        desPath = getPhotoTmpPath();
        File file = new File(desPath);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bm.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPhotoTmpPath() {
        String photoName = DateUtil.convertDate(new Date(), "yyyyMMddHHmss") + "s.jpg";
        File dir = new File(getDirPath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath + photoName;
    }

    /**
     * 压缩回调接口
     */
    public interface CompressResult {
        void onCompelete(String path);
    }
}
