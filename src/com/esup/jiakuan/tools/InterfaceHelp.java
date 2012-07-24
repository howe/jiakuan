package com.esup.jiakuan.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Esup接口工具
 * 
 * @author yangq(qi.yang.cn@gmail.com) 2012-7-20
 */
public class InterfaceHelp {

	/** post Interface project */
	public static boolean queryUserByAccount(String account, String queryAccountUrl)
			throws Exception {

		StringBuilder getUrl = new StringBuilder(queryAccountUrl);
		getUrl.append(account);

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(getUrl.toString());
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			String tmp = EntityUtils.toString(entity);
			System.out.println("充值账户查询结果：" + tmp);
			if (tmp.equalsIgnoreCase("true")) {
				return true;
			}
		}

		return false;
	}

	/** 通过账户给用户加款 */
	public static boolean addMoneyByAccount(String account,
											String received_payment,
											Long tid,
											String addMoneyUrl) throws Exception {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("account", account));
		formparams.add(new BasicNameValuePair("received_payment", received_payment));
		formparams.add(new BasicNameValuePair("tid", "" + tid));
		UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
		HttpPost httppost = new HttpPost(addMoneyUrl);
		httppost.setEntity(uef);

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			String tmp = EntityUtils.toString(entity);
			System.out.println("用户账户加款结果：" + tmp);
			if (tmp.equalsIgnoreCase("true")) {
				return true;
			}
		}

		return false;
	}

	/** 加款日志 */
	public static boolean addMoneyLog(	String account,
										Long tid,
										String alipay_no,
										String buyer_nick,
										Date created,
										Date pay_time,
										Date end_time,
										String received_payment,
										String addMoneyLogUrl) throws Exception {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("account", account));
		formparams.add(new BasicNameValuePair("tid", "" + tid));
		formparams.add(new BasicNameValuePair("alipay_no", alipay_no));
		formparams.add(new BasicNameValuePair("buyer_nick", buyer_nick));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formparams.add(new BasicNameValuePair("created", sdf.format(created)));
		formparams.add(new BasicNameValuePair("pay_time", sdf.format(pay_time)));
		formparams.add(new BasicNameValuePair("end_time", sdf.format(end_time)));

		formparams.add(new BasicNameValuePair("received_payment", received_payment));
		UrlEncodedFormEntity uef = new UrlEncodedFormEntity(formparams, "UTF-8");
		HttpPost httppost = new HttpPost(addMoneyLogUrl);
		httppost.setEntity(uef);

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			String tmp = EntityUtils.toString(entity);
			System.out.println("加款日志操作结果：" + tmp);
			if (tmp.equalsIgnoreCase("true")) {
				return true;
			}
		}

		return false;

	}

	/** 通过支付宝流水号判断账户是否重复加款 */
	public static boolean isRepeatAddMoney(String alipay_no, String isRepeatAddMoneyUrl)
			throws Exception {
		StringBuilder getUrl = new StringBuilder(isRepeatAddMoneyUrl);
		getUrl.append(alipay_no);

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(getUrl.toString());
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			String tmp = EntityUtils.toString(entity);
			System.out.println("充值账户查询结果：" + tmp);
			if (tmp.equalsIgnoreCase("true")) {
				return true;
			}
		}

		return false;
	}

}
