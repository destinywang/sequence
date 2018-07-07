package org.destiny.sequence.converter;

import org.destiny.sequence.bean.IdMeta;
import org.destiny.sequence.model.Id;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p>
 *     Id 元数据 与 Id 长整型 的相互转换
 *     在主流程的 ID 元数据对象中设置了 ID 的各个属性之后, 通过本接口进行转换
 * </p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 19:59
 */
public interface IdConverter {

    long convert(Id id);

    Id convert(long id);
}
