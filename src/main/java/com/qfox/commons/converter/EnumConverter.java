
package com.qfox.commons.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * 
 * 
 * @Description:枚举类型转换器,类型不需要一致但枚举对象名称需要一致
 * @author Change
 * @date 2014年1月9日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日<br/>
 *       内容:初始<br/>
 * 
 */
public class EnumConverter implements Converter {

	public boolean support(Type type) {
		return type instanceof Class<?> && Class.class.cast(type).isEnum();
	}

	public Object convert(Object source, Class<?> _class, TypeConverter context) throws Exception {
		return _class.getMethod("valueOf", String.class).invoke(null, source.getClass().getMethod("name").invoke(source));
	}

	public Object convert(Object source, ParameterizedType type, TypeConverter context) throws Exception {
		return null;
	}

	public Object convert(Object source, GenericArrayType type, TypeConverter context) throws Exception {
		return null;
	}

}
