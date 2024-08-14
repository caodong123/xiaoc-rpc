package org.xiaoc.rpc.easy.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        // 创建vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建http服务器
        final io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        /*server.requestHandler(request -> {
            //处理http请求
            System.out.println("Received request" + request.method() + " " + request.uri());
            //发送http响应
            request.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello From Vert.x Http Server!");
        });*/
        server.requestHandler(new HttpServerHandler());

        //启动服务器并监听端口
        server.listen(port, result->{
            if(result.succeeded()){
                System.out.println("Server is now listening port " + port);
            }else{
                System.out.println("Failed to start server " + result.cause());
            }
        });
    }
}
