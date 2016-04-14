import org.eclipse.jetty.websocket.WebSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Endpoints {
    private final Map<String, Endpoint> endpoints = new ConcurrentHashMap<String, Endpoint>();

    void broadcast(String data) {
        for (Endpoint endpoint : endpoints.values()) {
            endpoint.send(data);
        }
    }

    void remove(Endpoint endpoint) {
        endpoints.remove(endpoint.user());
    }

    public synchronized WebSocket newEndpoint(String user) {
        if(user ==  null || user.trim().length() == 0) {
            throw new IllegalStateException("Empty user");
        }
        if (endpoints.containsKey(user)) {
            throw new IllegalStateException("Duplicate user");
        }
        Endpoint endpoint = new Endpoint(this, user);
        endpoints.put(user, endpoint);
        return endpoint;
    }
}
