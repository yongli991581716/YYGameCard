package com.lordcard.network.http;

public interface HttpCallback {
	/**
	 * 成功后回调
	 * 
	 * @param obj
	 */
	public void onSucceed(Object... obj);

	/**
	 * 失败后回调
	 * 
	 * @param obj
	 */
	public void onFailed(Object... obj);

}