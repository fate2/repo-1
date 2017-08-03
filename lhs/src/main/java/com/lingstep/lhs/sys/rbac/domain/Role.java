package com.lingstep.lhs.sys.rbac.domain;


import com.lingstep.lhs.common.domain.BaseBean;

public class Role extends BaseBean {

	private static final long serialVersionUID = 1L;
	private String name;
    // 序号
    private Integer indexNo;
    // 备注
    private String remark;
    // 已删除
    private Integer isDelete;
    private String tenantId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //英文简码，需唯一性校验
    private String code;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
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
}