package indi.ninet.amplifierprove.util;

/**
 * 
 * @author wfzhou
 * @since 2017-05-27 单节点的ID生成器 为用户的每一个作业分配全局唯一的ID,参考了twitter的snowflake源码 ID：LONG
 *        前52位表示当前时间戳(精确到毫秒),后12位表示递增序列 理论上可以 每一毫秒可以生成4096个不同的ID，完全符合系统的需求
 *        同时ID是递增的，因为系统中mysql数据库使用的是Innodb存储引擎 自增ID可以大大减少节点的分裂，减少数据库插入的性能消耗
 *
 */
public class UniqueID {

	// 单例模式
	private static final UniqueID instance = new UniqueID();
	// 计算标记时间
	private final long twepoch = 1288834974657L;
	// 递增序列的初始值
	private Long sequence = 0L;
	// 递增序列的位数
	private final long sequenceBits = 12L;
	// 递增序列的最大值
	private final long sequenceMax = -1L ^ (-1L << sequenceBits);
	// 时间戳左移位数
	private final long timestampLeftShift = sequenceBits;
	// 记录生成ID的上一个时间戳
	private long lastTimestamp = -1L;

	public static UniqueID getInstance() {
		return instance;
	}

	public synchronized Long nextId() {
		long timestamp = getTime();
		// 上次生成时间和当前时间相同,处于同一毫秒
		if (timestamp == lastTimestamp) {
			// 下一个递增序列值
			sequence = (sequence + 1) & sequenceMax;
			// 发生溢出
			if (sequence == 0L) {
				// 自旋到下一毫秒
				timestamp = nextMillis();
			}
		} else {
			// 如果和上次生成时间不同,重置sequence，sequence计数重新从0开始累加
			sequence = 0L;
		}
		/**
		 * 如果当前时间戳小于上一个时间戳,直接抛出异常 因为不同服务器的时间戳可能是不同步的，或者同一个服务器在系统运行期间 调整了时间
		 */
		if (timestamp < lastTimestamp) {
			try {
				throw new Exception(
						String.format(
								"Clock moved backwards. Refusing to generate id for %d milliseconds",
								lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 修改上一个时间戳为当前时间戳
		lastTimestamp = timestamp;
		long nextid = ((timestamp - twepoch) << timestampLeftShift)
				| (sequence);
		/*
		 * System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
		 * + timestampLeftShift + ",sequence:" +
		 * sequence+",lastTimestamp:"+lastTimestamp);
		 */
		return nextid;
	}

	// 获取当前时间戳,精确到毫秒
	private long getTime() {
		return System.currentTimeMillis();
	}

	// 自旋到下一毫秒
	private Long nextMillis() {
		long timestamp = getTime();
		while (timestamp <= lastTimestamp) {
			timestamp = getTime();
		}
		return timestamp;
	}

	public static void main(String args[]) {
		UniqueID uniqueID = UniqueID.getInstance();
		for (int i = 0; i < 100; i++) {
			System.out.println(uniqueID.nextId());
		}
	}
}
