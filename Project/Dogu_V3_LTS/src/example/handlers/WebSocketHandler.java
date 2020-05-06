package example.handlers;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;
public class WebSocketHandler extends Handler {
	
	private Pattern skipedUrlPattern;
	
	public WebSocketHandler(String skipedUrlRegx, boolean isCaseSensitive) {
		if (StrKit.isBlank(skipedUrlRegx)) {
			throw new IllegalArgumentException("The para excludedUrlRegx can not be blank.");
		}
		skipedUrlPattern = isCaseSensitive ? Pattern.compile(skipedUrlRegx) : Pattern.compile(skipedUrlRegx, Pattern.CASE_INSENSITIVE);
	}
	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		if (skipedUrlPattern.matcher(target).matches()) {
			return ;
		} else {
			next.handle(target, request, response, isHandled);
		}
	}
}