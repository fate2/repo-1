package com.lingstep.lhs.sys.rbac.service;

import com.lingstep.lhs.sys.authentication.domain.CustomToken;
import com.lingstep.lhs.sys.rbac.domain.DeptTreeDto;
import com.lingstep.lhs.sys.rbac.domain.User;
import com.lingstep.lhs.sys.rbac.domain.UserDto;
import com.lingstep.lhs.sys.rbac.filter.UserFilter;
import com.lingstep.lhs.utils.KeyValueBean;

import java.util.List;
import java.util.Map;


public interface UserService{

    /**
     * 根据用户ID查询用户信息
     * @param id 用户ID
     * @return
     */
    public User getUserById(String id);

    /**
     * 根据登录名查询用户信息
     * @param loginName
     * @return
     */
    public User getUserByLoginName(String loginName);

    /**
     * 根据用户名租户名查询用户
     * @param loginName
     * @param tenantName
     * @return
     */
    public User getUserByLoginNameAndTenant(String loginName, String tenantName);

    /**
     * 查询用户列表
     * @param filter
     * @return
     */
    public List<UserDto> listUserDto(UserFilter filter);
    
    /**
     * 新增用户
     * @param user
     * @return
     */
    public String saveUser(User user);

    /**
     * 新增用户信息和角色信息
     * @param user
     * @param roles
     */
    public String saveUserRoles(User user, String[] roles);

    /**
     * 登陆逻辑
     * @param loginName 登陆名
     * @param password 密码
     * @return
     */
    public User login(String loginName, String password);
    
    /**
     * 更新用户信息
     * @param user
     */
    public String updateUser(User user);

    /**
     * 更新用户和角色信息
     * @param user
     * @param roles
     */
    public String updateUserRoles(User user, String[] roles);
    
    /**
     * 批量删除用户
     * @param ids
     */
    public void deleteUserByIds(String[] ids);
    
    /**
     * 重置用户密码
     * @param id
     * @param newPassword
     */
    public void resetPassword(String id, String newPassword);
    
    /**
     * 检查用户名是否存在
     * @param loginName
     * @param id
     * @return
     */
    public Boolean existLoginName(String loginName, String id); 
    
    /**
     * 检查旧密码是否正确
     * @param userId
     * @param password
     * @return
     */
    public Boolean checkPassword(String userId, String password);

    /**
     * 检查新密码是否符合要求
     * @param userId 用户ID
     * @param password 新密码
     * @return 返回"true"表示密码通过验证反之不通过
     */
    public String checkNewPassWord(String userId, String password);
    /**
     * 修改密码
     * @param currentPassword
     * @param newPassword
     * @param id
     */
    public void modifyPassword(String currentPassword, String newPassword, String id);
    
    
    /**
     * 保存或更新用户角色
     * @param user
     * @param roles
     * @param isUpdate 是否更新
     * @return 用户id
     */
    public String saveOrUpdateUserRole(User user, String[] roles, Boolean isUpdate) ;

    /**
     * 部门用户树数据
     * @param tenantId
     * @return
     */
    List<DeptTreeDto> getDeptTreeByTenantId(String tenantId);
    
    /**
     * 根据角色ID获取用户集合
     * @param roleId 角色ID
     * @return 用户集合
     */
    public List<User> listUserByRoleId(String roleId);
    
    /**
     * 修改本用户个人信息
     * @param user
     * @return
     */
	public String updateUserInfoByMi(User user);
	
	/**
	 * 解锁用户
	 * 清空锁定时间
	 * 修改登录失败次数为0
	 * @param user
	 */
	public void updateUserUnlock(String id);


	/**
	 * 用户登录帐号锁定和判断帐号错误次数
	 * @param token 登录帐号和密码信息
	 * @param user 登录的具体用户信息
	 * @param threshold 帐号输入错误密码锁定时间
	 * @param lockminutes 帐号每次锁定的时间(分钟)
	 */
	public void userLock(CustomToken token, User user, Integer threshold, Integer lockminutes);
	
	
	/**
	 * 获取指定用户所有角色名称的字符串，名称之间以逗号隔开
	 * @param userId
	 * @return
	 */
	public String getRoleNameByUserId(String userId);
	
	/**
	 * 获取指定用户所有角色名称的集合
	 * @param userId
	 * @return
	 */
	public List<String> getRoleNameListByUserId(String userId);
	
	/**
	 * 
	 * @Description:员工创建用户时根据员工编号查找用户信息
	 * @param loginName
	 * @return User
	 */
	public User getUserByLoginNameForStaff(String loginName);
	
	/**
	 * 
	 * @Description:编辑员工时没有勾选编辑用户，则更新所属部门和上级领导
	 * @param user	
	 * @return void
	 */
	public void updateDeptSupByUser(User user);

	public List<KeyValueBean> listUser();

	int updateUserRoleByStaff(Map<String, Object> map);

}
