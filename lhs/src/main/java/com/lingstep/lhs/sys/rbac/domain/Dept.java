package com.lingstep.lhs.sys.rbac.domain;

import com.lingstep.lhs.common.domain.BaseBean;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class Dept extends BaseBean {
	private static final long serialVersionUID = 1L;

	@NotBlank
    @Length(max = 50)
    private String name;

    private Long grade;

    private String parentId;

    private String tenantId;

    private String remark;

    private Integer isDelete;
    private String englishname;
    private String fullDeptCode;
    
    
    private Integer childCount;

    public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	public Dept(String id, String name, Long grade, String parentId, String tenantId, String createUser, Date createDatetime, String updateUser, Date updateDatetime, String remark,
            Integer isDelete,String englishname,String fullDeptCode) {
        setId(id);
        this.name = name;
        this.grade = grade;
        this.parentId = parentId;
        this.tenantId = tenantId;
        setCreateUser(createUser);
        setCreateDatetime(createDatetime);
        setUpdateUser(updateUser);
        setUpdateDatetime(updateDatetime);
        this.remark = remark;
        this.isDelete = isDelete;
        this.englishname = englishname;
        this.fullDeptCode = fullDeptCode;
    }
	public Dept(String id, String name, Long grade, String parentId, String tenantId, String createUser, Date createDatetime, String updateUser, Date updateDatetime, String remark,
			Integer isDelete,String englishname, String fullDeptCode, Integer childCount) {
		 setId(id);
		this.name = name;
		this.grade = grade;
		this.parentId = parentId;
		this.tenantId = tenantId;
		setCreateUser(createUser);
	    setCreateDatetime(createDatetime);
	    setUpdateUser(updateUser);
	    setUpdateDatetime(updateDatetime);
		this.remark = remark;
		this.isDelete = isDelete;
		this.englishname = englishname;
		this.fullDeptCode = fullDeptCode;
		this.childCount = childCount;
	}

    public Dept() {
        super();
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getGrade() {
		return grade;
	}

	public void setGrade(Long grade) {
		this.grade = grade;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getEnglishname() {
		return englishname;
	}

	public void setEnglishname(String englishname) {
		this.englishname = englishname;
	}

	public String getFullDeptCode() {
		return fullDeptCode;
	}

	public void setFullDeptCode(String fullDeptCode) {
		this.fullDeptCode = fullDeptCode;
	}	
}