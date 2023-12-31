# HttpServer

Java HTTP Server from scratch (not using `java.net.http` or any external libraries)

To install, download the `HttpServer.jar` from [releases](https://github.com/g-jensen/HttpServer/releases/tag/v1.0.0) and add it to your project

[An example app that I made](https://github.com/g-jensen/HttpServerApp)

A simpler example app:
```java
package org.example;

import org.httpserver.HttpServer;
import org.httpserver.HttpMessage;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1",8081);
        HttpServer server = new HttpServer(address);
        server.initialize();

        server.onConnection((req)->{
            System.out.println("Got request:\n" + req);
            String body = "<h1>Welcome to my Http Server!</h1>";

            HttpMessage res = new HttpMessage();
            res.setStartLine(HttpMessage.HttpOK);
            res.putHeader("Content-Length", String.valueOf(body.length()));
            res.putHeader("Content-Type","text/html");
            res.setBody(body);
            return res;
        });

        server.run();
    }
}
```
