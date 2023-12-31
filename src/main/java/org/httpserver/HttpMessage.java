package org.httpserver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpMessage {
    public boolean isRequest;
    public HttpMessage() {
        isRequest = false;
        URI = null;
        startLine = "";
        method = "";
        headerFields = new HashMap<>();
    }
    public HttpMessage(String string) throws BadRequestException {
        this();
        new HttpParser(this).parseFromStream(new ByteArrayInputStream(string.getBytes()));
    }
    public HttpMessage(InputStream stream) throws BadRequestException {
        this();
        new HttpParser(this).parseFromStream(stream);
    }
    @Override
    public String toString() {
        String str = startLine + "\r\n" +
                headerFields.keySet().stream()
                        .map(key -> key + ": " + headerFields.get(key))
                        .collect(Collectors.joining("\r\n")) + "\r\n\r\n";
        if (Objects.nonNull(body))
            str += new String(body);
        return str;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {return true;}
        if (!(obj instanceof HttpMessage)) {return false;}
        HttpMessage m = (HttpMessage) obj;
        return m.toString().equals(this.toString());
    }
    public String getStartLineAndHeaders() {
        return  startLine + "\r\n" +
                headerFields.keySet().stream()
                        .map(key -> key + ": " + headerFields.get(key))
                        .collect(Collectors.joining("\r\n")) + "\r\n\r\n";
    }
    public String getStartLine() {
        return startLine;
    }
    public void setStartLine(String startLine) {
        this.startLine = startLine;
        HttpParser p = new HttpParser(this);
        p.parseMethod(startLine);
        p.parseURI(startLine);
    }
    public byte[] getBody() {
        return body;
    }
    public void setBody(byte[] body) {
        this.body = body;
    }
    public void setBody(String body) {this.body = body.getBytes();}
    public HashMap<String, String> getHeaderFields() {
        return headerFields;
    }
    public void putHeader(String key, String value) {
        headerFields.put(key,value);
    }
    public String getURI() {
        return URI;
    }
    public void setURI(String URI) {
        this.URI = URI;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public final static String HttpOK = "HTTP/1.1 200 OK";
    private String startLine;
    private String URI;
    private String method;
    private HashMap<String,String> headerFields;
    private byte[] body;
}