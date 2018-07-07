package org.destiny.sequence.populater;

import org.destiny.sequence.bean.IdMeta;
import org.destiny.sequence.bean.IdType;
import org.destiny.sequence.model.Id;
import org.destiny.sequence.util.TimeUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p>
 *     CAS 无锁版
 *     1. 取得并保存原来的变量, 这个变量包含原来的时间和序列号字段
 *     2. 基于原来的变量计算新的时间和序列号, 计算逻辑和 {@link org.destiny.sequence.populater.LockIdPopulator} 一致
 *     3. 计算后, 使用 CAS 操作更新原来的变量, 在更新过程中, 需要传递保存原来的变量
 *     4. 如果保存原来的变量被其他线程改变了, 就需要在这里重新拿到最新的变量, 并再次计算和尝试更新
 * </p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/7 09:11
 */
public class AtomicIdPopulator implements IdPopulator {

    private class Variant {
        private long sequence = 0;
        private long lastTimestamp = -1;

        public Variant() {
        }
    }

    /**
     * 通过 AtomicReference 来实现对 Variant 两个变量线程安全的修改
     */
    private AtomicReference<Variant> variant = new AtomicReference<>(new Variant());

    @Override
    public void populateId(Id id, IdMeta idMeta) {
        Variant oldVar;
        Variant newVar;

        long timestamp;
        long sequence;

        while (true) {
            // 保存旧值
            oldVar = variant.get();
            // 计算新的当前值
            timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
            TimeUtils.validateTimestamp(oldVar.lastTimestamp, timestamp);

            sequence = oldVar.sequence;

            if (timestamp == oldVar.lastTimestamp) {
                sequence ++;
                sequence &= idMeta.getSeqBitsStartPos();
                if (sequence == 0) {
                    timestamp = TimeUtils.tillNextTimeUnit(oldVar.lastTimestamp, IdType.parse(id.getType()));
                }
            } else {
                sequence = 0;
            }
            //
            newVar = new Variant();
            newVar.sequence = sequence;
            newVar.lastTimestamp = timestamp;

            if (variant.compareAndSet(oldVar, newVar)) {
                id.setSeq(sequence);
                id.setTime(timestamp);
                break;
            }
        }

    }
}
