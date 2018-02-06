package com.qfox.commons.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author yangchangpei 646742615@qq.com
 *
 * @date 2015年9月9日 下午3:22:59
 *
 * @version 1.0.0
 */
public class DateConverter implements Converter {

	public boolean support(Type type) {
		return type instanceof Class<?> && Date.class.isAssignableFrom((Class<?>) type);
	}

	public Object convert(Object source, Class<?> _class, TypeConverter context) throws Exception {
		return _class.getConstructor(long.class).newInstance(Date.class.cast(source).getTime());
	}

	public Object convert(Object source, ParameterizedType type, TypeConverter context) throws Exception {
		return null;
	}

	public Object convert(Object source, GenericArrayType type, TypeConverter context) throws Exception {
		return null;
	}

}
