package com.qfox.commons.converter;

import java.io.Serializable;
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
 * @date 2015年9月11日 上午9:55:22
 *
 * @version 1.0.0
 */
public class Entity<T extends Serializable> implements Domain<T> {
	private T id;
	private Date dateCreated;

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
