package org.destiny.sequence.service;

import org.destiny.sequence.model.Id;
import org.destiny.sequence.populater.IdPopulator;
import org.destiny.sequence.util.CommonUtils;

import java.util.Date;

/**
 * @author 王康
 * hzwangkang1@corp.netease.com
 * ------------------------------------------------------------------
 * <p></p>
 * ------------------------------------------------------------------
 * Corpright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 19:13
 */
public class IdServiceImpl extends AbstractIdServiceImpl {

    private static final String SYNC_LOCK_IMPL_KEY = "sequence.sync.lock.impl.key";
    private static final String ATOMIC_IMPL_KEY = "sequence.atomic.impl.key";

    private IdPopulator idPopulator;

    public IdServiceImpl() {
        super();

    }

    @Override
    protected void populateId(Id id) {

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

    private void initPopulator() {
        if (CommonUtils.isPropKeyOn(SYNC_LOCK_IMPL_KEY)) {
            logger.info("The SyncIdPopulator is used");

        } else if (CommonUtils.isPropKeyOn(ATOMIC_IMPL_KEY)) {

        }
    }
}
