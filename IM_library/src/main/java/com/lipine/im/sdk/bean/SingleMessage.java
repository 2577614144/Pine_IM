package com.lipine.im.sdk.bean;

import com.blankj.utilcode.util.StringUtils;
/**
 * Time:  2020/3/13 17:50
 * Author: lipine
 * Email: liqingsongandroid@163.com
 * Description: 单聊消息
 */

public class SingleMessage extends ContentMessage implements Cloneable {

    @Override
    public int hashCode() {
        try {
            return this.msgId.hashCode();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof SingleMessage)) {
            return false;
        }

        return StringUtils.equals(this.msgId, ((SingleMessage) obj).getMsgId());
    }
}
