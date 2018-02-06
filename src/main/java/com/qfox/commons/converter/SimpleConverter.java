package com.qfox.commons.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * 
 * @Description:简单类型转换器,实现对八种基本类型和String类型的转换
 * @author Change
 * @date 2014年1月9日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日<br/>
 *       内容:初始<br/>
 * 
 */
public class SimpleConverter implements Converter {

	private static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();

	static {
		map.put(byte.class, Byte.class);
		map.put(char.class, Character.class);
		map.put(short.class, Short.class);
		map.put(int.class, Integer.class);
		map.put(float.class, Float.class);
		map.put(long.class, Long.class);
		map.put(double.class, Double.class);
		map.put(boolean.class, Boolean.class);
		map.put(String.class, String.class);
		map.put(Class.class, Class.class);
	}

	public boolean support(Type type) {
		if (type instanceof Class<?>) {
			Class<?> _class = Class.class.cast(type);
			return map.containsKey(_class) || map.containsValue(_class);
		}
		if (type instanceof ParameterizedType) {
			Class<?> _class = Class.class.cast(ParameterizedType.class.cast(type).getRawType());
			return map.containsKey(_class) || map.containsValue(_class);
		}
		return false;
	}

	public Object convert(Object source, Class<?> _class, TypeConverter context) throws Exception {
		if (map.containsKey(_class)) {
			return map.get(_class).cast(source);
		} else {
			return _class.cast(source);
		}
	}

	public Object convert(Object source, ParameterizedType type, TypeConverter context) throws Exception {
		Class<?> _class = Class.class.cast(type.getRawType());
		if (map.containsKey(_class)) {
			return map.get(_class).cast(source);
		} else {
			return _class.cast(source);
		}
	}

	public Object convert(Object source, GenericArrayType type, TypeConverter context) throws Exception {
		return null;
	}

}
