# 1. 基本需求

需求 | 描述
:-:|:-:
全局唯一 | 可以利用时间的有序性, 并且在某个时间单元下采用自增序列
粗略有序 | 在分布式系统中, 难以做到绝对有序, 因此可以采用相对有序的方式
可反解 | 一个 ID 在生成后, 本身就带有很多信息量, 在存储层面可以省下传统的 timestamp 等字段.
高性能 | ID 生成取决于网络 I/O 和 CPU 的性能
高可用 | 在一台机器挂掉的时候, 请求必须能够转发到其他机器上, 远程服务宕机, 也需要有本地容错方案
可伸缩 | 业务量永远都在增长, 系统必须考虑后续扩展

# 2. 设计要点
## 2.1 发布模式
根据最终的用户使用方式, 发布模式可以分为 `嵌入发布模式`, `中心服务器发布模式` 和 `REST` 发布模式

- 嵌入发布模式: 只适用于 Java 客户端, 提供一个本地的 Jar 包, Jar 包是嵌入式的原生服务, 需要提前配置本地机器的 ID, 但是不依赖与中心服务器
- 中心服务器发布模式: 只适用于 Java 客户端, 提供一个服务的客户端 Jar 包, Java 程序像本地 API 一样来调用, 但是依赖于中心的 ID 产生服务器.
- REST 发布模式: 中心服务器通过 RESTFul API 提供服务, 供任何客户端使用.

## 2.2 ID 类型
### 2.2.1 最大峰值型

    采用秒级有序, 秒级时间占 30 位, 序列号占 20 位

字段 | 版本 | 类型 | 生成方式 | 秒级时间 | 序列号 | 机器 ID
:-:|:-:|:-:|:-:|:-:|:-:|:-:|
位数 | 63 | 62 | 60 ~ 61 | 30 ~ 59 | 10 ~ 29 | 0 ~ 9

### 2.2.2 最小粒度型
    
    采用毫秒级生成, 毫秒级时间占用 40 位, 序列号占 10 位

字段 | 版本 | 类型 | 生成方式 | 秒级时间 | 序列号 | 机器 ID
:-:|:-:|:-:|:-:|:-:|:-:|:-:|
位数 | 63 | 62 | 60 ~ 61 | 20 ~ 59 | 10 ~ 19 | 0 ~ 9

最大峰值型能承受更大的峰值压力, 但是粗略有序的力度有点大; 最小粒度型有较细致的粒度, 但是每个毫秒能承受的理论峰值有限, 为 1024, 如果在同一个毫秒有更多的请求产生, 则必须等到下一个毫秒再响应.

## 2.3 数据结构
### 2.3.1 机器 ID
共 10 位, `2 ^ 10 = 1024`, 最多支持 1024 台服务器的集群

### 2.3.2 序列号
1. 最大峰值型: 理论上每秒支持 `2 ^ 20 = 1048576` 个 ID, 为百万级别
2. 最小粒度型: 理论上每毫秒支持 `2 ^ 10 = 1024` 个

### 2.3.3 秒级时间
1. 最大峰值型: 30 位, 表示秒级时间, `2 ^ 30 / 60 / 60 / 24 / 265 = 34`, 大致可以使用 30 年;
2. 最小粒度型: 40 位, 表示毫秒级时间: `2 ^ 40 / 1000 / 60 / 60 / 24 / 265 = 34`, 同样可以大致使用 30 年.

### 2.3.4 生成方式

    占 2 位, 用来区分 嵌入发布模式, 中心服务器发布模式, REST 发布模式
- 00: 嵌入发布模式
- 01: 中心服务器发布模式
- 10: REST 发布模式
- 11: 保留

### 2.3.5 ID 类型
0: 最大峰值型
1: 最小粒度型

### 2.3.6 版本
    
    占 1 位, 用来做扩展位或扩容时的临时方案

- 0: 默认值
- 1: 表示扩展或扩容中

## 2.4 并发
对于中心服务器和 Rest 发布模式, ID 生成的过程涉及网络 I/O 和 CPU 操作, ID 的生成基本上是内存到高速缓存的操作, 没有磁盘 I/O 操作, 网络 I/O 是系统的瓶颈.

相对于网络 I/O 来说, CPU 计算速度是瓶颈, 因此 ID 产生服务使用多线程的方式, 对于 ID 生成过程中的竞争点 `time` 和 `sequence`, 使用了多种实现方式.

