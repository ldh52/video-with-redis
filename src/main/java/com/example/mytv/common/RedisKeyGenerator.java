package com.example.mytv.common;

import static com.example.mytv.common.CacheNames.*;

public class RedisKeyGenerator {
    public static String getUserSessionKey(String authKey) {
        return USER_SESSION + SEPARATOR + authKey;
    }

    public static String getUserCommentBlock(String userId) {
        return USER_COMMENT_BLOCK + SEPARATOR + userId;
    }

    public static String getSubscribeChannelKey(String channelId) {
        return SUBSCRIBE_CHANNEL_BY_USER + SEPARATOR + channelId;
    }

    public static String getSubscribeUserKey(String userId) {
        return SUBSCRIBE_USER + SEPARATOR + userId;
    }

    public static String getVideoViewCountKey(String videoId) {
        return VIDEO_VIEW_COUNT + SEPARATOR +  videoId;
    }

    public static String getVideoViewCountSetKey() {
        return VIDEO_VIEW_COUNT_SET;
    }

    public static String getVideoLikeKey(String videoId) {
        return VIDEO_LIKE + SEPARATOR +  videoId;
    }

    public static String getCommentLikeKey(String commentId) {
        return COMMENT_LIKE + SEPARATOR + commentId;
    }

    public static String getPinnedCommentKey(String videoId) {
        return COMMENT_PINNED + SEPARATOR + videoId;
    }

}
