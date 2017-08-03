package com.lingstep.lhs.sys.rbac.service;

import com.genertech.mems.sys.rbac.domain.Dept;
import com.genertech.mems.sys.rbac.domain.DeptTreeDto;

import java.util.List;

public interface DeptService {

    /**
     * 查询全部
     * @return
     */
    List<Dept> list(String tenantId);

    /**
     * 添加组织机构
     * 
     * @param dept
     */
    String save(Dept dept);

    /**
     * 更新组织机构
     * 
     * @param dept
     */
    void update(Dept dept);

    /**
     * 批量删除
     * @param id
     */
    void delete(String id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Dept getById(String id);

    /**
     * 组织机构重命名
     * @param id
     * @param newName
     */
    void rename(String id, String newName);
    
    /**
     * 检查是否存在相同名称
     * @param name
     * @param id
     * @param parentId
     * @return
     */
    public boolean checkExists(String name, String id, String parentId);
    
    /**
     * 根据租户ID查询部门树
     * @param tenantId
     * @param deptId TODO
     * @return
     */
    List<DeptTreeDto> getDeptTreeByTenantId(String tenantId, String deptId);
    
    /**
     * 
     * @Description:根据id查找平级部门(从第3层开始)
     * @param deptId
     * @return List<Dept>
     */
    List<Dept> findDeptById(String deptId);

}