1. 使用 `concurrent` 包的 `ReentrantLock` 进行互斥, 这是默认的实现方式, 也是追求性能和稳定这两个目标的妥协方案;
2. 使用传统的 `synchronized` 进行互斥, 通过传入 JVM 参数 `-Dsequence.sync.impl.key=true` 来开启.
3. 使用 `concurrent` 包的原子变量进行互斥, 性能很高, 但是在高并发下 CPU 负载会很高, 通过传入 JVM 参数 `-Dsequence.atomic.impl.key=true` 来开启.

## 2.5 机器 ID 的分配
我们将 ID 分为两个区段, 一个区段服务于中心服务发布模式和 REST 发布模式, 另一个区段服务于嵌入发布模式
- 0 ~ 923: 嵌入发布模式, 预先配置机器 ID, 最多支持 924 台内嵌服务器.
- 924 ~ 1023: 中心服务发布模式和 REST 发布模式, 最多支持 100 台

分配方式:
1. 通过共享数据库的方式为发号器服务池中的每个节点生成唯一的机器 ID, 这适合于服务池中节点较多的情况;
2. 通过配置发号服务器中每个节点的 IP 的方式确定每个节点的 ID, 这适合节点比较少的情况;
3. 在 Spring 配置文件中直接配置每个节点的机器 ID, 适合测试时使用;
4. 依托 Zookeeper 顺序节点生成 ID

## 2.6 时间同步
运行发号器的服务器需要保证时间的正确性, 这里使用 Linux 的定时任务 `crontab`, 周期性地通过时间服器虚拟机群来核准服务器时间.

```shell
ntpdate -u pool.ntp.orgpool.ntp.org
```

时间对发号器的影响如下:
1. 调整时间是否会影响 ID 产生?
    1. 未重启机器调慢时间, sequence 抛出异常, 拒绝产生 ID, 重启机器调快时间, 调整后正常产生 ID, 在调整时间段内没有 ID 产生;
    2. 重启机器调慢时间, sequence 将可能产生重复 ID
2. 每 4 年一次同步闰秒会不会影响 ID 产生?
    1. 调快 1 秒的情况下, 不影响 ID 产生, 所以调整的 1 秒内没有 ID 产生.

# 2. 根据设计实现多场景的发号器
## 2.1 项目结构
![](http://oetw0yrii.bkt.clouddn.com/18-7-3/4651141.jpg)

- sequence-parent: 父项目
- sequence-parent/sequence-intf: 发号器抽象出来的对外接口
- sequence-parent/sequence-service: 实现发号器接口的核心项目
- sequence-parent/sequence-server: 把发号器服务通过 Dubbo 服务导出的项目
- sequence-parent/sequence-rest: 通过 SpringBoot 启动的 REST 模式的发号器服务
- sequence-parent/sequence-rest-netty: 通过  Netty 启动的 REST 模式的发号器服务
- sequence-parent/sequence-client: 导入发号器 Dubbo 服务的客户端项目
- sequence-parent/sequence-sample: 嵌入式部署模式和 Dubbo 服务部署模式的使用实例
- sequence-parent/sequence-doc: 文档
- sequence-parent/deploy-maven.sh: 一键发布发号器依赖 Jar 包到 Maven 库
- sequence-parent/make-release.sh: 意见打包发号器
- sequence-parent/pom.xml

## 2.2 服务接口定义
```java
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
```

## 2.3 时间和序号的生成方式

一个完整的 sequence 序号, 包含:
1. 机器 ID
2. 序列号
3. 时间
4. 生成方式
5. 类型
6. 版本

其中 `序列号` 和 `时间` 不同于其他几项:
- 序列号在该项目中有多种生成方式, 如 `Lock 锁`, `synchronize 锁`, `CAS` 等
- 时间同样有多种格式, 包括 秒 和 毫秒

因此在生成 sequence 序列号时, 将这两个字段的生成逻辑独立出来, 有了如下设计

![](http://oetw0yrii.bkt.clouddn.com/18-7-6/99630784.jpg)

## 2.4 机器 ID 的生成

- DbMachineIdProvider: 通过在数据库配置机器 ID 来实现, 适用于任何情况, 但是使用起来比较麻烦
- IpConfigurableMachineIdProvider: 适用于线上生产环境, 通过所有 IP 的机器列表为每个机器生成一个唯一的机器 ID
- PropertyMachineIdProvider: 基于配置属性实现, 适用于测试环境

![](http://oetw0yrii.bkt.clouddn.com/18-7-7/33459177.jpg)

