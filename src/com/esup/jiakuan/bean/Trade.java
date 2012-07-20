package com.esup.jiakuan.bean;

import java.util.Date;

/**
 * 淘宝支付实体
 * 
 * @author yangq(qi.yang.cn@gmail.com) 2012-7-20
 */
public class Trade {

	// alipay_no,buyer_nick,created,pay_time,consign_time,end_time,total_fee,received_payment,has_buyer_message

	private String alipay_no;// 支付宝交易号
	private String buyer_nick;// 买家昵称
	private Date created;// 交易创建时间
	private Date pay_time;// 买家付款时间
	private Date consign_time;// 卖家发货时间
	private Date end_time;// 交易结束时间
	private Double total_fee;// 商品总金额
	private Double received_payment;// 实际到账金额
	private boolean has_buyer_message;// 判断订单是否有买家留言
	private String buyer_message;// 买家留言

	public String getAlipay_no() {
		return alipay_no;
	}

	public void setAlipay_no(String alipay_no) {
		this.alipay_no = alipay_no;
	}

	public String getBuyer_nick() {
		return buyer_nick;
	}

	public void setBuyer_nick(String buyer_nick) {
		this.buyer_nick = buyer_nick;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getPay_time() {
		return pay_time;
	}

	public void setPay_time(Date pay_time) {
		this.pay_time = pay_time;
	}

	public Date getConsign_time() {
		return consign_time;
	}

	public void setConsign_time(Date consign_time) {
		this.consign_time = consign_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public Double getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Double total_fee) {
		this.total_fee = total_fee;
	}

	public Double getReceived_payment() {
		return received_payment;
	}

	public void setReceived_payment(Double received_payment) {
		this.received_payment = received_payment;
	}

	public boolean isHas_buyer_message() {
		return has_buyer_message;
	}

	public void setHas_buyer_message(boolean has_buyer_message) {
		this.has_buyer_message = has_buyer_message;
	}

	public String getBuyer_message() {
		return buyer_message;
	}

	public void setBuyer_message(String buyer_message) {
		this.buyer_message = buyer_message;
	}

}
