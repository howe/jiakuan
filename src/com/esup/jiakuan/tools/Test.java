package com.esup.jiakuan.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.service.EntityService;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.LogisticsDummySendRequest;
import com.taobao.api.request.TradeCloseRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.LogisticsDummySendResponse;
import com.taobao.api.response.TradeCloseResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

@At
@IocBean(fields = {"dao"})
public class Test extends EntityService<Object> {
	private static final Log log = Logs.get();

	private static String url = "http://gw.api.taobao.com/router/rest";
	private static String appkey = "12482076";
	private static String secret = "8cdd79bc1a6b340696512fb64d8f3a3f";
	private static String sessionKey = "610151840891a184594e7e6a011a49573dafdf78b18e67726003866";

	private static final TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);

	/** 将已经付款的有效订单发货 */
	@At
	public void t() throws Exception {

		List<Trade> list = queryPaidTradeList(Calendar.getInstance());// 查询已付款的交易集合

		if (list != null) {
			for (Trade trade : list) {
				// System.out.println(trade.getBuyerNick());

				// System.out.println(trade.getReceiverAddress());// 虚拟商品收货区服备注等

				// String[] tmp = trade.getReceiverAddress().split("\n");

				String account = trade.getReceiverAddress();

				if (account != null && !account.equalsIgnoreCase("")) {// 备注填写正确

					// String account = tmp[2].split(":")[1];

					System.out.println("网站账户号:" + account);// 网站账户号
					// System.out.println(queryUserByAccount(account));// 网站会员充值

					shipVirtualProduct(trade.getOrders());// 发货

				} else {// 备注未填写或填写错误

					System.out.println("订单备注填写不正确，执行关闭订单操作");
					closeOrderById(trade.getOrders(), trade.getCreated());
				}

			}
		} else {
			System.out.println("当前买家已付款交易数量为0");
		}

	}

	/** 查询指定时间段的已付款交易集合 */
	public List<Trade> queryPaidTradeList(Calendar calendar) throws Exception {

		/**
		 * 1. 搜索当前会话用户作为卖家已卖出的增量交易数据 2.
		 * 只能查询时间跨度为一天的增量交易记录：start_modified：2011-7-1 16:00:00 end_modified：
		 * 2011-7-2 15:59:59（注意不能写成16:00:00） 3. 返回数据结果为创建订单时间的倒序
		 * 
		 * 参考： http://api.taobao.com/apidoc/api.htm?path=cid:5-apiId:128
		 */

		TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
		req.setFields("seller_nick,buyer_nick,title,type,created,sid,tid,seller_rate,buyer_rate,buyer_message,buyer_memo,trade_memo,status,payment,discount_fee,adjust_fee,post_fee,total_fee,pay_time,end_time,modified,consign_time,buyer_obtain_point_fee,point_fee,real_point_fee,received_payment,commission_fee,pic_path,num_iid,num_iid,num,price,cod_fee,cod_status,shipping_type,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,orders.title,orders.pic_path,orders.price,orders.num,orders.iid,orders.num_iid,orders.sku_id,orders.refund_status,orders.status,orders.oid,orders.total_fee,orders.payment,orders.discount_fee,orders.adjust_fee,orders.sku_properties_name,orders.item_meal_name,orders.buyer_rate,orders.seller_rate,orders.outer_iid,orders.outer_sku_id,orders.refund_id,orders.seller_type");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tmp = sdf.format(calendar.getTime());

		calendar.add(Calendar.MINUTE, -15);
		Date dateTime = SimpleDateFormat.getDateTimeInstance()
										.parse(sdf.format(calendar.getTime()));
		req.setStartModified(dateTime);

		dateTime = SimpleDateFormat.getDateTimeInstance().parse(tmp);
		req.setEndModified(dateTime);

		req.setStatus("WAIT_SELLER_SEND_GOODS");// 买家已付款

		TradesSoldIncrementGetResponse response = client.execute(req, sessionKey);

		return response.getTrades();
	}

	/** 查询指定时间段的已交易成功的交易集合 */
	public List<Trade> queryFinishedTradeList(Calendar calendar) throws Exception {

		/**
		 * 1. 搜索当前会话用户作为卖家已卖出的增量交易数据 2.
		 * 只能查询时间跨度为一天的增量交易记录：start_modified：2011-7-1 16:00:00 end_modified：
		 * 2011-7-2 15:59:59（注意不能写成16:00:00） 3. 返回数据结果为创建订单时间的倒序
		 * 
		 * 参考： http://api.taobao.com/apidoc/api.htm?path=cid:5-apiId:128
		 */

		TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
		req.setFields("seller_nick,buyer_nick,title,type,created,sid,tid,seller_rate,buyer_rate,buyer_message,buyer_memo,trade_memo,status,payment,discount_fee,adjust_fee,post_fee,total_fee,pay_time,end_time,modified,consign_time,buyer_obtain_point_fee,point_fee,real_point_fee,received_payment,commission_fee,pic_path,num_iid,num_iid,num,price,cod_fee,cod_status,shipping_type,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,orders.title,orders.pic_path,orders.price,orders.num,orders.iid,orders.num_iid,orders.sku_id,orders.refund_status,orders.status,orders.oid,orders.total_fee,orders.payment,orders.discount_fee,orders.adjust_fee,orders.sku_properties_name,orders.item_meal_name,orders.buyer_rate,orders.seller_rate,orders.outer_iid,orders.outer_sku_id,orders.refund_id,orders.seller_type");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tmp = sdf.format(calendar.getTime());

		calendar.add(Calendar.MINUTE, -15);
		Date dateTime = SimpleDateFormat.getDateTimeInstance()
										.parse(sdf.format(calendar.getTime()));
		req.setStartModified(dateTime);

		dateTime = SimpleDateFormat.getDateTimeInstance().parse(tmp);
		req.setEndModified(dateTime);

		req.setStatus("TRADE_FINISHED");// 交易成功

		TradesSoldIncrementGetResponse response = client.execute(req, sessionKey);

		return response.getTrades();
	}

	/** 关闭指定的订单集合 */
	public boolean closeOrderById(List<Order> orders, Date created) throws Exception {

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
				System.out.println(order.getOid());

				TradeCloseRequest req = new TradeCloseRequest();
				req.setTid(order.getOid());
				req.setCloseReason("2、信息填写错误，重新拍");
				TradeCloseResponse response = client.execute(req, sessionKey);
				System.out.println(response.getMsg());
			}
			return true;
		}
		return false;
	}

	/** 发货指定的虚拟物品订单集合 */
	public boolean shipVirtualProduct(List<Order> orders) throws Exception {
		if (orders != null) {

			for (Order order : orders) {
				System.out.println(order.getOid());

				LogisticsDummySendRequest req = new LogisticsDummySendRequest();
				req.setTid(order.getOid());
				LogisticsDummySendResponse response = client.execute(req, sessionKey);
				System.out.println(response.getMsg());
			}
			return true;
		}
		return false;
	}

	/** 通过网站账户名精确查询用户 */
	public boolean queryUserByAccount(String account) {
		Sql sql = Sqls.create("select count(t.user_id) num from es_users t where t.user_name = @name");
		sql.params().set("name", account);

		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				rs.next();
				return rs.getInt("num");
			}
		});
		dao().execute(sql);

		int i = sql.getInt();

		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

}
