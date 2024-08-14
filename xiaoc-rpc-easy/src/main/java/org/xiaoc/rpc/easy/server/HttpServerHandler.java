package org.xiaoc.rpc.easy.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.xiaoc.rpc.easy.model.RpcRequest;
import org.xiaoc.rpc.easy.model.RpcResponse;
import org.xiaoc.rpc.easy.registry.LocalRegistry;
import org.xiaoc.rpc.easy.serializer.JdkSerializer;
import org.xiaoc.rpc.easy.serializer.Serializer;

import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {

    /**
     * 处理请求
     * @param request
     */
    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化器
        final Serializer serializer =new JdkSerializer();

        //记录日志
        System.out.println("Received request: " + request.method() + " " + request.uri());

        //异步处理http请求
        request.bodyHandler(body->{
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;

            try {
                //反序列化
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            }catch (Exception e){
                e.printStackTrace();
            }

            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            // 如果请求为空直接返回
            if(rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request,rpcResponse,serializer);
                return;
            }

            //请求不为空的话 反射获取实现类，调用方法返回结果
            try {
                Class<?> implClass = LocalRegistry.getService(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getParams());

                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(String.valueOf(method.getReturnType()));
                rpcResponse.setMessage("OK");

            }catch (Exception e){
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(request,rpcResponse,serializer);
        });
    }

    /**
     * 响应结果
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {

        HttpServerResponse httpServerResponse = request.response().putHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            //序列化
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        }catch (Exception e){
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }


}
