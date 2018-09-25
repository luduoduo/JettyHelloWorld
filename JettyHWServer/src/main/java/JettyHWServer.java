import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class JettyHWServer extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        System.out.print("handle a request: http request is \n");
        System.out.println(request);

        // 声明response的编码和文件类型
        response.setContentType("text/html; charset=utf-8");

        // 声明返回状态码
        response.setStatus(HttpServletResponse.SC_OK);

        // 请求的返回值
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("sleep failed");
        }
        response.getWriter().println("<h1>Hello World！我的第一个服务程序</h1>");

        // 通知Jettyrequest使用此处理器处理请求
        baseRequest.setHandled(true);
    }

    public static void main(String[] args) throws Exception {
        System.out.print("main entry\n");
        //创建一个应用服务监听8080端口
        Server server = new Server(8080);
        System.out.print("new a handler as servlet\n");
        server.setHandler(new JettyHWServer());

        //启动应用服务并等待请求
        server.start();
        server.join();
    }
}