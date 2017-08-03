package com.lingstep.lhs;

import com.genertech.mems.utils.ConfigUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {

	public static final String SESSION_UNCACHE = ConfigUtil.getConfig("redis.channel");

	/**
	 * 甘特图上 定时刷新间隔(分钟)
	 */
	public static final int GANTT_CHART_INTER_MIN = 5;
	/**
	 * 员工计划DAILY_SHIFT显示航班的计划到达时间提前小时数
	 */
	
	public static final int STAFF_SCHEDULE_ARRIVE_BEFORE_HOUR = 1;

	/**
	 * 员工计划DAILY_SHIFT夜班显示航班的计划到达时间
	 */
	
	public static final String STAFF_SCHEDULE_NIGHT_ARRIVE_TIME = "21:00";

	/**
	 * 权限管理员(只有这个permission 才能操作 权限管理)
	 */
	public static final String PERMISSION_ADMINISTRATOR = "permissionmgt-administrator";

    /***
     * <pre>
     * 功能说明：r日志级别
     * ;0:INFO:信息;1:WARNING:警告;2:ERROR:错误;3:CRUEL:严重;4:DEAD:瘫痪;
     * </pre>
     */
    public static class LogLevel {
        /** 信息 **/
        public static final int INFO = 0;
        /** 警告 **/
        public static final int WARNING = 1;
        /** 错误 **/
        public static final int ERROR = 2;
        /** 严重 **/
        public static final int CRUEL = 3;
        /** 瘫痪 **/
        public static final int DEAD = 4;

        @SuppressWarnings("unused")
		private static Map<String, String> buildMap() {
            Map<String, String> m = new LinkedHashMap<String, String>();
            m.put("0", "信息");
            m.put("1", "警告");
            m.put("2", "错误");
            m.put("3", "严重");
            m.put("4", "瘫痪");
            return m;
        }

        public static String labelOf(int i) {
            switch (i) {
            case 0:
                return "信息";
            case 1:
                return "警告";
            case 2:
                return "错误";
            case 3:
                return "严重";
            case 4:
                return "瘫痪";
            default:
                return "";
            }
        }
    }

    /***
     * <pre>
     * 功能说明：r日志类型
     * ;1:SYSTEM:系统日志;3:SECRITY:安全日志;2:BUSINESS:用户日志;4:OTHER:其它日志;
     * </pre>
     */
    public static class LogType {
        /** 系统日志 **/
        public static final int SYSTEM = 1;
        /** 用户日志 **/
        public static final int BUSINESS = 2;//应用日志
        /** 安全日志 **/
        public static final int SECRITY = 3;
        /** 其它日志 **/
        public static final int OTHER = 4;

        @SuppressWarnings("unused")
		private static Map<String, String> buildMap() {
            Map<String, String> m = new LinkedHashMap<String, String>();
            m.put("1", "系统日志");
            m.put("2", "用户日志");
            m.put("3", "安全日志");
            m.put("4", "其它日志");
            return m;
        }

        public static String labelOf(int i) {
            switch (i) {
            case 1:
                return "系统日志";
            case 2:
                return "用户日志";
            case 3:
                return "安全日志";
            case 4:
                return "其它日志";
            default:
                return "";
            }
        }
    }

    public static boolean synToActiviti = false;


    public static final String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * 部门ID常量
     */
   public static final String AM_DEPT_ID = "f05cdd98132b4bdfb637cba2d6fc7d63"; //飞机维修部id
   public static final String MCC_DEPT_ID = "334acc4b9e1d41cb832e6017733e9b3f";//维修控制部id(mcc)
   public static final String LM_DEPT_ID = "785111de7345439189db5194d0f77182"; //AM-航线部id
   /**
    * 澳门航空机务工程部
    */
   public static final String ME_DEPT_ID = "74935bbdcb3c44f7926b339eef345618";

   /**
    * 澳门机场三字码
    */
   public static final String APT_MFM  = "MFM";

   /**
    * 无效的日期字符串
    */
   public static final String INVALID_DATE_STR = "0000-00-00";


    /**
     * DD 模板文件名稱
     */
   public static final String DD_DETAIL = "PM L2.3.4-01-F.xlsx";

    /**
     * DD PR/DR 报表
     */
   public static final String DD_PR_DR = "DD Production and Daily Report.xls";




   /**
	 * WO SCHE FTP 相关常量
	 */
	public final static String FTP_WO_SCHE_IP = ConfigUtil.getConfig("ftp_wo_sche_ip");
	public final static String FTP_WO_SCHE_PATH = ConfigUtil.getConfig("ftp_wo_sche_path");
	public final static String FTP_WO_SCHE_USR = ConfigUtil.getConfig("ftp_wo_sche_usr");
	public final static String FTP_WO_SCHE_PWD = ConfigUtil.getConfig("ftp_wo_sche_pwd");
	public final static int FTP_WO_SCHE_PORT = ConfigUtil.getConfigI("ftp_wo_sche_port");


	 /**
	 * Mcs Sync相关常量
	 */
	public final static String MCS_SYNC_URL = ConfigUtil.getConfig("mcs_sync.url");

	public enum McsSync{
		WO_SYNC(MCS_SYNC_URL+ "wo/syncWoListByScan"), // 扫描后获取  wo数据
		WO_UPDATE_DL(MCS_SYNC_URL+ "wo/updateWoDlByManu"),//在WO详情页，点击 sync amasis 同步某一个wo dl 数据
		TC_SYNC(MCS_SYNC_URL +"tc/syncTcListByManu"),// 同步 TCI INFO数据
		TC_48_SYNC(MCS_SYNC_URL +"tc/syncTc48ListByManu"),// 同步 在翼 TCI 48  数据
		TC_STORE_48_SYNC(MCS_SYNC_URL +"tc/syncStoreTc48ListByManu"),// 同步 在库 TCI 48  数据
		TC_WO_SYNC(MCS_SYNC_URL +"tc/syncTcWoListByManu"),// 同步 TCI 90内到期wo 数据
        PN_IN_STOCK_SYNC(MCS_SYNC_URL+"pis/syncPisListByManu"),//同步 件-库存数据
        PNSN_RF_SYNC(MCS_SYNC_URL+"pnrf/syncPnsnRfListByManu"),//同步 具体件-收货数据
		RQPN_SYNC(MCS_SYNC_URL+"ws/syncRqPnListByManu?rqNo="),
		WS_SYNC(MCS_SYNC_URL+ "ws/syncBizWsListByMrManu"), // 同步Ws Rq
		FAULT_SYNC(MCS_SYNC_URL+ "fault/syncFaultList"),//fault 同步
		WO_LEFT(MCS_SYNC_URL+ "wo/syncWoLeftListByManual"); // WO Check left
		
		private String value = null;
		private McsSync(String value){
			setValue(value);
		}
		 public String getValue() {
	         return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
	}

	public final static String BIZ_ID = "bizId";
	public final static String BIZ_NO = "bizNo";
}