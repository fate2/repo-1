package com.lingstep.lhs.sys.authentication.domian;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

public class CustomToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    /**
     */
    private static final long serialVersionUID = 7836740849384086273L;
    private String loginName;
    private String password;
    private String tenantName;
    private String host;
    private boolean rememberMe = false;

    public CustomToken() {
    }

    public CustomToken(String loginName, String password, String tenant) {
        this(loginName, password, tenant, false, null);
    }

    public CustomToken(String loginName, String password, String tenant, String host) {
        this(loginName, password, tenant, false, host);
    }

    public CustomToken(String loginName, String password, String tenant, boolean rememberMe) {
        this(loginName, password, tenant, rememberMe, null);
    }

    public CustomToken(String loginName, String password, String tenant, boolean rememberMe, String host) {
        this.loginName = loginName;
        this.password = password;
        this.tenantName = tenant;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public Object getPrincipal() {
        return getLoginName();
    }

    public Object getCredentials() {
        return getPassword();
    }

    public String getHost() {
        return host;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void clear() {
        this.loginName = null;
        this.host = null;
        this.password = null;
        this.tenantName = null;
        this.rememberMe = false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append("租户:" + tenantName + " - ");
        sb.append(loginName);
        sb.append(", rememberMe=").append(rememberMe);
        if (StringUtils.isNotBlank(host)) {
            sb.append(" (").append(host).append(")");
        }
        return sb.toString();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
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
}
