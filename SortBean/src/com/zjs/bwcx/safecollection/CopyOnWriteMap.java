package com.zjs.bwcx.safecollection;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CopyOnWriteMap<K,V> implements Map<K, V>,Cloneable,Serializable {
	
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */  
	private static final long serialVersionUID = -1977877492670660600L;
	private volatile Map<K, V> internalMap;
	
	@Override
	public V put(K key, V value) {
		synchronized (this) {
			Map<K, V> newMap = new HashMap<>();
			V val = newMap.put(key, value);
			internalMap = newMap;
			return val;
		}
	}
	
	@Override
	public V get(Object key) {
		return this.internalMap.get(key);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		synchronized(this) {
			Map<K, V> newMap = new HashMap<>(internalMap);
			newMap.putAll(m);
			internalMap = newMap;
		}
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
