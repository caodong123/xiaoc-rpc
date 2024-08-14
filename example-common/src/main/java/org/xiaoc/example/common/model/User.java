package org.xiaoc.example.common.model;

import java.io.Serializable;

//实现序列化接口，便于后序进行网络传输
public class User implements Serializable {

    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
