/**
 * ICallback.java [v 1.0.0]
 * classes : interfaces.ICallback
 * auth : yinhongbiao
 * time : 2012 2012-11-22 下午2:49:36
 */
package com.lordcard.network.socket;

import com.lordcard.network.cmdmgr.CmdDetail;

/**
 * interfaces.ICallback
 * 
 * @author yinhb <br/>
 *         create at 2012 2012-11-22 下午2:49:36
 */
public interface ICallback {
	
	public void messageHandler(CmdDetail cmdDetail);	//消息接收凸显
}
