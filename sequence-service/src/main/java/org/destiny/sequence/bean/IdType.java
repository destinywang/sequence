package org.destiny.sequence.bean;

/**
 * @author 王康
 * hzwangkang1@corp.netease.com
 * ------------------------------------------------------------------
 * <p>
 *
 * </p>
 * ------------------------------------------------------------------
 * Corpright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/4 20:03
 */
public enum IdType {

    SECONDS("seconds"),
    MILLISECONDS("milliseconds");

    private String name;

    private IdType(String name) {
        this.name = name;
    }

    public static IdType parse(String name) {
        if ("seconds".equals(name)) {
            return SECONDS;
        } else if ("milliseconds".equals(name)) {
            return MILLISECONDS;
        }

        throw new IllegalArgumentException("Illegal IdType name <[" + name + "]>, available names are seconds and milliseconds");
    }

    public static IdType parse(long type) {
        if (type == 1) {
            return MILLISECONDS;
        } else if (type == 0) {
            return SECONDS;
        }

        throw new IllegalArgumentException("Illegal IdType value <[" + type + "]>, available values are 0 (for seconds) and 1 (for milliseconds)");
    }
}
