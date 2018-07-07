package org.destiny.sequence.populater;

import org.destiny.sequence.bean.IdMeta;
import org.destiny.sequence.bean.IdType;
import org.destiny.sequence.model.Id;
import org.destiny.sequence.util.TimeUtils;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p></p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 22:32
 */
public class SyncIdPopulator implements IdPopulator {

    private long sequence = 0;

    private long lastTimestamp = -1;

    /**
     * 使用 synchronize 关键字来保证
     * @param id
     * @param idMeta
     */
    @Override
    public synchronized void populateId(Id id, IdMeta idMeta) {
        long timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
        TimeUtils.validateTimestamp(lastTimestamp, timestamp);

        if (timestamp == lastTimestamp) {
            sequence ++;
            sequence &= idMeta.getSeqBitsStartPos();
            if (sequence == 0) {
                timestamp = TimeUtils.tillNextTimeUnit(lastTimestamp, IdType.parse(id.getType()));
            }
        } else {
            lastTimestamp = timestamp;
            sequence = 0;
        }

        id.setSeq(sequence);
        id.setTime(timestamp);
    }
}
