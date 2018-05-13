package com.example.lalal.Tools.ConStant;

import com.example.lalal.Po.User;

public class Constants {

    public static final String IPURL = "http://192.168.1.129:8080";
    public static final String MAINURL=IPURL+"/MyNewGraduate";

    public static final String LOGINURL = MAINURL+"/LoginServlet";
    public static final String REGISTERURL = MAINURL+"/RegisterServlet";
    public static final String FORGETURL = MAINURL+"/ForgetServlet";
    public static final String UPLOADURL = MAINURL + "/UploadServlet";
    public static final String FINDUSERURL = MAINURL + "/FindUserNameServlet";
    public static final String ADDFRIENDURL = MAINURL+"/AddFriendServlet";
    public static final String SHOWSTTATUSURL = MAINURL+"/ShowStatusServlet";
    public static final String CHANGESTATUSURL = MAINURL+"/ChangeStatusServlet";
    public static final String SHOWFRIENDNUL = MAINURL +"/ShowFriendServlet";
    public static final String ADDMESSAGEURL = MAINURL +"/AddMessageServlet";
    public static final String SHOWMESSAGEURL = MAINURL +"/ShowMessageServlet";


    public static final String SUCCESSFUL = "successful";
    public static final String DEFEATE = "defeate";
    public static final String EXITNAME = "exitname";
    public static final String NOUSER = "nouser";
    public static final String NOMSG = "nomsg";
    public static final String EXITFRIEND = "exitfriend";
    public static final String NOFRIEND = "nofriend";

    public static final String PROJECTPATH = "/storage/emulated/0/MyNewGraduate/";

    public static User user;
    public static String userpath;
}
