package com.esup.jiakuan;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.mvc.annotation.At;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;

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

	private static int cycle = -29;// 默认搜索时间差为29分钟
	private static String flag = "";// 指定商品标识，模糊匹配

	private static String queryAccountUrl = "";// 用户账户查询url
	private static String addMoneyUrl = "";// 用户账户加款url
	private static String addMoneyLogUrl = "";// 用户账户加款日志url
	private static String isRepeatAddMoneyUrl = "";// 判断用户账户是否重复加款url

	private static TaobaoClient client = null;

	public static void main(String[] args) throws Exception {
		initConfig();

		// Timer timer = new Timer();
		// timer.schedule(new MyTask(sessionKey, flag, client), 1000, 1000);

		while (true) {

			new MyTask(	sessionKey,
						cycle,
						flag,
						client,
						queryAccountUrl,
						addMoneyUrl,
						addMoneyLogUrl,
						isRepeatAddMoneyUrl).run();

			Thread.sleep(1000);

		}

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
			cycle = proxy.getInt("cycle");
			flag = proxy.get("flag");
			queryAccountUrl = proxy.get("queryAccountUrl");
			addMoneyUrl = proxy.get("addMoneyUrl");
			addMoneyLogUrl = proxy.get("addMoneyLogUrl");
			isRepeatAddMoneyUrl = proxy.get("isRepeatAddMoneyUrl");

			client = new DefaultTaobaoClient(url, appkey, secret);

			return true;
		}
		catch (Exception e) {

			return false;
		}

	}

	/** 标识符过滤 */
	public static boolean checkFlag(String tmp) {

		if (tmp.equals(flag)) {
			return true;
		} else {
			return false;
		}

	}

}
