package com.common.net;

public class DataPackage {

	/**
	 * 包头长度
	 */
	public static int PACKAGE_HEAD_LENGTH = 5;

	/**
	 * 长度
	 */
	private int size = 0;

	/**
	 * 消息缓存
	 */
	private byte[] bytes;

	/**
	 * proto数据对象
	 */
	private Object data;

	/**
	 * 流水号
	 */
	private int cmd;

	/**
	 * 是否加密 1-是 0不是
	 */
	private byte isZip;

	public DataPackage() {
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
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

	public void setBytes(byte[] data) {
		this.bytes = data;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
