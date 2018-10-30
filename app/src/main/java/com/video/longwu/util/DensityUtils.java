/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.video.longwu.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;


public final class DensityUtils {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px( float dpValue) {
		return Math.round((Resources.getSystem().getDisplayMetrics().density * dpValue) + 0.5f);

	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dp(float pxValue) {
		return Math.round((pxValue / Resources.getSystem().getDisplayMetrics().density) + 0.5f);

	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int px2sp( float pxValue) {
		return Math.round((pxValue / Resources.getSystem().getDisplayMetrics().scaledDensity) + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px
	 */
	public static int sp2px( float spValue) {
		return Math.round((Resources.getSystem().getDisplayMetrics().scaledDensity * spValue) + 0.5f);
	}

	/**
	 * 获取dialog宽度
	 */
	public static int getDialogW(Activity aty) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int w = dm.widthPixels - 100;
		// int w = aty.getWindowManager().getDefaultDisplay().getWidth() - 100;
		return w;
	}
}