package com.common.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.common.net.Cmd;

public abstract class AbstractHandler implements Handler {

	private HashMap<Integer, Method> cmds = new HashMap<Integer, Method>();

	public AbstractHandler() {
		initialize();
	}

	public void initialize() {

		Dispatcher.put(100, this);
		for (Method m : this.getClass().getDeclaredMethods()) {
			Cmd cmd = m.getAnnotation(Cmd.class);
			if (cmd != null) {
				cmds.put(cmd.id(), m);
				try {
					m.invoke(this);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exceute(Object body) {
		// TODO Auto-generated method stub

	}

}
