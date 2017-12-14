package com.quick.core.util.io;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.MimeTypeMap;


import com.quick.core.ui.widget.ToastUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import quick.com.core.R;


/**
 * Created by dailichun on 2017/12/7.
 * 文件操作工具类
 */
public class FileUtil {

    private static final String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"},
            {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"},
            {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"},
            {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"},
            {"", "*/*"}};

    private static final String[] FileType = {"ai", "css", "dll", "eps", "gif", "html", "ico", "jpg", "mp3", "pdf", "png", "ppt", "ps", "svg", "swf", "tif", "txt", "word", "xls", "zip"};

    /**
     * 文件转base64
     *
     * @param f 文件对象
     * @return
     */
    public static String file2Base64(File f) {
        byte[] b = file2Byte(f);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * base64转文件
     *
     * @param base64Str base64字符串
     * @param filePath  文件路径
     */
    public static void base642file(String base64Str, String filePath) {
        saveFile(Base64.decode(base64Str, Base64.DEFAULT), filePath);
    }

    /**
     * 向一个文本中另起一行续写数据
     *
     * @param path 文件路径
     * @param text 内容
     */
    public static void writeText2PathEnd(String path, String text) {
        try {
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(path, true)));
            pw.write(text);
            pw.write("\r\n");
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文本写入某路径
     *
     * @param path
     * @param msg
     */
    public static void writeText2Path(String path, String msg) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(msg.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件转二进制
     *
     * @param f
     * @return
     */
    public static byte[] file2Byte(File f) {
        try {
            InputStream is = new FileInputStream(f);
            return IOUtil.inputStream2Byte(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 创建文件夹
     *
     * @param path
     */
    public static void foldCreate(String path) {
        File f = new File(path);
        foldCreate(f);
    }

    /**
     * 创建文件
     *
     * @param path
     */
    public static void fileCreate(String path) {
        File f = new File(path);
        foldCreate(f.getParentFile());
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     *
     * @param f
     */
    public static void foldCreate(File f) {
        if (f.exists()) {
            if (f.isFile()) {
                f.delete();
                f.mkdirs();
            }
        }else{
            f.mkdirs();
        }
    }

    /**
     * 删除文件或者文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFile(file1);
                }
            }
            file.delete();
        }
    }

    /**
     * 拷贝文件
     *
     * @param assfilename
     * @param destpathname
     * @throws java.io.IOException
     */
    public static void copyFile(String assfilename, String destpathname) throws IOException {
        FileInputStream fis = new FileInputStream(assfilename);
        FileOutputStream fos = new FileOutputStream(destpathname);
        byte data[] = new byte[1024];
        int count;
        while ((count = fis.read(data)) != -1) {
            fos.write(data, 0, count);
        }
        fos.flush();
        fos.close();
        fis.close();
    }

    /**
     * 读取文件中字符串
     *
     * @param path
     * @return
     */
    public static String readFile(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String str = "";
            String r = br.readLine();
            while (r != null) {
                str += r;
                r = br.readLine();
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 读取文件中字符串
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = "";
            String r = br.readLine();
            while (r != null) {
                str += r;
                r = br.readLine();
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 保存文件
     *
     * @param b
     * @param filepath
     */
    public static void byte2file(byte[] b, String filepath) {
        saveFile(b, filepath);
    }

    /**
     * 保存文件
     */
    public static boolean saveFile(byte[] b, String filepath) {
        try {
            File f = new File(filepath);
            File pdir = new File(f.getParent());
            if (!pdir.exists()) {
                pdir.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(filepath);
            out.write(b);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存图片 如果已经存在则替换原图
     *
     * @param bm
     * @param filepath
     * @return
     */
    public static boolean saveBitmap(Bitmap bm, String filepath) {
        try {
            File f = new File(filepath);
            File pdir = new File(f.getParent());
            if (!pdir.exists()) {
                pdir.mkdirs();
            } else if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 拷贝文件
     *
     * @param src
     * @param dest
     */
    public static void copyFileTemp(String src, String dest) {
        InputStream is = null;
        OutputStream os = null;
        String temp = src.substring(src.lastIndexOf("/"));
        dest = dest + temp;

        try {
            is = new BufferedInputStream(new FileInputStream(src));
            os = new BufferedOutputStream(new FileOutputStream(dest));

            byte[] b = new byte[1024];
            int len = 0;

            try {
                while ((len = is.read(b)) != -1) {
                    os.write(b, 0, len);
                }
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从assets拷贝文件到文件夹
     *
     * @param con
     * @param assfilename
     * @param destpathname
     * @throws java.io.IOException
     */
    public static void copyFileFromAss2Dir(Context con, String assfilename, String destpathname) throws IOException {
        InputStream fis = con.getResources().getAssets().open(assfilename);
        FileOutputStream fos = new FileOutputStream(destpathname);
        byte data[] = new byte[1024];
        int count;
        while ((count = fis.read(data)) != -1) {
            fos.write(data, 0, count);
        }
        fos.flush();
        fos.close();
        fis.close();
    }

    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (TextUtils.isEmpty(end))
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (String[] aMIME_MapTable : MIME_MapTable) {
            if (end.equals(aMIME_MapTable[0]))
                type = aMIME_MapTable[1];
        }
        return type;
    }

    public static void openFile(Context con, String path) {
        File file = new File(path);
        openFile(con, file);
    }

    /**
     * 弹出手机内所有可打开该文件类型的app选择
     *
     * @param con
     * @param file
     */
    public static void openFile(Context con, File file) {
        if (con == null) {
            return;
        }
        try {
            if (!file.exists() || file.isDirectory()) {
                ToastUtil.toastShort(con, con.getString(R.string.file_not_found));
                return;
            }
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            // 获取文件file的MIME类型
            String type = getMIMEType(file);
            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);
            // 跳转
            con.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.toastShort(con, con.getString(R.string.file_unknow_type));
        }
    }

    /**
     * 从路径中获取文件名
     *
     * @param path
     * @return
     */
    public static String getFileNameFromPath(String path) {
        String fileName = "";
        File file = new File(path);
        if (file.isFile()) {
            fileName = file.getName();
        }
        return fileName;
    }

    /**
     * 寻找适合的程序直接打开文件
     *
     * @param con
     * @param f
     */
    public static void doOpenFile(Context con, File f) {
        if (con == null) {
            return;
        }

        if (!f.exists()) {
            ToastUtil.toastShort(con, con.getString(R.string.file_not_found));
            return;
        }
        String filePath = f.getPath();
        String fileName = f.getName();
        if (!fileName.contains(".") || fileName.endsWith(".")) {
            ToastUtil.toastShort(con, con.getString(R.string.file_unknow_type));
            return;
        }

        String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String param = "file://" + filePath;
        Uri uri = Uri.parse(param);
        String type = null;
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(filePath));
        if (type == null) {
            String tmp = filePath.toLowerCase();
            if (tmp.endsWith("mp3") || tmp.endsWith("wav") || tmp.endsWith("wma")) {
                type = "audio/*";
            } else if (tmp.endsWith("apk")) {
                type = "application/vnd.android.package-archive";
            }
        }
        if (type != null && !suffix.equals(".rar")) {
            intent.setDataAndType(uri, type);

            try {
                ((Activity) con).startActivityForResult(intent, 1);
            } catch (ActivityNotFoundException e) {
                ToastUtil.toastShort(con, con.getString(R.string.file_cannot_open));
            }
        } else {
            try {
                Intent intent1 = null;
                if (suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".gif") || suffix.equals(".bmp")) {
                    intent1 = getImageFileIntent(filePath);
                } else if (suffix.equals(".pdf")) {
                    intent1 = getPdfFileIntent(filePath);
                } else if (suffix.equals(".doc") || suffix.equals(".docx")) {
                    intent1 = getWordFileIntent(filePath);
                } else if (suffix.equals(".xls") || suffix.equals(".xlsx")) {
                    intent1 = getExcelFileIntent(filePath, con);
                } else if (suffix.equals(".mp3") || suffix.equals(".wma") || suffix.equals(".mp4") || suffix.equals(".wav")) {
                    intent1 = getAudioFileIntent(filePath);
                } else if (suffix.equals(".txt")) {
                    intent1 = getTextFileIntent(filePath, false);
                } else if (suffix.equals(".html")) {
                    intent1 = getHtmlFileIntent(filePath);
                } else if (suffix.equals(".ppt") || suffix.equals(".pptx")) {
                    intent1 = getPptFileIntent(filePath);
                } else if (suffix.equals(".rar") || suffix.equals(".zip")) {
                    intent1 = getRARFileIntent(filePath);
                }
                con.startActivity(intent1);
            } catch (Exception e) {
                ToastUtil.toastShort(con, con.getString(R.string.file_no_app_open));
            }
        }
    }

    /**
     * 寻找适合的程序直接打开文件
     *
     * @param con
     * @param path
     */
    public static void doOpenFile(Context con, String path) {
        doOpenFile(con, new File(path));
    }

    /**
     * 发送文件
     *
     * @param context
     * @param file
     */
    public static void sendFileByOtherApp(Context context, File file) {
        if (file.exists()) {
            String type = getMIMEType(file);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType(type);
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.send)));
        }
    }


    /**
     * 打开工程assets目录下的音乐文件
     *
     * @param con
     * @param filename
     * @return
     */
    public static void playAssetsMusic(Context con, String filename) throws IOException {
        MediaPlayer m = new MediaPlayer();
        AssetFileDescriptor descriptor = con.getAssets().openFd(
                filename);
        m.setDataSource(descriptor.getFileDescriptor(),
                descriptor.getStartOffset(), descriptor.getLength());
        descriptor.close();
        m.prepare();
        m.start();
    }

    /**
     * android获取一个用于打开HTML文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * android获取一个用于打开图片文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * android获取一个用于打开PDF文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * android获取一个用于打开文本文件的intent
     *
     * @param param
     * @param paramBoolean
     * @return
     */
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    /**
     * android获取一个用于打开音频文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * android获取一个用于打开视频文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * android获取一个用于打开CHM文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * android获取一个用于打开Word文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * android获取一个用于打开Excel文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getExcelFileIntent(String param, Context con) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * android获取一个用于打开PPT文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /**
     * android获取一个用于打开rar文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getRARFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-rar-compressed");
        intent.setAction("com.asrazpaid");
        return intent;
    }

    /**
     * 根据文件类型获取文件图标
     *
     * @param filename
     * @return
     */
    public static Bitmap getImgByFileName(Context context, String filename) {
        String dotName = "default";
        if (!TextUtils.isEmpty(filename) && filename.contains(".")) {
            dotName = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        }
        for (String type : FileType) {
            if (dotName.startsWith(type)) {
                dotName = type;
            }
        }
        if (dotName.equals("rar") || dotName.equals("7z")) {
            dotName = "zip";
        }
        if (dotName.startsWith("doc")) {
            dotName = "word";
        }
        Bitmap icon = null;
        try {
            InputStream in = context.getResources().getAssets().open("attachicon/img_" + dotName + ".png");
            icon = BitmapFactory.decodeStream(in);
            if (icon == null) {
                in = context.getResources().getAssets().open("attachicon/img_default.png");
                icon = BitmapFactory.decodeStream(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (icon == null) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_default);
            }
        }
        return icon;
    }

    /**
     * 获得某个文件夹下所有文件大小
     *
     * @param file 文件或者文件夹
     * @return 单位为b
     */
    public static long getDirSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children) {
                    size += getDirSize(f);
                }
                return size;
            } else {
                // 如果是文件则直接返回其大小
                return file.length();
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取文件大小
     *
     * @param fileSize 输入单位B
     * @return 输出带单位
     */
    public static String getFileSize(String fileSize) {
        if (fileSize == null) {
            return "";
        }
        Pattern pattern = Pattern.compile("[0-9]+.?[0-9]*");
        if (pattern.matcher(fileSize).matches()) {
            fileSize = formetFileSize(Long.parseLong(fileSize));
        }
        return fileSize;
    }

    /**
     * 计算文件大小
     *
     * @param fileS
     * @return
     */
    public static String formetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS == 0) {
            fileSizeString = "0B";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 扫描多媒体文件
     *
     * @param context
     * @param filename
     * @param listener
     */
    public static void scanImage(Context context, String filename, MediaScannerConnection.OnScanCompletedListener listener) {
        MediaScannerConnection.scanFile(context, new String[]{filename}, null, listener);
    }
}
