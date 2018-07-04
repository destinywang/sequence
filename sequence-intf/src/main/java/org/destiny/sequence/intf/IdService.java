package org.destiny.sequence.intf;

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
 * @since 2018/7/3 23:44
 */
public interface IdService {

    /**
     * 产生唯一 ID
     *
     * @return
     */
    long genId();

    /**
     * 伪造某一时间的 ID
     *
     * @param time
     * @param seq
     * @return
     */
    long makeId(long time, long seq);

    /**
     * 伪造某一时间的 ID
     *
     * @param time
     * @param seq
     * @param machine
     * @return
     */
    long makeId(long time, long seq, long machine);

    /**
     * 伪造某一时间的 ID
     *
     * @param genMethod
     * @param time
     * @param seq
     * @param machine
     * @return
     */
    long makeId(long genMethod, long time, long seq, long machine);

    /**
     * 将整型时间翻译成格式化时间
     *
     * @param time
     * @return
     */
    Date transTime(long time);
}
