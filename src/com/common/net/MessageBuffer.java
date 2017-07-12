package com.common.net;

public class MessageBuffer {

	/**
	 * 默认buff分配长度64字节
	 */
	private static int DEFALUT_SIZE = 64;

	/**
	 * 
	 */
	private byte[] _buffer = null;

	/**
	 * 读写位置
	 */
	private int position = 0;

	/**
	 * buff总长度
	 */
	private int _length = 0;

	/**
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

	/**
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

	/**
	 * 读取一个byte值 成功读取返回0,失败返回-1
	 */
	public byte readByte() {
		if (!checkCanReadSize(1)) {
			return -1;
		}
		int index = position;
		position++;
		return _buffer[index + 0];
	}

	/**
	 * 内容的长度
	 */
	public int getContentSize() {
		return _length - _free;
	}

	/**
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

	/**
	 * 
	 * @param buf读取字节流到
	 * @param len
	 * @return 成功读取返回0,失败返回-1
	 */
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

	public synchronized int readBytes(byte b[], int offset, int len) {
		if (b == null) {
			throw new NullPointerException();
		} else if (offset < 0 || len < 0 || len > b.length - offset) {
			throw new IndexOutOfBoundsException();
		}
		if (position >= getContentSize()) {
			return -1;
		}
		if (position + len > getContentSize()) {
			len = getContentSize() - position;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(_buffer, position, b, offset, len);
		position += len;
		return len;
	}

	public void writeInt(int value) {
		increaceContenSize(4);
		write((byte) (value >> 0));
		write((byte) (value >> 8));
		write((byte) (value >> 16));
		write((byte) (value >> 24));
	}

	public int readInt() {
		if (!checkCanReadSize(4)) {
			return -1;
		}
		int result = _buffer[position + 0] & 0xFF
				| (_buffer[position + 1] & 0xFF) << 8
				| (_buffer[position + 2] & 0xFF) << 16
				| (_buffer[position + 3] & 0xFF) << 24;
		position += 4;
		return result;
	}

	public long readLong() {
		if (!checkCanReadSize(8)) {
			return -1;
		}
		long result = ((((long) _buffer[position + 0] & 0xff) << 56)
				| (((long) _buffer[position + 1] & 0xff) << 48)
				| (((long) _buffer[position + 2] & 0xff) << 40)
				| (((long) _buffer[position + 3] & 0xff) << 32)
				| (((long) _buffer[position + 4] & 0xff) << 24)
				| (((long) _buffer[position + 5] & 0xff) << 16)
				| (((long) _buffer[position + 6] & 0xff) << 8) | (((long) _buffer[position + 7] & 0xff) << 0));
		position += 8;
		return result;
	}

	public void writeLong(long value) {
		increaceContenSize(8);
		write((byte) (value >> 0));
		write((byte) (value >> 8));
		write((byte) (value >> 16));
		write((byte) (value >> 24));
		write((byte) (value >> 32));
		write((byte) (value >> 40));
		write((byte) (value >> 48));
		write((byte) (value >> 56));
	}

	public float readfloat() {
		if (!checkCanReadSize(4)) {
			return -1;
		}
		int l;
		l = _buffer[position + 3];
		l &= 0xff;
		l |= ((long) _buffer[position + 2] << 8);
		l &= 0xffff;
		l |= ((long) _buffer[position + 1] << 16);
		l &= 0xffffff;
		l |= ((long) _buffer[position + 0] << 24);
		position += 4;
		return Float.intBitsToFloat(l);
	}

	public void writeShort(short value) {
		increaceContenSize(2);
		write((byte) (value >> 0));
		write((byte) (value >> 8));
	}

	public short readShort() {
		if (!checkCanReadSize(2)) {
			return -1;
		}
		// short ii = (short) ((((byte)0 << 8) | (byte)8 & 0xff));
		short result = (short) (((_buffer[position + 0] << 8) | _buffer[position + 1] & 0xff));
		position += 2;
		return result;
	}

	public void writeDouble(double value) {
		increaceContenSize(8);
		long l = Double.doubleToRawLongBits(value);
		for (int i = 0; i < 8; i++) {
			write((byte) ((l >> 8 * i) & 0xff));
		}
	}

	public double readDouble() {
		if (!checkCanReadSize(8)) {
			return -1;
		}
		long l;
		l = _buffer[position + 7];
		l &= 0xff;
		l |= ((long) _buffer[position + 6] << 8);
		l &= 0xffff;
		l |= ((long) _buffer[position + 5] << 16);
		l &= 0xffffff;
		l |= ((long) _buffer[position + 4] << 24);
		l &= 0xffffffffl;
		l |= ((long) _buffer[position + 3] << 32);
		l &= 0xffffffffffl;
		l |= ((long) _buffer[position + 2] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) _buffer[position + 1] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) _buffer[position + 0] << 56);
		position += 8;
		return Double.longBitsToDouble(l);
	}

	public void writeChar(char ch) {
		increaceContenSize(2);
		int temp = (int) ch;
		for (int i = 0; i < 2; i++) {
			write(new Integer(temp & 0xff).byteValue());// 将最高位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
	}

	/**
	 * 
	 */
	public char readChar() {
		int s = 0;
		int index = getContentSize() - 2;
		if (_buffer[position + 1] > 0)
			s += _buffer[position + 1];
		else
			s += 256 + _buffer[position + 0];
		s *= 256;
		if (_buffer[index + 0] > 0)
			s += _buffer[1];
		else
			s += 256 + _buffer[position + 0];
		char ch = (char) s;
		position += 2;
		return ch;
	}

	private boolean checkCanReadSize(int size) {
		if (getContentSize() < size) {
			try {
				throw new Exception(getClass().getName() + "  类型转换长度不足");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	/**
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

	/**
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

	/**
	 * 判断能增加内容能增加需要的长度
	 */
	protected void increaceContenSize(int size) {
		if (_buffer != null && _free < size) {
			int _len = _buffer.length + (size - _free);
			_length = _len;
			_free = size;
			byte[] temp = new byte[_len];
			System.arraycopy(_buffer, 0, temp, 0, getContentSize());
			_buffer = temp;
		}
	}

	/**
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
