package org.destiny.sequence.populater;

import org.destiny.sequence.bean.IdMeta;
import org.destiny.sequence.bean.IdType;
import org.destiny.sequence.model.Id;
import org.destiny.sequence.util.TimeUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p>
 *     使用可重入锁来进行同步修改
 * </p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 22:36
 */
public class LockIdPopulator implements IdPopulator {

    private long sequence = 0;

    private long lastTimestamp = -1;

    /**
     * 使用可重入锁来进行同步的修改
     */
    private Lock lock = new ReentrantLock();

    @Override
    public void populateId(Id id, IdMeta idMeta) {
        lock.lock();
        try {
            long timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
            TimeUtils.validateTimestamp(lastTimestamp, timestamp);

            if (timestamp == lastTimestamp) {
                sequence ++;
                sequence &= idMeta.getSeqBitsMask();
                if (sequence == 0) {
                    timestamp = TimeUtils.tillNextTimeUnit(lastTimestamp, IdType.parse(id.getType()));
                }
            } else {
                lastTimestamp = timestamp;
                sequence = 0;
            }
            id.setSeq(sequence);
            id.setTime(timestamp);
        } finally {
            lock.unlock();
        }
    }
}
