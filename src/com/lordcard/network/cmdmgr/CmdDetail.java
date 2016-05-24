package com.lordcard.network.cmdmgr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lordcard.common.util.Base64Util;
import com.lordcard.common.util.JsonHelper;

import java.io.Serializable;
import java.util.Random;

public class CmdDetail implements Serializable{
	
	private static final long serialVersionUID = 2612673405887202488L;

	public static int PLAY = 1;			//1:打牌业务消息

	@Expose @SerializedName("c") private String cmd; 		//指令类型	
	@Expose @SerializedName("g") private String token; 		//游戏登录Token
	@Expose @SerializedName("d") private String detail; 	//指令内容
	@Expose @SerializedName("r") private Integer rand; 		//随即数 防止数据缓存
	@Expose @SerializedName("m") private String androId; 	//手机端唯一ID
	
	@Expose @SerializedName("s") private long seq; 	//消息序列
//	@Expose @SerializedName("p") private String phone; //手机名称
	@Expose @SerializedName("e") private String mes;	//提示消息
	@Expose@SerializedName("vs") private String version; //版本号



	/**
	 * 聊天时 0为聊天，1为思考，2为表情、3为美女
	 * {cmd:"chat",detail:"{type:1,value:\"12\",fromUserId:645}"}
	 * 
	 * 打牌时    0:普通消息，1:打牌业务消息
	 */
	@Expose @SerializedName("t") private int type = 0;						
	private long time;							//发送时间
	private int count;							//重发的次数
	private boolean hasDo;						//是否已经处理
	private String tag;							//标识是in 还是 out
	
	
	/**聊天用 内容：当类型为0和1时，这是文本信息，当类型为2时，为表情Id */
	@Expose @SerializedName("v") private String value;
	@Expose @SerializedName("f") private String fromUserId;
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getRand() {
		return rand;
	}

	public void setRand(Integer rand) {
		this.rand = rand;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public boolean isHasDo() {
		return hasDo;
	}

	public void setHasDo(boolean hasDo) {
		this.hasDo = hasDo;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getAndroId() {
		return androId;
	}

	public void setAndroId(String androId) {
		this.androId = androId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public final String getMes() {
		return mes;
	}

	public final void setMes(String mes) {
		this.mes = mes;
	}
    public void urlDecode()
    {
    	try {
			if (detail != null && !detail.equals(""))
				this.detail = new String(Base64Util.decode(detail), "UTF-8");
			if (mes != null && !mes.equals(""))
				this.mes = new String(Base64Util.decode(detail), "UTF-8");
		} catch (Exception ex) {

		}
    }
	public void urlEncode()
	{
		try {
			if (detail != null && !detail.equals("")) {
				this.detail = Base64Util.encode(detail.getBytes("UTF-8"));
			}

			if (mes != null && !mes.equals("")) {
				this.mes = Base64Util.encode(mes.getBytes("UTF-8"));
			}
		} catch (Exception ex) {

		}
	    }
    /*public void urlDecode()
    {
    	if(detail != null && !detail.equals(""))
    	  this.detail = EncodeUtils.urlDecode(detail);
    	if(mes != null && !mes.equals(""))
    	  this.mes = EncodeUtils.urlDecode(mes);
    }*/
	/**
	 * 转成json字符串
	 * 
	 * @return
	 */
	public String toJson() {
//		this.setPhone("##xt615##");
		this.setRand(new Random().nextInt(100));
		return JsonHelper.toJson(this) + ";";
	}
}
