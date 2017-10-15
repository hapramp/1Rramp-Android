package bxute.config;

/**
 * Created by Ankit on 7/20/2017.
 */

public class ChatConfig {

    public static final String STATUS_TYPING = "1";
    public static final String STATUS_NOT_TYPING = "0";

    public static String getChatRoomId(String myId,String companionID) {
        return new StringBuilder().append("cr_" + myId + "_" + companionID).toString();
    }

    public static String getCompanionIdFromChatRoomId(String chatRoomId){
        return chatRoomId.substring(chatRoomId.lastIndexOf('_')+1);
    }

    public static String getMessageID(String myId,String companionID) {
        return new StringBuilder().append("msg_" + myId + "_" + companionID + "_" + LocalTimeManager.getInstance().getTime()).toString();
    }

}
