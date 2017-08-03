package com.lingstep.lhs.sys.authentication.realm;

import com.genertech.commons.core.security.shiro.ShiroUser;
import com.genertech.commons.core.utils.Encodes;
import com.lingstep.lhs.Constants;
import com.lingstep.lhs.sys.authentication.domian.CustomToken;
import com.lingstep.lhs.sys.rbac.domain.Permission;
import com.lingstep.lhs.sys.rbac.domain.Role;
import com.lingstep.lhs.sys.rbac.domain.User;
import com.lingstep.lhs.sys.rbac.service.PermissionService;
import com.lingstep.lhs.sys.rbac.service.RoleService;
import com.lingstep.lhs.sys.rbac.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class ShiroDbRealm extends AuthorizingRealm {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int SALT_SIZE = 8;
    public static final int HASH_INTERATIONS = 1024;

    @Autowired
    private UserService userService;

    @Autowired
    protected PermissionService permissionService;
    @Autowired
    protected RoleService roleService;
    
    public ShiroDbRealm() {
        super();
        setAuthenticationTokenClass(CustomToken.class);
    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        CustomToken token = (CustomToken) authcToken;
        String loginName = token.getLoginName();
        String host = token.getHost();
        //String tenantName = token.getTenantName();
        User user = userService.getUserByLoginName(loginName);
        //帐号输入错误密码锁定时间、帐号每次锁定的时间(分钟)
		Integer threshold = 5, lockminutes = 5;
        if (user != null) {
        	if (threshold != null && lockminutes != null && threshold > 0 && lockminutes > 0)
				userService.userLock(token, user, threshold, lockminutes);
			
            ShiroUser shiroUser = new ShiroUser.Builder(user.getId(), loginName).name(user.getName())
                    .tenant(user.getTenantId(), user.getTenantName(), user.getIsAdmin() == 2 ? true : false)
                    .isAdmin(user.getIsAdmin() == 1 ? true : false).ip(host).
                    dept(user.getDeptId(), user.getDeptName() ,user.getFullDeptCode()).builder();
            byte[] salt = Encodes.decodeHex(user.getSalt());
            
            return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), ByteSource.Util.bytes(salt),
                    getName());
        } else {
            return null;
        }

    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        List<Role> roleList = null;// 角色集合//
        List<String> pList = new ArrayList<String>();
        List<Permission> listP = null;// 权限集合//

        /**  为了方便看菜单，所有人先赋值所有权限及角色**/
        if (shiroUser.isAdmin()) {
            // 超级管理员获取所有角色//
            roleList = roleService.getAll();
            // 超级管理员获取所有权限//
            listP = permissionService.getAllPermissions();
        }else if(shiroUser.isTenantAdmin()){
            roleList = roleService.getAllByTenantId(shiroUser.getTenantId());
            listP = permissionService.getByTenantId(shiroUser.getTenantId());
        } else {
            roleList = roleService.getRolesByUserId(shiroUser.getId());
            listP = permissionService.getByUserId(shiroUser.getId());
        }
        // 遍历角色
        shiroUser.getRoles().clear();
        for (Role role : roleList) {
            info.addRole(role.getName());
            shiroUser.getRoles().add(role.getId());
        }
        // 遍历权限
        for (Permission per : listP) {
            if (per != null) {
                String[] ps = per.getCode().split(";");
                for (String element : ps) {
                    pList.add(element);
                }
            }
        }
        
        // 超级用户才能 操作 权限管理 数据
        if (shiroUser.isAdmin()) {
            pList.add(Constants.PERMISSION_ADMINISTRATOR);
        }
        
        // 基于Permission的权限信息
        info.addStringPermissions(pList);
        return info;
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(HASH_ALGORITHM);
        matcher.setHashIterations(HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

}
