package com.common.net;

public class MessageBuffer {

	/*
	 * 
	 */
	private byte[] _buffer = null;

	/*
	 * buff总长度
	 */
	private int _length = 0;

	public MessageBuffer(int len) {

	}

	public MessageBuffer() {
		// if (nLen <= 0)
		// {
		// _length = MEM_INCREASE_SIZE;
		// _len_free = MEM_INCREASE_SIZE;
		// _buffer = new byte[MEM_INCREASE_SIZE];
		// }
		// else
		// {
		// _length = nLen;
		// _len_free = nLen;
		// _buffer = new byte[nLen];
		// }
	}

	// 写入一个字节
	// 返回写入的字节长度
	public int write(byte b) {
		if (_buffer == null)
			return 0;
		else {
//			while (_len_free < 1) {
//				IncreaceBufferSize();
//			}
//			_buffer[getDataSize()] = b;
//			_len_free--;

			return 1;
		}
	}

	// 写入字节数组
	// 返回写入的字节长度
	public int write(byte[] sBuffer, int nWriteSize) {
		if (sBuffer == null || nWriteSize <= 0)
			return 0;
		else {
//			while (_len_free < nWriteSize) {
//				IncreaceBufferSize();
//			}
//
//			byte[] _tmp;
//			if (nWriteSize >= sBuffer.Length) {
//				_tmp = sBuffer;
//			} else {
//				_tmp = new byte[nWriteSize];
//				Array.Copy(sBuffer, 0, _tmp, 0, nWriteSize);
//			}
//			_tmp.CopyTo(_buffer, getDataSize());
//			_len_free -= nWriteSize;
			return nWriteSize;
		}
	}

	public byte[] get_buffer() {
		return _buffer;
	}

	public void set_buffer(byte[] buffer) {
		_buffer = buffer;
	}

	public int get_length() {
		return _length;
	}

	public void set_length(int length) {
		_length = length;
	}
}
