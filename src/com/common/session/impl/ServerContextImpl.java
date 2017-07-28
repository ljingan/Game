package com.common.session.impl;

import java.io.IOException;

import com.common.session.ServerContext;
import com.common.session.Session;
import com.common.session.SessionContainer;

public class ServerContextImpl<T> implements ServerContext<T> {

	private static ServerContext<?> ctx;

	@SuppressWarnings("unchecked")
	public static <T> ServerContext<T> getInstance() {
		if (ctx == null) {
			ctx = new ServerContextImpl<T>();
		}
		return (ServerContext<T>) ctx;
	}

	private SessionContainer<T> containers = new SessionContainerImpl<T>();

	@Override
	public Session<T> joinSession(T connection, long sessionId)
			throws IOException {
		return containers.joinSession(connection, sessionId);
	}

	@Override
	public void removeSession(Session<T> session) {
		containers.removeSession(session);
	}

	@Override
	public Session<T> addSession(T connection) {
		return containers.addSession(connection);
	}

	@Override
	public Session<T> getSession(T connection) {
		return containers.getSession(connection);
	}

}
