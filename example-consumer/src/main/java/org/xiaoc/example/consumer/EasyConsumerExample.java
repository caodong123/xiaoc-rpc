package org.xiaoc.example.consumer;

import org.xiaoc.example.common.model.User;
import org.xiaoc.example.common.service.UserService;
import org.xiaoc.example.consumer.Proxy.ServiceProxyFactory;

/**
 * 简易消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
//        UserService userService = null;
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("xiaoc");
        //调用
        User newUser = userService.getUser(user);

        if(newUser != null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("newUser == null");
        }
    }

}
