package com.common.handler;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.C2S.C2SPtl.C2SLogin;
import com.common.net.Cmd;
import com.common.net.DataPackage;

public abstract class AbstractHandler implements Handler {

	private Map<Integer, Method> cmds = new ConcurrentHashMap<Integer, Method>();
	private Map<Integer, Class<?>> protos = new ConcurrentHashMap<Integer, Class<?>>();

	public AbstractHandler() {
		initialize();
	}

	public void initialize() {

		for (Method m : this.getClass().getDeclaredMethods()) {
			Cmd cmd = m.getAnnotation(Cmd.class);
			if (cmd != null) {
				cmds.put(cmd.id(), m);
				protos.put(cmd.id(), cmd.protoClass());
				Dispatcher.put(cmd.id(), this);
			}
		}
	}

	@Override
	public void exceute(DataPackage pack) {
		Method m = cmds.get(pack.getCmd());
		Class<?> cl = protos.get(pack.getCmd());
		if (m != null && cl != null) {
			try {
				Method parseMethod = cl.getMethod("parseFrom",
						byte[].class );
				Object data = parseMethod.invoke(null, pack.getBytes());
				pack.setData(data);
				m.invoke(this, pack);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}

}
