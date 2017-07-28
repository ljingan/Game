package com.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

import com.common.handler.Dispatcher;
import com.common.handler.Handler;
import com.common.net.ByteArray;
import com.common.net.DataPackage;
import com.common.session.ServerContext;
import com.common.session.Session;
import com.common.session.impl.ServerContextImpl;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {

	private ServerContext<Channel> serverContext = ServerContextImpl
			.getInstance();

	private Session<?> getSession(Channel connection) {
		return serverContext.getSession(connection);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		getSession(ctx.channel());
		System.out.println("SimpleServerHandler.channelRead");
		ByteBuf result = (ByteBuf) msg;
		byte[] bytes = result.array();
		ByteArray buff = new ByteArray();
		buff.write(bytes, result.readableBytes());
		buff.setPosition(0);
		unPack(buff);
		// // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
		// result.readBytes(result1);
		// String resultStr = new String(result1);
		// // 接收并打印客户端的信息
		// System.out.println("Client said:" + resultStr);
		// // 释放资源，这行很关键
		// result.release();
		//  
		// // 向客户端发送消息
		// String response = "hello client!";
		// // 在当前场景下，发送的数据必须转换成ByteBuf数组
		// ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
		// encoded.writeBytes(response.getBytes());
		// ctx.write(encoded);
		// ctx.flush();
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

	/*
	 * 
	 * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
	 * 
	 * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		System.out.println("RamoteAddress : " + ctx.channel().remoteAddress()
				+ " active !  ");

		ctx.writeAndFlush("Welcome to "
				+ InetAddress.getLocalHost().getHostName() + " service!\n");

		super.channelActive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
}