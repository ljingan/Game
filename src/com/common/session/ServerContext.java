package com.common.session;

import java.io.IOException;

public interface ServerContext<T> {
	/**
	 * 获取当前连接的Session, 不存在时新创建
	 * 
	 * @param connection
	 *            连接
	 * @return Session
	 */
	Session<T> getSession(T connection);

	/**
	 * 添加与当前连接相关session
	 * 
	 * @param connection
	 *            当前连接
	 * @return
	 */
	Session<T> addSession(T connection);

	/**
	 * 马上断开连接
	 * 
	 * @param connection
	 */
	void removeSession(Session<T> session);

	/**
	 * 断线重连时将当前连接加入原来的Session
	 * 
	 * @param connection
	 *            连接
	 * @param sessionId
	 *            Session Id
	 * @return 加入的Session
	 * @throws IOException
	 */
	Session<T> joinSession(T connection, long sessionId) throws IOException;
	
}
