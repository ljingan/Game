package com.common.handler;

public interface Handler {
	void exceute(Object body);
	void regiter(int cmd, String fun);
	void clear();
}
