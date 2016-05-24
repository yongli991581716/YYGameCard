package com.lordcard.network.task;

import com.ylly.playcard.R;
import android.content.Context;

import com.lordcard.common.exception.CrashApplication;
import com.lordcard.common.task.GenericTask;
import com.lordcard.common.task.base.TaskParams;
import com.lordcard.common.task.base.TaskResult;
import com.lordcard.common.util.DialogUtils;
import com.lordcard.constant.CacheKey;
import com.lordcard.entity.GameUser;
import com.lordcard.network.cmdmgr.CmdUtils;
import com.lordcard.network.http.GameCache;

/**请求排名记录线程
 * @author Administrator
 */
public class GetRankTask extends GenericTask {
	Context context=CrashApplication.getInstance();
	protected TaskResult _doInBackground(TaskParams... params) {
		try {
			GameUser gu = (GameUser) GameCache.getObj(CacheKey.GAME_USER);
			CmdUtils.sendGetRankCmd(gu.getLoginToken());
			return TaskResult.OK;
		} catch (Exception e) {
			DialogUtils.mesTip(context.getString(R.string.link_server_fail), true);
			return TaskResult.FAILED;
		}
	}
}
