package com.esup.jiakuan.tools;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.mvc.annotation.At;

/**
 * 执行任务
 * 
 * @author yangq(qi.yang.cn@gmail.com) 2012-7-18
 */
@At("runtask")
public class RunTask {

	private static String url = "";// TOP正式版本链接
	private static String appkey = "";// TOP授权密钥
	private static String secret = "";// TOP授权加密字符串
	private static String sessionKey = "";// TOP session密钥
	private static String flag = "";// 指定商品标识，模糊匹配

	public static void main(String[] args) {
		initConfig();

	}

	/** 初始化应用程序配置 */
	public static boolean initConfig() {

		try {

			Ioc ioc = new NutIoc(new JsonLoader("conf/ioc/dao.js"));
			PropertiesProxy proxy = ioc.get(PropertiesProxy.class, "config");

			url = proxy.get("url");
			appkey = proxy.get("appkey");
			secret = proxy.get("secret");
			sessionKey = proxy.get("sessionKey");
			flag = proxy.get("flag");

			return true;
		}
		catch (Exception e) {

			return false;
		}

	}

}
