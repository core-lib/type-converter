package com.qfox.commons.converter;

import java.lang.reflect.Type;

/**
 * 拓展转换器,用于需要自定义转换的类上
 * 
 * @author Change
 * 
 */
public interface ExtendConverter {

	/**
	 * 拓展转换方法
	 * 
	 * @param type
	 *            转换后的目标类型
	 * @param context
	 *            转换器容器
	 * @return 转换成目标类型后的对象
	 */
	Object convert(Type type, TypeConverter context);

}
