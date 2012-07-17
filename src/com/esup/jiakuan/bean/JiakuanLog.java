package com.esup.jiakuan.bean;

import org.nutz.dao.entity.annotation.*;

/**
* 加款记录表
*/
@Table("es_jiakuan_log")
@PK({"logId"})
public class JiakuanLog {

	/**
	 * ID
	 */
	@Id
	@Column("log_id")
	private Integer logId;
	/**
	 * 拍下加款卡的淘宝或拍拍帐号
	 */
	@Column("buyer_nick")
	private String buyerNick;
	/**
	 * 加款的网站帐号
	 */
	@Column("user_name")
	private String userName;
	/**
	 * 订单编号（淘宝编号或者财付通编号）
	 */
	@Column("order_number")
	private String orderNumber;
	/**
	 * 支付编号（支付宝交易号或财付通订单号）
	 */
	@Column("payment_number")
	private String paymentNumber;
	/**
	 * 下单时间
	 */
	@Column("order_date")
	private java.util.Date orderDate;
	/**
	 * 付款时间
	 */
	@Column("payment_date")
	private java.util.Date paymentDate;
	/**
	 * 发货时间
	 */
	@Column("delivery_date")
	private java.util.Date deliveryDate;
	/**
	 * 确认时间
	 */
	@Column("confirm_date")
	private java.util.Date confirmDate;
	/**
	 * 加款充值表ID（es_account_log的log_id）
	 */
	@Column("account_id")
	private Integer accountId;
	
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getPaymentNumber() {
		return paymentNumber;
	}
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public java.util.Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}
	public java.util.Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(java.util.Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public java.util.Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(java.util.Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public java.util.Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(java.util.Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	
}