package com.kodak.utility;

import com.kodak.constants.NotiType;

public class NotificationModel {
	private String type;
	private String msg;

	public String getType() {
		return type;
	}

	public void setType(NotiType type) {
		this.type = type.toString();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
