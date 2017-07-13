package com.common.handler;

import com.common.net.Cmd;

public class UserHandler extends AbstractHandler {
	public UserHandler() {
		// System.out.print(color + "\n");
	}

	@Cmd(id = 1001)
	public void login() {
		System.out.print("loginloginlogin\n");
	}

	@Cmd(id = 1000)
	public void val() {
		System.out.print("valvalval\n");
	}

}
