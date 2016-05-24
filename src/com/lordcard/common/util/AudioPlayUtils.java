package com.lordcard.common.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.util.Log;

import com.lordcard.common.exception.CrashApplication;
import com.ylly.playcard.R;

import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class AudioPlayUtils {

	private static String TAG = "MediaPlayerUtils";

	private static AudioPlayUtils mediaPlayerUtils;

	public static boolean isRestartGame = false; // 新游戏

	private MediaPlayer mediaPlayer;
	private MediaPlayer mediaPlayer2;
	private SoundPool soundPool;
	// private HashMap<Integer, Integer> soundPoolMap; //播放列表
	public static boolean isPlay = true; // 是否播放
	public static boolean isGameEnd = false; // 是否是结束
	private static boolean BgisPlaying = false; // 是否正在播放
	private int lotIngId, lotEndId;
	private static AudioManager audioManager;
	// private static int currentVol, maxVol;
	private static Context context;
	private HashMap<Integer, Integer> soundPoolMap = null;
	private int soundId[] = new int[] {
			// 男声
			R.raw.nan_bomb, R.raw.nan_bujiao, R.raw.nan_1fen, R.raw.nan_2fen,
			R.raw.nan_3fen, R.raw.nan_pass, R.raw.nan_3dai0, R.raw.nan_3dai1,
			R.raw.nan_3dai2, R.raw.nan_4dai2, R.raw.nan_4dai22,
			R.raw.nan_feiji,
			R.raw.nan_liandui,
			R.raw.nan_shunzi,
			R.raw.nan_wangzha,
			R.raw.nan_jiabei,
			R.raw.nan_bujiabei,
			// 女
			R.raw.nv_bomb, R.raw.nv_bujiao, R.raw.nv_1fen, R.raw.nv_2fen,
			R.raw.nv_3fen, R.raw.nv_pass, R.raw.nv_3dai0, R.raw.nv_3dai1,
			R.raw.nv_3dai2, R.raw.nv_4dai2, R.raw.nv_4dai22, R.raw.nv_feiji,
			R.raw.nv_liandui, R.raw.nv_shunzi, R.raw.nv_wangzha,
			R.raw.nv_jiabei, R.raw.nv_bujiabei,
			// 其他
			R.raw.start, R.raw.audio_warn, R.raw.boombeffect, R.raw.outcard,
			R.raw.warn, R.raw.win, R.raw.planeeffect,R.raw.get_glod,
			//单张 男
			R.raw.m_1_3,R.raw.m_1_4,R.raw.m_1_5,R.raw.m_1_6,
			R.raw.m_1_7,R.raw.m_1_8,R.raw.m_1_9,R.raw.m_1_10,
			R.raw.m_1_11,R.raw.m_1_12,R.raw.m_1_13,R.raw.m_1_14,
			R.raw.m_1_15,R.raw.m_1_16,R.raw.m_1_17,
			//单张女
			R.raw.w_1_3,R.raw.w_1_4,R.raw.w_1_5,R.raw.w_1_6,
			R.raw.w_1_7,R.raw.w_1_8,R.raw.w_1_9,R.raw.w_1_10,
			R.raw.w_1_11,R.raw.w_1_12,R.raw.w_1_13,R.raw.w_1_14,
			R.raw.w_1_15,R.raw.w_1_16,R.raw.w_1_17,
			//对牌男
			R.raw.m_2_3,R.raw.m_2_4,R.raw.m_2_5,R.raw.m_2_6,
			R.raw.m_2_7,R.raw.m_2_8,R.raw.m_2_9,R.raw.m_2_10,
			R.raw.m_2_11,R.raw.m_2_12,R.raw.m_2_13,R.raw.m_2_14,
			R.raw.m_2_15,
			//对牌女
			R.raw.w_2_3,R.raw.w_2_4,R.raw.w_2_5,R.raw.w_2_6,
			R.raw.w_2_7,R.raw.w_2_8,R.raw.w_2_9,R.raw.w_2_10,
			R.raw.w_2_11,R.raw.w_2_12,R.raw.w_2_13,R.raw.w_2_14,
			R.raw.w_2_15,
			//三张男
			R.raw.m_tuple3,R.raw.m_tuple4,R.raw.m_tuple5,R.raw.m_tuple6,
			R.raw.m_tuple7,R.raw.m_tuple8,R.raw.m_tuple9,R.raw.m_tuple10,
			R.raw.m_tuple11,R.raw.m_tuple12,R.raw.m_tuple13,R.raw.m_tuple14,
			R.raw.m_tuple15,
			//三张女
			R.raw.w_tuple3,R.raw.w_tuple4,R.raw.w_tuple5,R.raw.w_tuple6,
			R.raw.w_tuple7,R.raw.w_tuple8,R.raw.w_tuple9,R.raw.w_tuple10,
			R.raw.w_tuple11,R.raw.w_tuple12,R.raw.w_tuple13,R.raw.w_tuple14,
			R.raw.w_tuple15
			};

	public boolean isBgisPlaying() {
		return BgisPlaying;
	}

	private AudioPlayUtils() {
		this.mediaPlayer = new MediaPlayer();
		this.mediaPlayer2 = new MediaPlayer();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 100);
		lotIngId = soundPool.load(context, R.raw.lot_ing, 1);
		lotEndId = soundPool.load(context, R.raw.lot_end, 1);
		soundPoolMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < soundId.length; i++) {
			soundPoolMap
					.put(soundId[i], soundPool.load(context, soundId[i], 1));
		}
	}

	/**
	 * 获取 MediaPlayer实例
	 * 
	 * @return
	 */
	public synchronized static AudioPlayUtils getInstance() {

		if (mediaPlayerUtils == null) {
			context = CrashApplication.getInstance();
			audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			mediaPlayerUtils = new AudioPlayUtils();
		}
		return mediaPlayerUtils;
	}

	/**
	 * 播放音乐
	 * 
	 * @param resid
	 *            播放文件资源ID
	 * @param looping
	 *            是否循环播放
	 */
	public void playMusic(boolean looping, int resid) {
		try {
			if (!PreferenceHelper.getMyPreference().getSetting()
					.getBoolean("jingyin", false)) {
				Log.d(TAG, "播放音乐");
				if (isPlay) {
					mediaPlayer = MediaPlayer.create(context, resid);
					mediaPlayer.setLooping(looping);
					mediaPlayer.start();
					mediaPlayer
							.setOnCompletionListener(new OnCompletionListener() {
								public void onCompletion(MediaPlayer mp) {
									try {
										mediaPlayer.release();
									} catch (Exception e) {
										Log.d(TAG, "mediaPlayer2.release() 报错");
										e.printStackTrace();
									}
								}
							});
				}
			}
		} catch (Exception e) {
			Log.d(TAG, "播放音乐错误");
			e.printStackTrace();
		}
	}

	/**
	 * 播放音效
	 * 
	 * @param resId
	 */
	public void playSound(int resId) {
		try {
			if (!PreferenceHelper.getMyPreference().getSetting()
					.getBoolean("jingyin", false)) {
				if (AudioPlayUtils.isPlay) {
					AudioManager mgr = (AudioManager) context
							.getSystemService(Context.AUDIO_SERVICE);
					float streamVolumeCurrent = mgr
							.getStreamVolume(AudioManager.STREAM_MUSIC);
					float streamVolumeMax = mgr
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					float volume = streamVolumeCurrent / streamVolumeMax;
					//如果声音太小，把音效调高一点，要不然音效听不清楚
					if(0.0 < volume && 0.5 > volume){
						volume=volume+0.3f;
					}
					soundPool.play(soundPoolMap.get(resId), volume, volume, 1,
							0, 1f);
				}
			}
		} catch (Exception e) {
			Log.d(TAG, "播放音乐错误");
			e.printStackTrace();
		}
	}

	/**
	 * 播放bg音乐
	 * 
	 * @param resid
	 *            播放文件资源ID
	 * @param looping
	 *            是否循环播放
	 */
	public void playBgMusic(int Res) {
		try {
			if (!PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
				if (null != mediaPlayer2) {
					stopBgMusic();
					mediaPlayer2 = null;
				}
				mediaPlayer2 = MediaPlayer.create(context, Res);
				mediaPlayer2.setLooping(true);
				mediaPlayer2.start();
				mediaPlayer2.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						try {
							mediaPlayer2.release();
						} catch (Exception e) {
							Log.d(TAG, "mediaPlayer2.release() 报错");
							e.printStackTrace();
						}
					}
				});
				BgisPlaying = true;
			}
		} catch (Exception e) {
			Log.d(TAG, "播放音乐错误");
			e.printStackTrace();
		}
	}

	/**
	 * 播放多个文件
	 * 
	 * @param context
	 * @param looping
	 * @param resid
	 */
	private int playIndex = 1;
	public void playMultiMusic(final boolean looping, final Integer... resids) {
		try {
			if (!PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
				if (resids.length > 0) {
					playIndex = 1;
					playMusic(looping, resids[playIndex - 1]);
					if (resids.length > 1) { // 有多个则继续播放
						mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
							public void onCompletion(MediaPlayer mp) {
								playIndex++;
								if (playIndex <= resids.length) {
									playMusic(looping, resids[playIndex - 1]);
								} else {
									mediaPlayer.setOnCompletionListener(null);
								}
							}
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playMultiMusic2(final Integer... resids) {
		try {
			new Thread(){
				public void run() {
					if (!PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
						if (resids.length > 0) {
							playIndex = 1;
							playSound(resids[playIndex - 1]);
							if (resids.length > 1) { // 有多个则继续播放
								try {
									sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								playIndex++;
								if (playIndex <= resids.length) {
									playSound(resids[playIndex - 1]);
								} 
							}
						}
					}
				};
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止播放音乐
	 */
	public void stopMusic() {
		try {
			if (!isGameEnd) {
				mediaPlayer.stop();
				mediaPlayer.release();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 停止播放bg音乐
	 */
	public void stopBgMusic() {

		if (isRestartGame) {
			isRestartGame = false;
			return;
		}

		if (BgisPlaying) {
			try {
				mediaPlayer2.stop();
				mediaPlayer2.release();
				BgisPlaying = false;
			} catch (Exception e) {
				Log.d("eden", "----------停播音乐错误");
			}
		}
	}

	/**
	 * 继续播放背景音乐
	 */
	public void ContinueBgMusic() {
		try {
			if (!BgisPlaying) {
				mediaPlayer2.start();
				BgisPlaying = true;
			}
		} catch (Exception e) {
			Log.d("eden", "----------继续播放音乐错误");
		}
	}

	/**
	 * 设置游戏音乐声音大小
	 */
	public void SetVoice(int vol) {
		if (!PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol,
					AudioManager.FLAG_PLAY_SOUND);
		}
	}

	/**
	 * 调高音量(多媒体音量)
	 */
	public void raiseVoice() {
		if (!PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
			// 强制调用多媒体音量
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND
					| AudioManager.FLAG_SHOW_UI);
			// 本地保存多媒体音量
			int nowVol2 = PreferenceHelper.getMyPreference().getSetting()
					.getInt("music", 0);
			if (nowVol2 != 15) {
				PreferenceHelper.getMyPreference().getEditor()
				.putInt("music", nowVol2 + 1);
				PreferenceHelper.getMyPreference().getEditor().commit();
				AudioPlayUtils.getInstance().SetVoice(
						PreferenceHelper.getMyPreference().getSetting()
						.getInt("music", 0));
			}
		}
	}

	/**
	 * 调小音量(多媒体音量)
	 */
	public void lowerVoice() {
		if (!PreferenceHelper.getMyPreference().getSetting().getBoolean("jingyin", false)) {
			// 强制调用多媒体音量
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND
					| AudioManager.FLAG_SHOW_UI);
			// 本地保存多媒体音量
			int nowVol = PreferenceHelper.getMyPreference().getSetting()
					.getInt("music", 0);
			if (nowVol != 0) {
				PreferenceHelper.getMyPreference().getEditor()
				.putInt("music", nowVol - 1);
				PreferenceHelper.getMyPreference().getEditor().commit();
				AudioPlayUtils.getInstance().SetVoice(
						PreferenceHelper.getMyPreference().getSetting()
						.getInt("music", 0));
			}
		}
	}
}
