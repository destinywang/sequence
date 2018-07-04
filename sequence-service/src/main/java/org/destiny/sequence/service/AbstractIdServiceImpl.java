package org.destiny.sequence.service;

import org.destiny.sequence.bean.IdMeta;
import org.destiny.sequence.bean.IdType;
import org.destiny.sequence.converter.IdConverter;
import org.destiny.sequence.intf.IdService;
import org.destiny.sequence.model.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 王康
 * hzwangkang1@corp.netease.com
 * ------------------------------------------------------------------
 * <p>
 *     模板类, 实现在任何场景下都不变的逻辑
 * </p>
 * ------------------------------------------------------------------
 * Corpright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/3 23:49
 */
public abstract class AbstractIdServiceImpl implements IdService {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractIdServiceImpl.class);

    /**
     * 机器 ID
     */
    protected long machineId = -1;

    /**
     * 生成方式
     */
    protected long genMethod = 0;

    /**
     * 类型
     */
    protected long type;

    /**
     * 版本
     */
    protected long version;

    protected IdConverter idConverter;

    protected IdMeta idMeta;
    protected IdType idType;

    protected abstract void populateId(Id id);

    /**
     * 产生唯一 ID
     *
     * @return
     */
    @Override
    public long genId() {
        Id id = new Id();

        id.setMachine(machineId);
        id.setGenMethod(genMethod);
        id.setType(type);
        id.setVersion(version);

        populateId(id);

        // Use trace because it cause low performance
        long ret = idConverter.convert(id, this.idMeta);
        logger.info(String.format("Id: %s => %d", id, ret));
        return ret;
    }
}
