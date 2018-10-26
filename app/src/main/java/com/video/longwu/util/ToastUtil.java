package com.video.longwu.util;

import android.widget.Toast;

import com.video.longwu.BaseApplication;
/**
 * 显示toast信息
 * @author Luke
 */
public final class ToastUtil {

	private static Toast mToast = null;

	private ToastUtil() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void showShort(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(BaseApplication.getContext(), resId, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(resId);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public static void showShort(String message) {
		if (mToast == null) {
			mToast = Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(message);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public static void showLong(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(BaseApplication.getContext(), resId, Toast.LENGTH_LONG);
		} else {
			mToast.setText(resId);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.show();
	}

	public static void showLong(String message) {
		if (mToast == null) {
			mToast = Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_LONG);
		} else {
			mToast.setText(message);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.show();
	}


}