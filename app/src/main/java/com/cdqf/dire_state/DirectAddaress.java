package com.cdqf.dire_state;

public class DirectAddaress {

    //线下
//    public static final String ADDRESS = "http://192.168.31.9";

    //线上
//    public static final String ADDRESS = "https://jx.baodi520.com";

    //线上
    public static final String ADDRESS = "http://jx.quanyubao.cn";

    //会馆列表http://jx.quanyubao.cn/api/v1/Site/getSite
    public static final String SITE = ADDRESS + "/api/v1/Site/getSite";

    //会馆路线
    public static final String LIVE = ADDRESS + "/api/v1/Live/getLive";

    //会馆中节点路线
    public static final String NODES = ADDRESS + "/api/v1/Node/getNodes";

    //当前节点的题目
    public static final String TOPICS = ADDRESS + "/api/v1/Topic/getTopics";

    //验证码
    public static final String CODE = ADDRESS + "/api/v1/User/SendMessage";

    //注册
    public static final String REGISTER = ADDRESS + "/api/v1/User/UserRegister";

    //登录
    public static final String LOGIN = ADDRESS + "/api/v1/User/UserLogin";

    //保存个人信息
    public static final String EDITUSER = ADDRESS + "/api/v1/User/editUser";

    //上传头像
    public static final String HEAD = ADDRESS + "/api/v1/Admin_User/Updateavatar";

    //找回密码
    public static final String PASSWORD = ADDRESS + "/api/v1/User/AppForgePassword";

    //查询用户线路ID
    public static final String USERLIVE = ADDRESS + "/api/v1/user/getsLive";

    //用户线路id的跟踪
    public static final String USERLIVEID = ADDRESS + "/api/v1/user/addNodes";

    //修改id
    public static final String EDITLINE = ADDRESS + "/api/v1/user/editLives";

    //查询队伍
    public static final String DATA = ADDRESS + "/api/v1/User/getTeamData";

    //查询会馆
    public static final String SEARCH = ADDRESS + "/api/v1/Site/getSiteMsg";

    //获奖
    public static final String REWARD = ADDRESS + "/api/v1/User/complete_live";

    //获取队长中队员
    public static final String PLAYERS = ADDRESS + "";

    //添加队员
    public static final String PLAYERS_ADD = ADDRESS + "/api/v1/User/addTeamOne";

    //删除队员
    public static final String PLAYERS_DELETE = ADDRESS + "/api/v1/User/delTeam";

    //赛事活动
    public static final String GAME = ADDRESS + "/api/v1/User/activity";

    //活动详情
    public static final String GAME_DETAILS = ADDRESS + "/api/v1/User/getActivity";

    //活动详情之点标
    public static final String GAME_DETAILS_CONTEXT = ADDRESS + "/api/v1/User/getNodes";

    //QQ登录
    public static final String QQ_LOGIN = ADDRESS + "/api/v1/User/regQQUser";

    //微信登录
    public static final String WX_LOGIN = ADDRESS + "/api/v1/User/regWXUser";

    //用户协义
    public static final String USER_AGREEMENT = ADDRESS + "/api/v1/User/getXY";

    //关于我们
    public static final String ABOUT = ADDRESS + "/api/v1/User/getAboutW";

    //依次穿越 http://zhcg.quanyubao.cn/api/v1/user/get_marker
    public static final String THROUGH = ADDRESS + "/api/v1/user/get_marker";

    //发现活动
    public static final String PERIOD = ADDRESS + "/api/v1/User/get_activity";

    //发现活动详情
    public static final String DETAILS = ADDRESS + "/api/v1/User/show_activity";

    //发现景区
    public static final String SCENERY = ADDRESS + "/api/v1/User/getScenic";

    //发现景区详情
    public static final String SCENERYS = ADDRESS + "/api/v1/User/get_Scenic_info";

    //我的勋章
    public static final String MEDAL = ADDRESS + "/api/v1/User/complete_medal";

    //当前所在景区
    public static final String POSITION = ADDRESS + "/api/v1/Live/introduce";
}
