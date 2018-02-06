package com.qfox.commons.converter;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.openbeans.IntrospectionException;
import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;
import com.qfox.commons.converter.reflection.GenericArrayTypeImpl;
import com.qfox.commons.converter.reflection.ParameterizedTypeImpl;

/**
 * 
 * 
 * 
 * @Description: 复合类型转换器,完成包含其他类型属性的类型转换,包括无泛型参数和有泛型参数
 * @author Change
 * @date 2014年1月9日
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月9日<br/>
 *       内容:初始<br/>
 * 
 */
public class ComplexConverter implements Converter {
	private static Map<Class<?>, Map<TypeVariable<?>, Type>> mappings = new HashMap<Class<?>, Map<TypeVariable<?>, Type>>();
	private Map<Reference, Object> cache = new HashMap<Reference, Object>();

	public boolean support(Type type) {
		return true;
	}

	public Object convert(Object source, Class<?> _class, TypeConverter context) throws Exception {
		Reference reference = new Reference(source);
		if (cache.containsKey(reference)) {
			return cache.get(reference);
		}
		Object result = _class.newInstance();
		cache.put(reference, result);

		Map<TypeVariable<?>, Type> maping = mappings.get(_class);
		if (maping == null) {
			maping = mapping(_class, null);
			mappings.put(_class, maping);
		}

		PropertyDescriptor[] descriptors = Introspector.getBeanInfo(_class).getPropertyDescriptors();
		for (PropertyDescriptor descriptor : descriptors) {
			if (descriptor.getName().equals("class")) {
				continue;
			}
			try {
				descriptor = new PropertyDescriptor(descriptor.getName(), _class);
				PropertyDescriptor _descriptor = new PropertyDescriptor(descriptor.getName(), source.getClass());
				Object _source = _descriptor.getReadMethod().invoke(source);
				Type type = descriptor.getReadMethod().getGenericReturnType();
				Object value = context.doConvert(_source, replace(type, maping));
				descriptor.getWriteMethod().invoke(result, value);
			} catch (IntrospectionException e) {
				continue;
			}
		}
		return result;
	}

	public Object convert(Object source, ParameterizedType type, TypeConverter context) throws Exception {
		Class<?> _class = Class.class.cast(type.getRawType());

		Reference reference = new Reference(source);
		if (cache.containsKey(reference)) {
			return cache.get(reference);
		}
		Object result = _class.newInstance();
		cache.put(reference, result);

		Map<TypeVariable<?>, Type> maping = mappings.get(_class);
		if (maping == null) {
			maping = mapping(_class, null);
			mappings.put(_class, maping);
		}

		PropertyDescriptor[] descriptors = Introspector.getBeanInfo(_class).getPropertyDescriptors();
		for (PropertyDescriptor descriptor : descriptors) {
			if (descriptor.getName().equals("class")) {
				continue;
			}
			try {
				descriptor = new PropertyDescriptor(descriptor.getName(), _class);
				PropertyDescriptor _descriptor = new PropertyDescriptor(descriptor.getName(), source.getClass());
				Object _source = _descriptor.getReadMethod().invoke(source);
				Type _type = descriptor.getReadMethod().getGenericReturnType();
				Object value = context.doConvert(_source, replace(_type, maping));
				descriptor.getWriteMethod().invoke(result, value);
			} catch (IntrospectionException e) {
				continue;
			}
		}
		return result;
	}

	public Object convert(Object source, GenericArrayType type, TypeConverter context) throws Exception {
		return null;
	}

	/**
	 * 递归找出泛型参数类型对应的实际类型
	 * 
	 * @param clazz
	 *            普通类型
	 * @param parameterizedType
	 *            对应的参数化类型
	 * @return 泛型参数类型和实际类型的映射
	 */
	private static Map<TypeVariable<?>, Type> mapping(Class<?> clazz, ParameterizedType parameterizedType) {
		Map<TypeVariable<?>, Type> map = new HashMap<TypeVariable<?>, Type>();

		if (clazz == Object.class || clazz == null) {
			return map;
		}

		// 处理接口
		Class<?>[] interfaces = clazz.getInterfaces();
		Type[] generics = clazz.getGenericInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			map.putAll(mapping(interfaces[i], generics[i] instanceof ParameterizedType ? (ParameterizedType) generics[i] : null));
		}

		// 处理父类
		if (parameterizedType != null) {
			TypeVariable<?>[] variables = clazz.getTypeParameters();
			Type[] types = parameterizedType.getActualTypeArguments();
			for (int i = 0; i < variables.length; i++) {
				map.put(variables[i], types[i]);
			}
		}
		parameterizedType = clazz.getGenericSuperclass() instanceof ParameterizedType ? (ParameterizedType) clazz.getGenericSuperclass() : null;
		clazz = clazz.getSuperclass();
		map.putAll(mapping(clazz, parameterizedType));

		return map;
	}

	/**
	 * 将泛型类型参数转换成实际类型
	 * 
	 * @param type
	 *            任意类型
	 * @param map
	 *            映射关系
	 * @return 对应的实际类型
	 */
	private static Type replace(Type type, Map<TypeVariable<?>, Type> map) {
		Type actual = null;
		// 普通类型
		if (type instanceof Class<?>) {
			actual = type;
		}
		// T;
		else if (type instanceof TypeVariable<?>) {
			actual = map.get(type);
		}
		// T[] || AnyType<T>[]
		else if (type instanceof GenericArrayType) {
			Type temp = type;
			int dimension = 0;
			while (temp instanceof GenericArrayType) {
				temp = GenericArrayType.class.cast(temp).getGenericComponentType();
				dimension = dimension + 1;
			}
			if (temp instanceof TypeVariable<?>) {
				actual = Array.newInstance(Class.class.cast(map.get(temp)), new int[dimension]).getClass();
			} else if (temp instanceof ParameterizedType) {
				actual = replace(temp, map);
				while (dimension != 0) {
					actual = new GenericArrayTypeImpl(actual);
					dimension -= 1;
				}
			}
		}
		// AnyType<T> || AnyType<T[]>
		else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
			Type[] typeArguments = parameterizedType.getActualTypeArguments();
			final Type actualRawType = parameterizedType.getRawType();
			final Type actualOwnerType = parameterizedType.getOwnerType();
			final Type[] actualTypeArguments = new Type[typeArguments.length];
			for (int i = 0; i < typeArguments.length; i++) {
				actualTypeArguments[i] = replace(typeArguments[i], map);
			}
			actual = new ParameterizedTypeImpl(actualRawType, actualOwnerType, actualTypeArguments);
		}
		return actual;
	}

	private static class Reference {
		private final Object object;

		public Reference(Object object) {
			super();
			this.object = object;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((object == null) ? 0 : object.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Reference other = (Reference) obj;
			return this.object == other.object;
		}

		@Override
		public String toString() {
			return "Reference [object=" + object + "]";
		}

	}

}
