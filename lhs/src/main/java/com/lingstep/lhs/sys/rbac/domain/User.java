package com.lingstep.lhs.sys.rbac.domain;

import com.lingstep.lhs.common.domain.BaseBean;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class User extends BaseBean{
    // 登陆名
    @NotBlank
    @Length(max = 30)
    private String loginName;
    // 密码
    @Length(max = 30)
    private String password;
    // 盐
    private String salt;
    // 用户名
    @Length(max = 30)
    private String name;
    // Email
    @Email
    private String email;
    //手机号码
    @Length(max = 11)
    private String mobilePhone;
    //座机电话
    @Length(max = 20)
    private String landline;
    //办公室
    @Length(max = 50)
    private String theOffice;
    // 是否是管理员 0非 1超级 2租户管理员
    private Integer isAdmin;
    // 状态 0:禁用 1:启用
    private Integer status;
    //登录失败次数
    private Integer failureNum;
    //帐号锁定时间
    private Date lockTime;
    
    // 备注
    private String remark;
    // 是否删除 0:未删除 1:已删除
    private Integer isDelete;
    // 上级Id
    private String rptMgrId;
    // 租户Id
    private String tenantId;
    // 租户名
    private String tenantName;

    //部门id
    private String deptId;
    
    //部门name
    private String deptName;
    
    //部门code
    private String fullDeptCode;
    
    // 状态 0:禁用 1:启用
    private Integer uStatus;//在新增或修改员工信息时员工status和用户status命名冲突，所以在这加个uStatus
    
    //员工id
    private String staffId;
    
    //员工姓名 add by linfeng 2016-2-23
    private String staffName;
    
    // 上级名称
    private String rptMgrName;
    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId the tenantId to set
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return the tenantName
     */
    public String getTenantName() {
        return tenantName;
    }

    /**
     * @param tenantName the tenantName to set
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public User() {
        super();
    }

    public User(String id, String loginName, String password, String name, String salt, String email,
                Integer isAdmin, Integer status, String createUser, Date createDatetime, String updateUser,
                Date updateDatetime, String remark, Integer isDelete) {
        setId(id);
        this.loginName = loginName;
        this.password = password;
        this.name = name;
        this.salt = salt;
        this.email = email;
        this.isAdmin = isAdmin;
        this.status = status;
        setCreateUser(createUser);
        setCreateDatetime(createDatetime);
        setUpdateUser(updateUser);
        setUpdateDatetime(updateDatetime);
        this.remark = remark;
        this.isDelete = isDelete;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getRptMgrId() {
        return rptMgrId;
    }

    public void setRptMgrId(String rptMgrId) {
        this.rptMgrId = rptMgrId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline == null ? null : landline.trim();
	}

	public String getTheOffice() {
		return theOffice;
	}

	public void setTheOffice(String theOffice) {
		this.theOffice = theOffice == null ? null : theOffice.trim();
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Integer getFailureNum() {
		return failureNum;
	}

	public void setFailureNum(Integer failureNum) {
		this.failureNum = failureNum;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getuStatus() {
		return uStatus;
	}

	public void setuStatus(Integer uStatus) {
		this.uStatus = uStatus;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getRptMgrName() {
		return rptMgrName;
	}

	public void setRptMgrName(String rptMgrName) {
		this.rptMgrName = rptMgrName;
	}

	public String getFullDeptCode() {
		return fullDeptCode;
	}

	public void setFullDeptCode(String fullDeptCode) {
		this.fullDeptCode = fullDeptCode;
	}

	
	
}