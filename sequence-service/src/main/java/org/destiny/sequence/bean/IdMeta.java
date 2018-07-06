package org.destiny.sequence.bean;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p>
 *     id 的字节信息封装
 * </p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 19:59
 */
public class IdMeta {

    private byte machineBits;       // 机器编号
    private byte seqBitsMask;           // 序号
    private byte timeBits;          // 时间
    private byte genMethodBits;     // 生成方式
    private byte typeBits;          // 类型
    private byte versionBits;       // 版本

    public IdMeta(byte machineBits, byte seqBitsMask, byte timeBits, byte genMethodBits, byte typeBits, byte versionBits) {
        this.machineBits = machineBits;
        this.seqBitsMask = seqBitsMask;
        this.timeBits = timeBits;
        this.genMethodBits = genMethodBits;
        this.typeBits = typeBits;
        this.versionBits = versionBits;
    }

    public byte getMachineBits() {
        return machineBits;
    }

    public void setMachineBits(byte machineBits) {
        this.machineBits = machineBits;
    }

    public byte getSeqBitsMask() {
        return seqBitsMask;
    }

    public void setSeqBitsMask(byte seqBitsMask) {
        this.seqBitsMask = seqBitsMask;
    }

    public byte getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(byte timeBits) {
        this.timeBits = timeBits;
    }

    public byte getGenMethodBits() {
        return genMethodBits;
    }

    public void setGenMethodBits(byte genMethodBits) {
        this.genMethodBits = genMethodBits;
    }

    public byte getTypeBits() {
        return typeBits;
    }

    public void setTypeBits(byte typeBits) {
        this.typeBits = typeBits;
    }

    public byte getVersionBits() {
        return versionBits;
    }

    public void setVersionBits(byte versionBits) {
        this.versionBits = versionBits;
    }
}
