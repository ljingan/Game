package com.common.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;

public class TcpChannel implements Runnable {
	// 超时时间，单位毫秒
	private static final int TimeOut = 1000 * 10;
	// 端口
	private Integer port = 20006;
	// 服务器通道 服务
	private ServerSocketChannel serversocket;
	
	// 选择器，主要用来监控各个通道的事件
	private Selector selector;
	private MessageBuffer mesBuf;

	// 缓冲区
	private ByteBuffer buf = ByteBuffer.allocate(16);

	public TcpChannel() {
		init();
	}

	/**
	 * 这个method的作用1：是初始化选择器 2：打开两个通道 3：给通道上绑定一个socket 4：将选择器注册到通道上
	 * */
	public void init() {
		mesBuf = new MessageBuffer(16);
		try {
			// 创建选择器
			this.selector = SelectorProvider.provider().openSelector();
			// 打开第一个服务器通道
			this.serversocket = ServerSocketChannel.open();
			// 告诉程序现在不是阻塞方式的
			this.serversocket.configureBlocking(false);
			// 获取现在与该通道关联的套接字
			this.serversocket.socket().bind(new InetSocketAddress(this.port));
			// 将选择器注册到通道上，返回一个选择键
			// OP_ACCEPT用于套接字接受操作的操作集位
			this.serversocket.register(this.selector, SelectionKey.OP_ACCEPT);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 这个方法是连接 客户端连接服务器
	 * 
	 * @throws IOException
	 * */
	public void accept(SelectionKey key) throws IOException {
		SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
		channel.configureBlocking(false);
		// OP_READ用于读取操作的操作集位
		channel.register(this.selector, SelectionKey.OP_READ);
	}

	/**
	 * 从通道中读取数据 并且判断是给那个服务通道的
	 * 
	 * @throws IOException
	 * */
	public void read(SelectionKey key) throws IOException {

		this.buf.clear();
		// 通过选择键来找到之前注册的通道
		// 但是这里注册的是ServerSocketChannel为什么会返回一个SocketChannel？？
		SocketChannel channel = (SocketChannel) key.channel();
		Socket socket = channel.socket();
		InputStream is = socket.getInputStream();
		 DataInputStream input = new DataInputStream(socket.getInputStream());  
//		 String clientInputStr = input.readUTF();
		byte[] ft = new byte[100];  
	    int len = -1;  
//	    while ((len = is.read(ft)) != -1) {  
//	    	System.out.print(len+ "  ");
//	    }  
	    ByteBuffer bf  = ByteBuffer.wrap(ft);
//		System.out.print(is.available() + "   " + clientInputStr+ "\n");
		//System.out.print(channel.socket().getInetAddress().toString() + "  \n");
		// 从通道里面读取数据到缓冲区并返回读取字节数

		// int count = channel.read(this.buf);
		//
		// if (count == -1) {
		// // 取消这个通道的注册
		// key.channel().close();
		// key.cancel();
		// return;
		// }
		//得到并清空缓冲区
		ByteBuffer buffer  = ByteBuffer.allocate(16);
		//buffer.clear();
		
		
		Charset charset = Charset.forName("gb2312");
//		ByteBuffer buff = ByteBuffer.allocate(64);
		//读取信息获得读取的字节数
		int bytesRead;
		String content = "";
		ByteBuffer[] dsts = new ByteBuffer[]{buffer};
		int offset = 0;
		// 开始读数据
		try {
			while ((bytesRead = channel.read(buffer)) != -1) {
				
				//buffer.get(dst, offset, length);
				// System.out.print("ssss  " + buff.getInt() + "   " +
				// buf.position() + "\n");
				//buffer.flip();
//				 while (buf.remaining() > 0) {
//			          System.out.print((char) buf.get());
//			        }
				byte[] temp = buffer.array();
				mesBuf.write(temp, bytesRead);
				buffer.clear();
				//ByteBuffer buffer = new by
				String receivedString = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
				System.out.print(receivedString + "  sss\n");
				//content += charset.decode(buffer);
			}
			// 打印从该sk对应的Channel里读到的数据
			System.out.println("====" + content);
			// 将sk对应的Channel设置成准备下一次读取
			key.interestOps(SelectionKey.OP_READ);
		}
		// 如果捕捉到该sk对应的Channel出现了异常，即表明该Channel对应的Client出现了问题
		// 所以从Selector中取消sk的注册
		catch (IOException ex) {
			// 从Selector中删除指定的SelectionKey
			// 取消这个通道的注册
			key.channel().close();
			key.cancel();
		}

		// 将数据从缓冲区中拿出来
		//String input = new String(this.buf.array()).trim();
		// 那么现在判断是连接的那种服务
		//System.out.println("您的输入为：" + input);
		// 定义编码格式
		//channel.write(buf);
	}

	// 解包
	private boolean unPack(MessageBuffer buff) {
		// if (buff.DataSize < 6) return false;

		// CBytesBuffer tmpbuff = new CBytesBuffer(buff);
		// dPackage.buffer.clear();
		//
		// dPackage.size = CBufferFilter.readuint32(tmpbuff);
		// dPackage.msgid = CBufferFilter.readuint16(tmpbuff);
		//
		// if (tmpbuff.getDataSize() < dPackage.size - 6)
		// {
		// return false;
		// }
		// dPackage.buffer.write(tmpbuff.Buffer, (int)dPackage.size - 6);
		//
		//
		// buff.popBytes((int)dPackage.size);
		return true;
	}

	@Override
	public void run() {
		while (true) {
			Iterator<?> selectorKeys = null;
			SelectionKey currentKey;
			try {
				// 选择一组键，其相应的通道已为 I/O 操作准备就绪。
				// if (selector.select(TimeOut) == 0) {
				// // 监听注册通道，当其中有注册的 IO
				// // 操作可以进行时，该函数返回，并将对应的
				// System.out.print("独自等待.\n");
				// continue;
				// }
				this.selector.select();

				// 返回此选择器的已选择键集
				selectorKeys = this.selector.selectedKeys().iterator();
				
				while (selectorKeys.hasNext()) {
					// 这里找到当前的选择键
					currentKey = (SelectionKey) selectorKeys.next();
					if (!currentKey.isValid()) {
						continue;
					}
					if (currentKey.isAcceptable()) {
						// 如果遇到请求那么就响应
						this.accept(currentKey);
					} else if (currentKey.isReadable()) {
						// 读取客户端的数据
						this.read(currentKey);
					}
					//selectorKeys.remove();
				}
			} catch (Exception e) {
				selectorKeys.remove();
			}
		}

	}

}
