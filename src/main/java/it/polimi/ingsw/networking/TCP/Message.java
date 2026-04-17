package it.polimi.ingsw.networking.TCP;

import java.io.Serializable;

public class Message implements Serializable {
    private RequestType request;
    private Object[] params;

    public Message(RequestType request, Object... params) {
        this.request = request;
        this.params = params;
    }

    public RequestType getRequest() {
        return request;
    }

    public Object[] getParams() {
        return params;
    }
}
