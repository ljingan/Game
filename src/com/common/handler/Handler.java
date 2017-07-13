package com.common.handler;

import com.common.net.DataPackage;

public interface Handler {
	void exceute(DataPackage pack);
	void clear();
}
