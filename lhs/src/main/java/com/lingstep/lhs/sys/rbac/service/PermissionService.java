package com.lingstep.lhs.sys.rbac.service;

import com.lingstep.lhs.sys.rbac.domain.Permission;

import java.util.List;

/**
 * 
 * <pre>
 * 功能说明：权限service API
 * </pre>
 * 
 * @author <a href="mailto:wang.g@gener-tech.com">WangGang</a>
 * @version 1.0
 */
public interface PermissionService {

	/**
	 * 根菜单
	 */
	Long MENU_LEVEL_0 = 0l;

	/**
	 * 一级菜单
	 */
	Long MENU_LEVEL_1 = 1l;

	/**
	 * 二级菜单
	 */
	Long MENU_LEVEL_2 = 2l;

	/**
	 * 三级菜单
	 */
	Long MENU_LEVEL_3 = 3l;

	/**
	 * 根据角色id查询权限
	 * 
	 * @param roleId
	 * @return List<Permission>
	 */
	List<Permission> getPermissionsByRoleId(String roleId);

	/**
	 * 查询全部权限
	 * 
	 * @return List<Permission>
	 */
	List<Permission> getAllPermissions();

	/**
	 * 添加权限
	 * 
	 * @param permission
	 */
	void save(Permission permission);

	/**
	 * 更新权限
	 * 
	 * @param permission
	 */
	void update(Permission permission);

	/**
	 * 删除权限
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * 根据用户id查询权限
	 * 
	 * @param userId
	 * @return List<Permission>
	 */
	List<Permission> getByUserId(String userId);

	/**
	 * 根据租户id查询权限
	 * 
	 * @param tenantId
	 * @return List<Permission>
	 */
	List<Permission> getByTenantId(String tenantId);

	/**
	 * 根据code 和用户id返回权限，若该用户无此权限返回null
	 * 
	 * @param code
	 * @param userId
	 * @return Permission
	 */
	Permission getByCode(String code, String userId);

	/**
	 * 根据code 和租户id返回权限，若该用户无此权限返回null
	 * 
	 * @param code
	 * @param tenantId
	 * @return Permission
	 */
	Permission getByCodeAndTenantId(String code, String tenantId);

	/**
	 * 根据parentId 和用户id返回权限，若该用户无此权限返回null
	 * 
	 * @param parentId
	 * @param userId
	 * @return Permission
	 */
	Permission getByParentId(String parentId, String userId);


	/**
	 * 根据parentId 和租户id返回权限，若该用户无此权限返回null
	 * 
	 * @param id
	 * @return Permission
	 */
	Permission getByParentId(String id);

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉
     * 
     * @param id
     * @return List<Permission>
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    List<Permission> getPermissionsByModuleId(String id);

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉
     * 
     * @return List<Permission>
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    List<Permission> getAllModulePermissions();

    
    /**
     * 检查是否存在相同Code
     * @param code
     * @param id
     * @return boolean
     */
    public boolean checkExists(String code, String id);
}
