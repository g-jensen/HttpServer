package org.httpserver;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class HttpMessageTest {

    @Test
    void constructsMethod() {
        HttpMessage m1 = new HttpMessage();
        m1.setStartLine("GET / HTTP/1.1");
        m1.putHeader("Content-Length","5");
        m1.setBody("hello");

        assertEquals(m1.getMethod(),"GET");
    }

    @Test
    void constructsURI() {
        HttpMessage m1 = new HttpMessage();
        m1.setStartLine("GET /hi HTTP/1.1");
        m1.putHeader("Content-Length","5");
        m1.setBody("hello");

        assertEquals(m1.getURI(),"hi");
    }

    @Test
    void parsesFromString() throws BadRequestException {
        HttpMessage m1 = new HttpMessage("GET / HTTP/1.1\r\n" +
                "Host: 127.0.0.1\r\n" +
                "Content-Length: 5\r\n" +
                "\r\n" +
                "hello");
        assertEquals("GET / HTTP/1.1",m1.getStartLine());
        assertEquals(2,m1.getHeaderFields().size());
        assertEquals("127.0.0.1", m1.getHeaderFields().get("Host"));
        assertEquals("5", m1.getHeaderFields().get("Content-Length"));
        assertEquals("hello",new String(m1.getBody()));
    }

    @Test
    void convertsToString() throws BadRequestException {
        String s1 = "GET /hello.txt HTTP/1.1\r\n" +
                "User-Agent: curl/7.64.1\r\n" +
                "Host: www.example.com\r\n" +
                "Content-Length: 21\r\n" +
                "Date: Fri, 28 Jul 2023 21:15:11 GMT\r\n" +
                "\r\n" +
                "hello! im in the body";
        InputStream in1 = new ByteArrayInputStream(s1.getBytes());
        HttpMessage m = new HttpMessage(in1);
        assertEquals(s1,m.toString());
    }

    @Test
    void checksEqualityWithString() throws BadRequestException {
        HttpMessage m1 = new HttpMessage("GET / HTTP/1.1\r\n" +
                "Host: 127.0.0.1\r\n\r\n");
        HttpMessage m2 = new HttpMessage("GET / HTTP/1.1\r\n" +
                "Host: 127.0.0.1\r\n\r\n");
        assertEquals(m1,m2);
    }

    @Test
    void checksEqualityWithBytes() {
        HttpMessage m1 = new HttpMessage();
        m1.setStartLine("GET / HTTP/1.1");
        m1.putHeader("Host","greg");
        m1.setBody(new byte[]{2});
        HttpMessage m2 = new HttpMessage();
        m2.putHeader("Host","greg");
        m2.setBody(new byte[]{1});
        assertNotEquals(m1,m2);
    }

    @Test
    void checksEqualityWithNonInstance() throws BadRequestException {
        HttpMessage m1 = new HttpMessage("GET / HTTP/1.1\r\n" +
                "Host: 127.0.0.1\r\n\r\n");
        assertNotEquals(m1,new Object());
    }
}