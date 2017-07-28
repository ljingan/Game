package com.common.session.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.common.session.Session;
import com.common.session.SessionContainer;
import com.common.session.SessionFactory;

public class SessionContainerImpl<T> implements SessionContainer<T> {

	private Map<T, Session<T>> sessionsMap = new ConcurrentHashMap<T, Session<T>>(
			2000);
	private Map<Long, Session<T>> sessionsIdMap = new ConcurrentHashMap<Long, Session<T>>(
			2000);

	private AtomicLong sequence = new AtomicLong();

	private long createSessionId() {
		return sequence.incrementAndGet();
	}

	@Override
	public Session<T> addSession(T connection) {
		if (contains(connection)) {
			return sessionsMap.get(connection);
		}
		long sessionId = createSessionId();
		Session<T> session = SessionFactory
				.createSession(connection, sessionId);
		sessionsMap.put(connection, session);
		sessionsIdMap.put(sessionId, session);
		return session;
	}

	@Override
	public boolean contains(T connection) {
		return sessionsMap.containsKey(connection);
	}

	@Override
	public boolean contains(long sessionId) {
		return sessionsIdMap.containsKey(sessionId);
	}

	@Override
	public Session<T> getSession(long sessionId) {
		return sessionsIdMap.get(sessionId);
	}

	@Override
	public Session<T> getSession(T connection) {
		Session<T> session = sessionsMap.get(connection);
		if (session == null) {
			session = addSession(connection);
		}
		return session;
	}

	@Override
	public Session<T> joinSession(T connection, long sessionId)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session<T> removeSession(Session<T> session) {
		if (session == null) {
			return null;
		}
		long sessionId = session.getSessionId();
		sessionsMap.remove(session.getConnection());
		sessionsIdMap.remove(sessionId);
		return session;
	}

	@Override
	public Session<T> removeSession(long sessionId) {
		return removeSession(getSession(sessionId));
	}

	@Override
	public Session<T> removeSession(T connection) {
		return removeSession(getSession(connection));
	}

}
