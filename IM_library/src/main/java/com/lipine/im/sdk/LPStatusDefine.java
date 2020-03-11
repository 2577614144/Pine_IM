package com.lipine.im.sdk;

/**
 * Time:2020/3/9
 * Author:lipine
 * Email:liqingsongandroid@163.com
 * Description: 状态定义类
 */

public class LPStatusDefine {

    /**
 * 这里定义登录失败后原因，返回给客户
 * LPConnectErrorCodeType1: 参数为空
 * LPConnectErrorCodeType2: 网络
 * LPConnectErrorCodeType3: 用户名或密码错误
 * LPConnectErrorCodeType4: 参数错误
 * LPConnectErrorCodeType5: ClientkeyFailedException
 * LPConnectErrorCodeType6: 其他
 */
public enum LPConnectErrorCode {
    LPConnectErrorCode_type1,
    LPConnectErrorCode_type2,
    LPConnectErrorCode_type3,
    LPConnectErrorCode_type4,
    LPConnectErrorCode_type5,
    LPConnectErrorCode_type6
}


    /**
     * 这里定义SDK和OCS连接的状态，返回给客户
     * LPCONNECTIONSTATUS_CONNECTING: 正在连接
     * LPCONNECTIONSTATUS_CONNECT_SUCCEED: 连接成功
     * LPCONNECTIONSTATUS_CONNECT_ERROR: 连接异常
     * LPCONNECTIONSTATUS_NETWORK_ERROR: 网络异常
     * LPCONNECTIONSTATUS_GET_USER_INFO_ERROR: 获取用户信息失败
     * LPCONNECTIONSTATUS_GET_GROUP_LIST_ERROR: 获取群组列表失败
     * LPCONNECTIONSTATUS_SESSION_CLOSED: sessionClose
     */
    public enum LPConnectionStatus {
        LPCONNECTIONSTATUS_CONNECTING,
        LPCONNECTIONSTATUS_CONNECT_SUCCEED,
        LPCONNECTIONSTATUS_CONNECT_ERROR,
        LPCONNECTIONSTATUS_NETWORK_ERROR,
        LPCONNECTIONSTATUS_GET_USER_INFO_ERROR,
        LPCONNECTIONSTATUS_GET_GROUP_LIST_ERROR,
        LPCONNECTIONSTATUS_SESSION_CLOSED,
    }

    /**
     * 当前所处的网络
     */
    public enum LPNetworkStatus {
        /**
         * 当前网络不可用
         */
        LP_NotReachable,
        /**
         * 当前处于WiFi网络
         */
        LP_ReachableViaWiFi,
        /**
         * 移动网络
         */
        LP_ReachableViaWWAN
    }

    /**
     * SDK当前所处的状态
     */
    public enum LPSDKRunningMode {
        /**
         * 后台运行状态
         */
        LPSDKRunningMode_Background,
        /**
         * 前台运行状态
         */
        LPSDKRunningMode_Foreground
    }

    /**
     * 具体业务错误码
     */
    public enum LPErrorCode {

        /**
         * 未知错误（预留）
         */
        LPErrorCode_UNKNOWN,

        /**
         * 超时
         */
        LPErrorCode_TIMEOUT,

        /**
         * 空参数
         */
        LPErrorCode_NULL_PARAMETER,

        /**
         * session异常
         */
        LPErrorCode_SESSION_ERROR,

        /**
         * 地址错误
         */
        LPErrorCode_URL_ERROR,

        /**
         * 失败
         */
        LPErrorCode_FAILURE,

        /**
         * 没有权限
         */
        LPErrorCode_NOT_PERMISSION,

        /**
         * 人员不存在
         */
        LPErrorCode_USER_INEXISTENCE,

        /**
         * 撤回超时
         */
        LPErrorCode_WITHDRAW_TIMEOUT
    }

    /**
     * 会话类型
     */
    public enum LPConversationType {
        /**
         * 单聊
         */
        LPConversationType_PRIVATE(0),
        /**
         * 讨论组
         */
        LPConversationType_DISCUSSION(1),

        /**
         * 群组
         */
        LPConversationType_GROUP(2),

        /**
         * 通知消息
         */
        LPConversationType_BROADCAST(3),

        /**
         * 系统消息
         */
        LPConversationType_SYSTEM(4),

        /**
         * 群发消息
         */
        LPConversationType_GROUP_SEND(5);

        private int value;

        LPConversationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 消息类型
     */
    public enum LPIMMessageType {
        /**
         * 文字
         */
        LPMessageType_Text(1),
        /**
         * 图片
         */
        LPMessageType_Picture(2),

        /**
         * 语音
         */
        LPMessageType_Audio(3),
        /**
         * 文件
         */
        LPMessageType_File(4),

        /**
         * 位置
         */
        LPMessageType_Position(5),

        /**
         * 震动
         */
        LPMessageType_Vibration(6),

        /**
         * OCS名片
         */
        LPMessageType_OcsVcard(7),

        /**
         * 手机名片
         */
        LPMessageType_AddressVcard(8),

        /**
         * 广播消息
         */
        LPMessageType_Broadcast(9),

        /**
         * 撤回消息
         */
        LPMessageType_Withdraw(10),

        /**
         * 系统消息
         */
        LPMessageType_SYSTEM_MESSAGE(998),

