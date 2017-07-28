package com.common.net;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

public class ByteArray {

	/**
	 * 默认buff分配长度64字节
	 */
	private static int DEFALUT_SIZE = 64;

	/**
	 * 数据集
	 */
	private byte[] _buffer = null;

	/**
	 * 读写位置
	 */
	private int _position = 0;

	/**
	 * buff总长度
	 */
	private int _length = 0;

	/**
	 * 大小端
	 */
	private ByteOrder _orger = ByteOrder.BIG_ENDIAN;

	/**
	 * 空闲的长度
	 */
	private int _free = 0;

	private int _orginSize = 0;

	public ByteArray(int len) {
		if (len <= 0) {
			_length = DEFALUT_SIZE;
			_free = DEFALUT_SIZE;
			_buffer = new byte[DEFALUT_SIZE];
		} else {
			_length = len;
			_free = len;
			_buffer = new byte[len];
		}
		_orginSize = len;
	}

	public ByteArray() {
		_length = DEFALUT_SIZE;
		_free = DEFALUT_SIZE;
		_buffer = new byte[DEFALUT_SIZE];
		_orginSize = _length;
	}

	/**
	 * 写入一个字节返回写入的字节长度
	 */
	public synchronized int write(byte b) {
		if (_buffer == null)
			return 0;
		else {
			while (_free < 1) {
				increaceAutoSize(1);
			}
			_buffer[size()] = b;
			_free--;
			_position = size();
			return 1;
		}
	}

	public synchronized void writeByte(int value) {
		write((byte) value);
	}

	/**
	 * 读取一个byte值 成功读取返回0,失败返回-1
	 */
	public synchronized byte readByte() {
		if (!checkCanReadSize(1)) {
			return -1;
		}
		int index = _position;
		_position++;
		return _buffer[index + 0];
	}

	/**
	 * 写入字节数组返回写入的字节长度
	 */
	public synchronized int write(byte[] bytes, int len) {
		if (bytes == null || len <= 0)
			return 0;
		else {
			while (_free < len) {
				increaceAutoSize(len);
			}
			byte[] temp;
			if (len >= bytes.length) {
				temp = bytes;
			} else {
				temp = new byte[len];
				System.arraycopy(bytes, 0, temp, 0, len);
			}
			System.arraycopy(temp, 0, _buffer, size(), len);
			_free -= len;
			_position = size();
			return len;
		}
	}

