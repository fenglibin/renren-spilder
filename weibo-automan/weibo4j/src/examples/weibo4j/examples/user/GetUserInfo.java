/**
 * 
 */
package weibo4j.examples.user;

import java.io.UnsupportedEncodingException;

import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * @author sina
 *
 */
public class GetUserInfo {

	/**
	 * 根据用户ID获取用户资料（授权用户） 
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
			String screen_name="丽芙美眉";
			Weibo weibo = new Weibo();
			//weibo.setToken(args[0],args[1]);
			weibo.setToken("0a836ede51b0cf8b94bb1f14b2b255ae","ae74aeafa7da51c6ac213354806b63cf");
			User user = weibo.showUser(screen_name);
			System.out.println(user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}
