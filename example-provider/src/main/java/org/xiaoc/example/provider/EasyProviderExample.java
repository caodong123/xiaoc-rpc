package org.xiaoc.example.provider;

import org.xiaoc.example.common.service.UserService;
import org.xiaoc.rpc.easy.registry.LocalRegistry;
import org.xiaoc.rpc.easy.server.HttpServer;
import org.xiaoc.rpc.easy.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        //在本地注册
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //提供服务
        HttpServer server = new VertxHttpServer();

        server.doStart(8080);
    }
}
