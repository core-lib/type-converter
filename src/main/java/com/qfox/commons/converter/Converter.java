package com.qfox.commons.converter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * 
 * 
 * @Description:转换器接口,可以通过实现该转换器接口,完成各种类型的转换或复制
 * @author Change
 * @date 2014年1月9日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日<br/>
 *       内容:初始<br/>
 * 
 */
public interface Converter {

	/**
	 * 判断是否支持该类型转换
	 * 
	 * @param type
	 *            类型
	 * @return 如果支持则返回 true 否则 false
	 */
	boolean support(Type type);

	/**
	 * 不带泛型参数的类型转换
	 * 
	 * @param source
	 *            源对象
	 * @param _class
	 *            目标类型
	 * @param context
	 *            转换器上下文
	 * @return 转换过后目标类的对象
	 */
	Object convert(Object source, Class<?> _class, TypeConverter context) throws Exception;

	/**
	 * 带有泛型参数的类型转换
	 * 
	 * @param source
	 *            源对象
	 * @param type
	 *            目标泛型类型
	 * @param context
	 *            转换器上下文
	 * @return 转换过后目标类的对象
	 */
	Object convert(Object source, ParameterizedType type, TypeConverter context) throws Exception;

	/**
	 * 带有泛型参数的数组类型转换
	 * 
	 * @param source
	 *            源对象
	 * @param type
	 *            目标泛型数组类型
	 * @param context
	 *            转换器上下文
	 * @return 转换过后目标类的对象
	 */
	Object convert(Object source, GenericArrayType type, TypeConverter context) throws Exception;

}
