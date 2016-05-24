package com.lordcard.network.socket;
import java.io.*;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class HURLEncoder {

	 public static String readUTF(String s){
		 if(s == null || s.equals(""))return s;
	     StringBuffer strBuf = new StringBuffer();
	     int lenFlag = s.indexOf("|");
	     if(lenFlag<0 || lenFlag>=s.length())return s;
	     int len = Integer.parseInt(s.substring(0,lenFlag));
	     s = s.substring(lenFlag+1);
	     byte b[] = new byte[len];
	     int index = 0;
	     for(int i = 0; i < s.length (); i++) {
		 char c = s.charAt(i);
		 if(c != '*') {
		     strBuf.append(c);
		 }else{
		     b[index] = (byte) (Integer.parseInt (strBuf.toString ()));
		     strBuf.delete(0,strBuf.length());
		     index++;
		 }
	     }
	    try {
		return new String (b, "UTF-8");
	    }
	    catch(UnsupportedEncodingException ex) {
		return null;
	    }
	 }
	 public static String encode(String s) {
	  int len = 0;
	  boolean wroteUnencodedChar = false;

	  StringBuffer writer = new StringBuffer();

	  StringBuffer out = new StringBuffer(s.length());

	  for (int i = 0; i < s.length(); i++) {
	   char c = s.charAt(i);
	    try {
	     if (wroteUnencodedChar) {
	      writer = new StringBuffer();
	      wroteUnencodedChar = false;
	     }

	     writer.append(c);

	     if (c >= 0xD800 && c <= 0xDBFF) {
	      if ((i + 1) < s.length()) {
	       int d = (int) (s.charAt(i + 1));

	       if (d >= 0xDC00 && d <= 0xDFFF) {
		writer.append(d);
		i++;
	       }
	      }
	     }

	    } catch (Exception e) {
	     writer = new StringBuffer();
	     continue;
	    }

	    String str = writer.toString();

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    DataOutputStream dos = new DataOutputStream(baos);
	    try {
	     dos.writeUTF(str);
	     dos.flush();
	    } catch (Exception e) {
	     e.printStackTrace();
	    }

	    byte[] temp = baos.toByteArray();

	    byte[] ba = new byte[temp.length - 2];
	    for (int ix = 0; ix < ba.length; ix++) {
	     ba[ix] = temp[ix + 2];
	    }

	    for (int j = 0; j < ba.length; j++) {
		out.append(ba[j]);
	     out.append('*');
	     len++;
	    }
	    writer = new StringBuffer();
	    try {
	     dos.close();
	     baos.close();
	    } catch (Exception e) {
	     e.printStackTrace();
	    }
	//   }
	  }

	  return len+"|"+out.toString();
	 }
	 /** 字符串默认键值     */
	 private static String strDefaultKey = "national";

	 /** 加密工具     */
	 private static Cipher encryptCipher = null;

	 /** 解密工具     */
	 private static Cipher decryptCipher = null;

	 /**  
	  * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]  
	  * hexStr2ByteArr(String strIn) 互为可逆的转换过程  
	  *   
	  * @param arrB  
	  *            需要转换的byte数组  
	  * @return 转换后的字符串  
	  * @throws Exception  
	  *             本方法不处理任何异常，所有异常全部抛出  
	  */
	 public static String byteArr2HexStr(byte[] arrB) throws Exception {
	   int iLen = arrB.length;
	   // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍   
	   StringBuffer sb = new StringBuffer(iLen * 2);
	   for (int i = 0; i < iLen; i++) {
	     int intTmp = arrB[i];
	     // 把负数转换为正数   
	     while (intTmp < 0) {
	       intTmp = intTmp + 256;
	     }
	     // 小于0F的数需要在前面补0   
	     if (intTmp < 16) {
	       sb.append("0");
	     }
	     sb.append(Integer.toString(intTmp, 16));
	   }
	   return sb.toString();
	 }

	 /**  
	  * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)  
	  * 互为可逆的转换过程  
	  *   
	  * @param strIn  
	  *            需要转换的字符串  
	  * @return 转换后的byte数组  
	  * @throws Exception  
	  *             本方法不处理任何异常，所有异常全部抛出  
	  * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>  
	  */
	 public static byte[] hexStr2ByteArr(String strIn) throws Exception {
	   byte[] arrB = strIn.getBytes();
	   int iLen = arrB.length;

	   // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2   
	   byte[] arrOut = new byte[iLen / 2];
	   for (int i = 0; i < iLen; i = i + 2) {
	     String strTmp = new String(arrB, i, 2);
	     arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
	   }
	   return arrOut;
	 }


	 /**  
	  * 指定密钥构造方法  
	  *   
	  * @param strKey  
	  *            指定的密钥  
	  * @throws Exception  
	  */
	 static String strKey = "zjlinegame";
	 static boolean noInit = true;
	 public static void init() throws Exception {

	   Key key = getKey(strKey.getBytes());

	   encryptCipher = Cipher.getInstance("DES");
	   encryptCipher.init(Cipher.ENCRYPT_MODE, key);

	   decryptCipher = Cipher.getInstance("DES");
	   decryptCipher.init(Cipher.DECRYPT_MODE, key);
	 }

	 /**  
	  * 加密字节数组  
	  *   
	  * @param arrB  
	  *            需加密的字节数组  
	  * @return 加密后的字节数组  
	  * @throws Exception  
	  */
		public static byte[] encrypt(byte[] arrB) {
			byte data[] = null;
			try {
				data = encryptCipher.doFinal(arrB);
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
		}

	 /**  
	  * 加密字符串  
	  *   
	  * @param strIn  
	  *            需加密的字符串  
	  * @return 加密后的字符串  
	  * @throws Exception  
	  */
		public static String encrypt(String strIn) {
			if(noInit)
			{
				noInit = false;
				try {
					init();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String res = "";
			try {
				res = byteArr2HexStr(encrypt(strIn.getBytes()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return res;
		}

	 /**  
	  * 解密字节数组  
	  *   
	  * @param arrB  
	  *            需解密的字节数组  
	  * @return 解密后的字节数组  
	  * @throws Exception  
	  */
	 public static byte[] decrypt(byte[] arrB) throws Exception {
	   return decryptCipher.doFinal(arrB);
	 }

	 /**  
	  * 解密字符串  
	  *   
	  * @param strIn  
	  *            需解密的字符串  
	  * @return 解密后的字符串  
	  * @throws Exception  
	  */
	 public static String decrypt(String strIn) throws Exception {
		if (noInit) {
			noInit = false;
			try {
				init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	   return new String(decrypt(hexStr2ByteArr(strIn)),"UTF-8");
	 }

	 /**  
	  * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位  
	  *   
	  * @param arrBTmp  
	  *            构成该字符串的字节数组  
	  * @return 生成的密钥  
	  * @throws java.lang.Exception  
	  */
	 private static Key getKey(byte[] arrBTmp) throws Exception {
	   // 创建一个空的8位字节数组（默认值为0）   
	   byte[] arrB = new byte[8];

	   // 将原始字节数组转换为8位   
	   for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
	     arrB[i] = arrBTmp[i];
	   }

	   // 生成密钥   
	   Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

	   return key;
	 }
	}
