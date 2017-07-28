package com.common.session.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.common.session.Session;

public class SessionImpl<T> implements Session<T> {
	private Map<String, Object> attrs = new HashMap<String, Object>();
	private T t;
	private long sesionId;
	private int intervalTime;

	public SessionImpl(long sessionId, T t) {
		this.t = t;
		this.sesionId = sessionId;
	}

	@Override
	public T getConnection() {
		return t;
	}

	@Override
	public long getSessionId() {
		return sesionId;
	}

	@Override
	public String getSessionLock() {
		return null;
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public boolean disconnect() throws IOException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> V getAttribute(K name) {
		return (V) attrs.get(name);
	}

	@Override
	public Set<String> getAttributeNames() {
		if (isConnected()) {
			return null;
		}
		return attrs.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> V removeAttribute(K name) {
		if (isConnected()) {
			return null;
		}
		return (V) attrs.remove(name);
	}

	@Override
	public <K, V> void setAttribute(K name, V value) {
		if (isConnected()) {
			attrs.put((String) name, value);
		}
	}

	@Override
	public int getMaxInActiveInterval() {
		return intervalTime;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		intervalTime = interval;
	}

}
