package com.qfox.commons.converter;

import com.qfox.commons.converter.reflection.ParameterizedTypeImpl;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Change
 * <p>
 * 修订记录<br/>
 * 姓名:Change<br/>
 * 时间:2014年1月7日<br/>
 * 内容:初始<br/>
 * 转换器上下文, 主要为了解开转换器与转换器之间的耦合, 让每个转换器只关心自己能力范围内的工作, 其他交给上下文去调度<br/>
 * 该转换器设计是为了简便和统一从 Domain 转换成 DTO 之间的过程,提供一个简单透明的转换方法,以下是使用该转换器的约束<br/>
 * 1.需要转换的属性名必须一致<br/>
 * 2.如果类型是简单类型或简单类型的数组即八大基本类型和String类型及其数组,那么类型必须一致<br/>
 * 3.集合/Map类型提倡面向接口,而且实现类必须提供只有一个Collection.class/Map.class参数的构造器<br/>
 * 4.泛型参数是简单类型需要一致,如果为普通类型可以一致或相似<br/>
 * 5.普通类型的属性或泛型参数不可以为抽象类或接口而且必须提供无参构造方法<br/>
 * @date 2014年1月7日
 */
public class TypeConverter {
    private List<Converter> converters = new ArrayList<Converter>();

    {
        converters.add(new SimpleConverter());
        converters.add(new EnumConverter());
        converters.add(new ArrayConverter());
        converters.add(new CollectionConverter());
        converters.add(new MapConverter());
        converters.add(new DateConverter());
        converters.add(new ComplexConverter());
    }

    private TypeConverter() {
    }

    /**
     * 转换
     *
     * @param source    源对象
     * @param _class    最外层类型
     * @param arguments 实际参数类型
     * @return 转换之后的目标类型对象
     */
    public static <T> T convert(Object source, final Class<T> _class, final Type... arguments) throws ConvertException {
        if (arguments == null || arguments.length == 0) return _class.cast(new TypeConverter().doConvert(source, _class));
        else return _class.cast(new TypeConverter().doConvert(source, new ParameterizedTypeImpl(_class, null, arguments)));
    }

    public static <K, V> Map<K, V> convert(Map<?, ?> map, Class<K> key, Class<V> value) throws ConvertException {
        Map<?, ?> source = convert(map, Map.class, key, value);
        Map<K, V> result = new HashMap<K, V>();
        for (Entry<?, ?> entry : source.entrySet()) result.put(key.cast(entry.getKey()), value.cast(entry.getValue()));
        return result;
    }

    public static <E> List<E> convert(List<?> list, Class<E> element) throws ConvertException {
        List<?> source = convert(list, List.class, element);
        List<E> result = new ArrayList<E>();
        for (Object object : source) result.add(element.cast(object));
        return result;
    }

    public static <E> Set<E> convert(Set<?> set, Class<E> element) throws ConvertException {
        Set<?> source = convert(set, Set.class, element);
        Set<E> result = new LinkedHashSet<E>();
        for (Object object : source) result.add(element.cast(object));
        return result;
    }

    <T> T doConvert(Object source, Class<T> type) throws ConvertException {
        return type.cast(doConvert(source, Type.class.cast(type)));
    }

    Object doConvert(Object source, Type type) throws ConvertException {
        if (source == null || source.getClass() == type) return source;
        if (source instanceof ExtendConverter) return ExtendConverter.class.cast(source).convert(type, this);
        for (Converter converter : converters) {
            try {
                if (!converter.support(type)) continue;
                if (type instanceof Class<?>) return converter.convert(source, Class.class.cast(type), this);
                if (type instanceof ParameterizedType) return converter.convert(source, ParameterizedType.class.cast(type), this);
                if (type instanceof GenericArrayType) return converter.convert(source, GenericArrayType.class.cast(type), this);
            } catch (ConvertException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return source;
    }

}
