package cc.messcat.gjfeng.common.wechat.popular.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import cc.messcat.gjfeng.common.pay.wechat.weixin.popular.bean.Ticket;
import cc.messcat.gjfeng.common.pay.wechat.weixin.popular.client.LocalHttpClient;



/**
 * JSAPI ticket
 * @author LiYi
 *
 */
public class TicketAPI extends BaseAPI{

	/**
	 * 获取 jsapi_ticket
	 * @param access_token
	 * @return
	 */
	public static Ticket ticketGetticket(String access_token){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setUri(BASE_URI + "/cgi-bin/ticket/getticket")
				.addParameter("access_token",access_token)
				.addParameter("type", "jsapi")
				.build();
		return LocalHttpClient.executeJsonResult(httpUriRequest,Ticket.class);
	}
}
