package com.lingstep.lhs.sys.rbac.domain;

/**
 * Created by Administrator on 2014/8/23.
 */
public class DeptTreeDto {

    private String id ;
    private String name;
    private String pId;
    private String type;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
