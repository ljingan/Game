package com.common.session;

import com.common.session.impl.SessionImpl;

public class SessionFactory {
	/**
	 * 创建session
	 * 
	 * @param <T>
	 * @param connection
	 * @param sessionId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Session<T> createSession(T connection, long sessionId) {
		if (connection instanceof io.netty.channel.Channel) {
			return (Session<T>) new SessionImpl(sessionId,
					(io.netty.channel.Channel) connection);
		} else if (connection instanceof java.nio.channels.SocketChannel) {
			return (Session<T>) new SessionImpl(sessionId,
					(java.nio.channels.SocketChannel) connection);
		}
		return null;
	}
}
