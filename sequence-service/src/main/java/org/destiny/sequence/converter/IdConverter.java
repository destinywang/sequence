package org.destiny.sequence.converter;

import org.destiny.sequence.bean.IdMeta;
import org.destiny.sequence.model.Id;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p></p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 19:59
 */
public interface IdConverter {

    long convert(Id id, IdMeta idMeta);

    Id convert(long id, IdMeta idMeta);
}
