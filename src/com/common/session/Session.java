package com.common.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
	/**
	 * 当前的会话ID。每个会话在服务器端都存在一个唯一的标示sessionID，session对象发送到浏览器的唯一数据就是sessionID，
	 */
	private long sesionId;

	private Map<String, Object> attrs = new HashMap<String, Object>();

	/**
	 * 设定指定名字的属性的值，并将它添加到session会话范围内，如果这个属性是会话范围内存在，则更改该属性的值。
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value) {
		attrs.put(name, value);
	}

	/**
	 * 会话范围内获取指定名字的属性的值，返回值类型为object，如果该属性不存在，则返回null。
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name) {
		return attrs.get(name);
	}

	/**
	 * 删除指定名字的session属性，若该属性不存在，则出现异常。
	 * 
	 * @param name
	 */
	public void removeAttribute(String name) {
		attrs.remove(name);
	}

	/**
	 * 使session失效。可以立即使当前会话失效，原来会话中存储的所有对象都不能再被访问。
	 */
	public void invalidate() {
	}

	
	public long getSesionId() {
		return sesionId;
	}

	public void setSesionId(long sesionId) {
		this.sesionId = sesionId;
	}

	/**
	 * 设置会话的最大持续时间，单位是秒，负数表明会话永不失效。
	 * 
	 * @param interval
	 */
	public void setMaxInactiveInterval(int interval) {
	}

	/**
	 * 获取会话的最大持续时间。
	 * 
	 * @return
	 */
	public int getMaxInActiveInterval() {
		return 0;
	}
}
