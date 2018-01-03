package com.xiaolyuh.model;

public class Message {

	public static final int HELLO = 0;
    public static final int GOODBYE = 1;
	@org.kie.api.definition.type.Label("消息")
	private String msg = "test";
	private int status;

	public Message() {
		super();
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String message) {
		this.msg = message;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}