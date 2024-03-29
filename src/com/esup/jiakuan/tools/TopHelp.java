package com.esup.jiakuan.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.LogisticsDummySendRequest;
import com.taobao.api.request.TradeCloseRequest;
import com.taobao.api.request.TradeGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.LogisticsDummySendResponse;
import com.taobao.api.response.TradeCloseResponse;
import com.taobao.api.response.TradeGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

/**
 * 淘宝TOP工具
 * 
 * @author yangq(qi.yang.cn@gmail.com) 2012-7-20
 */
public class TopHelp {
	private static final Log log = Logs.get();

	/** 查询指定时间段的未付款交易集合 */
	public static List<Trade> queryUnPaidTradeList(TaobaoClient client, String sessionKey, int cycle)
			throws Exception {

		/**
		 * 1. 搜索当前会话用户作为卖家已卖出的增量交易数据 2.
		 * 只能查询时间跨度为一天的增量交易记录：start_modified：2011-7-1 16:00:00 end_modified：
		 * 2011-7-2 15:59:59（注意不能写成16:00:00） 3. 返回数据结果为创建订单时间的倒序
		 * 
		 * 参考： http://api.taobao.com/apidoc/api.htm?path=cid:5-apiId:128
		 */

		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
		req.setFields("tid,alipay_no,buyer_nick,created,pay_time,consign_time,end_time,total_fee,received_payment,has_buyer_message,orders.oid,orders.title");

		/**
		 * 建议使用30分钟以内的时间跨度，能大大提高响应速度和成功率。
		 */
		String end = sdf.format(calendar.getTime());
		calendar.add(Calendar.MINUTE, cycle);
		String begin = sdf.format(calendar.getTime());

		System.out.println("搜索未付款交易时间为：" + begin + " 到 " + end);

		Date dateTime = SimpleDateFormat.getDateTimeInstance().parse(begin);
		req.setStartModified(dateTime);

		dateTime = SimpleDateFormat.getDateTimeInstance().parse(end);
		req.setEndModified(dateTime);

		req.setStatus("WAIT_BUYER_PAY");// 等待买家付款

		TradesSoldIncrementGetResponse response = client.execute(req, sessionKey);

		return response.getTrades();
	}

	/** 查询指定时间段的已付款交易集合 */
	public static List<Trade> queryPaidTradeList(TaobaoClient client, String sessionKey, int cycle)
			throws Exception {

		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
		req.setFields("tid,alipay_no,buyer_nick,created,pay_time,consign_time,end_time,total_fee,received_payment,has_buyer_message,orders.oid,orders.title");

		String end = sdf.format(calendar.getTime());
		calendar.add(Calendar.MINUTE, cycle);
		String begin = sdf.format(calendar.getTime());

		System.out.println("搜索已付款交易时间为：" + begin + " 到 " + end);

		Date dateTime = SimpleDateFormat.getDateTimeInstance().parse(begin);
		req.setStartModified(dateTime);

		dateTime = SimpleDateFormat.getDateTimeInstance().parse(end);
		req.setEndModified(dateTime);

		req.setStatus("WAIT_SELLER_SEND_GOODS");// 等待卖家发货，买家已付款

		TradesSoldIncrementGetResponse response = client.execute(req, sessionKey);

		return response.getTrades();
	}

	/** 查询指定时间段的已交易成功的交易集合 */
	public static List<Trade> queryFinishedTradeList(	TaobaoClient client,
														String sessionKey,
														int cycle) throws Exception {

		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
		req.setFields("tid,alipay_no,buyer_nick,created,pay_time,consign_time,end_time,total_fee,received_payment,has_buyer_message,orders.oid,orders.title");

		String end = sdf.format(calendar.getTime());
		calendar.add(Calendar.MINUTE, cycle);
		String begin = sdf.format(calendar.getTime());

		System.out.println("搜索已交易交易时间为：" + begin + " 到 " + end);

		Date dateTime = SimpleDateFormat.getDateTimeInstance().parse(begin);
		req.setStartModified(dateTime);

		dateTime = SimpleDateFormat.getDateTimeInstance().parse(end);
		req.setEndModified(dateTime);

		req.setStatus("TRADE_FINISHED");// 交易成功

		TradesSoldIncrementGetResponse response = client.execute(req, sessionKey);

		return response.getTrades();
	}

	/** 关闭指定的订单集合 */
	public static boolean closeOrderById(	TaobaoClient client,
											String sessionKey,
											List<Order> orders,
											Date created) throws Exception {

		/**
		 * 关闭一笔订单，可以是主订单或子订单。当订单从创建到关闭时间小于10s的时候，会报“ CLOSE_TRADE_TOO_FAST”错误。
		 * 
		 * 参考： http://api.taobao.com/apidoc/api.htm?path=cid:5-apiId:83
		 */

		Date date = new Date();
		if (date.getTime() - created.getTime() < 10000) {// 推迟10s执行关闭操作
			Thread.sleep(10000);
		}

		if (orders != null) {

			for (Order order : orders) {
				System.out.println("关闭子订单编号为：" + order.getOid());

				TradeCloseRequest req = new TradeCloseRequest();
				req.setTid(order.getOid());
				req.setCloseReason("2、信息填写错误，重新拍");
				TradeCloseResponse response = client.execute(req, sessionKey);
				System.out.println("关闭订单返回消息：" + response.getMsg());
			}
			return true;
		}
		return false;
	}

	/** 发货指定的虚拟物品订单集合 */
	public static boolean shipVirtualProduct(	TaobaoClient client,
												String sessionKey,
												List<Order> orders) throws Exception {
		if (orders != null) {

			for (Order order : orders) {
				LogisticsDummySendRequest req = new LogisticsDummySendRequest();
				req.setTid(order.getOid());
				LogisticsDummySendResponse response = client.execute(req, sessionKey);
				System.out.println("调用TOP错误：" + response.getMsg());
			}
			return true;
		}
		return false;
	}

	/** 获取单笔交易的买家留言（性能高） */
	public static String queryBuyerMessageByTid(TaobaoClient client, String sessionKey, Long tid)
			throws Exception {

		TradeGetRequest req = new TradeGetRequest();
		req.setFields("buyer_message");
		req.setTid(tid);
		TradeGetResponse response = client.execute(req, sessionKey);

		return response.getTrade().getBuyerMessage();
	}

}
