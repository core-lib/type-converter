package com.qfox.commons.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * 
 * 
 * @Description:映射集合类型转换器,提供无泛型参数集合转换 和 有泛型参数集合的转换
 * @author Change
 * @date 2014年1月9日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日<br/>
 *       内容:初始<br/>
 * 
 */
public class MapConverter implements Converter {
	private static Map<Class<?>, Class<?>> implementations = new HashMap<Class<?>, Class<?>>();

	static {
		implementations.put(Map.class, LinkedHashMap.class);
		implementations.put(SortedMap.class, NavigableMap.class);
		implementations.put(NavigableMap.class, TreeMap.class);
	}

	public boolean support(Type type) {
		if (type instanceof Class<?>) {
			Class<?> _class = Class.class.cast(type);
			return Map.class.isAssignableFrom(_class);
		}
		if (type instanceof ParameterizedType) {
			Class<?> _class = Class.class.cast(ParameterizedType.class.cast(type).getRawType());
			return Map.class.isAssignableFrom(_class);
		}
		return false;
	}

	public Object convert(Object source, Class<?> _class, TypeConverter context) throws Exception {
		Map<?, ?> sources = Map.class.cast(source);
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		for (Entry<?, ?> entry : sources.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			map.put(context.doConvert(key, key.getClass()), context.doConvert(value, value.getClass()));
		}
		if (_class.isInterface()) {
			_class = implementations.get(_class);
		}
		return _class.getConstructor(Map.class).newInstance(map);
	}

	public Object convert(Object source, ParameterizedType type, TypeConverter context) throws Exception {
		Class<?> _class = (Class<?>) type.getRawType();
		Map<?, ?> sources = Map.class.cast(source);
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		for (Entry<?, ?> entry : sources.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			map.put(context.doConvert(key, type.getActualTypeArguments()[0]), context.doConvert(value, type.getActualTypeArguments()[1]));
		}
		if (_class.isInterface()) {
			_class = implementations.get(_class);
		}
		return _class.getConstructor(Map.class).newInstance(map);
	}

	public Object convert(Object source, GenericArrayType type, TypeConverter context) throws Exception {
		return null;
	}

}
