package com.lingstep.lhs.sys.rbac.domain;

import java.util.Date;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author <a href="mailto:lin.f@gener-tech.com">linfeng</a>
 * @version 1.0
 */

public class UserDto {
    // 主键ID
    private String id;
    // 登录名
    private String loginName;
    // 用户名
    private String name;
    // 状态
    private Integer status;
    
    private Date lockTime;
    // 角色
    private String roleName;
    // 邮箱
    private String email;

    private Integer isAdmin;

    //部门名称
    private String deptName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

	/**
	 * lockTimeget方法
	 * @return the lockTime
	 */
	public Date getLockTime() {
		return lockTime;
	}

	/**
	 * lockTimeset方法
	 * @param lockTime the lockTime to set
	 */
	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}
    
    
}
