package okhttp3;

import javax.annotation.*;
import okio.*;

public abstract class WebSocketListener
{
    public void onClosed(final WebSocket webSocket, final int n, final String s) {
    }
    
    public void onClosing(final WebSocket webSocket, final int n, final String s) {
    }
    
    public void onFailure(final WebSocket webSocket, final Throwable t, @Nullable final Response response) {
    }
    
    public void onMessage(final WebSocket webSocket, final String s) {
    }
    
    public void onMessage(final WebSocket webSocket, final ByteString byteString) {
    }
    
    public void onOpen(final WebSocket webSocket, final Response response) {
    }
}
