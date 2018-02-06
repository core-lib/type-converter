package com.qfox.commons.converter;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * 
 * 
 * @Description:数组类型转换器,实现对无限维数组的转换
 * @author Change
 * @date 2014年1月9日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日<br/>
 *       内容:初始<br/>
 * 
 */
public class ArrayConverter implements Converter {

	public boolean support(Type type) {
		return (type instanceof Class<?> && Class.class.cast(type).isArray()) || type instanceof GenericArrayType;
	}

	public Object convert(Object source, Class<?> _class, TypeConverter context) throws Exception {
		int length = Array.getLength(source);
		Object target = Array.newInstance(_class.getComponentType(), length);
		if (_class.getComponentType().isPrimitive()) {
			System.arraycopy(source, 0, target, 0, length);
		} else {
			for (int i = 0; i < length; i++) {
				Array.set(target, i, context.doConvert(Array.get(source, i), _class.getComponentType()));
			}
		}
		return target;
	}

	public Object convert(Object source, ParameterizedType type, TypeConverter context) throws Exception {
		return null;
	}

	public Object convert(Object source, GenericArrayType type, TypeConverter context) throws Exception {
		int dimension = 0;
		Type temp = type;
		while (temp instanceof GenericArrayType) {
			temp = GenericArrayType.class.cast(temp).getGenericComponentType();
			dimension += 1;
		}
		int length = Array.getLength(source);
		int[] dimensions = new int[dimension];
		dimensions[0] = length;
		Object target = Array.newInstance(Class.class.cast(ParameterizedType.class.cast(temp).getRawType()), dimensions);
		for (int i = 0; i < length; i++) {
			Array.set(target, i, context.doConvert(Array.get(source, i), type.getGenericComponentType()));
		}
		return target;
	}

}
