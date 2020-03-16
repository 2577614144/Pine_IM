package com.lipine.im.sdk.event;

/**
 * Time:  2020/3/14 23:08
 * Author: lipine
 * Email: liqingsongandroid@163.com
 * Description:事件的线程池
 */

public class CEvenObjPool extends ObjectPool<CEvent> {

    public CEvenObjPool(int capacity) {
        super(capacity);
    }

    @Override
    protected CEvent[] createObjPool(int capacity) {
        return new CEvent[capacity];
    }

    @Override
    protected CEvent createNewObj() {
        return new CEvent();
    }
}
