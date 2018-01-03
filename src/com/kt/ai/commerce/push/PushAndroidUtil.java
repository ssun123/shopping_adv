package com.kt.ai.commerce.push;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class PushAndroidUtil {

	private String apiKey = "AAAAvJUXwx0:APA91bFJapNsVQ4FJmNUSSva4gRvHsP8S5y_ixwg-_NTvhrhLm3Bl8bEB82Oh0jQqdmaUWHbnuissqOixnXuF5w-Yd24QvXOSpt6z-XEktJO5tQY2rJ8bhDrYTk4Ke_brmp4moqlZW6I";
	
	public void sendMessage(String deviceToken, String title, String msg,String prodId,String url, int newcount) throws Exception {
		
		Sender sender = new Sender(apiKey);
				
		Message message = new Message.Builder()
							//.collapseKey("mTaxRefund")
							//.delayWhileIdle(true)
							//.timeToLive(3)
							//.addData("key1", name)
							//.addData("key2", msg)
							//.addData("etc", etc)
							//.addData("badgecount", Integer.toString(newcount))
							.addData("title", title)
							.addData("msg", msg)
							.addData("prodId", prodId)
							.addData("url", url)
							.addData("favoriteYn", "N")
							//.addData("badgecount", Integer.toString(newcount))
							.addData("badgecount", "0")
							.build();
		
		List<String> list = new ArrayList<String>();

		list.add(deviceToken);

		MulticastResult multiResult = sender.send(message, list, 5);
		
		System.out.println("multiResult : "+multiResult);

	}
	
	public static void main(String[] args) throws Exception {

		PushAndroidUtil s = new PushAndroidUtil();

		s.sendMessage("dlXOvJiYB5Q:APA91bGWt4VkzvOpx-S4FJn8r2MY5hhekDeHFMbt-u0NaR39O1clo8HzaJZGqeFLQKxdqM_tVSPNXijvIponiuceTI0ghbwz9sUuiiZ68bRNlvavPggVIJWmRE5ekotMcAfWYZSV9Yrc", "GiNi Shopping", "안녕하세요","12415434343", "http://www.lotteimall.com/coop/affilGate.lotte?chl_no=151776&chl_dtl_no=2550013&returnUrl=/goods/viewGoodsDetail.lotte?goods_no=10000069", 0);

		
	}

}
