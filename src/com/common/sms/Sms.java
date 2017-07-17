package com.common.sms;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 短信验证登录
 * 
 */
public class Sms {

	public String sendMsg(String phones, String content) throws Exception {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn"); // 该第三方短信服务地址
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
		NameValuePair[] data = { new NameValuePair("Uid", "liujingan"),
				new NameValuePair("Key", "234578"),
				new NameValuePair("smsMob", phones),
				new NameValuePair("smsText", content) };
		post.setRequestBody(data);
		client.executeMethod(post);
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		String result = new String(post.getResponseBodyAsString().getBytes(
				"gbk"));
		System.out.println(result); // 打印返回消息状态
		post.releaseConnection();
		return result;
	}

	/**
	 * 随机生成位随机验证码
	 * 
	 */
	public String createRandomVcode() {
		// 验证码
		String vcode = "";
		for (int i = 0; i < 4; i++) {
			vcode = vcode + (int) (Math.random() * 9.99999);
		}
		return vcode;
	}

}
