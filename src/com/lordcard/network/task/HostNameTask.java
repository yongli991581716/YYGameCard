/**
 * HostNameTask.java [v 1.0.0]
 * classes : com.lordcard.common.task.HostNameTask
 * auth : yinhongbiao
 * time : 2013 2013-4-28 下午1:53:26
 */
package com.lordcard.network.task;

import java.util.TimerTask;

/**
 * com.lordcard.common.task.HostNameTask
 * 
 * @author Administrator <br/>
 *         create at 2013 2013-4-28 下午1:53:26
 */
public class HostNameTask extends TimerTask {

	public void run() {
		try {
			//			java.security.Security.setProperty("networkaddress.cache.ttl", "15");
			//			for (String key : HttpURL.HOST_MAP.keySet()) {
			//				String host = HttpURL.HOST_MAP.get(key);
			//
			//				// 修改缓存数据结束
			//				InetAddress address = InetAddress.getByName(host);
			//				if (address != null) {
			//					String ip = address.getHostAddress();
			//					if (!TextUtils.isEmpty(ip)) {
			//						HttpURL.HOST_CACHE_MAP.put(key, ip);
			//						continue;
			//					}
			//				}
			//				HttpURL.HOST_CACHE_MAP.put(key, host);
			//			}
		} catch (Exception e) {
		}
	}
}
