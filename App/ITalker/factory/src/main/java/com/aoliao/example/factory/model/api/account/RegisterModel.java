package com.aoliao.example.factory.model.api.account;

/**
 * @author 你的奥利奥
 * @version 2017/8/16
 */

public class RegisterModel {
    private String account;
    private String name;
    private String password;
    private String pushId;

    public RegisterModel(String account, String name, String password) {
        this(account,name,password,null);
    }

    public RegisterModel(String account, String name, String password, String pushId) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.pushId = pushId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", pushId='" + pushId + '\'' +
                '}';
    }
}
