package com.quick.jsbridge.control;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;


import com.quick.core.baseapp.component.FileChooseActivity;
import com.quick.core.ui.widget.dialog.ActionSheet;
import com.quick.core.util.device.PhotoSelector;
import com.quick.jsbridge.view.IQuickFragment;
import com.quick.jsbridge.view.webview.IFileChooser;

import java.io.File;

import quick.com.jsbridge.R;

import static android.app.Activity.RESULT_OK;


/**
 * Created by dailichun on 2017/12/7.
 * quick容器中input=file控件的默认实现
 */
public class FileChooser implements IFileChooser {

    private String imageTitle;

    private String cameraTtile;

    private String fileTitle;

    /**
     * 请求拍照的requestCode
     */
    private final static int CAMERA_REQUEST_CODE = 0x2001;
    /**
     * 选择相册的requestCode
     */
    private final static int IMAGE_REQUEST_CODE = 0x2002;
    /**
     * 选择文件的requestCode
     */
    private final static int FILE_REQUEST_CODE = 0x2003;

    /**
     * file控件回调对象
     */
    private ValueCallback filePathCallback;

    /**
     * file控件回调对象
     */
    private ValueCallback<Uri[]> filePathCallbacks;

    /**
     * 文件选择器
     */
    private PhotoSelector photoSelector;

    private IQuickFragment fragment;

    public FileChooser(IQuickFragment fragment) {
        this.fragment = fragment;
        photoSelector = new PhotoSelector();
        imageTitle = fragment.getPageControl().getContext().getString(R.string.album);
        cameraTtile = fragment.getPageControl().getContext().getString(R.string.take_photo);
        fileTitle = fragment.getPageControl().getContext().getString(R.string.file_name);
    }

    @Override
    public void showFileChooser(ValueCallback uploadMsg, String acceptType) {
        setFilePathCallback(uploadMsg);
        dealOpenFileChooser(acceptType);
    }

    @Override
    public void showFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        setFilePathCallback(uploadMsg);
        dealOpenFileChooser(acceptType);
    }

    @Override
    public void showFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        setFilePathCallbacks(filePathCallback);
        String[] acceptTypes = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            acceptTypes = fileChooserParams.getAcceptTypes();
        }
        dealOpenFileChooser(acceptTypes.length > 0 ? acceptTypes[0] : "");
    }

    public void setFilePathCallback(ValueCallback filePathCallback) {
        this.filePathCallback = filePathCallback;
    }

    public void setFilePathCallbacks(ValueCallback<Uri[]> filePathCallbacks) {
        this.filePathCallbacks = filePathCallbacks;
    }

    /**
     * 监听到选择文件操作后弹出选择项
     *
     * @param acceptType 操作类型,格式 [type]/*
     *                   image:单独相册
     *                   camera:单独拍照
     *                   file:单独文件
     *                   3个类型可任意搭配，多个用_隔开，例如 image_camera/*
     *                   *:所有 跟 image_camera_file/* 含义一致
     *                   其他格式自动转化为image_camera/*
     */
    public void dealOpenFileChooser(String acceptType) {
        String ACCEPT_IMAGE = "image";
        String ACCEPT_CAMERA = "camera";
        String ACCEPT_FILE = "file";
        if (TextUtils.isEmpty(acceptType)) {
            acceptType = ACCEPT_IMAGE + "_" + ACCEPT_CAMERA;
        } else if ("*/*".equals(acceptType)) {
            acceptType = ACCEPT_IMAGE + "_" + ACCEPT_CAMERA + "_" + ACCEPT_FILE;
        } else {
            acceptType = acceptType.split("/")[0];
        }
        acceptType = acceptType.replace(ACCEPT_IMAGE, imageTitle);
        acceptType = acceptType.replace(ACCEPT_CAMERA, cameraTtile);
        acceptType = acceptType.replace(ACCEPT_FILE, fileTitle);
        String[] items = acceptType.split("_");
        if (items.length == 1) {
            if (cameraTtile.equals(items[0])) {
                photoSelector.requestSysCamera(fragment.getPageControl().getFragment(), CAMERA_REQUEST_CODE);

            } else if (imageTitle.equals(items[0])) {
                photoSelector.requestPhotoPick(fragment.getPageControl().getFragment(), IMAGE_REQUEST_CODE);

            } else if (fileTitle.equals(items[0])) {
                FileChooseActivity.goFileChooseActivity(fragment.getPageControl().getFragment(), FILE_REQUEST_CODE);
            } else {
                showMenuItem(items);
            }
        } else {
            showMenuItem(items);
        }
    }

    /**
     * 将选择的数据回调给file控件
     *
     * @param results 回调数据
     */
    public void filePathCallback(Uri[] results) {
        if (filePathCallbacks != null) {
            filePathCallbacks.onReceiveValue(results);
            filePathCallbacks = null;
        }
        if (filePathCallback != null) {
            filePathCallback.onReceiveValue(results == null ? null : results[0]);
            filePathCallback = null;
        }
    }

    @Override
    public void onChooseFileResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                //系统拍照
                photoSelector.handleCamera(new PhotoSelector.CompressResult() {
                    @Override
                    public void onCompelete(String path) {
                        filePathCallback(file2Uri(path));
                    }
                });

            } else if (requestCode == IMAGE_REQUEST_CODE) {
                //选择相册
                photoSelector.handlePick(fragment.getPageControl().getActivity(), data, new PhotoSelector.CompressResult() {
                    @Override
                    public void onCompelete(String path) {
                        filePathCallback(file2Uri(path));
                    }
                });

            } else if (requestCode == FILE_REQUEST_CODE) {
                //选择文件
                String filePath = data.getStringExtra(WebloaderControl.RESULT_DATA);
                filePathCallback(file2Uri(filePath));
            }

        } else {
            filePathCallback(null);
        }
    }

    /**
     * 显示底部弹出菜单
     *
     * @param items
     */
    public void showMenuItem(String[] items) {
        if (items == null || TextUtils.isEmpty(items[0])) {
            items = new String[]{cameraTtile, imageTitle};
        }
        final ActionSheet menuView = new ActionSheet(fragment.getPageControl().getActivity());
        menuView.setCancelButtonTitle(fragment.getPageControl().getContext().getString(R.string.cancel));
        menuView.addItems(items);
        menuView.setItemClickListener(new ActionSheet.MenusItemClickListener() {
            @Override
            public void onItemClick(int index, View btn) {
                String title = ((Button) btn).getText().toString();
                if (imageTitle.equals(title)) {
                    photoSelector.requestPhotoPick(fragment, IMAGE_REQUEST_CODE);

                } else if (cameraTtile.equals(title)) {
                    photoSelector.requestSysCamera(fragment, CAMERA_REQUEST_CODE);

                } else if (fileTitle.equals(title)) {
                    FileChooseActivity.goFileChooseActivity(fragment, FILE_REQUEST_CODE);
                }
            }
        });
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (menuView.getIsCancle()) {
                    filePathCallback(null);
                }
            }
        });
        menuView.showMenu();
    }

    /**
     * 文件路径转为Uri
     *
     * @param path 文件本地路径
     * @return file开头的uri
     */
    private Uri[] file2Uri(String path) {
        Uri[] uri = null;
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                if (file.length() > 50 * 1024 * 1024) {
                    fragment.getPageControl().toast(fragment.getPageControl().getContext().getString(R.string.file_too_large));
                } else {
                    uri = new Uri[]{Uri.fromFile(file)};
                }
            } else {
                fragment.getPageControl().toast(fragment.getPageControl().getContext().getString(R.string.file_not_found));
            }
        }
        return uri;
    }
}
