package org.destiny.sequence.provider;

import org.destiny.sequence.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 王康
 * destinywk@163.com
 * ------------------------------------------------------------------
 * <p></p>
 * ------------------------------------------------------------------
 * Corpright 2018 Destiny, Org. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version JDK 1.8.0_101
 * @since 2018/7/7 13:29
 */
public class IpConfigurableMachineIdProvider implements MachineIdProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpConfigurableMachineIdProvider.class);

    private long machineId;

    private Map<String, Long> ipMap = new HashMap<>();

    public IpConfigurableMachineIdProvider() {
    }

    public IpConfigurableMachineIdProvider(String ips) {
        LOGGER.info("IpConfigurableMachineIdProvider.init, ips: {}", ips);
        if (!StringUtils.isEmpty(ips)) {
            String[] strings = ips.split(",");

            for (String string : strings) {
                ipMap.put(string, Long.valueOf(string));
            }
        }

    }

    public void init() {
        String ip = IpUtils.getHostIp();
        if (StringUtils.isEmpty(ip)) {
            LOGGER.error("Fail to get host Ip Address.");
            throw new IllegalStateException("Fail to get host Ip Address.");
        }
        if (!ipMap.containsKey(ip)) {
            String msg = String.format("Fail to configure ID for host IP address %s. " +
                    "Stop to initialize the IpConfigurableMachineIdProvider", ip);
            LOGGER.error(msg);
            throw new IllegalStateException(msg);
        }
        machineId = ipMap.get(ip);
        LOGGER.info("MachineId: {}, ip: {}", machineId, ip);
    }

    @Override
    public long getMachineId() {
        return machineId;
    }
}
