package com.zjs.bwcx.stampedlock;

import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo {

}

class Point {
	private double x, y;// 内部定义表示坐标点
	private final StampedLock sl = new StampedLock();// 定义了StampedLock锁,

	void move(double deltaX, double deltaY) {
		long stamp = sl.writeLock();// 这里的含义和distanceFormOrigin方法中 s1.readLock()是类似的
		try {
			x += deltaX;
			y += deltaY;
		} finally {
			sl.unlock(stamp);
		}
	}

	// 乐观读锁案例
	double distanceFormOrigin() {// 只读方法
		long stamp = sl.tryOptimisticRead(); // 试图尝试一次乐观读 返回一个类似于时间戳的邮戳整数stamp 这个stamp就可以作为这一个所获取的凭证
		double currentX = x, currentY = y;// 读取x和y的值,这时候我们并不确定x和y是否是一致的
		if (!sl.validate(stamp)) {// 判断这个stamp是否在读过程发生期间被修改过,如果stamp没有被修改过,责任无这次读取时有效的,因此就可以直接return了,反之,如果stamp是不可用的,则意味着在读取的过程中,可能被其他线程改写了数据,因此,有可能出现脏读,如果如果出现这种情况,我们可以像CAS操作那样在一个死循环中一直使用乐观锁,知道成功为止
			stamp = sl.readLock();// 也可以升级锁的级别,这里我们升级乐观锁的级别,将乐观锁变为悲观锁, 如果当前对象正在被修改,则读锁的申请可能导致线程挂起.
			try {
				currentX = x;
				currentY = y;
			} finally {
				sl.unlockRead(stamp);// 退出临界区,释放读锁
			}
		}
		return Math.sqrt(currentX * currentX + currentY * currentY);
	}

	// 使用悲观锁获取读锁，并尝试转换为写锁
	void moveIfAtOrigin(double newX, double newY) {
		// 这里可以使用乐观读锁替换（1）
		long stamp = sl.readLock();
		try {
			// 如果当前点在原点则移动（2）
			while (x == 0.0 && y == 0.0) {
				// 尝试将获取的读锁升级为写锁（3）
				long ws = sl.tryConvertToWriteLock(stamp);
				// 升级成功，则更新票据，并设置坐标值，然后退出循环（4）
				if (ws != 0L) {
					stamp = ws;
					x = newX;
					y = newY;
					break;
				} else {
					// 读锁升级写锁失败则释放读锁，显示获取独占写锁，然后循环重试（5）
					sl.unlockRead(stamp);
					stamp = sl.writeLock();
				}
			}
		} finally {
			// 释放锁（6）
			sl.unlock(stamp);
		}
	}

}

