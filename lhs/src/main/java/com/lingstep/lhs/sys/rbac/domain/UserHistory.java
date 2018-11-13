package com.lingstep.lhs.sys.rbac.domain;

import java.util.Date;

/**
 * 
 * <pre>
 * 功能说明：用户密码信息修改历史
 * </pre>
 * 
 * @author <a href="mailto:lin.f@gener-tech.com">linfeng</a>
 * @version 1.0
 */
public class UserHistory {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 修改用户ID
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private Date updateDatetime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_USER_HISTORY.ID
     *
     * @return the value of SYS_USER_HISTORY.ID
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_USER_HISTORY.ID
     *
     * @param id the value for SYS_USER_HISTORY.ID
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_USER_HISTORY.USER_ID
     *
     * @return the value of SYS_USER_HISTORY.USER_ID
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_USER_HISTORY.USER_ID
     *
     * @param userId the value for SYS_USER_HISTORY.USER_ID
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_USER_HISTORY.PASSWORD
     *
     * @return the value of SYS_USER_HISTORY.PASSWORD
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_USER_HISTORY.PASSWORD
     *
     * @param password the value for SYS_USER_HISTORY.PASSWORD
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_USER_HISTORY.SALT
     *
     * @return the value of SYS_USER_HISTORY.SALT
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public String getSalt() {
        return salt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_USER_HISTORY.SALT
     *
     * @param salt the value for SYS_USER_HISTORY.SALT
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_USER_HISTORY.UPDATE_USER
     *
     * @return the value of SYS_USER_HISTORY.UPDATE_USER
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_USER_HISTORY.UPDATE_USER
     *
     * @param updateUser the value for SYS_USER_HISTORY.UPDATE_USER
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_USER_HISTORY.UPDATE_DATETIME
     *
     * @return the value of SYS_USER_HISTORY.UPDATE_DATETIME
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_USER_HISTORY.UPDATE_DATETIME
     *
     * @param updateDatetime the value for SYS_USER_HISTORY.UPDATE_DATETIME
     *
     * @mbggenerated Tue Oct 21 11:18:33 CST 2014
     */
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}