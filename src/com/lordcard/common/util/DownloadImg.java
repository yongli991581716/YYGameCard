package com.lordcard.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public class DownloadImg {

	public static File download(Context context, String imageUrl) {
//		Bitmap bitmap = null;
		if (imageUrl == null)
			return null;
//		String imagePath = "";
		String fileName = "";
		if (imageUrl != null && imageUrl.length() != 0) {
			try {
				fileName = imageUrl;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		imagePath = context.getCacheDir() + "/" + fileName;
		File file = new File(context.getCacheDir(), fileName);
		if (!file.exists() && !file.isDirectory()) {
			try {
				byte[] data = readInputStream(getRequest(imageUrl));
				saveFileFromBytes(data, file);
			} catch (Exception e) {}
			return file;
		} else {
			return file;
		}
	}

	public static InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000); //
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		byte[] bytes = outSteam.toByteArray();
		outSteam.close();
		inStream.close();
		return bytes;
	}

	public static void saveFileFromBytes(byte[] b, File outputFile) throws Exception {
		BufferedOutputStream stream = null;
		FileOutputStream fstream = new FileOutputStream(outputFile);
		stream = new BufferedOutputStream(fstream);
		stream.write(b);
		stream.close();
	}
}
