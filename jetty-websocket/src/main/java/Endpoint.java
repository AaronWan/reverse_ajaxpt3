import org.eclipse.jetty.websocket.WebSocket;

import java.io.IOException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Endpoint implements WebSocket {

    private final Endpoints endpoints;
    private final String user;

    private Outbound outbound;

    Endpoint(Endpoints endpoints, String user) {
        this.endpoints = endpoints;
        this.user = user;
    }

    @Override
    public void onConnect(Outbound outbound) {
        this.outbound = outbound;
        endpoints.broadcast(user + " connected !");
    }

    @Override
    public void onMessage(byte opcode, String data) {
        if("/disconnect".equals(data)) {
            endpoints.broadcast(user + " disconnected !");
            outbound.disconnect();
        } else {
            endpoints.broadcast("[" + user + "] " + data);
        }
    }

    @Override
    public void onFragment(boolean more, byte opcode, byte[] data, int offset, int length) {
        // when a fragment is completed, onMessage is called
    }

    @Override
    public void onMessage(byte opcode, byte[] data, int offset, int length) {
        onMessage(opcode, new String(data, offset, length));
    }

    @Override
    public void onDisconnect() {
        endpoints.remove(this);
        outbound = null;
    }

    void send(String data) {
        try {
            if (outbound != null && outbound.isOpen()) {
                outbound.sendMessage(data);
            }
        } catch (IOException e) {
            outbound.disconnect();
        }
    }

    public String user() {
        return user;
    }
}
