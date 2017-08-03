package com.lingstep.lhs.sys.rbac.service.impl;

import com.genertech.commons.core.exception.ServiceException;
import com.genertech.commons.core.security.utils.Securitys;
import com.genertech.commons.core.utils.Ids;
import com.genertech.mems.Constants;
import com.genertech.mems.sys.rbac.dao.RoleDao;
import com.genertech.mems.sys.rbac.domain.Role;
import com.genertech.mems.sys.rbac.domain.RolePermissionDTO;
import com.genertech.mems.sys.rbac.filter.RoleFilter;
import com.genertech.mems.sys.rbac.service.RoleService;
import com.genertech.mems.workflow.utils.ActivitiConstants;
import com.google.common.collect.Lists;
import org.activiti.engine.IdentityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 
 * 
 * <pre>
 * 功能说明：角色Service实现
 * </pre>
 * 
 * @author <a href="mailto:wang.g@gener-tech.com">WangGang</a>
 * @version 1.0
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private IdentityService identityService;
    @Autowired
    private RoleDao roleDao;

    private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public boolean checkColumnValueExists(String columnName, String value, String id, String tenantId) {
        long count = roleDao.checkColumnValueExists(columnName, value,id, tenantId);
        return count > 0;
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#delete(java.lang.String
     * )
     */
    @Override
    public void delete(String id) {

        try {
            String[] ids = { id };
            if (roleDao.queryUserRoleCount(ids) > 0) {
                throw new ServiceException("角色下存在用户关联不能删除");
            }
            roleDao.delete(id);
            roleDao.deleteRolePermission(id);

            if (Constants.synToActiviti) {
                identityService.deleteGroup(id);
            }
            //logService.warning(Consts.SystemModel.SYS, "删除角色" + id);
        } catch (Exception e) {
            //logService.error(Consts.SystemModel.SYS, "删除角色失败");
            throw new ServiceException("删除角色失败", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#deleteByIds(java.util
     * .List)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#deleteByIds(java.util
     * .List)
     */
    @Override
    public void deleteByIds(String[] ids) {
        try {
            if (roleDao.queryUserRoleCount(ids) > 0) {
                throw new ServiceException("角色下存在用户关联不能删除");
            }
            roleDao.deleteByIds(ids);
            roleDao.deleteRolePermissions(ids);

            if (Constants.synToActiviti) {
                for (String id : ids) {
                    identityService.deleteGroup(id);
                }
            }
            //logService.warning(Consts.SystemModel.SYS, "批量删除角色");
        } catch (Exception e) {
            //logService.error(Consts.SystemModel.SYS, "批量删除角色失败");
            throw new ServiceException("批量删除角色失败", e);
        }

    }

    @Override
    public void deleteByRoleId(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("参数为空.");
        }
        try {
            roleDao.deleteByRoleId(id);
        } catch (Exception e) {
            //logService.error(Consts.SystemModel.SYS, "deleteByRoleId fail.");
            throw new ServiceException("删除错误", e);
        }

    }

    @Override
    public List<String> getCandidateUserIdsByRoleCode(String code,String tenantId) {
        return roleDao.getCandidateUserIdsByRoleCode(code,tenantId);
    }

    @Override
    public String getRoleIdByCodeAndUserInfo(String[] roleCodeMcc, String tenantId, String userId) {
        List<Role> roles = roleDao.getRoleIdByCodeAndUserInfo(roleCodeMcc,  tenantId,  userId);
        if (roles == null || roles.isEmpty()) {
            logger.error("业务逻辑异常：当前用户没有关联工卡流程相关角色");
            throw new ServiceException("业务逻辑异常：当前用户没有关联工卡流程相关角色.");
        }
        else if (roles.size()==1){
            return roles.get(0).getId();
        }
        else{
            boolean isAdmin = false;
            String adminId = null;
            for (Role role :roles){
                if (role.getCode().equals(ActivitiConstants.ROLE_CODE_ADMIN)){
                    isAdmin = true;
                    adminId = role.getId();
                }
            }
            if (isAdmin){
                return adminId;
            }else{
                logger.error("业务逻辑异常：当前用户被赋予了两个以上互斥角色.");
                throw new ServiceException("业务逻辑异常：当前用户被赋予了两个以上互斥角色.");
            }
        }
    }

    @Override
    public String getBizRoleId() {
        return getRoleIdByCodeAndUserInfo(ActivitiConstants.roleCodes,
                Securitys.getTenantId(), Securitys.getUserId());
    }

    @Override
    public Role getBizRole() {
        String roleId = getRoleIdByCodeAndUserInfo(ActivitiConstants.roleCodes,
                Securitys.getTenantId(), Securitys.getUserId());
        return getById(roleId);
    }

    @Override
    public String getRoleString(Map<String,List<String>> maps,String[] customStates) {
        List<String> standardStates = maps.get(getBizRole().getCode());
        if (standardStates==null||standardStates.isEmpty()){return null;}
        List<String> stateList = Lists.newArrayList();
        if (customStates==null||customStates.length==0){
            stateList.addAll(standardStates);
        }else{
            List<String> states = Arrays.asList(customStates);
            states.retainAll(standardStates);
            stateList.addAll(states);
        }
        StringBuilder c = new StringBuilder();
        for (String st : stateList){
            c.append("'").append(st).append("',");
        }
        return c.substring(0,c.length()-1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#getAll(java.lang.String
     * )
     */
    @Override
    public List<Role> getAll() {
        try {
            return roleDao.queryAll();
        } catch (Exception e) {
            //logService.error(Consts.SystemModel.SYS, "查询所有用户失败");
            throw new ServiceException("查询所有用户失败", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#getById(java.lang.
     * String)
     */
    @Override
    public Role getById(String roleId) {
        try {
            return roleDao.queryById(roleId);
        } catch (Exception e) {
            //logService.error(Consts.SystemModel.SYS, "根据用户查询角色失败");
            throw new ServiceException("根据用户查询角色失败", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#getRolesByFilter(com
     * .genertech.lcm.sys.rbac.filter.RoleFilter)
     */
    @Override
    public List<Role> getRolesByFilter(RoleFilter filter) {

        try {
            filter.setTenantId(Securitys.getTenantId());
            return roleDao.queryByFilter(filter);
        } catch (Exception e) {
            //logService.error(Consts.SystemModel.SYS, "按条件查询角色失败");
            throw new ServiceException("按条件查询角色失败", e);
        }
    }

    @Override
    public List<Role> getRolesByUserId(String userId) {
        try {
            return roleDao.queryByUserId(userId);
        } catch (Exception e) {
            throw new ServiceException("查询角色失败", e);
        }
    }

    @Override
    public List<Role> getAllByTenantId(String tenantId){
        try{
            return roleDao.queryByTenantId(tenantId);
        }catch(Exception e){
            throw new ServiceException("查询角色失败",e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#getUserRoleCount(java
     * .lang.String[])
     */
    @Override
    public Long getUserRoleCount(String[] ids) {
        try {
            return roleDao.queryUserRoleCount(ids);
        } catch (Exception e) {
            throw new ServiceException("查询角色关联用户失败", e);
        }
    }

    @Override
    public String save(Role role) {

        String roleId = Ids.uuid();
        role.setId(roleId);
        role.setCreateUser(Securitys.getUserId());
        role.setCreateDatetime(new Date());
        role.setIsDelete(0);
        //role.setTenantId(Securitys.getTenantId());
        if (checkColumnValueExists("name", role.getName(), null, role.getTenantId())) {
            throw new ServiceException("角色名称已存在");
        }
        if (StringUtils.isNotEmpty(role.getCode())){
            if (checkColumnValueExists("code",role.getCode(),null , Securitys.getTenantId())) {
                throw new ServiceException("角色简码已存在");
            }
        }
        try {
            Integer indexNo = roleDao.queryMaxIndexNo();
            role.setIndexNo(indexNo);
            roleDao.save(role);
            if(Constants.synToActiviti){
                org.activiti.engine.identity.Group group=identityService.newGroup(role.getId());
                identityService.saveGroup(group);
            }
            //logService.info(Consts.SystemModel.SYS, "添加角色" + role.getName());
            return roleId;
        } catch (Exception e) {
            //logService.error(Consts.SystemModel.SYS, "添加角色失败");
            throw new ServiceException("添加角色失败", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#save(com.genertech
     * .lcm.sys.rbac.domain.Role, java.lang.String[])
     */
    @Override
    public String save(Role role, String[] permissions) {

        return saveOrUpdateTenantModule(role, permissions, null);
    }

    /**
     * 保存或更新角色权限
     * 
     * @param role
     * @param permissions
     * @param isUpdate
     * @return
     */
    private String saveOrUpdateTenantModule(Role role, String[] permissions,
            Boolean isUpdate) {

        String roleId = null;

        // 如果更新，删除该角色之前的权限
        if (null != isUpdate && isUpdate) {
            // 更新角色
            roleId = update(role);

            try {
                // 删除该角色之前的权限
                roleDao.deleteRolePermission(roleId);
                // logService.warning(Consts.SystemModel.SYS, "删除角色关联的权限");
            } catch (Exception e) {
                //logService.error(Consts.SystemModel.SYS, "删除角色关联的权限失败");
                throw new ServiceException("删除角色关联的权限失败", e);
            }

        } else {

            // 保存角色
            roleId = save(role);
        }

        if (null == permissions || permissions.length == 0) {
            return roleId;
        }

        List<RolePermissionDTO> rpList = new ArrayList<RolePermissionDTO>();

        RolePermissionDTO rp = null;

        // 构造角色权限集合
        for (String permissionId : permissions) {

            rp = new RolePermissionDTO(roleId, permissionId);

            rpList.add(rp);
        }

        // 更新角色权限
        if (!rpList.isEmpty()) {
            try {

                roleDao.saveRolePermission(rpList);
                //logService.info(Consts.SystemModel.SYS, "保存角色权限");
            } catch (Exception e) {
                //logService.error(Consts.SystemModel.SYS, "保存角色权限失败");
                throw new ServiceException("保存角色权限失败", e);
            }
        }
        return roleId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#update(com.genertech
     * .lcm.sys.rbac.domain.Role)
     */
    @Override
    public String update(Role role) {

        role.setUpdateDatetime(new Date());

        role.setUpdateUser(Securitys.getUserId());
        if (checkColumnValueExists("name",role.getName(), role.getId(), Securitys.getTenantId())) {
            throw new ServiceException("角色名称已存在");
        }
        if (StringUtils.isNotEmpty(role.getCode())){
            if (checkColumnValueExists("code",role.getCode(), role.getId(), Securitys.getTenantId())) {
                throw new ServiceException("角色简码已存在");
            }
        }
        try {
            roleDao.update(role);

            //logService.info(Consts.SystemModel.SYS, "角色更新");

            return role.getId();

        } catch (Exception e) {
            //logService.error(Consts.SystemModel.SYS, "更新角色失败");
            throw new ServiceException("更新角色失败", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genertech.mems.sys.rbac.service.RoleService#update(com.genertech
     * .lcm.sys.rbac.domain.Role, java.lang.String[])
     */
    @Override
    public String update(Role role, String[] permissions) {

        return saveOrUpdateTenantModule(role, permissions, true);
    }


	@Override
	public Role getRoleByRoleCode(String roleCode) {
		return roleDao.getRoleByRoleCode(roleCode);
	}

}
