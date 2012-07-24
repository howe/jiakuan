package com.esup.jiakuan.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws Exception {

		Calendar calendar = Calendar.getInstance();

		Calendar tmp = calendar;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		System.out.println(sdf.format(calendar.getTime()));

		calendar.add(Calendar.MINUTE, 30);
		System.out.println(sdf.format(calendar.getTime()));
		System.out.println(sdf.format(tmp.getTime()));

		Date date = new Date();
		calendar.setTime(date);

		System.out.println(sdf.format(Calendar.getInstance().getTime()));

		// Timer timer = new Timer();
		// timer.schedule(new MyTask(), 1000, 1000);
		//
		// while (true) {
		// int ch = System.in.read();
		// if (ch - 'c' == 0) {
		// timer.cancel();
		// }
		// }
	}

}

// class MyTask extends TimerTask {
//
// @Override
// public void run() {
// System.out.println("~~~~~~~~~~~~~~~~~~~");
// }
//
// }