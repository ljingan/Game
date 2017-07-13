package com.common.handler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.C2S.C2SPtl.C2SLogin;
import com.common.net.Cmd;
import com.common.net.DataPackage;

public abstract class AbstractHandler implements Handler {

	private HashMap<Integer, Method> cmds = new HashMap<Integer, Method>();

	public AbstractHandler() {
		initialize();
	}

	public void initialize() {

		for (Method m : this.getClass().getDeclaredMethods()) {
			Cmd cmd = m.getAnnotation(Cmd.class);
			if (cmd != null) {
				cmds.put(cmd.id(), m);
				Dispatcher.put(cmd.id(), this);

			}
		}
	}

	@Override
	public void exceute(DataPackage pack) {
		Method m = cmds.get(pack.getCmd());
		if (m != null) {
			try {
				ByteArrayInputStream input = new ByteArrayInputStream(pack
						.getData());
				try {
//			        String className = "ss.Use";
			        Class<?> cl = Class.forName(C2SLogin.class.getName());
			        Method saddMethod2 = cl.getMethod("parseFrom", new Class[]{InputStream.class});
//			        C2SLogin login = C2SLogin.parseFrom(input);
			        C2SLogin login = (C2SLogin) saddMethod2.invoke(null, input);
			        login.getId();
//			          String result=saddMethod2.invoke(null,new Object[]{"测试反射"}).toString();
//			          System.out.println(result);
			      } catch (Exception e) {
			        e.printStackTrace();
			      }

				// ByteArrayInputStream input = new
				// ByteArrayInputStream(pack.getData());
				// C2SLogin login = C2SLogin.parseFrom(input);
				// try {
				// ByteArrayInputStream input = new ByteArrayInputStream(data);
				// C2SLogin login = C2SLogin.parseFrom(input);
				// login.getId();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// return false;
				// }
				m.invoke(this);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}

	private <T> void unpack(T c, ByteArrayInputStream input) {
//		c.getClass()
//		T parseFrom = (T)C2SLogin.parseFrom(input);
//		T login = parseFrom;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}

}
