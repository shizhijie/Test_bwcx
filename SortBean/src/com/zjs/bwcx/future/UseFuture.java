package com.zjs.bwcx.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UseFuture{

	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		
		
		
		Callable<String> task = new Callable<String>() {
			String para = "query";
			@Override
			public String call() throws Exception {
				//模拟执行耗时
				Thread.sleep(5000);
				String result = this.para + "处理完成";
				return result;
			}
		};
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<String> res = executor.submit(task);
		String string = res.get(50000, TimeUnit.MILLISECONDS);
		System.out.println(string);
		executor.shutdown();
	}

}
