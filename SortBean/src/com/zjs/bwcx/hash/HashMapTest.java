package com.zjs.bwcx.hash;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapTest  {
	 //  使用ConcurrentHashMap可运行正常
    final static ConcurrentHashMap<String, String> ch = new ConcurrentHashMap<>();
    //final static HashMap<String, String> ch = new HashMap<>();
 
    //  运行的线程
    public static class R implements Runnable {
 
        private String end;
 
        public R(String end) {
            super();
            this.end = end;
        }
 
        @Override
        public void run() {
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            while(!this.end.equals(ch.get("end"))) {
                System.out.println(this.end);
            }
            System.out.println(this.end + ": is End!");
        }
 
    }
 
    public static void main(String[] args) {
        //  启动线程
        for(int i = 0; i < 10; i++) {
            new Thread(new R(String.valueOf(i))).start();
        }
        new Thread(() -> {
            int j = 0;
            while(j < 10) {
                ch.put("end", String.valueOf(j));
                //  让其他线程有执行的机会
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                j ++;
            }
        }).start();
    }
}
