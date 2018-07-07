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

    /**
     * 首先查看
     * @param id
     * @param idMeta
     */
    @Override
    public void populateId(Id id, IdMeta idMeta) {
        lock.lock();
        try {
            // 生成当前时间
            long timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
            // 如果当前时间小于等于上个时间, 报错
            TimeUtils.validateTimestamp(lastTimestamp, timestamp);

            if (timestamp == lastTimestamp) {
                // 如果当前时间等于上个时间, 则自增 sequence 序号
                sequence ++;
                // 当前序号做逻辑与操作, 判断是否越界
                sequence &= idMeta.getSeqBitsMask();
                // 如果越界, 阻塞到下一个时间单位
                if (sequence == 0) {
                    timestamp = TimeUtils.tillNextTimeUnit(lastTimestamp, IdType.parse(id.getType()));
                }
            } else {
                // 如果不在同一个时间, 则将序号清零
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
