package com.cdqf.dire_class;

/**
 * 用于实时踪当前用户选择的路线的所有信息
 */
public class UserLine {
    //用户id
    private int id;

    //当前线路的id
    private int lineId;

    //当前所在点的id
    private int linePositionId;

    //用户是否选择了路线
    private boolean isRoute = false;

    //用户是否选择了当前路线的用户协义
    private boolean isAgree = false;

    //当前的线路是否通关
    private boolean isCustoms = false;

    //代表当前用户在当前线路完成了多少个点
    private int number = 0;

    //是否签到
    private boolean isSign = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public int getLinePositionId() {
        return linePositionId;
    }

    public void setLinePositionId(int linePositionId) {
        this.linePositionId = linePositionId;
    }

    public boolean isRoute() {
        return isRoute;
    }

    public void setRoute(boolean route) {
        isRoute = route;
    }

    public boolean isAgree() {
        return isAgree;
    }

    public void setAgree(boolean agree) {
        isAgree = agree;
    }

    public boolean isCustoms() {
        return isCustoms;
    }

    public void setCustoms(boolean customs) {
        isCustoms = customs;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public void clearUserLine() {
        //用户id
        id = 0;

        //当前线路的id
        lineId = 0;

        //当前所在点的id
        linePositionId = 0;

        //用户是否选择了路线
        isRoute = false;

        //用户是否选择了当前路线的用户协义
        isAgree = false;

        //当前的线路是否通关
        isCustoms = false;

        //代表当前用户在当前线路完成了多少个点
        number = 0;
    }
}
