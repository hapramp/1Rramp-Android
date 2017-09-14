package bxute.config;

/**
 * Created by Ankit on 7/20/2017.
 */

public class ChatConfig {

    public static String getChatRoomId(String companionID) {
        return new StringBuilder().append("cr_" + UserPreference.getUserId() + "_" + companionID).toString();
    }

    public static String getCompanionIdFromChatRoomId(String chatRoomId){
        return chatRoomId.substring(chatRoomId.lastIndexOf('_')+1);
    }

    public static String getMessageID(String companionID) {
        return new StringBuilder().append("msg_" + UserPreference.getUserId() + "_" + companionID + "_" + HaprampTime.getInstance().getTime()).toString();
    }

}
