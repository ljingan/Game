package com.common.sms;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 短信验证登录
 * 
 */
public class Sms {

	public String sendMsg(String phones, String content) {
		// 短信接口URL提交地址
		String url = "http://gbk.sms.webchinese.cn";

		Map<String, String> params = new HashMap<String, String>();

		params.put("zh", "用户账号");
		params.put("mm", "用户密码");
		params.put("dxlbid", "短信类别编号");
		params.put("extno", "扩展编号");

		// 手机号码，多个号码使用英文逗号进行分割
		params.put("hm", phones);
		// 将短信内容进行URLEncoder编码
		params.put("nr", content);

		return postRequest(url, params);
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

	/**
	 * HttpClient 模拟POST请求 方
	 */
	public String postRequest(String url, Map<String, String> params) {
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();

		// 创建POST方法的实例
		PostMethod postMethod = new PostMethod(url);

		// 设置请求头信息
		postMethod.setRequestHeader("Connection", "close");

		// 添加参数
		for (Map.Entry<String, String> entry : params.entrySet()) {
			postMethod.addParameter(entry.getKey(), entry.getValue());
		}

		// 使用系统提供的默认的恢复策略,设置请求重试处理，用的是默认的重试处理：请求三次
		httpClient.getParams().setBooleanParameter(
				"http.protocol.expect-continue", false);

		// 接收处理结果
		String result = null;
		try {
			// 执行Http Post请求
			httpClient.executeMethod(postMethod);

			// 返回处理结果
			result = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("请检查输入的URL!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			System.out.println("发生网络异常!");
			e.printStackTrace();
		} finally {
			// 释放链接
			postMethod.releaseConnection();

			// 关闭HttpClient实例
			if (httpClient != null) {
				((SimpleHttpConnectionManager) httpClient
						.getHttpConnectionManager()).shutdown();
				httpClient = null;
			}
		}
		return result;
	}

	/**
	 * HttpClient 模拟GET请求
	 */
	public String getRequest(String url, Map<String, String> params) {
		// 构造HttpClient实例
		HttpClient client = new HttpClient();

		// 拼接参数
		String paramStr = "";
		for (String key : params.keySet()) {
			paramStr = paramStr + "&" + key + "=" + params.get(key);
		}
		paramStr = paramStr.substring(1);

		// 创建GET方法的实例
		GetMethod method = new GetMethod(url + "?" + paramStr);

		// 接收返回结果
		String result = null;
		try {
			// 执行HTTP GET方法请求
			client.executeMethod(method);

			// 返回处理结果
			result = method.getResponseBodyAsString();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("请检查输入的URL!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			System.out.println("发生网络异常!");
			e.printStackTrace();
		} finally {
			// 释放链接
			method.releaseConnection();

			// 关闭HttpClient实例
			if (client != null) {
				((SimpleHttpConnectionManager) client
						.getHttpConnectionManager()).shutdown();
				client = null;
			}
		}
		return result;
	}
}
