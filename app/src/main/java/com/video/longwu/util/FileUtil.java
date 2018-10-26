package com.video.longwu.util;

import android.content.Intent;
import android.net.Uri;

import com.video.longwu.BaseApplication;
import com.video.longwu.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    // sdcard存储基本路径
    private final static String BASE_PATH = new StringBuffer(SDCardUtil.getExternalFilePath() + "/").append(BaseApplication.getContext().getResources().getString(R.string.app_name)).toString();

    public final static String IMAGE_PATH = new StringBuffer(BASE_PATH).append("/images").toString();

    static {
        initFilePath();
    }

    /**
     * 初始化文件保存路径
     */
    private static void initFilePath() {
        if (SDCardUtil.isSDCardEnable()) {// 如果sdcard可用
            createDir(IMAGE_PATH);
        }
    }

    private static void createDir(String imagePath) {
        File dir = new File(imagePath);
        if (!dir.exists())
            dir.mkdirs();
    }

    public static void saveJPG(byte[] data, String fileName) {
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File file = new File(IMAGE_PATH, fileName);
            fileOutputStream = new FileOutputStream(file);

            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(data, 0, data.length);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                    bufferedOutputStream = null;
                }

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    fileOutputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
