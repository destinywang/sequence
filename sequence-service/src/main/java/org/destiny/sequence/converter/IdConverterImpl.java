package org.destiny.sequence.converter;

import org.destiny.sequence.bean.IdMeta;
import org.destiny.sequence.bean.IdMetaFactory;
import org.destiny.sequence.bean.IdType;
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
 * @since 2018/7/7 10:48
 */
public class IdConverterImpl implements IdConverter {

    private IdMeta idMeta;

    private IdType idType;

    @Override
    public long convert(Id id) {
        return doConvert(id, IdMetaFactory.getIdMeta(idType));
    }

    private long doConvert(Id id, IdMeta idMeta) {
        long ret = 0;
        ret |= id.getMachine();
        ret |= id.getSeq() << idMeta.getSeqBitsStartPos();
        ret |= id.getTime() << idMeta.getTimeBitsStartPos();
        ret |= id.getGenMethod() << idMeta.getGenMethodBitsStartPos();
        ret |= id.getType() << idMeta.getTypeBitsStartPos();
        ret |= id.getVersion() << idMeta.getVersionBitsStartPos();
        return ret;
    }

    @Override
    public Id convert(long id) {
        return doConvert(id, IdMetaFactory.getIdMeta(idType));
    }

    private Id doConvert(long id, IdMeta idMeta) {
        Id ret = new Id();
        ret.setMachine(id & idMeta.getMachineBits());
        ret.setSeq((id >>> idMeta.getSeqBitsStartPos()) & idMeta.getSeqBitsStartPos());
        ret.setTime((id >>> idMeta.getTimeBitsStartPos()) & idMeta.getTimeBitsStartPos());
        ret.setGenMethod((id >>> idMeta.getGenMethodBitsStartPos()) & idMeta.getGenMethodBitsStartPos());
        ret.setType((id >>> idMeta.getTypeBitsStartPos()) & idMeta.getTypeBitsStartPos());
        ret.setVersion((id >>> idMeta.getVersionBitsStartPos()) & idMeta.getVersionBitsStartPos());
        return ret;
    }
}
