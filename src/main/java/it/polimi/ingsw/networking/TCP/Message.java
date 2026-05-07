package it.polimi.ingsw.networking.TCP;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class Message implements Serializable {
    private RequestType request;
    private JsonNode payload;

    public Message () {}

    public Message(RequestType request, JsonNode payload) {
        this.request = request;
        this.payload = payload;
    }

    public RequestType getRequest() {
        return request;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setRequest(RequestType request) {
        this.request = request;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }
}
