package com.lingstep.lhs;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONResponse implements Serializable {

    protected enum JSONResult {
        FAILED("failed"), SUCCESS("success"), WARNING("warning");
        private String value = null;

        private JSONResult(String value) {
            setValue(value);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = -1986982315094947073L;

    private JSONResult status = JSONResult.SUCCESS;
    private String msg = "";
    private final Map<String, Object> data = new HashMap<String, Object>();

    public JSONResponse() {
        new JSONResponse(true);
    }

    public JSONResponse(boolean result) {
        new JSONResponse(true, "");
    }

    public JSONResponse(boolean result, String msg) {
        if (result) {
            if (StringUtils.isBlank(msg)) {
                setStatus(JSONResult.SUCCESS);
            } else {
                setStatus(JSONResult.WARNING);
            }
        } else {

            setStatus(JSONResult.FAILED);
        }
        this.msg = msg;
    }

    public JSONResponse(boolean result, String msg,List<Object> data) {
        new JSONResponse( result,  msg);
        setData( data);
    }

    public JSONResponse(String msg) {
        new JSONResponse(false, msg);
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }


    public String getStatus() {
        return status.getValue();
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public void setData(Object obj) {
        put("data", obj);
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(JSONResult status) {
        this.status = status;
    }

}
