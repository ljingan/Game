package com.common.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher {

	private static Map<Integer, Handler> handlers = new ConcurrentHashMap<Integer, Handler>();

	public static boolean contains(int key) {
		return handlers.containsKey(key);
	}

	public static boolean contains(Handler handler) {
		return handlers.containsValue(handler);
	}

	public static Handler get(int key) {
		return handlers.get(key);
	}

	public static void put(int key, Handler handler) {
		handlers.put(key, handler);
	}

	public static void remove(int key) {
		handlers.remove(key);
	}

}
