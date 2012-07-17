package com.esup.jiakuan.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.service.EntityService;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

@At
@IocBean(fields = {"dao"})
public class Test extends EntityService<Object> {
	private static final Log log = Logs.get();

	private static String url = "http://gw.api.taobao.com/router/rest";
	private static String appkey = "12482076";
	private static String secret = "8cdd79bc1a6b340696512fb64d8f3a3f";
	private static String sessionKey = "6101a29d527b18c5644e8fc52add8c0969694188b2d68e126003866";

	@At
	public static void t() throws Exception {

		// TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		// TopatsTradesSoldGetRequest req = new TopatsTradesSoldGetRequest();
		//
		// req.setStartTime("20120714");
		// req.setEndTime("20120715");
		// req.setFields("tid,seller_nick,buyer_nick,payment");
		//
		// TopatsTradesSoldGetResponse tsr = client.execute(req, sessionKey);
		// System.out.println(tsr.getTask());

		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
		req.setFields("seller_nick,buyer_nick,title,type,created,sid,tid,seller_rate,buyer_rate,buyer_message,buyer_memo,trade_memo,status,payment,discount_fee,adjust_fee,post_fee,total_fee,pay_time,end_time,modified,consign_time,buyer_obtain_point_fee,point_fee,real_point_fee,received_payment,commission_fee,pic_path,num_iid,num_iid,num,price,cod_fee,cod_status,shipping_type,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,orders.title,orders.pic_path,orders.price,orders.num,orders.iid,orders.num_iid,orders.sku_id,orders.refund_status,orders.status,orders.oid,orders.total_fee,orders.payment,orders.discount_fee,orders.adjust_fee,orders.sku_properties_name,orders.item_meal_name,orders.buyer_rate,orders.seller_rate,orders.outer_iid,orders.outer_sku_id,orders.refund_id,orders.seller_type");
		Date dateTime = SimpleDateFormat.getDateTimeInstance().parse("2012-07-17 12:44:00");
		req.setStartModified(dateTime);
		dateTime = SimpleDateFormat.getDateTimeInstance().parse("2012-07-17 12:46:00");
		req.setEndModified(dateTime);
		// req.setStatus("WAIT_SELLER_SEND_GOODS");// 买家已付款
		TradesSoldIncrementGetResponse response = client.execute(req, sessionKey);

		// ObjectJsonParser<TradeFullinfoGetResponse> parser = new
		// ObjectJsonParser<TradeFullinfoGetResponse>(TradeFullinfoGetResponse.class);
		// TradeFullinfoGetResponse rsp = parser.parse(response.getBody());
		// Trade trade = rsp.getTrade();

		List<Trade> list = response.getTrades();
		// response.getTotalResults();

		for (Trade trade : list) {
			// System.out.println(trade.getBuyerNick());

			// System.out.println(trade.getReceiverAddress());// 虚拟商品收货区服

			String[] tmp = trade.getReceiverAddress().split("\n");
			int i = 1;
			for (String s : tmp) {
				if (i != 1) {
					System.out.print(s);
				}
				i++;
			}

			// List<Order> temp = trade.getOrders();
			// for (Order order : temp) {
			// System.out.println(order.getNumIid() + "," + order.getTitle());
			// }

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}

	}

	public static void main(String[] args) throws Exception {
		Test.t();
	}

}
