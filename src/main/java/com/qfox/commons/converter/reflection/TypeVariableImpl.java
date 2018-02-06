/**
 * utils[com.change.utils.reflect]
 * Change
 * 2014年1月12日 下午12:10:51
 */

package com.qfox.commons.converter.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * 
 * 
 * @Description:类型变量实现
 * 
 * 
 * @author Change
 * @date 2014年1月12日 下午12:10:51
 * 
 *       修订记录<br/>
 *       姓名:Change<br/>
 *       时间:2014年1月12日 下午12:10:51<br/>
 *       内容:初始<br/>
 * 
 */
public class TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D> {

	private Type[] bounds;

	private D genericDeclaration;

	private String name;

	public TypeVariableImpl(Type[] bounds, D genericDeclaration, String name) {
		super();
		this.bounds = bounds;
		this.genericDeclaration = genericDeclaration;
		this.name = name;
	}

	public Type[] getBounds() {
		return bounds;
	}

	public D getGenericDeclaration() {
		return genericDeclaration;
	}

	public String getName() {
		return name;
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public Annotation[] getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	public Annotation[] getDeclaredAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	public AnnotatedType[] getAnnotatedBounds() {
		// TODO Auto-generated method stub
		return null;
	}

}
