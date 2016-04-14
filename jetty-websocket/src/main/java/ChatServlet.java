import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ChatServlet extends WebSocketServlet {
    private final Endpoints endpoints = new Endpoints();

    @Override
    protected WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return endpoints.newEndpoint(request.getParameter("user"));
    }
}
