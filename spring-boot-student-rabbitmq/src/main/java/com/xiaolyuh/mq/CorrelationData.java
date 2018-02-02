package com.xiaolyuh.mq;

/**
 * 发送消息的相关数据
 * @author yuhao.wang
 */
public class CorrelationData extends org.springframework.amqp.rabbit.support.CorrelationData {
	/**
	 * 消息体
	 */
	private volatile Object message;

	/**
	 * 交换机名称
	 */
	private String exchange;

	/**
	 * 路由key
	 */
	private String routingKey;

	/**
	 * 重试次数
	 */
	private int retryCount = 0;

	public CorrelationData() {
		super();
	}

	public CorrelationData(String id) {
		super(id);
	}

	public CorrelationData(String id, Object data) {
		this(id);
		this.message = data;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
}