	public synchronized int readBytes(byte b[], int offset, int len) {
		if (b == null) {
			throw new NullPointerException();
		} else if (offset < 0 || len < 0 || len > b.length - offset) {
			throw new IndexOutOfBoundsException();
		}
		if (_position >= size()) {
			return -1;
		}
		if (_position + len > size()) {
			len = size() - _position;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(_buffer, _position, b, offset, len);
		_position += len;
		return len;
	}

	public synchronized void writeInt(int value) {
		if (_orger == ByteOrder.BIG_ENDIAN) {
			write((byte) (value >> 24));
			write((byte) (value >> 16));
			write((byte) (value >> 8));
			write((byte) (value >> 0));
		} else {
			write((byte) (value >> 0));
			write((byte) (value >> 8));
			write((byte) (value >> 16));
			write((byte) (value >> 24));
		}
	}

	public synchronized int readInt() {
		if (!checkCanReadSize(4)) {
			return -1;
		}
		int result = 0;
		if (_orger == ByteOrder.BIG_ENDIAN) {
			result = _buffer[_position + 3] & 0xFF
					| (_buffer[_position + 2] & 0xFF) << 8
					| (_buffer[_position + 1] & 0xFF) << 16
					| (_buffer[_position + 0] & 0xFF) << 24;
		} else {
			result = _buffer[_position + 0] & 0xFF
					| (_buffer[_position + 1] & 0xFF) << 8
					| (_buffer[_position + 2] & 0xFF) << 16
					| (_buffer[_position + 3] & 0xFF) << 24;
		}
		_position += 4;
		return result;
	}

	public synchronized long readLong() {
		if (!checkCanReadSize(8)) {
			return -1;
		}
		long result = 0;
		if (_orger == ByteOrder.BIG_ENDIAN) {
			result = ((((long) _buffer[_position + 0] & 0xff) << 56)
					| (((long) _buffer[_position + 1] & 0xff) << 48)
					| (((long) _buffer[_position + 2] & 0xff) << 40)
					| (((long) _buffer[_position + 3] & 0xff) << 32)
					| (((long) _buffer[_position + 4] & 0xff) << 24)
					| (((long) _buffer[_position + 5] & 0xff) << 16)
					| (((long) _buffer[_position + 6] & 0xff) << 8) | (((long) _buffer[_position + 7] & 0xff) << 0));
		} else {
			result = ((((long) _buffer[_position + 7] & 0xff) << 56)
					| (((long) _buffer[_position + 6] & 0xff) << 48)
					| (((long) _buffer[_position + 5] & 0xff) << 40)
					| (((long) _buffer[_position + 4] & 0xff) << 32)
					| (((long) _buffer[_position + 3] & 0xff) << 24)
					| (((long) _buffer[_position + 2] & 0xff) << 16)
					| (((long) _buffer[_position + 1] & 0xff) << 8) | (((long) _buffer[_position + 0] & 0xff) << 0));
		}
		_position += 8;
		return result;
	}

	public synchronized void writeLong(long value) {
		if (_orger == ByteOrder.BIG_ENDIAN) {
			write((byte) (value >> 56));
			write((byte) (value >> 48));
			write((byte) (value >> 40));
			write((byte) (value >> 32));
			write((byte) (value >> 24));
			write((byte) (value >> 16));
			write((byte) (value >> 8));
			write((byte) (value >> 0));
		} else {
			write((byte) (value >> 0));
			write((byte) (value >> 8));
			write((byte) (value >> 16));
			write((byte) (value >> 24));
			write((byte) (value >> 32));
			write((byte) (value >> 40));
			write((byte) (value >> 48));
			write((byte) (value >> 56));
		}
	}

	public synchronized float readfloat() {
		if (!checkCanReadSize(4)) {
			return -1;
		}
		int l;
		if (_orger == ByteOrder.BIG_ENDIAN) {
			l = _buffer[_position + 3];
			l &= 0xff;
			l |= ((long) _buffer[_position + 2] << 8);
			l &= 0xffff;
			l |= ((long) _buffer[_position + 1] << 16);
			l &= 0xffffff;
			l |= ((long) _buffer[_position + 0] << 24);
		} else {
			l = _buffer[_position + 0];
			l &= 0xff;
			l |= ((long) _buffer[_position + 1] << 8);
			l &= 0xffff;
			l |= ((long) _buffer[_position + 2] << 16);
			l &= 0xffffff;
			l |= ((long) _buffer[_position + 3] << 24);
		}
		_position += 4;
		return Float.intBitsToFloat(l);
	}

	public synchronized void writeFloat(float value) {
		int i = 0;
		int l = Float.floatToIntBits(value);
		if (_orger == ByteOrder.BIG_ENDIAN) {
			for (i = 0; i < 4; i++) {
				write((byte) ((l >> 8 * (3 - i)) & 0xff));
			}
		} else {
			for (i = 0; i < 4; i++) {
				write((byte) ((l >> 8 * i) & 0xff));
			}
		}
	}

	public synchronized void writeShort(int value) {
		if (_orger == ByteOrder.BIG_ENDIAN) {
			write((byte) (value >> 8));
			write((byte) (value >> 0));
		} else {
			write((byte) (value >> 0));
			write((byte) (value >> 8));
		}
	}

	public synchronized short readShort() {
		if (!checkCanReadSize(2)) {
			return -1;
		}
		short result = 0;
		if (_orger == ByteOrder.BIG_ENDIAN) {
			result = (short) (((_buffer[_position + 0] << 8) | _buffer[_position + 1] & 0xff));
		} else {
			result = (short) (((_buffer[_position + 1] << 8) | _buffer[_position + 0] & 0xff));
		}
		_position += 2;
		return result;
	}

	public synchronized void writeDouble(double value) {
		long l = Double.doubleToRawLongBits(value);
		int i = 0;
		if (_orger == ByteOrder.BIG_ENDIAN) {
			for (i = 0; i < 8; i++) {
				write((byte) ((l >> 8 * (7 - i)) & 0xff));
			}
		} else {
			for (i = 0; i < 8; i++) {
				write((byte) ((l >> 8 * i) & 0xff));
			}
		}
	}

	public synchronized double readDouble() {
		if (!checkCanReadSize(8)) {
			return -1;
		}
		long l;
		if (_orger == ByteOrder.BIG_ENDIAN) {
			l = _buffer[_position + 7];
			l &= 0xff;
			l |= ((long) _buffer[_position + 6] << 8);
			l &= 0xffff;
			l |= ((long) _buffer[_position + 5] << 16);
			l &= 0xffffff;
			l |= ((long) _buffer[_position + 4] << 24);
			l &= 0xffffffffl;
			l |= ((long) _buffer[_position + 3] << 32);
			l &= 0xffffffffffl;
			l |= ((long) _buffer[_position + 2] << 40);
			l &= 0xffffffffffffl;
			l |= ((long) _buffer[_position + 1] << 48);
			l &= 0xffffffffffffffl;
			l |= ((long) _buffer[_position + 0] << 56);
		} else {
			l = _buffer[_position + 0];
			l &= 0xff;
			l |= ((long) _buffer[_position + 1] << 8);
			l &= 0xffff;
			l |= ((long) _buffer[_position + 2] << 16);
			l &= 0xffffff;
			l |= ((long) _buffer[_position + 3] << 24);
			l &= 0xffffffffl;
			l |= ((long) _buffer[_position + 4] << 32);
			l &= 0xffffffffffl;
			l |= ((long) _buffer[_position + 5] << 40);
			l &= 0xffffffffffffl;
			l |= ((long) _buffer[_position + 6] << 48);
			l &= 0xffffffffffffffl;
			l |= ((long) _buffer[_position + 7] << 56);
		}
		_position += 8;
		return Double.longBitsToDouble(l);
	}

	public synchronized void writeChar(char ch) {
		int l = (int) ch;
		int i = 0;
		// if (_orger == ByteOrder.BIG_ENDIAN) {
		// for (i = 0; i < 2; i++) {
		// write((byte) ((l >> 8 * (1 - i)) & 0xff));
		// }
		// } else {

		for (i = 0; i < 2; i++) {
			// 将最高位保存在最低位
			write(new Integer(l & 0xff).byteValue());
			l = l >> 8; // 向右移8位
		}

		// for (i = 0; i < 2; i++) {
		// write((byte) ((l >> 8 * i) & 0xff));
		// }
		// }
	}

	/**
	 * 
	 */
	public synchronized char readChar() {
		int s = 0;
		// if (_orger == ByteOrder.BIG_ENDIAN) {
		// if (_buffer[_position + 0] > 0)
		// s += _buffer[_position + 0];
		// else
		// s += 256 + _buffer[_position + 1];
		// s *= 256;
		// if (_buffer[_position + 1] > 0)
		// s += _buffer[_position + 0];
		// else
		// s += 256 + _buffer[_position + 1];
		//
		// } else {

		if (_buffer[_position + 1] > 0)
			s += _buffer[_position + 1];
		else
			s += 256 + _buffer[_position + 0];
		s *= 256;
		if (_buffer[_position + 0] > 0)
			s += _buffer[_position + 1];
		else
			s += 256 + _buffer[_position + 0];
		// }

		char ch = (char) s;
		_position += 2;
		return ch;
	}

	public synchronized void writeBoolean(boolean value) {
		byte l = (byte) (value ? 1 : 0);
		write(l);
	}

	public synchronized boolean readBoolean() {
		if (!checkCanReadSize(1)) {
			return false;
		}
		boolean result = _buffer[_position + 0] == 1 ? true : false;
		_position += 1;
		return result;
	}

	public synchronized void writeUTF(String value)
			throws UnsupportedEncodingException {
		byte b[] = value.getBytes("UTF-8");
		writeShort(b.length);
		write(b, b.length);
	}

	public synchronized String readUTF() throws UnsupportedEncodingException {
		if (!checkCanReadSize(3)) {
			return "";
		}
		int len = readShort();
		if (available() < len) {
			throw new IndexOutOfBoundsException(getClass().getName()
					+ "  字符串长度不足");
		}
		byte b[] = new byte[len];
		readBytes(b, 0, len);
		String result = new String(b, "UTF-8");
		return result;
	}

	private boolean checkCanReadSize(int len) {
		boolean result = true;
		if (available() < len) {
			result = false;
			if (_buffer == null) {
				throw new NullPointerException(getClass().getName() + "  数据为空");
			} else {
				throw new IndexOutOfBoundsException(getClass().getName()
						+ "  类型转换长度不足");
			}
		}
		return result;
	}

	/**
	 * 获取整个buff内容
	 */
	public synchronized byte[] getData() {
		if (size() <= 0)
			return new byte[0];
		else {
			byte[] temp = new byte[size()];
			readBytes(temp, 0, size());
			return temp;
		}
	}

	/**
	 * 判断能增加内容能增加需要的长度
	 */
	private void increaceAutoSize(int size) {
		if (_buffer != null && _free < size) {
			increaceContenSize(DEFALUT_SIZE);
		}
	}

	/**
	 * 判断能增加内容能增加需要的长度
	 */
	private void increaceContenSize(int size) {
		if (_buffer != null && _free < size) {
			int _len = _buffer.length + (size - _free);
			_length = _len;
			_free = size;
			byte[] temp = new byte[_len];
			System.arraycopy(_buffer, 0, temp, 0, size());
			_buffer = temp;
		}
	}

	/**
	 * 内容的长度
	 */
	public int size() {
		return _length - _free;
	}

	/**
	 * [只读] 可从字节数组的当前位置到数组末尾读取的数据的字节数。 每次访问 ByteArray 对象时，将 available
	 * 属性与读取方法结合使用，以确保读取有效的数据。
	 * 
	 * @return
	 */
	public int available() {
		int result = size() - _position;
		return result;
	}

	/**
	 * 清除
	 */
	public void clear() {
		_position = 0;
		if (_length != _orginSize) {
			// 纠结
			_buffer = new byte[_orginSize];
		}
		_length = _orginSize;
		_free = _orginSize;
	}

	public int getPosition() {
		return _position;
	}

	public void setPosition(int position) {
		this._position = Math.max(0, Math.min(position, size()));
	}

	public ByteOrder getOrger() {
		return _orger;
	}

	public void setOrger(ByteOrder orger) {
		if (orger == ByteOrder.LITTLE_ENDIAN) {
			this._orger = ByteOrder.LITTLE_ENDIAN;
		} else {
			this._orger = ByteOrder.BIG_ENDIAN;
		}
	}
}
