package com.common.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ServerThread implements Runnable {
	private Socket client = null;
	private boolean isRuning = true;
	private static final int BUFSIZE = 256;
	private ByteBuffer buffer = ByteBuffer.allocate(BUFSIZE); 

	public ServerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		InputStream in = null;
		OutputStream out = null;
		try {
			in = client.getInputStream();
			out = client.getOutputStream();
			int len;
			int totalBytesEchoed = 0;
			int position = 0;
			byte[] echoBuffer = new byte[32];
			while ((len = in.read(echoBuffer)) != -1) {
				buffer.put(echoBuffer, position, len);
//				echoBuffer.length = 0;
//				out.write(echoBuffer, position, recvMsgSize);
				position += len;
				totalBytesEchoed += len;
				System.out.print("接收了  " + totalBytesEchoed + "   "
						+ len + "字节\n");
			}

		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
