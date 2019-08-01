package com.chenshinan.exercises.rocketchat.rest.dto;

import java.util.List;

public class SendMessageParam {

    public final String mid;
    /**
     * The text of the message to send, is optional because of attachments.
     */
    public final String msg;

    /**
     * [REQUIRED] The room id of where the message is to be sent.
     */
    public final String rid;

    /**
     * This will cause the message’s name to appear as the given alias, but your username will still display.
     */
    public final String alias;

    /**
     * If provided, this will make the avatar use the provided image url.
     */
    public final String avatar;

    /**
     * If provided, this will make the avatar on this message be an emoji.
     */
    public final String emoji;

    public List<Attachment> attachments;

    public SendMessageParam(String mid, String msg, String rid, String alias, String avatar, String emoji,
                            List<Attachment> attachments) {
        this.mid = mid;
        this.msg = msg;
        this.rid = rid;
        this.alias = alias;
        this.avatar = avatar;
        this.emoji = emoji;
        this.attachments = attachments;
    }

    public static SendMessageParam forSendMessage(String mid, String msg, String rid) {
        return new SendMessageParam(mid, msg, rid, null, null, null, null);
    }

    public static SendMessageParam forSendMessage(String msg, String rid, String alias, String avatar, String emoji, List<Attachment> attachments) {
        return new SendMessageParam(null, msg, rid, alias, avatar, emoji, attachments);
    }

    public static SendMessageParam forUpdate(String msgId, String msg, String rid, String alias, String avatar, String emoji, List<Attachment> attachments) {
        return new SendMessageParam(null, msg, rid, alias, avatar, emoji, attachments);
    }

}