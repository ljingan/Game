package com.common.net;

public class MessageBuffer {
	private static int DEFALUT_SIZE = 128;// 默认buff分配长度128字节
	/*
	 * 
	 */
	private byte[] _buffer = null;

	/*
	 * buff总长度
	 */
	private int _length = 0;

	/*
	 * 空闲的长度
	 */
	private int _free = 0;

	public MessageBuffer(int len) {

		if (len <= 0) {
			_length = DEFALUT_SIZE;
			_free = DEFALUT_SIZE;
			_buffer = new byte[DEFALUT_SIZE];
		} else {
			_length = len;
			_free = len;
			_buffer = new byte[len];
		}
	}

	public MessageBuffer() {
		_length = DEFALUT_SIZE;
		_free = DEFALUT_SIZE;
		_buffer = new byte[DEFALUT_SIZE];
	}

	/*
	 * 写入一个字节返回写入的字节长度
	 */
	public int write(byte b) {
		if (_buffer == null)
			return 0;
		else {
			while (_free < 1) {
				increaceBufferSize();
			}
			_buffer[getContentSize()] = b;
			_free--;
			return 1;
		}
	}

	/*
	 * 内容的长度
	 */
	public int getContentSize() {
		return _length - _free;
	}

	/*
	 * 写入字节数组返回写入的字节长度
	 */
	public int write(byte[] bytes, int len) {
		if (bytes == null || len <= 0)
			return 0;
		else {
			while (_free < len) {
				increaceBufferSize();
			}
			byte[] temp;
			if (len >= bytes.length) {
				temp = bytes;
			} else {
				temp = new byte[len];
				System.arraycopy(bytes, 0, temp, 0, len);
			}
			System.arraycopy(temp, 0, _buffer, getContentSize(), len);
			_free -= len;
			return len;
		}
	}

	// / 读取一个byte值
	// 成功读取返回0,失败返回-1
	public int read(byte value) {
		value = 0;
		if (getContentSize() > 0) {
			value = _buffer[0];
			popBytes(1);
			return 0;
		} else {
			return -1;
		}
	}

	// / 读取字节流到buffer
	// 成功读取返回0,失败返回-1
	public boolean read(byte[] buf, int len) {
		buf = null;
		if (getContentSize() <= 0 || len <= 0)
			return false;
		else {
			if (len > getContentSize()) {
				len = getContentSize();
			}
			buf = new byte[len];
			System.arraycopy(_buffer, 0, buf, 0, len);
			popBytes(len);
			return true;
		}
	}

	/*
	 * 从字节流中弹出字节 返回弹出的字节数
	 */
	public int popBytes(int len) {
		if (len <= 0)
			return -1;
		else if (len >= getContentSize()) {
			int _size = getContentSize();
			_free = _length;
			return _size;
		} else {
			System.arraycopy(_buffer, len, _buffer, 0, getContentSize() - len);
			// Array.Copy(_buffer, poplen, _buffer, 0, getDataSize() - poplen);
			_free += len;
			return len;
		}
	}

	/*
	 * 获取整个buff内容
	 */
	public byte[] getData() {
		if (getContentSize() <= 0)
			return new byte[0];
		else {
			byte[] temp = new byte[getContentSize()];
			read(temp, getContentSize());
			return temp;
		}
	}

	/*
	 * 倍数增加buffer数组长度 返回增加后buffer的总长度
	 */
	protected int increaceBufferSize() {
		int _len = _buffer.length * 2;
		if (_free == _length) {
			_length = _len;
			_free = _len;
			_buffer = new byte[_length];
		} else {
			_free += _length;
			_length = _len;
			byte[] temp = new byte[_len];
			System.arraycopy(_buffer, 0, temp, 0, getContentSize());
			_buffer = temp;
		}
		return _length;
	}

}
