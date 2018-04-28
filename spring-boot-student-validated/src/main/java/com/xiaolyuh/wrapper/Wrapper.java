package com.xiaolyuh.wrapper;

import java.io.Serializable;

public class Wrapper<T> implements Serializable {

	private static final long serialVersionUID = -7755079424069021028L;
	
    /** 编号. */
    private int code;

    /** 信息. */
    private String message;

    /** 结果数据 */
    private T data;
    
    /**
     * Instantiates a new wrapper.
     * 
     * @param code
     *            the code
     * @param message
     *            the message
     */
    public Wrapper(int code, String message) {
        this(code, message, null);
    }

    /**
     * Instantiates a new wrapper.
     * 
     * @param code
     *            the code
     * @param message
     *            the message
     * @param result
     *            the result
     */
    public Wrapper(int code, String message, T result) {
        super();
        this.code(code).message(message).result(result);
    }

    /**
     * Gets the 编号.
     * 
     * @return the 编号
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the 编号.
     * 
     * @param code
     *            the new 编号
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets the 信息.
     * 
     * @return the 信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the 信息.
     * 
     * @param message
     *            the new 信息
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * Sets the 编号 ，返回自身的引用.
     * 
     * @param code
     *            the new 编号
     * 
     * @return the wrapper
     */
    public Wrapper<T> code(int code) {
        this.setCode(code);
        return this;
    }

    /**
     * Sets the 信息 ，返回自身的引用.
     * 
     * @param message
     *            the new 信息
     * 
     * @return the wrapper
     */
    public Wrapper<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * Sets the 结果数据 ，返回自身的引用.
     * 
     * @param result
     *            the new 结果数据
     * 
     * @return the wrapper
     */
    public Wrapper<T> result(T result) {
        this.setData(result);
        return this;
    }

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
