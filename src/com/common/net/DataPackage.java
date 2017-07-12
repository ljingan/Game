package com.common.net;

public class DataPackage {

	/*
	 * 包头长度
	 */
	public static int PACKAGE_HEAD_LENGTH = 5;
	/*
	 * 长度
	 */
	private int size = 0;

	/*
	 * 消息缓存
	 */
	private MessageBuffer buffer;

	/*
	 * 流水号
	 */
	private int cmd;

	/*
	 * 是否加密 1-是 0不是
	 */
	private byte isZip;

	public DataPackage() {
		setBuffer(new MessageBuffer());
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setBuffer(MessageBuffer buffer) {
		this.buffer = buffer;
	}

	public MessageBuffer getBuffer() {
		return buffer;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public int getCmd() {
		return cmd;
	}

	public void setIsZip(byte isZip) {
		this.isZip = isZip;
	}

	public byte getIsZip() {
		return isZip;
	}
}
