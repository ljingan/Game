package com.common.session;

import java.io.IOException;
import java.util.Set;

public interface Session<T> {
	/**
	 * @return 
	 *         当前的会话ID。每个会话在服务器端都存在一个唯一的标示sessionID，session对象发送到浏览器的唯一数据就是sessionID
	 *         ，
	 */
	long getSessionId();

	/**
	 * sessiong锁
	 * 
	 * @return
	 */
	String getSessionLock();

	/**
	 * @return 返回连接对象
	 */
	T getConnection();

	/**
	 * 关闭连接
	 * 
	 * @return true 成功 <false 失败
	 * @throws IOException
	 */
	boolean disconnect() throws IOException;

	/**
	 * 当前会话是否连接
	 * 
	 * @return true / false
	 */
	boolean isConnected();

	/**
	 * 设置会话的最大持续时间，单位是秒，负数表明会话永不失效。
	 * 
	 * @param interval
	 */
	void setMaxInactiveInterval(int interval);

	/**
	 * 会话的最大持续时间
	 * 
	 * @return
	 */
	int getMaxInActiveInterval();

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param name
	 * @param value
	 */
	<K, V> void setAttribute(K name, V value);

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param name
	 * @return
	 */
	<K, V> V getAttribute(K name);

	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param name
	 * @return
	 */
	<K, V> V removeAttribute(K name);

	/**
	 * 获得所有属性名
	 * @return
	 */
	Set<String> getAttributeNames();
}
