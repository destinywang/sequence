package org.destiny.sequence.model;

import java.io.Serializable;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p></p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/3 23:53
 */
public class Id implements Serializable {

    private static final long serialVersionUID = 6870931236218221183L;

    private long machine;
    private long seq;
    private long time;
    private long genMethod;
    private long type;
    private long version;

    public long getMachine() {
        return machine;
    }

    public void setMachine(long machine) {
        this.machine = machine;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getGenMethod() {
        return genMethod;
    }

    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Id{" +
                "machine=" + machine +
                ", seq=" + seq +
                ", time=" + time +
                ", genMethod=" + genMethod +
                ", type=" + type +
                ", version=" + version +
                '}';
    }
}
