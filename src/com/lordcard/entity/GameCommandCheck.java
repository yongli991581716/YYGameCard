package com.lordcard.entity;

import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 网络命令检测
 */
//@Entity
//@Table(name = "game_command_check")
public class GameCommandCheck {
	/** ping 命令类型 */
	public static final int													COMMAND_TYPE_PING	= 1;

	@Expose private Integer													commandCode;				// 指令编号
	@Expose private Integer													commandType;				// 指令类型(1=ping,)
	/**{"networkTypes":"1,2"}   需要测试的网络类型(1=wifi,2=2g,3=3g)*/  
	@Expose @SerializedName("commandTerm") private Map<String, String>		commandTermMap;	// 指令条件
	/**{"pingUrl":"www.baidu.com","pingCount":5,"nextAction":2}*/
	/**String	pingUrl;	// ping地址(域名、IP)
	 	 Integer	pingCount;	// ping次数
	 	 Integer	nextAction; // 接到下次相同命令时(1=终止当前命令，执行新命令,2=忽略新命令，直到当前命令执行完毕)*/
	@Expose @SerializedName("commandContent") private Map<String, String>	commandContentMap;// 指令内容
	@Expose @SerializedName("content") private String						commandContent;	
	@Expose @SerializedName("term") private String							commandTerm;

	public String getCommandTerm() {
		return commandTerm;
	}

	public void setCommandTerm(String commandTerm) {
		this.commandTerm = commandTerm;
	}

	public String getCommandContent() {
		return commandContent;
	}

	public void setCommandContent(String commandContent) {
		this.commandContent = commandContent;
	}

	public final Integer getCommandCode() {
		return commandCode;
	}
	
	public final void setCommandCode(Integer commandCode) {
		this.commandCode = commandCode;
	}
	public final Integer getCommandType() {
		return commandType;
	}
	public final void setCommandType(Integer commandType) {
		this.commandType = commandType;
	}
	public final Map<String, String> getCommandTermMap() {
		return commandTermMap;
	}
	public final void setCommandTermMap(Map<String, String> commandTermMap) {
		this.commandTermMap = commandTermMap;
	}
	public final Map<String, String> getCommandContentMap() {
		return commandContentMap;
	}
	public final void setCommandContentMap(Map<String, String> commandContentMap) {
		this.commandContentMap = commandContentMap;
	}
	
}
