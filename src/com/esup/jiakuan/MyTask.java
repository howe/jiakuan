package com.esup.jiakuan;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.esup.jiakuan.tools.InterfaceHelp;
import com.esup.jiakuan.tools.TopHelp;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;

public class MyTask extends TimerTask {

	private static String sessionKey = "";// TOP session密钥
	private static int cycle = -29;// 默认搜索时间差为29分钟
	private static String flag = "";// 指定商品标识，模糊匹配
	private static TaobaoClient client = null;

	private static String queryAccountUrl;
	private static String addMoneyUrl;
	private static String addMoneyLogUrl;
	private static String isRepeatAddMoneyUrl;

	public MyTask() {}

	public MyTask(	String sessionKey,
					int cycle,
					String flag,
					TaobaoClient client,
					String queryAccountUrl,
					String addMoneyUrl,
					String addMoneyLogUrl,
					String isRepeatAddMoneyUrl) {

		this.sessionKey = sessionKey;
		this.cycle = cycle;
		this.flag = flag;
		this.client = client;
		this.queryAccountUrl = queryAccountUrl;
		this.addMoneyUrl = addMoneyUrl;
		this.addMoneyLogUrl = addMoneyLogUrl;
		this.isRepeatAddMoneyUrl = isRepeatAddMoneyUrl;

	}

	@Override
	public void run() {

		try {
			List<Trade> unPaidTrades = TopHelp.queryUnPaidTradeList(client, sessionKey, cycle);
			List<Trade> paidTrades = TopHelp.queryPaidTradeList(client, sessionKey, cycle);
			List<Trade> finishedTrades = TopHelp.queryFinishedTradeList(client, sessionKey, cycle);

			/** 买家未付款 */
			if (unPaidTrades != null) {
				for (Trade trade : unPaidTrades) {

					List<Order> list = trade.getOrders();
					for (Order order : list) {

						if (order.getTitle().indexOf(flag) != -1) {// 商品标识正确

							if (!trade.getHasBuyerMessage()) {// 此类商品未填写用户留言自动关闭交易
								TopHelp.closeOrderById(client, sessionKey, list, trade.getCreated());
							}

						} else {
							return;
						}
					}

				}
			}

			/** 买家已付款 */
			if (paidTrades != null) {

				for (Trade trade : paidTrades) {

					List<Order> list = trade.getOrders();
					for (Order order : list) {

						if (order.getTitle().indexOf(flag) != -1) {// 商品标识正确

							if (trade.getHasBuyerMessage()) {// 买家填写留言

								String account = TopHelp.queryBuyerMessageByTid(client,
																				sessionKey,
																				trade.getTid());

								if (InterfaceHelp.queryUserByAccount(account, queryAccountUrl)) {// 充值账户有效，交易发货

									System.out.println("发货状态："
														+ TopHelp.shipVirtualProduct(	client,
																						sessionKey,
																						list));

								} else {// 记录异常

									System.out.println("该笔已付款交易充值账户异常，账户名为：" + account);

								}
							}

						} else {
							return;
						}
					}

				}

			}

			/** 买家已收货 */
			if (finishedTrades != null) {

				for (Trade trade : finishedTrades) {

					List<Order> list = trade.getOrders();
					for (Order order : list) {

						if (order.getTitle().indexOf(flag) != -1) {// 商品标识正确

							if (trade.getHasBuyerMessage()) {// 买家填写留言

								String account = TopHelp.queryBuyerMessageByTid(client,
																				sessionKey,
																				trade.getTid());

								if (InterfaceHelp.queryUserByAccount(account, queryAccountUrl)) {// 充值账户有效，账户充值

									int i = 0;
									if (i == 0) {// 安全起见，默认一个交易只能充值一次!!!

										// tid,alipay_no,buyer_nick,created,pay_time,consign_time,end_time,total_fee,received_payment,has_buyer_message,orders.oid,orders.title

										Long tid = trade.getTid();
										String alipay_no = trade.getAlipayNo();
										String buyer_nick = trade.getBuyerNick();
										Date created = trade.getCreated();
										Date pay_time = trade.getPayTime();
										Date end_time = trade.getEndTime();
										String received_payment = trade.getReceivedPayment();

										if (InterfaceHelp.isRepeatAddMoney(	alipay_no,
																			isRepeatAddMoneyUrl)) {// 重复加款
											i++;// ?
											return;
										}

										// 加款
										InterfaceHelp.addMoneyByAccount(account,
																		received_payment,
																		tid,
																		addMoneyUrl);

										// 日志
										InterfaceHelp.addMoneyLog(	account,
																	tid,
																	alipay_no,
																	buyer_nick,
																	created,
																	pay_time,
																	end_time,
																	received_payment,
																	addMoneyLogUrl);
										i++;
									}

								} else {// 记录异常

									System.out.println("该笔已收货交易充值账户异常，账户名为：" + account);

								}
							}

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
		finally {

		}

	}
}
