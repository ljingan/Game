package com.common.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;

import com.common.handler.Dispatcher;
import com.common.handler.Handler;
import com.common.session.ServerContext;
import com.common.session.Session;
import com.common.session.impl.ServerContextImpl;

public class TcpChannel implements Runnable {
	private ServerContext<SocketChannel> serverContext = ServerContextImpl
			.getInstance();

	private Session<SocketChannel> getSession(SocketChannel connection) {
		return serverContext.getSession(connection);
	}

	// 超时时间，单位毫秒
	private static final int TimeOut = 1000 * 10;
	// 端口
	private Integer port = 5273;
	// 服务器通道 服务
	private ServerSocketChannel serversocket;

	// 选择器，主要用来监控各个通道的事件
	private Selector selector;
	private ByteArray mesBuf;

	public TcpChannel() {
		init();
	}

	/**
	 * 这个method的作用1：是初始化选择器 2：打开两个通道 3：给通道上绑定一个socket 4：将选择器注册到通道上
	 * */
	public void init() {
		mesBuf = new ByteArray(16);
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
		SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
		System.out.print(channel.socket().getRemoteSocketAddress() + "\n");
		Session<SocketChannel> session =  getSession(channel);
		session.getSessionId();
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

		// 通过选择键来找到之前注册的通道
		// 但是这里注册的是ServerSocketChannel为什么会返回一个SocketChannel？？
		SocketChannel channel = (SocketChannel) key.channel();

		// 读取信息获得读取的字节数
		int bytesRead;
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.order(ByteOrder.BIG_ENDIAN);
		Session<SocketChannel> session =  getSession(channel);
		System.out.print("session  " + session.getSessionId() + "\n");
		// 开始读数据
		try {
			while ((bytesRead = channel.read(buffer)) > 0) {
				byte[] temp = buffer.array();
				int position = mesBuf.getPosition();
				mesBuf.write(temp, bytesRead);
				mesBuf.setPosition(position);
				buffer.clear();
			}
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			unPack(mesBuf);
		}
		// 如果捕捉到该sk对应的Channel出现了异常，即表明该Channel对应的Client出现了问题
		// 所以从Selector中取消sk的注册
		catch (IOException ex) {
			// 从Selector中删除指定的SelectionKey
			// 取消这个通道的注册
			key.channel().close();
			key.cancel();
		}
	}

	// 解包
	private boolean unPack(ByteArray buff) {
		ArrayList<DataPackage> list = new ArrayList<DataPackage>();
		while (buff.available() >= DataPackage.PACKAGE_HEAD_LENGTH) {
			int position = buff.getPosition();
			int size = buff.readShort();
			if (buff.available() + 2 >= size) {
				DataPackage pack = new DataPackage();
				pack.setSize(size);
				pack.setIsZip(buff.readByte());
				pack.setCmd(buff.readShort());
				int len = size - DataPackage.PACKAGE_HEAD_LENGTH;
				byte[] data = new byte[len];
				buff.readBytes(data, 0, len);
				pack.setBytes(data);
				list.add(pack);
			} else {
				buff.setPosition(position);
				break;
			}
		}
		if (buff.available() == 0) {
			buff.clear();
		}
		Iterator<DataPackage> it = list.iterator();
		while (it.hasNext()) {
			DataPackage pack = it.next();
			Handler handler = Dispatcher.get(pack.getCmd());
			handler.exceute(pack);
		}
		return true;
	}

	@Override
	public void run() {
		while (true) {
			Iterator<?> selectorKeys = null;
			SelectionKey currentKey;
			try {
				this.selector.select();

				// 返回此选择器的已选择键集
				selectorKeys = this.selector.selectedKeys().iterator();
				while (selectorKeys.hasNext()) {
					// System.out.print(keys.size() + "   " + (index++) +
					// " \n");
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
					// selectorKeys.remove();
				}
			} catch (Exception e) {
				selectorKeys.remove();
			}
		}

	}
}