        /**
         * 群组通知
         */
        LPMessageType_GroupNotification(999);

        private int value;

        LPIMMessageType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 群组类型
     */
    public enum LPGroupType {
        /**
         * 群组
         */
        LPGroupType_Group(0),
        /**
         * 讨论组
         */
        LPGroupType_discussion(2);

        private int value;

        LPGroupType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 群组成员类型
     */
    public enum LPGroupMemberType {
        /**
         * 旁听
         */
        LPGroupMemberType_SitOn,
        /**
         * 成员
         */
        LPGroupMemberType_Member,
        /**
         * 管理员
         */
        LPGroupMemberType_Administrator,
        /**
         * 创建者
         */
        LPGroupMemberType_Creater
    }

    /**
     * 语音录制状态
     */
    public enum LPVoiceRecordType {
        /**
         * 开始
         */
        LPVoiceRecord_Start,
        /**
         * 录制中
         */
        LPVoiceRecord_ing,
        /**
         * 结束
         */
        LPVoiceRecord_end
    }

    /**
     * 语音录制异常状态
     */
    public enum LPVoiceRecordExceptionType {
        /**
         * 异常1
         */
        LPVoiceRecordException_Type1,
        /**
         * 异常2
         */
        LPVoiceRecordException_Type2,
        /**
         * 异常3
         */
        LPVoiceRecordException_Type3
    }

    /**
     * 消息的方向
     */
    public enum LPMessageDirection {
        /**
         * 发送
         */
        LPMessageDirection_SEND(1),
        /**
         * 接收
         */
        LPMessageDirection_RECEIVE(0);
        private int value;

        LPMessageDirection(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 消息的接收状态
     */
    public enum LPReceivedStatus {
        /**
         * 已下载,用于文件
         */
        LPReceivedStatus_DOWNLOADED(0),
        /**
         * 正在接受中
         */
        LPReceivedStatus_RECEIVING(1),
        /**
         * 接收失败
         */
        LPReceivedStatus_FAILED(2),
        /**
         * 接收成功
         */
        LPReceivedStatus_SUCCESS(3),

        /**
         * 此数据为无意义数据 只是为了填充数据
         */
        LPReceivedStatus_UNRELATED(999);

        private int value;

        LPReceivedStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 消息的发送状态
     */
    public enum LPSentStatus {

        /**
         * 发送中
         */
        LPSentStatus_SENDING(0),
        /**
         * 发送失败
         */
        LPSentStatus_FAILED(1),
        /**
         * 已发送成功
         */
        LPSentStatus_SENT(2),
        /**
         * 此数据为无意义数据 只是为了填充数据
         */
        LPSentStatus_UNRELATED(999);

        private int value;

        LPSentStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 群组通知
     */
    public enum LPGroupNotificationType {
        /**
         * 成员退出群组,(群内成员都收到消息）i = 12
         */
        LPGroupNoticationType_MembersQuit(1),
        /**
         * 解散群组(其实也会收到i=12的协议) i = 14，自己解散后收到解散消息
         */
        LPGroupNoticationType_Disband(2),

        /**
         * 添加成员
         */
        LPGroupNoticationType_Add(3);

        private int value;

        LPGroupNotificationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 群组人员类型
     */
    public enum LPGroupMemberRole {
        /**
         * 旁听
         */
        LPGroupMemberRole_SitIn(0),

        /**
         * 参与者
         */
        LPGroupMemberRole_participant(1),

        /**
         * 拥有者
         */
        LPGroupMemberRole_Owner(3),

        /**
         * 管理员
         */
        LPGroupMemberRole_Administrator(2);

        private int value;

        LPGroupMemberRole(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 文件类型
     */
    public enum LPFileType {
        /**
         * jpg
         */
        LPFileType_Jpg(0),

        /**
         * gif
         */
        LPFileType_Gif(1),

        /**
         * png
         */
        LPFileType_Png(3);


        private int value;

        LPFileType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 消息处理类型
     */
    public enum LPMessageDisposeType {
        /**
         * 普通消息 支持撤回
         */
        ZERO(0),
        /**
         * 回执消息 支持撤回
         */
        ONE(1),
        /**
         * 阅后即焚
         */

        TWO(2);

        private int value;

        LPMessageDisposeType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 消息处理类型
     */
    public enum LPSystemMessageLevel {
        /**
         * 普通
         */
        GENERAL(0),
        /**
         * 中等
         */
        MEDIUM(1),
        /**
         * 紧急
         */
        URGENCY(2);

        private int value;

        LPSystemMessageLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

//    /**
//     * 消息类型
//     */
//    public enum LPIMMessageType{
//        /**
//         * 文字消息
//         */
//        LPIMMessageType_Text,
//        /**
//         * 图片消息
//         */
//        LPIMMessageType_Image,
//        /**
//         * 文件消息
//         */
//        LPIMMessageType_File,
//        /**
//         * 手机名片消息
//         */
//        LPIMMessageType_AddressVcard,
//        /**
//         * OCS组织结构名片消息
//         */
//        LPIMMessageType_OCSVcard,
//        /**
//         * 位置消息
//         */
//        LPIMMessageType_Position,
//        /**
//         * 群组通知消息
//         */
//        LPIMMessageType_GroupNoti
//    }
}
