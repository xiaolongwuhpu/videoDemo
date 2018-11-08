package com.video.longwu.util;

import android.content.Context;
import android.util.Log;

import com.video.longwu.BaseApplication;
import com.video.longwu.R;
import com.video.longwu.callback.SavePictureListener;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    static String TAG = "FileUtil";
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

    public static void saveJPG(byte[] data, String fileName, SavePictureListener listener) {

        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File file = new File(IMAGE_PATH, fileName);
            fileOutputStream = new FileOutputStream(file);

            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(data, 0, data.length);
            bufferedOutputStream.flush();
            listener.onSuccess();
        } catch (Exception e) {
            listener.onFailed("保存失败");
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
                listener.onFailed("关闭失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 按行读取数据
     *
     * @param strFilePath
     * @return
     */
    public static List<String> ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        List<String> newList = null;
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d(TAG, "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                newList = ReadAssetsTxt(instream);
            } catch (java.io.FileNotFoundException e) {
                Log.d(TAG, "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
        }
        return newList;
    }

    public static List<String> ReadAssetsTxt(InputStream instream) throws IOException {
        List<String> list = new ArrayList<>();
        if (instream != null) {
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            //分行读取
            while ((line = buffreader.readLine()) != null) {
                list.add(line + "\n");
            }
            instream.close();
        }
        return list;
    }

    /**
     * openFileInput 用于指定文件名称，不能包含路径分隔符“/”
     *
     * @param filePath "text.txt"指定文件名
     */
    public void readFileOnLine(Context context, String filePath) {//输入文件路径
        try {
            FileInputStream fis = context.openFileInput(filePath);//打开文件输入流
            StringBuffer sBuffer = new StringBuffer();
            DataInputStream dataIO = new DataInputStream(fis);//读取文件数据流
            String strLine = null;
            while ((strLine = dataIO.readLine()) != null) {//通过readline按行读取
                sBuffer.append(strLine + "\n");//strLine就是一行的内容
            }
            dataIO.close();
            fis.close();
        } catch (IOException e) {
        }
    }

}
