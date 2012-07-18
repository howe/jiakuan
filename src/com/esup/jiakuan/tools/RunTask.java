package com.esup.jiakuan.tools;

import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.mvc.annotation.At;

/**
 * 执行任务
 * 
 * @author yangq(qi.yang.cn@gmail.com) 2012-7-18
 */
@At("runtask")
public class RunTask {

	public void run() {
		System.out.println("running...");
	}

	public static void main(String[] args) {
		Ioc ioc = new NutIoc(new JsonLoader("conf/ioc/dao.js"));
		Dao dao = ioc.get(Dao.class, "dao");
		System.out.println(dao);
	}

}
