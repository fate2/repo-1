/**
 * 
 */
package com.lingstep.lhs.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linfeng
 * 基础bean
 */
public class BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	// 创建人
    private String createUser;
    // 创建日期
    private Date createDatetime;
    // 修改人
    private String updateUser;
    
    // 修改日期
    private Date updateDatetime;
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
}

