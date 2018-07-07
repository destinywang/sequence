package org.destiny.sequence.service;

import org.destiny.sequence.model.Id;
import org.destiny.sequence.populater.AtomicIdPopulator;
import org.destiny.sequence.populater.IdPopulator;
import org.destiny.sequence.populater.LockIdPopulator;
import org.destiny.sequence.populater.SyncIdPopulator;
import org.destiny.sequence.util.CommonUtils;

import java.util.Date;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p></p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 19:13
 */
public class IdServiceImpl extends AbstractIdServiceImpl {

    private static final String SYNC_LOCK_IMPL_KEY = "sequence.sync.lock.impl.key";
    private static final String ATOMIC_IMPL_KEY = "sequence.atomic.impl.key";

    /***
     * 此接口中需要实现构成 唯一 ID 的另外两个变量
     * 时间 和 序列号, 它们的变化方式是多种多样的, 因此将这两个变量的生成逻辑
     */
    private IdPopulator idPopulator;


    /**
     * 模板方法
     * 子类主要根据某一算法生成唯一 ID 的时间和序列号属性, 父类则对自己的 机器Id, 生成方式, 类型, 版本进行复制
     *
     * @param id
     */
    @Override
    protected void populateId(Id id) {
        idPopulator.populateId(id, idMeta);
    }

    /**
     * 伪造某一时间的 ID
     *
     * @param time
     * @param seq
     * @return
     */
    @Override
    public long makeId(long time, long seq) {
        return 0;
    }

    /**
     * 伪造某一时间的 ID
     *
     * @param time
     * @param seq
     * @param machine
     * @return
     */
    @Override
    public long makeId(long time, long seq, long machine) {
        return 0;
    }

    /**
     * 伪造某一时间的 ID
     *
     * @param genMethod
     * @param time
     * @param seq
     * @param machine
     * @return
     */
    @Override
    public long makeId(long genMethod, long time, long seq, long machine) {
        return 0;
    }

    /**
     * 将整型时间翻译成格式化时间
     *
     * @param time
     * @return
     */
    @Override
    public Date transTime(long time) {
        return null;
    }


    /**
     * 选择 IdPopulator
     * 默认情况下为 LockIdPopulator
     */
    public void initPopulator() {
        if (CommonUtils.isPropKeyOn(SYNC_LOCK_IMPL_KEY)) {
            idPopulator = new SyncIdPopulator();
        } else if (CommonUtils.isPropKeyOn(ATOMIC_IMPL_KEY)) {
            idPopulator = new AtomicIdPopulator();
        } else {
            idPopulator = new LockIdPopulator();
        }
    }


}
