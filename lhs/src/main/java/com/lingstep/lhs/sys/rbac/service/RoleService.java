package com.lingstep.lhs.sys.rbac.service;

import com.genertech.mems.sys.rbac.domain.Role;
import com.genertech.mems.sys.rbac.filter.RoleFilter;

import java.util.List;
import java.util.Map;

/**
 * 
 * <pre>
 * 功能说明：角色Service interface
 * </pre>
 * 
 * @author <a href="mailto:wang.g@gener-tech.com">WangGang</a>
 * @version 1.0
 */
public interface RoleService {

	/**
	 * 获取所有角色
	 * 
	 * @return
	 */
	public List<Role> getAll();


	/**
	 * 按条件获取角色
	 * 
	 * @param filter
	 * @return
	 */
	public List<Role> getRolesByFilter(RoleFilter filter);

	/**
	 * 获取用户所有角色
	 * 
	 * @param userId
	 * @return
	 */
	List<Role> getRolesByUserId(String userId);

    /**
     * 获取租户所有角色
     * @param tenantId
     * @return
     */
    List<Role> getAllByTenantId(String tenantId);

	/**
	 * 添加角色
	 * 
	 * @param role
	 * @return
	 */
	public String save(Role role);

	/**
	 * 添加角色权限
	 * 
	 * @param role
	 * @param permissions
	 * @return
	 */
	public String save(Role role, String[] permissions);

	/**
	 * 根据id获取角色
	 * 
	 * @param roleId
	 * @return
	 */
	public Role getById(String roleId);

	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	public String update(Role role);

	/**
	 * 更新角色权限
	 * 
	 * @param role
	 * @param permissions
	 * @return
	 */
	public String update(Role role, String[] permissions);

	/**
	 * 删除角色
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 批量删除角色
	 * 
	 * @param ids
	 */
	public void deleteByIds(String[] ids);

	/**
	 * 查询角色关联用户
	 * 
	 * @param ids
	 * @return
	 */
	Long getUserRoleCount(String[] ids);

	boolean checkColumnValueExists(String columnName, String value, String id, String tenantId);

	void deleteByRoleId(String id);

    List<String> getCandidateUserIdsByRoleCode(String code, String tenantId);

    /**
     * 根据角色码、租户ID和用户ID，获取唯一角色ID
     * 如果获取到多个角色ID，会抛出业务异常
     * @param roleCodeMcc
     * @param tenantId
     * @param userId
     * @return
     */
    String getRoleIdByCodeAndUserInfo(String[] roleCodeMcc, String tenantId, String userId);

    /**
     * 获取当前登陆人工卡角色ID
     * @return
     */
    String getBizRoleId();

    /**
     * 获取当前登陆人工卡角色
     * @return
     */
    Role getBizRole();

	String getRoleString(Map<String, List<String>> maps, String[] customStates);
	
	/**
	 * 
	 * @author linfeng
	 * @description 根据角色码获取角色对象
	 * @param roleCode
	 * @return Role
	 * @time 2017年6月6日 下午5:17:02
	 */
	Role getRoleByRoleCode(String roleCode);
}
