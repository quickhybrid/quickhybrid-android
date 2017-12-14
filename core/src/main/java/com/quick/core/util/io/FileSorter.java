package com.quick.core.util.io;

import java.io.File;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by dailichun on 2017/12/7.
 * 文件排序
 */
public class FileSorter implements Comparator<File> {

    /**
     * 默认排序方式，按目录，文件排序
     * TYPE_DIR
     */
    public static final int TYPE_DEFAULT = -1;

    /**
     * 按照修改时间，降序
     */
    public static final int TYPE_MODIFIED_DATE_DOWN = 1;

    /**
     * 按照修改时间，升序
     */
    public static final int TYPE_MODIFIED_DATE_UP = 2;

    /**
     * 按文件大小，降序
     */
    public static final int TYPE_SIZE_DOWN = 3;

    /**
     * 按文件大小，升序
     */
    public static final int TYPE_SIZE_UP = 4;

    /**
     * 按文件名
     */
    public static final int TYPE_NAME = 5;

    /**
     * 按目录，文件排序
     */
    public static final int TYPE_DIR = 7;

    private int mType = -1;

    public FileSorter(int type) {
        if (type < 0 || type > 7) {
            type = TYPE_DIR;
        }
        mType = type;
    }

    @Override
    public int compare(File object1, File object2) {
        int result = 0;
        switch (mType) {
            case TYPE_MODIFIED_DATE_DOWN:
                result = compareByModifiedDateDown(object1, object2);
                break;
            case TYPE_MODIFIED_DATE_UP:
                result = compareByModifiedDateUp(object1, object2);
                break;
            case TYPE_SIZE_DOWN:
                result = compareBySizeDown(object1, object2);
                break;
            case TYPE_SIZE_UP:
                result = compareBySizeUp(object1, object2);
                break;
            case TYPE_NAME:
                result = compareByName(object1, object2);
            case TYPE_DIR:
                result = compareByDir(object1, object2);
                break;
            default:
                result = compareByDir(object1, object2);
                break;
        }
        return result;
    }

    /**
     * @param object1
     * @param object2
     * @return
     */
    private int compareByDir(File object1, File object2) {
        if (object1.isDirectory() && object2.isFile()) {
            return -1;
        } else if (object1.isDirectory() && object2.isDirectory()) {
            return compareByName(object1, object2);
        } else if (object1.isFile() && object2.isDirectory()) {
            return 1;
        } else {
            return compare(object1, object2);
        }
    }

    /**
     * @param object1
     * @param object2
     * @return
     */
    private int compareByName(File object1, File object2) {
        Comparator<Object> cmp = Collator.getInstance(java.util.Locale.CHINA);
        return cmp.compare(object1.getName(), object2.getName());
    }

    /**
     * @param object1
     * @param object2
     * @return
     */
    private int compareBySizeUp(File object1, File object2) {
        if (object1.isDirectory() && object2.isDirectory()) {
            return 0;
        }
        if (object1.isDirectory() && object2.isFile()) {
            return -1;
        }
        if (object1.isFile() && object2.isDirectory()) {
            return 1;
        }
        long s1 = object1.length();
        long s2 = object2.length();
        if (s1 == s2) {
            return 0;
        } else {
            return s1 > s2 ? 1 : -1;
        }
    }

    /**
     * @param object1
     * @param object2
     * @return
     */
    private int compareBySizeDown(File object1, File object2) {
        if (object1.isDirectory() && object2.isDirectory()) {
            return 0;
        }
        if (object1.isDirectory() && object2.isFile()) {
            return -1;
        }
        if (object1.isFile() && object2.isDirectory()) {
            return 1;
        }
        long s1 = object1.length();
        long s2 = object2.length();
        if (s1 == s2) {
            return 0;
        } else {
            return s1 < s2 ? 1 : -1;
        }
    }

    private int compareByModifiedDateUp(File object1, File object2) {
        long d1 = object1.lastModified();
        long d2 = object2.lastModified();
        if (d1 == d2) {
            return 0;
        } else {
            return d1 > d2 ? 1 : -1;
        }
    }

    private int compareByModifiedDateDown(File object1, File object2) {
        long d1 = object1.lastModified();
        long d2 = object2.lastModified();
        if (d1 == d2) {
            return 0;
        } else {
            return d1 < d2 ? 1 : -1;
        }
    }

    /**
     * 按名称排序
     *
     * @param path
     * @return
     */
    public static List<File> sortByName(String path) {
        System.out.println(path);
        List<File> files = Arrays.asList(new File(path).listFiles());
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        return files;
    }
}
