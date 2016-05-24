package com.lordcard.constant;

public class ErrorList {
	public final static String ERROR_RET = "error_ret"; // 返回的错误码（1000-1010）
	public final static String ERROR_DES = "error_des"; // 返回的错误信息

	public final static int ERROR_RET_VERIFY_FAILURE = 1000;
	public final static String ERROR_DES_VERIFY_FAILURE = "应用程序身份验证失败。";

	public final static int ERROR_RET_TOKEN_FAILURE = 1001;
	public final static String ERROR_DES_TOKEN_FAILURE = "Token信息错误或已过期。";

	public final static int ERROR_RET_INTERFACE_FAILURE = 1002;
	public final static String ERROR_DES_INTERFACE_FAILURE = "接口失败。";

	public final static int ERROR_RET_PAY_ORDER_FAILURE = 1003;
	public final static String ERROR_DES_PAY_ORDER_FAILURE = "充值订单提交失败。";

	public final static int ERROR_RET_CANCEL_FAILURE = 1004;
	public final static String ERROR_DES_CANCEL_FAILURE = "用户取消。";

	public final static int ERROR_RET_PAY_FAILURE = 1005;
	public final static String ERROR_DES_PAY_FAILURE = "支付失败。";

	public final static int ERROR_RET_CONVERT_FIRST = 1006;
	public final static String ERROR_DES_CONVERT_FIRST = "json转换异常。";

	public final static int ERROR_RET_LOGIN_FIRST = 1005;
	public final static String ERROR_DES_LOGIN_FIRST = "请先登录。";

	public final static int ERROR_RET_APPID_MISMATCHING = 1006;
	public final static String ERROR_DES_APPID_MISMATCHING = "appId和密钥不匹配";

	public final static int ERROR_RET_ILLEGAL_DATA = 1007;
	public final static String ERROR_DES_ILLEGAL_DATA = "非法数据";

	public final static int ERROR_RET_SYSTEM_EXCEPTION = 1008;
	public final static String ERROR_DES_SYSTEM_EXCEPTION = "后台系统异常";

	public final static int ERROR_RET_OVERFLOW_EXCEPTION = 1009;
	public final static String ERROR_DES_OVERFLOW_EXCEPTION = "数据溢出";

	public final static int ERROR_RET_UPPERLIMIT_EXCEPTION = 1010;
	public final static String ERROR_UPPERLIMIT_EXCEPTION = "此次操作将使用户资产超过上限";

	public final static String ERROR_FileUrl = "ERROR_FileUrl"; // 返回的错误码（1000-1010）
	public final static String ERROR_FileSize = "ERROR_FileSize";
}
