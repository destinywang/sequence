package org.destiny.sequence.populater;

import org.destiny.sequence.bean.IdMeta;
import org.destiny.sequence.model.Id;

import java.util.Timer;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p>
 *     需要计算构成唯一 ID 的格式中的另外两个变量
 *     - 时间
 *     - 序列号
 *     而二者的生成方式是多种多样的, 将生成逻辑封装在子类中
 * </p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 22:27
 */
public interface IdPopulator {

    void populateId(Id id, IdMeta idMeta);

}
