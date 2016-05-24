package com.lordcard.common.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.lordcard.common.exception.CrashApplication;
import com.lordcard.common.util.ActivityUtils;
import com.lordcard.constant.Constant;

public class DownloadUtils {

	static int downFileSize = 0;
	static long fileSize = -1; // 文件大小
	static int progress = 0;

	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
		return Drawable.createFromStream(ins, null);
	}

	/**
	 * 通过输入url得到图片字节数组
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static byte[] getImage(String path) throws Exception {
		ByteArrayOutputStream outStream = null;
		InputStream inStream = null;
		byte[] result = null;
		try {
//		URL url = new URL(HttpUtils.replaceUrl(path));
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			inStream = conn.getInputStream();// 通过输入流获取图片数据
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024 * 6];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			result = outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (outStream != null) {
				outStream.close();
			}
		}
		return result;
	}

	public static byte[] ReadText(Context context, String fileName, String path) throws Exception {
		FileInputStream in = null;
		ByteArrayOutputStream out = null;
		byte[] result = null;
		try {
			in = context.openFileInput(fileName);
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024 * 6];
			int len = 0;
			try {
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			result = out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return result;
	}

	public static void WriteText(Context context, String fileName, byte[] data) {
		FileOutputStream fOut = null;
		try {
			fOut = context.openFileOutput(fileName, 0);
			fOut.write(data);
			fOut.flush();
			// System.out.println("save");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fOut) {
				try {
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static byte[] downWrite(String path, Context context, String fileName) throws Exception {
		WriteText(context, fileName, getImage(path));
		return getImage(path);
	}

	@SuppressLint("WorldReadableFiles")
	public static File downAPK(final String url) {
		File file = null;
		String apkName = Constant.APKNAME;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		FileOutputStream fileOutputStream = null;
		InputStream is = null;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			fileSize = entity.getContentLength();
			is = entity.getContent();
			if (is != null) {
				boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
				if (sdCardExist) { // 下载到sd卡
					file = new File(Environment.getExternalStorageDirectory(), apkName);
					if (file.exists()) {
						file.delete();
						downFileSize = 0;//防止超过200%情况
					}
					ActivityUtils.createFile(file);
					fileOutputStream = new FileOutputStream(file);
				} else { // 下载到手机内存
					file = CrashApplication.getInstance().getFileStreamPath(apkName);
					if (file.exists()) {
						file.delete();
						downFileSize = 0;//防止超过200%情况
					}
					fileOutputStream = CrashApplication.getInstance().openFileOutput(apkName, Context.MODE_WORLD_READABLE);
				}
				int tempProgress = -1;
				byte[] buf = new byte[1024];
				int ch = 0;
				while ((ch = is.read(buf)) != -1) {
					downFileSize = downFileSize + ch;
					// 下载进度
					progress = (int) (downFileSize * 100.0 / fileSize);
					fileOutputStream.write(buf, 0, ch);
					if (downFileSize == fileSize) {
						// 下载完成
					} else if (tempProgress != progress) {}
				}
			}
			fileOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != fileOutputStream) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static void downLoadApk(final String url) {
		new Thread() {

			public void run() {
				File file = downAPK(url); // 文件下载
				Intent openIntent = ActivityUtils.getInstallIntent(file);
				openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				CrashApplication.getInstance().startActivity(openIntent);
			};
		}.start();
	}
}
