package com.esup.jiakuan;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import com.esup.jiakuan.tools.InterfaceHelp;
import com.esup.jiakuan.tools.TopHelp;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;

public class MyTask extends TimerTask {

	private static String sessionKey = "";// TOP session密钥
	private static String flag = "";// 指定商品标识，模糊匹配
	private static TaobaoClient client = null;

	private static String queryAccountUrl;

	public MyTask() {}

	public MyTask(String sessionKey, String flag, TaobaoClient client, String queryAccountUrl) {
		this.sessionKey = sessionKey;
		this.flag = flag;
		this.client = client;
		this.queryAccountUrl = queryAccountUrl;
	}

	@Override
	public void run() {

		try {

			Calendar calendar = Calendar.getInstance();

			List<Trade> unPaidTrades = TopHelp.queryUnPaidTradeList(client, sessionKey, calendar);
			List<Trade> paidTrades = TopHelp.queryPaidTradeList(client, sessionKey, calendar);
			List<Trade> finishedTrades = TopHelp.queryFinishedTradeList(client,
																		sessionKey,
																		calendar);

			if (unPaidTrades != null) {
				for (Trade trade : unPaidTrades) {

					List<Order> list = trade.getOrders();
					for (Order order : list) {
						// System.out.println(order.getTitle());

						if (order.getTitle().indexOf(flag) != -1) {// 商品标识正确
							InterfaceHelp.queryUserByAccount(	trade.getBuyerMessage(),
																queryAccountUrl);
						} else {
							return;
						}
					}

				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
