package com.common.session;

import java.io.IOException;

public interface SessionContainer<T> {
	/**
	 * 添加新的连接
	 * 
	 * @param connection
	 * @return Session<IoSession>
	 */
	public Session<T> addSession(T connection);

	/**
	 * 删除session
	 * 
	 * @param session
	 * @return 返回会话信息:Session<IoSession>
	 */
	public Session<T> removeSession(Session<T> session);

	/**
	 * 删除session
	 * 
	 * @param sessionId
	 *            会话ID
	 * @return 返回会话信息:Session<IoSession>
	 */
	public Session<T> removeSession(long sessionId);

	/**
	 * 删除session
	 * 
	 * @param sessionId
	 *            会话ID
	 * @return 返回会话信息:Session<IoSession>
	 */
	public Session<T> removeSession(T connection);

	/**
	 * 根据sessionId查找相应Session
	 * 
	 * @param sessionId
	 *            ID
	 * @return 没有返回null
	 */
	public Session<T> getSession(long sessionId);

	/**
	 * 根据当前连接查找相应Session
	 * 
	 * @param connection
	 *            当前连接
	 * @return 没有返回null
	 */
	public Session<T> getSession(T connection);

	/**
	 * 判断当前连接是否有效
	 * 
	 * @param connection
	 *            当前连接
	 * @return true/false
	 */
	public boolean contains(T connection);

	/**
	 * 判断sessionId是否有存在Session
	 * 
	 * @param sessionId
	 *            ID
	 * @return true/false
	 */
	public boolean contains(long sessionId);

	/**
	 * 重新连接
	 * 
	 * @param connection
	 * @param sessionId
	 * @return
	 * @throws IOException
	 */
	public Session<T> joinSession(T connection, long sessionId)
			throws IOException;
}
