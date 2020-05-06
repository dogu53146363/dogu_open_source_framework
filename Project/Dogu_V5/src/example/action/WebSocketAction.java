package example.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.jfinal.plugin.activerecord.Db;

@ServerEndpoint("/websocket")
public class WebSocketAction {

	private static CopyOnWriteArraySet<WebSocketAction> webSocketSet = new CopyOnWriteArraySet<WebSocketAction>();
	private static Hashtable<String, Session> sessionMap = new Hashtable<String, Session>();
	private String UserName;
	private Session session;
	
	public WebSocketAction() {
		
	}

	@SuppressWarnings("unchecked")
	@OnOpen
	public void onOpen(Session session) throws UnsupportedEncodingException {
		System.out.println(session.getRequestParameterMap());
		Map<?, ?> requestMap = session.getRequestParameterMap();
		List<String> UserIDLs = (List<String>) requestMap.get("USERID");
		UserName = UserIDLs.get(0);
		UserName = URLDecoder.decode(UserName,"UTF-8");
		this.session = session;
		Db.update("INSERT INTO DOGU_SOCKET (F_USERNAME)"
				+ " VALUES ('"+this.UserName+"')");
		sessionMap.put(this.UserName, this.session);
		webSocketSet.add(this);
		String message = String.format("[%s,%s]",this.UserName, "加入连接");
		System.out.println(message);
	}

	@OnClose
	public void onClose(Session session) {
		sessionMap.remove(this.UserName);
		webSocketSet.remove(this);
		Db.update("DELETE FROM DOGU_SOCKET WHERE F_USERNAME = '"+this.UserName+"'");
		String message = String.format("[%s,%s]", this.UserName, "断开链接");
		System.out.println(message);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		try {
			System.out.println(this.session == session);
			System.out.println(message);
			broadcast(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送消息
	 * @param retMap
	 * @throws UnsupportedEncodingException 
	 */
	private void broadcast(String MSG) throws UnsupportedEncodingException {
		String [] MsgLs = MSG.split("-MESSAGE-");
		String MyUserName = MsgLs[0];
		MyUserName = URLDecoder.decode(MyUserName,"UTF-8");
		String ToUserName = MsgLs[1];
		ToUserName = URLDecoder.decode(ToUserName,"UTF-8");
		String FromMSG = MsgLs[2];
		FromMSG = URLDecoder.decode(FromMSG,"UTF-8");
		boolean SendStatus = false;
		//发送消息
		for (WebSocketAction w : webSocketSet) {
			try {
				synchronized (WebSocketAction.class) {
					if (w.UserName.equals(ToUserName)) {
						w.session.getBasicRemote().sendText("["+MyUserName+"]说："+FromMSG);
						SendStatus = true;
					}
				}
			} catch (IOException e) {
				System.out.println("向客户端" + ToUserName + "发送消息失败");
				Db.update("DELETE FROM DOGU_SOCKET WHERE F_SESSION_ID = '"+this.UserName+"'");
				webSocketSet.remove(w);
				try {
					w.session.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		//消息确认
		for (WebSocketAction w : webSocketSet) {
			try {
				synchronized (WebSocketAction.class) {
					if (w.UserName.equals(MyUserName)) {
						if(SendStatus) {
							w.session.getBasicRemote().sendText("发送成功！");
						}else {
							w.session.getBasicRemote().sendText("发送失败，对方不在线！");
						}
					}
				}
			} catch (IOException e) {
				System.out.println("向客户端" + ToUserName + "发送消息失败");
				Db.update("DELETE FROM DOGU_SOCKET WHERE F_SESSION_ID = '"+this.UserName+"'");
				webSocketSet.remove(w);
				try {
					w.session.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}