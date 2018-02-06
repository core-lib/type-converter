package com.qfox.commons.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 * 
 * 
 * @Description:单列集合转换器,提供无泛型参数集合转换 和 有泛型参数集合的转换
 * @author Change
 * @date 2014年1月9日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日<br/>
 *       内容:初始<br/>
 * 
 */
public class CollectionConverter implements Converter {

	private static Map<Class<?>, Class<?>> implementations = new HashMap<Class<?>, Class<?>>();

	static {
		implementations.put(List.class, ArrayList.class);
		implementations.put(Set.class, HashSet.class);
		implementations.put(SortedSet.class, NavigableSet.class);
		implementations.put(NavigableSet.class, TreeSet.class);
		implementations.put(Queue.class, PriorityQueue.class);
		implementations.put(Deque.class, ArrayDeque.class);
	}

	public boolean support(Type type) {
		if (type instanceof Class<?>) {
			Class<?> _class = Class.class.cast(type);
			return Collection.class.isAssignableFrom(_class);
		}
		if (type instanceof ParameterizedType) {
			Class<?> _class = Class.class.cast(ParameterizedType.class.cast(type).getRawType());
			return Collection.class.isAssignableFrom(_class);
		}
		return false;
	}

	public Object convert(Object source, Class<?> _class, TypeConverter context) throws Exception {
		Collection<?> sources = Collection.class.cast(source);
		Collection<Object> collection = new ArrayList<Object>();
		for (Object object : sources) {
			collection.add(context.doConvert(object, object.getClass()));
		}
		if (_class.isInterface()) {
			_class = implementations.get(_class);
		}
		return _class.getConstructor(Collection.class).newInstance(collection);
	}

	public Object convert(Object source, ParameterizedType type, TypeConverter context) throws Exception {
		Class<?> _class = (Class<?>) type.getRawType();
		Collection<?> sources = Collection.class.cast(source);
		Collection<Object> collection = new ArrayList<Object>();
		for (Object object : sources) {
			collection.add(context.doConvert(object, type.getActualTypeArguments()[0]));
		}
		if (_class.isInterface()) {
			_class = implementations.get(_class);
		}
		return _class.getConstructor(Collection.class).newInstance(collection);
	}

	public Object convert(Object source, GenericArrayType type, TypeConverter context) throws Exception {
		return null;
	}

}
