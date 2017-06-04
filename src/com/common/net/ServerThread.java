package com.common.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread implements Runnable {
	private Socket client = null;
	private boolean isRuning = true;

	// private Byte[] buf = new Byte();

	public ServerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		InputStream is = null;
		InputStreamReader isr = null;
		DataInputStream dis = null;
		try {

			is = client.getInputStream();

			// BufferedReader buf = null;
			// buf.r

			// 获取Socket的输出流，用来向客户端发送数据
			PrintStream out = new PrintStream(client.getOutputStream());
			// 获取Socket的输入流，用来接收从客户端发送过来的数据
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			isr = new InputStreamReader(is);
			dis = new DataInputStream(is);
			boolean flag = true;
			while (isRuning) {
				// 接收从客户端发送过来的数据

				// buf = new BufferedReader(isr);
				System.out.print("ssssss + " + System.currentTimeMillis()
						+ "   " + dis.available() + "\n");
				dis.readByte();
				if (dis.available() == 0) {
					flag = false;
					System.out.print("read end " + System.currentTimeMillis()
							+ "\n");
				}
				out.println("echo:" + "\n\n\n\n");
				System.out.print("\n\n\n");
				out.flush();
				Thread.sleep(1000);
				// di.read(b, off, len);
				// while (flag != -1) {
				// i++;
				// flag = di.read(temp = new byte[1]);
				// if (flag != -1)
				// tempByteList.add(temp);
				// if (i == 38)
				// break;
				// }
				// parseData();
				// flag = false;
				// buf.read(target);
				// String str = buf.readLine();
				// if (str == null || "".equals(str)) {
				// flag = false;
				// } else {
				// if ("bye".equals(str)) {
				// flag = false;
				// } else {
				// // 将接收到的字符串前面加上echo，发送到对应的客户端

				// Thread.sleep(100);
				// }
				// }
			}
			out.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (is != null) {
					is.close();
				}
				if (dis != null) {
					dis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// private void parseData() throws IOException {
	// InputStream is = this.client.getInputStream();
	// DataInputStream di = new DataInputStream(is);
	// byte[] temp = new byte[1];
	// int flag = 0;
	// ArrayList tempByteList = new ArrayList();
	// int i = 0;
	// // while (flag != -1) {
	// // i++;
	// // flag = di.read(temp = new byte[1]);
	// // if (flag != -1)
	// // tempByteList.add(temp);
	// // if (i == 38)
	// // break;
	// // }
	// }

}
