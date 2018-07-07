package org.destiny.sequence.util;

import org.destiny.sequence.bean.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtils {
    protected static final Logger log = LoggerFactory.getLogger(TimeUtils.class);

    /**
     * 2018-01-01 00:00:00
     */
    public static final long EPOCH = 1514736000000L;


    public static void validateTimestamp(long lastTimestamp, long timestamp) {
        if (timestamp < lastTimestamp) {
            if (log.isErrorEnabled()) {
                log.error(String.format("Clock moved backwards.  Refusing to generate id for %d second/milisecond.",
                                lastTimestamp - timestamp));
            }

            throw new IllegalStateException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d second/milisecond.",
                            lastTimestamp - timestamp));
        }
    }

    public static long tillNextTimeUnit(final long lastTimestamp, final IdType idType) {
        if (log.isInfoEnabled()) {
            log.info(String.format("Ids are used out during %d. Waiting till next second/milisencond.",
                            lastTimestamp));
        }

        long timestamp = TimeUtils.genTime(idType);
        while (timestamp <= lastTimestamp) {
            timestamp = TimeUtils.genTime(idType);
        }

        if (log.isInfoEnabled()) {
            log.info(String.format("Next second/milisencond %d is up.", timestamp));
        }
        return timestamp;
    }

    /**
     * 以 2018-01-01 00:00:00 作为开始
     * 根据 idType 的不同返回不同格式的时间
     *
     *
     * @param idType
     * @return
     */
    public static long genTime(final IdType idType) {
        if (idType == IdType.SECONDS) {
            return (System.currentTimeMillis() - TimeUtils.EPOCH) / 1000;
        } else if (idType == IdType.MILLISECONDS) {
            return (System.currentTimeMillis() - TimeUtils.EPOCH);
        }
        return (System.currentTimeMillis() - TimeUtils.EPOCH) / 1000;
    }

}
