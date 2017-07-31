package com.common;

public class ClassLoaderTest {
	public static void main(String[] args) {
		try {
			// 测试加载网络中的class文件
//			String rootUrl = "http://localhost:8080/httpweb/classes";
//			String className = "org.classloader.simple.NetClassLoaderSimple";
////			new ClassLoader().getSystemResource(rootUrl);
////			ClassLoader  ncl1 = new ClassLoader (rootUrl);
////			ClassLoader  ncl2 = new ClassLoader (rootUrl);
//			Class<?> clazz1 = ncl1.loadClass(className);
//			Class<?> clazz2 = ncl2.loadClass(className);
//			Object obj1 = clazz1.newInstance();
//			Object obj2 = clazz2.newInstance();
//			clazz1.getMethod("setNetClassLoaderSimple", Object.class).invoke(
//					obj1, obj2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
