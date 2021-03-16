package com.yyc.lock_demo.service;

import com.yyc.lock_demo.mapper.ShopStockMapper;
import com.yyc.lock_demo.pojo.ShopStock;
import com.yyc.lock_demo.utils.UnsafeInstanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.Unsafe;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: aqs
 * @description:
 * @author: Anakin Yang
 * @create: 2021-01-19 20:36
 */
@Service
@Slf4j
public class DemoService {
  @Autowired
  ShopStockMapper shopStockMapper;

  private volatile int state = 0;
  private static final Unsafe unsafe = UnsafeInstanceUtils.reflectGetUnsafe();
  private Thread lockHolder;
  private static final long stateOffset;

  // 同synchronized区别的锁，前者是JVM内置锁，不需要手动加锁与解锁
  // 显示锁，需要手动加锁解锁，基于Aqs实现 fair:true公平锁
  //    private  ReentrantLock lock = new ReentrantLock(true);

  private final ConcurrentLinkedDeque<Thread> queue = new ConcurrentLinkedDeque();

  public int getState() {
    return state;
  }

  static {
    try {
      stateOffset = unsafe.objectFieldOffset(DemoService.class.getDeclaredField("state"));
    } catch (Exception e) {
      throw new Error(e);
    }
  }

  public boolean compareAndSwapState(int expect, int update) {
    return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
  }

  // 减库存
  public String decStockWithLock() {
    // 此处加锁
    Thread current = Thread.currentThread();
    for (; ; ) { // T1:success T2:fail T3:fail
      int state = getState();
      // state++ 高并发场景下：1；state copy到工作内存 2：再进行自增
      // 可能会出现 T1 1+1=2  T2 1+1=2
      if (state == 0) {
        if (compareAndSwapState(0, 1)) {
          lockHolder = current;
          // 加锁成功
          break;
        }
      }

      // T2,T3必然执行会被阻塞
      // 如果不阻塞那就会一直有线程进来
      // 阻塞之前需要一个队列保存被阻塞线程，在持有锁的线程释放锁的时候去唤醒
      // 队列的好处是不会像synchronized唤醒所有线程
      queue.add(current);
      LockSupport.park();
    }
    Integer stock;
    Integer id = 1;
    ShopStock shopStock = shopStockMapper.selectByPrimaryKey(id);
    if (shopStock == null || (stock = shopStock.getStock()) <= 0) {
      log.info("下单失败，已经没有库存了"); // 此处如果发生异常
      return "下单失败，已经没有库存了";
    }
    stock--;
    ShopStock stockUpdate = new ShopStock(id, stock);
    shopStockMapper.updateByPrimaryKey(stockUpdate);
    log.info("下单成功，当前剩余产品数为：{}", stock);
    // 解锁
    // 如果未唤醒 T1会释放锁
    for (; ; ) {
      int state = getState();
      if (state != 0 && lockHolder == current) {
        compareAndSwapState(state, 0);
        break;
      }
      // 释放锁，需要唤醒阻塞线程
      if (queue.size() > 0) {
        Thread t = queue.poll();
        LockSupport.unpark(t);
      }
    }
    //        lock.unlock();
    return "下单成功，当前剩余产品数为：{}" + stock;
  }

  public String decStockNoLock() {
    Integer stock;
    Integer id = 1;
    ShopStock shopStock = shopStockMapper.selectByPrimaryKey(id);
    if (shopStock == null || (stock = shopStock.getStock()) <= 0) {
      log.info("下单失败，已经没有库存了"); // 此处如果发生异常
      return "下单失败，已经没有库存了";
    }
    stock--;
    ShopStock stockUpdate = new ShopStock(id, stock);
    int i = shopStockMapper.updateByPrimaryKey(stockUpdate);
    log.info("下单成功，当前剩余库存数为：{}", stock);
    return "下单成功，当前剩余库存数为：" + stock;
  }

  public String decStockReentrantLock() {
    ReentrantLock lock = new ReentrantLock(true);
    lock.lock();
    Integer stock;
    Integer id = 1;
    ShopStock shopStock = shopStockMapper.selectByPrimaryKey(id);
    if (shopStock == null || (stock = shopStock.getStock()) <= 0) {
      log.info("下单失败，已经没有库存了"); // 此处如果发生异常
      return "下单失败，已经没有库存了";
    }
    stock--;
    ShopStock stockUpdate = new ShopStock(id, stock);
    int i = shopStockMapper.updateByPrimaryKey(stockUpdate);
    log.info("下单成功，当前剩余库存数为：{}", stock);
    lock.unlock();
    return "下单成功，当前剩余库存数为：{}" + stock;
  }

  private String lockKey = "testRedisoon";

  @Autowired
  Redisson redisson;


  // redisson
  public void redisson() {
    RLock lock = redisson.getLock(lockKey);
    try {
      lock.lock(60, TimeUnit.SECONDS); // 设置60秒自动释放锁  （默认是30秒自动过期）
      Integer stock=0;
      Integer id = 1;
      ShopStock shopStock = shopStockMapper.selectByPrimaryKey(id);
      if (shopStock == null || (stock = shopStock.getStock()) <= 0) {
        log.info("下单失败，已经没有库存了"); // 此处如果发生异常
        return;
      }
      stock--;
      ShopStock stockUpdate = new ShopStock(id, stock);
      int i = shopStockMapper.updateByPrimaryKey(stockUpdate);
      log.info("下单成功，当前剩余库存数为：{}", stock);
      return;
    } catch (Exception e) {
      log.info("异常：{}", e);
    } finally {
      if (lock.isLocked()) { // 是否还是锁定状态
        if (lock.isHeldByCurrentThread()) { // 时候是当前执行线程的锁
          lock.unlock(); // 释放锁
        }
      }
    }
  }
}
