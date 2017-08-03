package com.lingstep.lhs.sys.rbac.service.impl;

import com.genertech.commons.core.exception.ServiceException;
import com.genertech.commons.core.security.utils.Securitys;
import com.genertech.commons.core.utils.Identities;
import com.genertech.mems.sys.log.service.LogService;
import com.genertech.mems.sys.rbac.dao.DeptDao;
import com.genertech.mems.sys.rbac.domain.Dept;
import com.genertech.mems.sys.rbac.domain.DeptTreeDto;
import com.genertech.mems.sys.rbac.service.DeptService;
import com.genertech.mems.utils.Consts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeptServiceImpl implements DeptService {

    private static Logger logger = LoggerFactory.getLogger(DeptServiceImpl.class);

    @Autowired
    private DeptDao deptDao;
    
    @Autowired
    private LogService logService;

    @Override
    public Dept getById(String id) {
        try {
            return deptDao.queryById(id);
        } catch (Exception e) {
            logService.error(Consts.SystemModel.SYS, "查询部门信息失败");
            throw new ServiceException("查询部门信息失败", e);
        }
    }

    @Override
    public List<Dept> list(String tenantId) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("tenantId", tenantId);
            return deptDao.list(map);
        } catch (Exception e) {
            logService.error(Consts.SystemModel.SYS, "查询部门列表失败");
            throw new ServiceException("查询部门列表失败", e);
        }

    }

    @Override
    public String save(Dept dept) {
        Dept oldDept = deptDao.findByNameAndParentId(dept.getName(), null,dept.getParentId());
        if (null != oldDept) {
            throw new ServiceException("已存在同名部门");
        }

        if (StringUtils.isNotBlank(dept.getParentId())) {
            Dept parentDept = deptDao.queryById(dept.getParentId());
            if (null == parentDept) {
                throw new ServiceException("上级部门不存在");
            }

            if (!"系统默认".equals(Securitys.getTenantName())) {
                if (!Securitys.getTenantId().equals(dept.getTenantId())) {
                    throw new ServiceException("租户信息不正确");
                }
            }
        }


        try {
            String deptId = Identities.uuid();
            dept.setId(deptId);
            dept.setIsDelete(0);
            dept.setCreateUser(Securitys.getUserId());
            dept.setCreateDatetime(new Date());
            dept.setFullDeptCode(deptDao.findNextFullDeptCodeByPid(dept.getFullDeptCode(), dept.getParentId()));
            deptDao.save(dept);
            logger.info("用户" + Securitys.getUserId() + "创建部门:" + deptId);
            logService.info(Consts.SystemModel.SYS, "创建部门");
            return deptId;
        } catch (Exception e) {
            logService.error(Consts.SystemModel.SYS, "添加部门列表失败");
            throw new ServiceException("添加部门列表失败", e);
        }
    }

    @Override
    public void delete(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("请选择要删除的组织机构");
        }
        try {
            deptDao.delete(id);
            logger.info("用户" + Securitys.getUserId() + "删除部门:" + id);
            logService.warning(Consts.SystemModel.SYS, "删除部门");
        } catch (Exception e) {
            logService.error(Consts.SystemModel.SYS, "删除部门失败");
            throw new ServiceException("删除租户失败", e);
        }

    }

    @Override
    public void update(Dept dept) {
        Dept oldOrganization = deptDao.findByNameAndParentId(dept.getName(), dept.getId(), dept.getParentId());
        if (null != oldOrganization) {
            throw new ServiceException("已存在同名部门");
        }
        try {
            dept.setUpdateUser(Securitys.getUserId());
            dept.setUpdateDatetime(new Date());
            deptDao.update(dept);
            logger.info("用户" + Securitys.getUserId() + "修改部门:" + dept.getId());
            logService.info(Consts.SystemModel.SYS, "修改部门");
        } catch (Exception e) {
            logService.error(Consts.SystemModel.SYS, "修改部门失败");
            throw new ServiceException("修改部门失败", e);
        }

    }

    @Override
    public void rename(String id, String newName) {
        
        try {
            Dept dept = getById(id);
            
            dept.setName(newName);
            
            update(dept);
            logService.info(Consts.SystemModel.SYS, "组织机构重命名");
        } catch (Exception e) {
            
            logService.error(Consts.SystemModel.SYS, "组织机构重命名失败");
            throw new ServiceException("组织机构重命名失败",e);
        }
    }
    
    public boolean checkExists(String name,String id,String parentId){
        Dept oldDept = deptDao.findByNameAndParentId(name, id, parentId);
        return null != oldDept;
    }

	@Override
	public List<DeptTreeDto> getDeptTreeByTenantId(String tenantId, String deptId) {
		try {
			return deptDao.queryDeptTreeByTenantId(tenantId, deptId);
		} catch (Exception e) {
			logService.error(Consts.SystemModel.SYS, "查询部门树失败");
			throw new ServiceException("查询部门树失败", e);
		}
	}

	@Override
	public List<Dept> findDeptById(String deptId) {
		return deptDao.findDeptById(deptId);
	}

}
