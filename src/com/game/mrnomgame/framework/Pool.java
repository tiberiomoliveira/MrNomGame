package com.game.mrnomgame.framework;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
	public interface PoolObjectFactory<T> {
		public T createObject();
	}
	
	private final PoolObjectFactory<T> factory;
	private final int                  max_size;
	private final List<T>              free_objects;
	
	public Pool(PoolObjectFactory<T> factory, int max_size) {
		this.factory      = factory;
		this.max_size     = max_size;
		this.free_objects = new ArrayList<T>(max_size); 
	}
	
	public T newObject() {
		T object = null;
		
		object = (free_objects.size() == 0) ?
					factory.createObject():
					free_objects.remove(free_objects.size() - 1);

		return object;
	}
	
	public void free(T object) {
		if (free_objects.size() < max_size)
			free_objects.add(object);
	}
}