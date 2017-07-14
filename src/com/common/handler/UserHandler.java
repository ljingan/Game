package com.common.handler;

import com.C2S.C2SPtl.C2SLogin;
import com.common.net.Cmd;
import com.common.net.DataPackage;

public class UserHandler extends AbstractHandler {
	public UserHandler() {
		// System.out.print(color + "\n");
	}

	@Cmd(id = 1001, protoClass = C2SLogin.class)
	public void login(DataPackage pack) {
		System.out.print("loginloginlogin\n");
	}

	@Cmd(id = 1000, protoClass = C2SLogin.class)
	public void val(DataPackage pack) {
		System.out.print("valvalval\n");
	}

}
