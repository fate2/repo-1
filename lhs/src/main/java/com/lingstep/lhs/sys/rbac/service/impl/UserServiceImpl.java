package com.lingstep.lhs.sys.rbac.service.impl;

import com.genertech.commons.core.exception.ServiceException;
import com.genertech.commons.core.security.utils.Digests;
import com.genertech.commons.core.security.utils.Securitys;
import com.genertech.commons.core.utils.Encodes;
import com.genertech.commons.core.utils.Ids;
import com.genertech.mems.Constants;
import com.genertech.mems.sys.authentication.domain.CustomToken;
import com.genertech.mems.sys.authentication.realm.ShiroDbRealm;
import com.genertech.mems.sys.rbac.dao.UserDao;
import com.genertech.mems.sys.rbac.dao.UserHistoryDao;
import com.genertech.mems.sys.rbac.domain.*;
import com.genertech.mems.sys.rbac.filter.UserFilter;
import com.genertech.mems.sys.rbac.service.DeptService;
import com.genertech.mems.sys.rbac.service.UserService;
import com.genertech.mems.sys.tenantmgt.dao.TenantDao;
import com.genertech.mems.sys.tenantmgt.domain.Tenant;
import com.genertech.mems.utils.KeyValueBean;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * <pre>
 * 功能说明：用户Service
 * </pre>
 * 
 * @author <a href="mailto:wang.g@gener-tech.com">WangGang</a>
 * @version 1.0
 * 
 * @author <a href="mailto:lv.b@gener-tech.com">lvbin</a>
 * @version 2.0
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserHistoryDao userHistoryDao;

    @Autowired
    private TenantDao tenantDao;

    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private DeptService deptService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.UserService#checkConfirmOldPassword
     * (java.lang.String, java.lang.String)
     */
    @Override
    public Boolean checkPassword(String userId, String password) {
        if (StringUtils.isBlank(userId)) {
            userId = Securitys.getUserId();
        }
        User user = getUserById(userId);

        if (null == user) {
            return false;
        }
        byte[] salt = Encodes.decodeHex(user.getSalt());

        String pwd = Securitys.getEntryptPassword(password, salt, ShiroDbRealm.HASH_INTERATIONS);
        if (!user.getPassword().equals(pwd)) {
            return false;
        }
        return true;
    }
    
    public String checkNewPassWord(String userId, String password){
    	String re = "true";
    	if (StringUtils.isBlank(userId)) {
    		userId = Securitys.getUserId();
    	}
    	User user = getUserById(userId);
		if (null == user)
			return "false";
		/**
		 * 查询并遍历历史修改密码集合
		 */
		List<UserHistory> userHises = userHistoryDao.selectUserHisByUserId(userId);
		for (UserHistory userHi : userHises) {
			byte[] salt = Encodes.decodeHex(userHi.getSalt());
	    	String pwd = Securitys.getEntryptPassword(password, salt, ShiroDbRealm.HASH_INTERATIONS);
	    	if (userHi.getPassword().equals(pwd)) {
	    		return "false";
	    	}
		}
		if (password.indexOf(user.getLoginName())!=-1)
			return "密码中不能包含登录名";
		if (user.getEmail()!=null&&password.indexOf(user.getEmail())!=-1)
			return "密码中不能包含Email";
		if (user.getMobilePhone()!=null&&password.indexOf(user.getMobilePhone())!=-1)
			return "密码中不能包含手机号码";
		if (user.getLandline()!=null&&password.indexOf(user.getLandline())!=-1)
			return "密码中不能包含座机电话";
		if (user.getTheOffice()!=null&&password.indexOf(user.getTheOffice())!=-1)
			return "密码中不能包含办公室";
		 List<Map<String,String>> commons = userHistoryDao.selectCommonWord();
		/**
		 * 常见词库验证
		 */
		for(Map<String,String> map : commons){
			if (map.get("WORD") != null
					&& password.indexOf(map.get("WORD")) != -1) {
				return "密码中不能包常见词库";
			}
		}
    	return re;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.UserService#chekcLoginNameExists
     * (java.lang.String, java.lang.String)
     */
    @Override
    public Boolean existLoginName(String loginName, String id) {

        User oldUser;
        try {
            oldUser = userDao.findUserByLoginName(loginName.trim());

            if (StringUtils.isNotBlank(id)) {
                if (null != oldUser && !id.equals(oldUser.getId())) {
                    return true;
                }
                return false;
            }

            if (null != oldUser) {

                return true;
            }

            return false;
        } catch (Exception e) {

            // logService.error(Consts.SystemModel.SYS, "根据租户id和登陆名查询失败");
            throw new ServiceException("根据用户查询用户是否存在失败", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.UserService#delete(java.lang.String
     * [])
     */
    @Override
    public void deleteUserByIds(String[] ids) {
        try {
            userDao.deleteUserRoles(ids);

            userDao.delete(ids);

            if (Constants.synToActiviti) {
                // 删除用户的membership
                for (String id : ids) {
                    List<Group> activitiGroups = identityService.createGroupQuery().groupMember(id).list();
                    for (Group group : activitiGroups) {
                        identityService.deleteMembership(id, group.getId());
                    }
                    identityService.deleteUser(id);
                }
            }
        } catch (Exception e) {
            // logService.error(Consts.SystemModel.SYS, "删除用户失败");
            throw new ServiceException("删除用户失败", e);
        }
    }

    @Override
    public User getUserById(String id) {
        try {
            return userDao.findUserById(id);
        } catch (Exception e) {
            // logService.error(Consts.SystemModel.SYS, "查找用户失败");
            throw new ServiceException("查找用户失败", e);
        }

    }


    @Override
    public User getUserByLoginName(String loginName) {

        if (null == loginName) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("msg", "用户登录名不能为空");
            throw new ServiceException("login", params);
        }

        try {
            User user = userDao.findUserByLoginName(loginName);
            if(null != user){
                if(user.getIsDelete() == 1 || user.getStatus() == 0){
                    throw new DisabledAccountException();
                }
                if (StringUtils.isNotBlank(user.getTenantId())) {
                    Tenant tenant = tenantDao.queryById(user.getTenantId());
                    // * 0 :NORMAL:正常 1 :LOCK:锁定 2 :BAN:禁用
                    if (null == tenant || 0 != tenant.getStatus()) {
                        // 非正常租户禁止登录
                        throw new UnknownAccountException();
                    }
                    user.setTenantName(tenant.getName());
                }
                
                if(StringUtils.isNotBlank(user.getDeptId())){
                	Dept dept = deptService.getById(user.getDeptId());
                	//部门为空 或 部门已经删除
                	if(dept == null || dept.getIsDelete() == 1){
                		throw new DisabledAccountException();
                	}
                	
                	user.setDeptName(dept.getName());
                	user.setFullDeptCode(dept.getFullDeptCode());
                }
                
                return user;
            }else{
                throw new UnknownAccountException();
            }
        } catch (Exception e) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("msg", "查找用户失败");
            throw new ServiceException("login", params, e);
        }

    }


    @Override
    public User getUserByLoginNameAndTenant(String loginName,String tenantName) {

        try {
            User result = userDao.findUserByLoginName(loginName);
            if (StringUtils.isNotBlank(result.getTenantId())) {
                Tenant tenant = tenantDao.findByName(tenantName);
                // * 0 :NORMAL:正常 1 :LOCK:锁定 2 :BAN:禁用
                if (null == tenant || 0 != tenant.getStatus()) {
                    // 非正常租户禁止登录
                    return null;
                }else if(!result.getTenantId().equals(tenant.getId())){ //租户信息与用户不匹配
                    return null;
                }
                result.setTenantName(tenant.getName());
            }
            return result;
        } catch (Exception e) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("msg", "查找用户失败");
            // e.printStackTrace();
            // logService.error(Consts.SystemModel.SYS, "查找用户失败");
            throw new ServiceException("login", params, e);
        }

    }

    @Override
    public List<UserDto> listUserDto(UserFilter filter) {
        try {
            if(Securitys.isAdmin()){
                filter.setIsAdmin(1);
            }else if(Securitys.isTenantAdmin()){
                filter.setIsAdmin(2);
            }else{
                filter.setIsAdmin(0);
            }
            filter.setTenantId(Securitys.getTenantId());
            return userDao.list(filter);
        } catch (Exception e) {
            // logService.error(Consts.SystemModel.SYS, "查询用户列表失败");
            throw new ServiceException("查询用户列表失败", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.UserService#login(java.lang.String
     * , java.lang.String, java.lang.String)
     */
    @Override
    public User login(String loginName, String password) {
        User user = null;
        if (StringUtils.isNotEmpty(loginName) && StringUtils.isNotEmpty(password)) {
            user = getUserByLoginName(loginName);
            if (null != user) {
                // 判断用户是否有效
                if (user.getStatus() == 0) {
                    throw new DisabledAccountException();
                }

                byte[] salt = Encodes.decodeHex(user.getSalt());
                String pwd = Securitys.getEntryptPassword(password, salt, ShiroDbRealm.SALT_SIZE);
                if (!user.getPassword().equals(pwd)) {
                    throw new AuthenticationException();
                }
            } else {
                throw new UnknownAccountException();
            }
        } else {
            throw new AuthenticationException("login param error...!!!");
        }
        return user;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genertech.mems.sys.rbac.service.UserService#modifyPassword(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void modifyPassword(String currentPassword, String newPassword, String id) {

        if (StringUtils.isBlank(id)) {
            id = Securitys.getUserId();
        }
        User user = getUserById(id);
        UserHistory userHis = new UserHistory();
        if (null == user) {
            throw new ServiceException("用户不存在");
        }
        byte[] salts = Encodes.decodeHex(user.getSalt());
        String pwd = Securitys.getEntryptPassword(currentPassword, salts, ShiroDbRealm.HASH_INTERATIONS);
        if (!user.getPassword().equals(pwd)) {
            throw new ServiceException("旧密码不正确");
        }
        String npwd = Securitys.getEntryptPassword(newPassword, salts, ShiroDbRealm.HASH_INTERATIONS);
        if (user.getPassword().equals(npwd)) {
            throw new ServiceException("新密码不能与旧密码一致");
        }
        byte[] newSalts = Digests.generateSalt(ShiroDbRealm.SALT_SIZE);
        userHis.setId(Ids.uuid());
        userHis.setUserId(user.getId());
        userHis.setSalt(user.getSalt());
        userHis.setPassword(user.getPassword());
        userHis.setUpdateDatetime(new Date());
        userHis.setUpdateUser(id);
        
        user.setSalt(Encodes.encodeHex(newSalts));
        user.setPassword(Securitys.getEntryptPassword(newPassword, newSalts, ShiroDbRealm.HASH_INTERATIONS));
        try {
            userDao.update(user);
            userHistoryDao.insertSelective(userHis);
        } catch (Exception e) {
            throw new ServiceException("修改密码失败！", e);

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.UserService#resetPassword(java
     * .lang.String, java.lang.String)
     */
    @Override
    public void resetPassword(String id, String newPassword) {

        User user = getUserById(id);
        if (null == user) {
            throw new ServiceException("请选择要重置密码的用户");
        }
        try {
            byte[] salts = Digests.generateSalt(ShiroDbRealm.SALT_SIZE);
            user.setSalt(Encodes.encodeHex(salts));
            user.setPassword(Securitys.getEntryptPassword(newPassword, salts, ShiroDbRealm.HASH_INTERATIONS));
            userDao.update(user);
        } catch (Exception e) {
            throw new ServiceException("用户密码重置失败", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genertech.mems.sys.rbac.service.UserService#save(com.genertech.mems.sys.rbac.domain.User)
     */
    @Override
    public String saveUser(User user) {
        User oldUser = userDao.findUserByLoginName(user.getLoginName());
        if (oldUser != null) {
            throw new ServiceException("已存在相同登录名");
        }
        try {
            String userId = Ids.uuid();
            user.setId(userId);
            // 获取8位盐
            byte[] salts = Digests.generateSalt(ShiroDbRealm.SALT_SIZE);
            user.setSalt(Encodes.encodeHex(salts));
            user.setPassword(Securitys.getEntryptPassword(user.getPassword(), salts,
                    ShiroDbRealm.HASH_INTERATIONS));
            user.setCreateUser(Securitys.getUserId());
            user.setCreateDatetime(new Date());
            user.setIsDelete(0);
            user.setTenantId(Securitys.getTenantId());
            if (null == user.getIsAdmin()) {
                user.setIsAdmin(0);
            }
            userDao.save(user);

            logger.info("用户" + Securitys.getName() + "创建用户:" + user.getName());



            if (Constants.synToActiviti) {
                org.activiti.engine.identity.User newUser = identityService.newUser(userId);
                newUser.setFirstName(user.getName());
                newUser.setLastName("");
                newUser.setPassword(user.getPassword());
                newUser.setEmail(user.getEmail());
                identityService.saveUser(newUser);

            }

            return userId;
        } catch (Exception e) {
            throw new ServiceException("新增用户失败", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genertech.mems.sys.rbac.service.UserService#save(com.genertech.mems.sys.rbac.domain.User,
     * java.util.List)
     */
    @Override
    public String saveUserRoles(User user, String[] roles) {

        return saveOrUpdateUserRole(user, roles, false);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genertech.mems.sys.rbac.service.UserService#saveUserRole(java.util.List, java.lang.Boolean)
     */
    @Override
    public String saveOrUpdateUserRole(User user, String[] roles, Boolean isUpdate) {

        String userId = null;

        // 如果更新，删除用户之前的角色
        if (null != isUpdate && isUpdate) {
            // 更新用户
            userId = updateUser(user);

            // 删除用户之前的权限
            try {
                userDao.deleteUserRole(userId);
            } catch (Exception e) {
                // logService.error(Consts.SystemModel.SYS, "删除用户角色失败"+user.getName());
            }

            // logService.warning(Consts.SystemModel.SYS, "用户角色被删除"+user.getName());

        } else {

            // 保存用户
            userId = saveUser(user);
        }

        if (null == roles || roles.length == 0) {
            return userId;
        }

        List<UserRoleDTO> urList = new ArrayList<UserRoleDTO>();

        UserRoleDTO ur = null;

        // 构造用户权限集合
        for (String roleId : roles) {

            ur = new UserRoleDTO(userId, roleId);

            urList.add(ur);


        }

        // 更新权限
        if (!urList.isEmpty()) {
            try {

                userDao.saveUserRole(urList);

                if (Constants.synToActiviti) {
                    // 删除用户的membership
                    List<Group> activitiGroups = identityService.createGroupQuery().groupMember(user.getId()).list();
                    for (Group group : activitiGroups) {
                        identityService.deleteMembership(user.getId(), group.getId());
                    }
                    // 添加membership
                    for (UserRoleDTO ms : urList) {
                        // Role role = roleManager.getEntity(ur.getRoleId());
                        identityService.createMembership(userId, ms.getRoleId());
                    }
                }

                // logService.warning(Consts.SystemModel.SYS, Securitys.getLoginName()+"更新"+user.getLoginName()+"的角色");
            } catch (Exception e) {
                // logService.error(Consts.SystemModel.SYS, Securitys.getLoginName()+"保存用户角色失败");
                throw new ServiceException("保存用户角色失败", e);
            }
        }
        return userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.UserService#update(com.genertech
     * .lcm.sys.rbac.domain.User)
     */
    @Override
    public String updateUser(User user) {

        String oldId = user.getId();

        if (StringUtils.isBlank(oldId)) {
            throw new ServiceException("请选择要修改的用户！");
        }
        User oldUser = userDao.findUserByLoginName(user.getLoginName());
        if (oldUser != null && !oldId.equals(oldUser.getId())) {
            throw new ServiceException("已存在相同登录名");
        }
        try {
            user.setUpdateDatetime(new Date());
            user.setUpdateUser(Securitys.getUserId());
            userDao.update(user);

            if (Constants.synToActiviti) {
                List<org.activiti.engine.identity.User> activitiUsers = identityService.createUserQuery().userId(user.getId()).list();
                if (activitiUsers.size() == 1) {
                    //更新信息
                    org.activiti.engine.identity.User activitiUser = activitiUsers.get(0);
                    activitiUser.setFirstName(user.getName());
                    activitiUser.setLastName("");
                    activitiUser.setPassword("");
                    activitiUser.setEmail(user.getEmail());
                    identityService.saveUser(activitiUser);
                }
            }

            // logService.info(Consts.SystemModel.SYS, "用户被更新"+user.getLoginName());

            return oldId;
        } catch (Exception e) {
            // logService.error(Consts.SystemModel.SYS, "修改用户失败");
            throw new ServiceException("修改用户失败id=" + oldId, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genertech.mems.sys.rbac.service.UserService#update(com.genertech.mems.sys.rbac.domain.User,
     * java.util.List)
     */
    @Override
    public String updateUserRoles(User user, String[] roles) {

        return saveOrUpdateUserRole(user, roles, true);
    }

    @Override
    public List<DeptTreeDto> getDeptTreeByTenantId(String tenantId) {
        return userDao.queryDeptTreeByTenantId(tenantId);
    }

	@Override
	public List<User> listUserByRoleId(String roleId) {
		 List<User> users = new ArrayList<User>();
		// TODO Auto-generated method stub
		try {
			if(roleId != null&&!"".equals(roleId))
				users = userDao.findUserByRoleId(roleId);

            return users;
        } catch (Exception e) {
            throw new ServiceException("根据角色ID查询用户集合失败", e);
        }
	}
	
	@Override
	public String updateUserInfoByMi(User user){
		String oldId = user.getId();
  
		try {
			user.setUpdateDatetime(new Date());
			user.setUpdateUser(Securitys.getUserId());
			userDao.update(user);
			if (Constants.synToActiviti) {
			    List<org.activiti.engine.identity.User> activitiUsers = identityService.createUserQuery().userId(user.getId()).list();
			    if (activitiUsers.size() == 1) {
				    //更新信息
				    org.activiti.engine.identity.User activitiUser = activitiUsers.get(0);
				    activitiUser.setFirstName(user.getName());
				    activitiUser.setLastName("");
			        activitiUser.setPassword(user.getPassword());
			        activitiUser.setEmail(user.getEmail());
			        identityService.saveUser(activitiUser);
			    }
			}
//			logService.info(Consts.SystemModel.SYS, "用户被更新"+user.getLoginName());
            return oldId;
		} catch (Exception e) {
//		    logService.error(Consts.SystemModel.SYS, "修改用户失败");
			throw new ServiceException("修改用户失败id=" + oldId, e);
		}
	}

	@Override
	public void updateUserUnlock(String id) { 
		try {
			userDao.updateUserUnlock(id);
		} catch (Exception e) {
			throw new ServiceException("解锁用户失败id=" + id, e);
		}
		
		
		
	}

	@Override
	public void userLock(CustomToken token,User user,Integer threshold,Integer lockminutes) {
    		/**
    		 * 帐号锁定功能启用
    		 */
			if (user.getLockTime() != null && !new Date().after(user.getLockTime())) {
        		/**
        		 * 账户锁定不能登录
        		 */
				 throw new DisabledAccountException();
			} else {
				if(user.getLockTime() != null){
					updateUserUnlock(user.getId());
				}
				byte[] salt = Encodes.decodeHex(user.getSalt());
		    	String pwd = Securitys.getEntryptPassword(token.getPassword(), salt, ShiroDbRealm.HASH_INTERATIONS);
		    	if (!user.getPassword().equals(pwd)) {
		    		//登录失败
				if (user.getFailureNum() != null && user.getFailureNum() >= threshold - 1) {
						Calendar c1 = Calendar.getInstance();
						c1.setTime(new Date());
						c1.add(Calendar.MINUTE, lockminutes);
						user.setLockTime(c1.getTime());
						userDao.updateUserLockTime(user);
						throw new DisabledAccountException("密码错误已达"+threshold+"帐号锁定到:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c1.getTime()));
					}else{
						user.setFailureNum(user.getFailureNum()==null? 0:user.getFailureNum());
						userDao.updateUserFailureNum(user);
//						throw new AuthenticationException("密码错误"+(user.getFailureNum()==null? 0:user.getFailureNum())+1+"次,密码错误达到"+threshold+"次帐号讲锁定");
						
					}
		    			
		    	}else{
		    		//登录成功
					updateUserUnlock(user.getId());
		    	}
					
		    	
			}
		
		
	}

	@Override
	public String getRoleNameByUserId(String userId) {
		return userDao.getRoleNameByUserId(userId);
	}

	@Override
	public List<String> getRoleNameListByUserId(String userId) {
		return userDao.getRoleNameListByUserId(userId);
	}
	
	@Override
	public User getUserByLoginNameForStaff(String loginName) {
        User user = userDao.findUserByLoginName(loginName);
        if(null != user){
            if(user.getIsDelete() == 1 || user.getStatus() == 0){
                return null;
            }
            if (StringUtils.isNotBlank(user.getTenantId())) {
                Tenant tenant = tenantDao.queryById(user.getTenantId());
                // * 0 :NORMAL:正常 1 :LOCK:锁定 2 :BAN:禁用
                if (null == tenant || 0 != tenant.getStatus()) {
                    return null;
                }
            }
            
            if(StringUtils.isNotBlank(user.getDeptId())){
            	Dept dept = deptService.getById(user.getDeptId());
            	//部门为空 或 部门已经删除
            	if(dept == null || dept.getIsDelete() == 1){
            		return null;
            	}
            	
            }
            
            if(StringUtils.isEmpty(user.getStaffId())){
            	return user;
            }else{
            	return null;
            }
            
        }else{
            return null;
        }
    

    }

	 @Override
	    public int updateUserRoleByStaff(Map<String,Object> map) {

	        return userDao.updateUserRoleByStaff(map);

	    }

	@Override
	public void updateDeptSupByUser(User user) {
		try {
			userDao.updateDeptSupByUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<KeyValueBean> listUser() {
		return userDao.listUser();
	}

}