/*
JDK8新增的StampedLock锁探究
StampedLock是并发包里面jdk8版本新增的一个锁，该锁提供了三种模式的读写控制，三种模式分别如下：

写锁writeLock，是个排它锁或者叫独占锁，同时只有一个线程可以获取该锁，当一个线程获取该锁后，其它请求的线程必须等待，当目前没有线程持有读锁或者写锁的时候才可以获取到该锁，请求该锁成功后会返回一个stamp票据变量用来表示该锁的版本，当释放该锁时候需要unlockWrite并传递参数stamp。
悲观读锁readLock，是个共享锁，在没有线程获取独占写锁的情况下，同时多个线程可以获取该锁，如果已经有线程持有写锁，其他线程请求获取该读锁会被阻塞。这里讲的悲观其实是参考数据库中的乐观悲观锁的，这里说的悲观是说在具体操作数据前悲观的认为其他线程可能要对自己操作的数据进行修改，所以需要先对数据加锁，这是在读少写多的情况下的一种考虑,请求该锁成功后会返回一个stamp票据变量用来表示该锁的版本，当释放该锁时候需要unlockRead并传递参数stamp。
乐观读锁tryOptimisticRead，是相对于悲观锁来说的，在操作数据前并没有通过CAS设置锁的状态，如果当前没有线程持有写锁，则简单的返回一个非0的stamp版本信息，获取该stamp后在具体操作数据前还需要调用validate验证下该stamp是否已经不可用，也就是看当调用tryOptimisticRead返回stamp后到到当前时间间是否有其他线程持有了写锁，如果是那么validate会返回0，否者就可以使用该stamp版本的锁对数据进行操作。由于tryOptimisticRead并没有使用CAS设置锁状态所以不需要显示的释放该锁。该锁的一个特点是适用于读多写少的场景，因为获取读锁只是使用与或操作进行检验，不涉及CAS操作，所以效率会高很多，但是同时由于没有使用真正的锁，在保证数据一致性上需要拷贝一份要操作的变量到方法栈，并且在操作数据时候可能其他写线程已经修改了数据，而我们操作的是方法栈里面的数据，也就是一个快照，所以最多返回的不是最新的数据，但是一致性还是得到保障的。

下面通过JDK8注释里面的一个例子讲解来加深对上面讲解的理解。
class Point {

    // 成员变量
    private double x, y;

    // 锁实例
    private final StampedLock sl = new StampedLock();

    // 排它锁-写锁（writeLock）
    void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    // 乐观读锁（tryOptimisticRead）
    double distanceFromOrigin() {

        // 尝试获取乐观读锁（1）
        long stamp = sl.tryOptimisticRead();
        // 将全部变量拷贝到方法体栈内（2）
        double currentX = x, currentY = y;
        // 检查在（1）获取到读锁票据后，锁有没被其他写线程排它性抢占（3）
        if (!sl.validate(stamp)) {
            // 如果被抢占则获取一个共享读锁（悲观获取）（4）
            stamp = sl.readLock();
            try {
                // 将全部变量拷贝到方法体栈内（5）
                currentX = x;
                currentY = y;
            } finally {
                // 释放共享读锁（6）
                sl.unlockRead(stamp);
            }
        }
        // 返回计算结果（7）
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    // 使用悲观锁获取读锁，并尝试转换为写锁
    void moveIfAtOrigin(double newX, double newY) {
        // 这里可以使用乐观读锁替换（1）
        long stamp = sl.readLock();
        try {
            // 如果当前点在原点则移动（2）
            while (x == 0.0 && y == 0.0) {
                // 尝试将获取的读锁升级为写锁（3）
                long ws = sl.tryConvertToWriteLock(stamp);
                // 升级成功，则更新票据，并设置坐标值，然后退出循环（4）
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    // 读锁升级写锁失败则释放读锁，显示获取独占写锁，然后循环重试（5）
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            // 释放锁（6）
            sl.unlock(stamp);
        }
    }
}

如上代码Point类里面有两个成员变量，和三个操作成员变量的方法，另外实例化了一个StampedLock对象用来保证操作的原子性。
首先分析下move方法，该函数作用是在添加增量，改变当前point坐标的位置，代码先获取到了写锁，然后对point坐标进行修改，然后释放锁。该锁是排它锁，这保证了其他线程调用move函数时候会被阻塞，直到当前线程显示释放了该锁，也就是保证了对变量x,y操作的原子性。
然后看下distanceFromOrigin方法，该方法作用是计算当前位置到原点的距离，代码（1）首先尝试获取乐观读锁，如果当前没有其它线程获取到了写锁，那么（1）会返回一个非0的stamp用来表示版本信息，代码（2）拷贝变量到本地方法栈里面，代码（3）检查在（1）获取到的票据是否还有效，之所以还要在此校验是因为代码（1）获取读锁时候并没有通过CAS操作修改锁的状态而是简单的通过与或操作返回了一个版本信息，这里校验是看在在获取版本信息到现在的时间段里面是否有其他线程持有了写锁，如果有则之前获取的版本信息就无效了。这里如果校验成功则执行（7）使用本地方法栈里面的值进行计算然后返回。需要注意的是在代码（3)校验成功后，代码（7）计算中其他线程可能获取到了写锁并且修改了x,y的值，而当前线程执行代码（7）进行计算时候采用的才是对修改前值的拷贝，也就是操作的值是对之前值的一个拷贝，并不是新的值。另外还有个问题，代码（2)和（3）能否互换，答案是不能，假设位置换了，那么首先执行validate，假如验证通过了，要拷贝x,y值到本地方法栈，而在拷贝的过程中很有可能其他线程已经修改了x,y中的一个，这就造成了数据的不一致性了。那么你可能会问，那不交换(2)和（3）时候在拷贝x,y值到本地方法栈里面时候也会存在其他线程修改了x,y中的一个值那，这个确实会存在，但是，别忘了拷贝后还有一道validate,如果这时候有线程修改了x,y中的值，那么肯定是有线程在调用validate前sl.tryOptimisticRead后获取了写锁，那么validate时候就会失败。现在应该明白了吧，这也是乐观读设计的精妙之处也是使用时候容易出问题的地方。下面继续分析validate失败后会执行代码（4）获取悲观读锁，如果这时候骑行线程持有写锁则代码（4）会导致的当前线程阻塞直到其它线程释放了写锁。获取到读锁后，代码（5）拷贝变量到本地方法栈，然后就是代码（6）释放了锁，拷贝的时候由于加了读锁在拷贝期间其它线程获取写锁时候会被阻塞，这保证了数据的一致性。最后代码（7）使用方法栈里面数据计算返回，同理这里在计算时候使用的数据也可能不是最新的，其它写线程可能已经修改过原来的x,y值了。
最后一个方法moveIfAtOrigin方法作用是如果当前坐标为原点则移动到指定的位置。代码（1）获取悲观读锁，保证其它线程不能获取写锁修改x,y值，然后代码（2）判断如果当前点在原点则更新坐标，代码（3)尝试升级读锁为写锁，这里升级不一定成功，因为多个线程都可以同时获取悲观读锁，当多个线程都执行到（3）时候只有一个可以升级成功，升级成功则返回非0的stamp，否非返回0，这里假设当前线程升级成功，然后执行步骤（4）更新stamp值和坐标值然后退出循环，如果升级失败则执行步骤（5）首先释放读锁然后申请写锁，获取到写锁后在循环重新设置坐标值。最后步骤（6)释放锁。
使用乐观读锁还是很容易犯错误的，必须要小心，必须要保证如下的使用顺序：
long stamp = lock.tryOptimisticRead(); //非阻塞获取版本信息
copyVaraibale2ThreadMemory();//拷贝变量到线程本地堆栈
if(!lock.validate(stamp)){ // 校验
    long stamp = lock.readLock();//获取读锁
    try {
        copyVaraibale2ThreadMemory();//拷贝变量到线程本地堆栈
     } finally {
       lock.unlock(stamp);//释放悲观锁
    }
    
}

useThreadMemoryVarables();//使用线程本地堆栈里面的数据进行操作

总结： 相比ReentrantLock读写锁，StampedLock通过提供乐观读锁在多线程多写线程少的情况下提供更好的性能，因为乐观读锁不需要进行CAS设置锁的状态而只是简单的测试状态。更具体测试数据期待Java并发编程基础之并发包源码剖析一书的出版。
*/