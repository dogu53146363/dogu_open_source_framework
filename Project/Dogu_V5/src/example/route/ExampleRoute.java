package example.route;

import com.jfinal.config.Routes;
import example.action.Example;
import example.route.ExampleRoute;

/**
 * 示例/开发路由配置
 * @author Dogu
 *
 */
public class ExampleRoute {
	
    public ExampleRoute(Routes me) {
		me.add("/Example", Example.class);//Example的路由
	}
}
