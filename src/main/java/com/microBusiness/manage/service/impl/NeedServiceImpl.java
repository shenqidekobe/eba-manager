package com.microBusiness.manage.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.AdminDao;
import com.microBusiness.manage.dao.AreaDao;
import com.microBusiness.manage.dao.HostingShopDao;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.dao.MemberMemberDao;
import com.microBusiness.manage.dao.MemberRankDao;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.NeedImportInfoDao;
import com.microBusiness.manage.dao.NeedImportLogDao;
import com.microBusiness.manage.dao.ReceiverDao;
import com.microBusiness.manage.dao.ShopDao;
import com.microBusiness.manage.dao.ass.AssChildMemberDao;
import com.microBusiness.manage.dao.ass.AssUpdateTipsDao;
import com.microBusiness.manage.dto.ShopAssistantDto;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberMember;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Need.NeedStatus;
import com.microBusiness.manage.entity.Need.Type;
import com.microBusiness.manage.entity.NeedImportError;
import com.microBusiness.manage.entity.NeedImportInfo;
import com.microBusiness.manage.entity.NeedImportLog;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.util.ValidatorUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.AreaDao;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.dao.MemberRankDao;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.NeedImportInfoDao;
import com.microBusiness.manage.dao.NeedImportLogDao;
import com.microBusiness.manage.dao.ReceiverDao;
import com.microBusiness.manage.dao.ass.AssChildMemberDao;
import com.microBusiness.manage.dao.ass.AssUpdateTipsDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Need.Type;
import com.microBusiness.manage.entity.NeedImportError;
import com.microBusiness.manage.entity.NeedImportInfo;
import com.microBusiness.manage.entity.NeedImportLog;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssUpdateTips;
import com.microBusiness.manage.service.BindPhoneSmsService;
import com.microBusiness.manage.service.NeedService;
import com.microBusiness.manage.util.ValidatorUtils;


/**
 * Created by mingbai on 2017/1/23.
 * 功能描述：
 * 修改记录：
 */
@Service
public class NeedServiceImpl extends BaseServiceImpl<Need , Long> implements NeedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeedServiceImpl.class);

    @Resource
    private MemberRankDao memberRankDao ;

    @Resource
    private MemberDao memberDao ;

    @Resource
    private NeedDao needDao ;

    @Resource
    private NeedImportLogDao needImportLogDao;

    @Resource
    private NeedImportInfoDao needImportInfoDao ;

    @Resource
    private AreaDao areaDao ;
    @Resource
    private ReceiverDao receiverDao;
    @Resource
    private AssUpdateTipsDao assUpdateTipsDao;
    @Resource
    private AssChildMemberDao assChildMemberDao;
    @Resource
	private MemberMemberDao memberMemberDao;
    @Resource
	private ShopDao shopDao;
    @Resource
	private HostingShopDao hostingShopDao;
    @Resource
    private AdminDao adminDao;
    

    @Resource
    private BindPhoneSmsService bindPhoneSmsService;


    @Override
    public Need save(Need entity) {

    	Member userInfo = memberDao.findByUsername(entity.getTel());
    	
        if (userInfo == null) {

        	userInfo = new Member();

	        userInfo.setUsername(entity.getTel());
	        userInfo.setNickname(entity.getUserName());
	        userInfo.setMobile(entity.getTel());
	        userInfo.setOpenId("-1");
	        //userInfo.setNeed(entity);

            userInfo.setPassword(DigestUtils.md5Hex("123456"));
            userInfo.setEmail("afeijackcool@163.com");
            userInfo.setPoint(BigDecimal.ZERO);
            userInfo.setBalance(BigDecimal.ZERO);
            userInfo.setAmount(BigDecimal.ZERO);
            userInfo.setIsEnabled(true);
            userInfo.setIsLocked(false);
            userInfo.setLoginFailureCount(0);
            userInfo.setLockedDate(null);
            userInfo.setRegisterIp("");
            userInfo.setLoginIp("");
            userInfo.setLoginDate(new Date());
            userInfo.setLoginPluginId(null);
            userInfo.setLockKey(null);
            userInfo.setSafeKey(null);
            userInfo.setMemberRank(memberRankDao.findDefault());
            userInfo.setCart(null);
            userInfo.setOrders(null);
            userInfo.setPaymentLogs(null);
            userInfo.setDepositLogs(null);
            userInfo.setCouponCodes(null);
            userInfo.setReceivers(null);
            userInfo.setReviews(null);
            userInfo.setConsultations(null);
            userInfo.setFavoriteGoods(null);
            userInfo.setProductNotifies(null);
            userInfo.setInMessages(null);
            userInfo.setOutMessages(null);
            userInfo.setPointLogs(null);

            memberDao.persist(userInfo);
        }

        entity.setMember(userInfo);
        
        super.save(entity);
        
        //根据member和企业查询地址
        Receiver receiver = receiverDao.find(userInfo, entity.getSupplier());
        if(receiver == null) {//地址不存在就添加地址
            receiver = new Receiver();
            receiver.setConsignee(entity.getUserName());
    		receiver.setPhone(entity.getTel());
    		receiver.setAddress(entity.getAddress());
    		receiver.setMember(userInfo);
    		receiver.setIsDefault(false);
    		receiver.setArea(entity.getArea());
    		receiver.setAreaName(entity.getArea().getFullName());
    		receiver.setZipCode("200000");
    		receiver.setSupplier(entity.getSupplier());
    		receiver.setType(Receiver.Type.backstage);
    		receiverDao.persist(receiver);
        }else {//否则就修改地址
        	receiver.setConsignee(entity.getUserName());
    		receiver.setPhone(entity.getTel());
    		receiver.setAddress(entity.getAddress());
    		receiver.setMember(userInfo);
    		receiver.setArea(entity.getArea());
    		receiver.setAreaName(entity.getArea().getFullName());
    		receiverDao.merge(receiver);
        }
        
		
		List<AssChildMember> assChildMembers = assChildMemberDao.findList(entity);
		for(AssChildMember childMember : assChildMembers) {
			AssUpdateTips updateTips = new AssUpdateTips();
			updateTips.setAssChildMember(childMember);
			updateTips.setNeed(entity);
			updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
			updateTips.setType(AssUpdateTips.Type.individualCustomers);
			assUpdateTipsDao.persist(updateTips);
		}
        
        return entity;
    }

    /**
     * @param pageable
     * @param need
     * @return
     */
    @Override
    public Page<Need> findPage(Pageable pageable, Need need , String searchName, Date startDate, Date endDate) {

        return needDao.findPage(pageable , need , searchName, startDate, endDate);
    }


    @Override
    public Page<Need> findPage(Pageable pageable, Need need, Date startDate, Date endDate) {
        return needDao.findPage(pageable , need , startDate , endDate);
    }

    @Override
    public List<Need> findBySupplier(Supplier supplier, Need.Type type) {
        return needDao.findBySupplier(supplier, type);
    }

    @Override
    public Need findByTel(String tel) {
        return needDao.findByTel(tel);
    }

	@Override
	public Page<Need> findPage(Pageable pageable, Supplier supplier) {
		// TODO Auto-generated method stub
		return needDao.findPage(pageable,supplier);
	}

    @Override
    public Page<Need> findPage(Pageable pageable, Supplier supplier, Need.Type type,String searchName) {
        return needDao.findPage(pageable,supplier , type,searchName);
    }
    
    @Override
    public Page<Need> findPage(Pageable pageable, Supplier supplier, Need.Type type,String searchName,
			List<NeedStatus> needStatus) {
        return needDao.findPage(pageable,supplier , type,searchName,needStatus);
    }

    /**
     * 处理批量导入收货点
     *
     * @param multipartFile
     * @param admin
     * @return
     */
    @Override
    public NeedImportLog dealImportMore(MultipartFile multipartFile, Admin admin , Supplier supplier) {

        NeedImportLog needImportLog = new NeedImportLog() ;

        try {
            Workbook wb;
            //excel 版本区别
            if (multipartFile.getOriginalFilename().matches("^.+\\.(?i)(xls)$")) {
                wb = new HSSFWorkbook(multipartFile.getInputStream());
            } else {
                wb = new XSSFWorkbook(multipartFile.getInputStream());
            }

            Sheet sheet = wb.getSheetAt(0);
            int rowLen = sheet.getPhysicalNumberOfRows();
            int successNum = 0 ;
            int errorNum = 0 ;

            needImportLog.setTotal(rowLen - 1);
            needImportLog.setAdmin(admin);

            List<NeedImportInfo> needImportInfos = new ArrayList<>() ;

            Map<String , NeedImportInfo> exitsTels = new HashMap<>() ;
            Map<String , NeedImportInfo> exitsNames = new HashMap<>() ;

            Row rowFlag = sheet.getRow(0);
            Cell flagCell = rowFlag.getCell(0);
            String flag = this.getCellValue(flagCell);
            if (flag.equals("直营门店导入信息")) {
            	for(int i = 2 ; i <= rowLen ; i++){
	                NeedImportInfo importInfo = new NeedImportInfo() ;
	                List<NeedImportError> errors = new ArrayList<>() ;

	                Row row = sheet.getRow(i) ;

					if(row == null){
						needImportLog.setTotal(needImportLog.getTotal()-1);
						continue;
					}
	                
	                //需要处理类型
	                // 收货人姓名
	                Cell userNameCell = row.getCell(0);
	                String userName = this.getCellValue(userNameCell) ;
	                
	                // 客户名称
	                Cell nameCell = row.getCell(1);
	                String name = this.getCellValue(nameCell) ;
	                
	                // 客户编号
	                Cell clientNumCell = row.getCell(2);
	                String clientNum = this.getCellValue(clientNumCell) ;
	                
	                Cell proviceCell = row.getCell(3);
	                String provice = this.getCellValue(proviceCell) ;

	                Cell cityCell = row.getCell(4);
	                String city = this.getCellValue(cityCell) ;
                
               // Cell clientNumCell = row.getCell(2);
               // String clientNum = this.getCellValue(clientNumCell) ;

	                Cell areaCell = row.getCell(5);
	                String area = this.getCellValue(areaCell) ;

	                Cell addressCell = row.getCell(6);
	                String address = this.getCellValue(addressCell) ;

	                // 店员
	                Cell clerkCell = row.getCell(7);
	                String clerk = this.getCellValue(clerkCell) ;
	                
	                // 店员手机号
	                Cell clerkPhoneCell = row.getCell(8);
	                String clerkPhone = this.getCellValue(clerkPhoneCell) ;
	                
	                //处理省市区
	                StringBuffer fullNameBuff = new StringBuffer() ;
	                fullNameBuff.append(provice).append(city).append(area);
	                String fullName = fullNameBuff.toString();

	                Area findArea = null ;

	                importInfo.setName(name);
	                importInfo.setAddress(address);
	                importInfo.setUserName(userName);

	                boolean vaild = true ;

	                if(StringUtils.isEmpty(fullName)){
	                    errors.add(new NeedImportError(){{
	                        this.setErrorField("area");
	                        this.setErrorInfo("省市区为空");
	                    }});
	                    vaild = false ;
	                }else{
	                    findArea = areaDao.findByFullName(fullName);
	                    if(null == findArea){
	                        errors.add(new NeedImportError(){{
	                            this.setErrorField("area");
	                            this.setErrorInfo("省市区系统中不存在");
	                        }});
	                        vaild = false ;
	                    }
	                }

	                if(StringUtils.isEmpty(address)){
	                    errors.add(new NeedImportError(){{
	                        this.setErrorField("address");
	                        this.setErrorInfo("详细地址为空");
	                    }});
	                    vaild = false ;
	                }

	                if(StringUtils.isEmpty(userName)){
	                    errors.add(new NeedImportError(){{
	                        this.setErrorField("userName");
	                        this.setErrorInfo("收货人姓名为空");
	                    }});
	                    vaild = false ;
	                }

	                if (!StringUtils.isEmpty(clerk)) {
	                	if(StringUtils.isEmpty(clerkPhone)){
	                		//手机号为空，则自动生成一个虚拟手机号，号码规则：
	                        Member member = memberDao.findByMobile(clerkPhone);	
	                        boolean flagNeed = false;
	                        String tel = "";
	                        do {
	                        	String opt = RandomStringUtils.randomNumeric(10);
	                	        tel = "2" + opt;
	                	        flagNeed = memberMemberDao.telExists(memberDao.findByMobile(admin.getBindPhoneNum()), member) ;
	                		} while (flagNeed);
	                        
	                        importInfo.setTel(tel);
		                }else{
		                    //判断手机号是否合法
		                    if(!ValidatorUtils.isTel(clerkPhone)){
		                        errors.add(new NeedImportError(){{
		                            this.setErrorField("tel");
		                            this.setErrorInfo("店员手机号不合法");
		                        }});
		                        vaild = false ;
		                    }else{
		                        //先判断 excel 里面是否有重复,手机号只记录一次
		                        //如果发现重复手机号，需要将重复的手机号处理为失败
		                        if(exitsTels.containsKey(clerkPhone)){
		                            errors.add(new NeedImportError(){{
		                                this.setErrorField("loginTel");
		                                this.setErrorInfo("excel店员手机号重复");
		                            }});
		                            vaild = false ;

		                            NeedImportInfo exits = exitsTels.get(clerkPhone);

		                            if(exits.getValid()){
		                                exits.setValid(false);
		                                exits.getErrors().add(new NeedImportError(){{
		                                    this.setErrorField("loginTel");
		                                    this.setErrorInfo("excel店员手机号重复");
		                                }});
										errorNum++;
		                                successNum--;
		                            }


		                        }else{
		                            exitsTels.put(clerkPhone , importInfo) ;
		                            //判断手机号是否重复
		                            Member member = memberDao.findByMobile(clerkPhone);
		                            if (member != null) {
		                            	 boolean need = memberMemberDao.telExists(memberDao.findByMobile(admin.getBindPhoneNum()), member) ;
				                         if(need){
				                            errors.add(new NeedImportError(){{
				                               this.setErrorField("loginTel");
				                               this.setErrorInfo("系统中已存在手机号");
				                            }});
				                            vaild = false ;
				                         }
									}
		                        }

		                    }

		                }
					} else {
						if(!StringUtils.isEmpty(clerkPhone)){
		                    errors.add(new NeedImportError(){{
		                        this.setErrorField("clerk");
		                        this.setErrorInfo("店员姓名为空");
		                    }});
		                    vaild = false ;
		                }
					}
	                
	                if (StringUtils.isEmpty(name)) {
	                	errors.add(new NeedImportError(){{
	                        this.setErrorField("name");
	                        this.setErrorInfo("门店名称为空");
	                    }});
	                    vaild = false ;
					} else {
						if(exitsNames.containsKey(name)){
	                        errors.add(new NeedImportError(){{
	                            this.setErrorField("name");
	                            this.setErrorInfo("excel门店名称重复");
	                        }});
	                        vaild = false ;

	                        NeedImportInfo exits = exitsNames.get(name);

	                        if(exits.getValid()){
	                            exits.setValid(false);
	                            exits.getErrors().add(new NeedImportError(){{
	                                this.setErrorField("name");
	                                this.setErrorInfo("excel门店名称重复");
	                            }});
								errorNum++;
	                            successNum--;
	                        }
	                    }else{
	                        exitsNames.put(name , importInfo) ;
	                        boolean need = needDao.nameExists(name, supplier);
	                        if(need){
	                            errors.add(new NeedImportError(){{
	                                this.setErrorField("name");
	                                this.setErrorInfo("系统中已存在门店名称");
	                            }});
	                            vaild = false ;
	                        }
	                    }
					}
	                
	                importInfo.setTel(clerkPhone);
	                importInfo.setClientNum(clientNum);
	                importInfo.setArea(findArea);
	                importInfo.setClerk(clerk);
	                importInfo.setErrors(errors);
	                importInfo.setValid(vaild);
	                needImportInfos.add(importInfo);

	                if(vaild){
	                    successNum++;
	                }else{
	                    errorNum++;
	                }
	            }
				
            	needImportLog.setShopType(ShopType.direct);
				needImportLog.setErrorNum(errorNum);
		        needImportLog.setSuccessNum(successNum);

		        needImportLogDao.persist(needImportLog);
		        for(NeedImportInfo needImportInfo : needImportInfos){

		           needImportInfo.setNeedImportLog(needImportLog);
		           needImportInfo.setSupplier(supplier);

		           needImportInfoDao.persist(needImportInfo);
		        }
            	
			}else if(flag.equals("加盟门店导入信息")){
				for(int i = 2 ; i <= rowLen ; i++){
	                NeedImportInfo importInfo = new NeedImportInfo() ;
	                List<NeedImportError> errors = new ArrayList<>() ;

	                Row row = sheet.getRow(i) ;
	                
	                if(row == null){
						needImportLog.setTotal(needImportLog.getTotal()-1);
						continue;
					}
	                
	                //需要处理类型
	                Cell telCell = row.getCell(0);
	                String loginTel = this.getCellValue(telCell) ;

	                Cell userNameCell = row.getCell(1);
	                String userName = this.getCellValue(userNameCell) ;

	                Cell clientNumCell = row.getCell(2);
	                String clientNum = this.getCellValue(clientNumCell) ;
	                
	                Cell nameCell = row.getCell(3);
	                String name = this.getCellValue(nameCell) ;

	                Cell proviceCell = row.getCell(4);
	                String provice = this.getCellValue(proviceCell) ;

	                Cell cityCell = row.getCell(5);
	                String city = this.getCellValue(cityCell) ;

	                Cell areaCell = row.getCell(6);
	                String area = this.getCellValue(areaCell) ;

	                Cell addressCell = row.getCell(7);
	                String address = this.getCellValue(addressCell) ;

	                //处理省市区
	                StringBuffer fullNameBuff = new StringBuffer() ;
	                fullNameBuff.append(provice).append(city).append(area);
	                String fullName = fullNameBuff.toString();

	                Area findArea = null ;

	                importInfo.setName(name);
	                importInfo.setAddress(address);
	                importInfo.setUserName(userName);
	                importInfo.setTel(loginTel);

	                boolean vaild = true ;

//	                if(StringUtils.isEmpty(loginTel)){
//	                    errors.add(new NeedImportError(){{
//	                        this.setErrorField("loginTel");
//	                        this.setErrorInfo("登录账号为空");
//	                    }});
//	                    vaild = false ;
//	                }

	                if(StringUtils.isEmpty(fullName)){
	                    errors.add(new NeedImportError(){{
	                        this.setErrorField("area");
	                        this.setErrorInfo("省市区为空");
	                    }});
	                    vaild = false ;
	                }else{
	                    findArea = areaDao.findByFullName(fullName);
	                    if(null == findArea){
	                        errors.add(new NeedImportError(){{
	                            this.setErrorField("area");
	                            this.setErrorInfo("省市区系统中不存在");
	                        }});
	                        vaild = false ;
	                    }
	                }

	                if(StringUtils.isEmpty(address)){
	                    errors.add(new NeedImportError(){{
	                        this.setErrorField("address");
	                        this.setErrorInfo("详细地址为空");
	                    }});
	                    vaild = false ;
	                }

	                if(StringUtils.isEmpty(userName)){
	                    errors.add(new NeedImportError(){{
	                        this.setErrorField("userName");
	                        this.setErrorInfo("收货人姓名为空");
	                    }});
	                    vaild = false ;
	                }

//	                if(StringUtils.isNotEmpty(desc) && desc.length() > 100){
//	                    errors.add(new NeedImportError(){{
//	                        this.setErrorField("description");
//	                        this.setErrorInfo("备注超过100字符");
//	                    }});
//	                    vaild = false ;
//	                }

	                if(StringUtils.isEmpty(loginTel)){
	                	//手机号为空，则自动生成一个虚拟手机号，号码规则：
                        Member member = memberDao.findByMobile(loginTel);	
                        boolean flagNeed = false;
                        String tel = "";
                        do {
                        	String opt = RandomStringUtils.randomNumeric(10);
                	        tel = "2" + opt;
                	        flagNeed = memberMemberDao.telExists(memberDao.findByMobile(admin.getBindPhoneNum()), member) ;
                		} while (flagNeed);
                        
                        importInfo.setTel(tel);
	                }else{
	                    //判断手机号是否合法
	                    if(!ValidatorUtils.isTel(loginTel)){
	                        errors.add(new NeedImportError(){{
	                            this.setErrorField("tel");
	                            this.setErrorInfo("手机号不合法");
	                        }});
	                        vaild = false ;
	                    }else{
	                        //先判断 excel 里面是否有重复,手机号只记录一次
	                        //如果发现重复手机号，需要将重复的手机号处理为失败
	                        if(exitsTels.containsKey(loginTel)){
	                            errors.add(new NeedImportError(){{
	                                this.setErrorField("loginTel");
	                                this.setErrorInfo("excel手机号重复");
	                            }});
	                            vaild = false ;

	                            NeedImportInfo exits = exitsTels.get(loginTel);

	                            if(exits.getValid()){
	                                exits.setValid(false);
	                                exits.getErrors().add(new NeedImportError(){{
	                                    this.setErrorField("loginTel");
	                                    this.setErrorInfo("excel手机号重复");
	                                }});
									errorNum++;
	                                successNum--;
	                            }


	                        }else{
	                            exitsTels.put(loginTel , importInfo) ;
	                            //判断手机号是否重复
	                            boolean need = needDao.telExists(loginTel, supplier) ;
	                            if(need){
	                                errors.add(new NeedImportError(){{
	                                    this.setErrorField("loginTel");
	                                    this.setErrorInfo("系统中已存在手机号");
	                                }});
	                                vaild = false ;
	                            }
	                        }

	                    }

	                }
	                
	                if (StringUtils.isEmpty(name)) {
	                	errors.add(new NeedImportError(){{
	                        this.setErrorField("userName");
	                        this.setErrorInfo("门店名称为空");
	                    }});
	                    vaild = false ;
					}else {
						if(exitsNames.containsKey(name)){
	                        errors.add(new NeedImportError(){{
	                            this.setErrorField("name");
	                            this.setErrorInfo("excel门店名称重复");
	                        }});
	                        vaild = false ;

	                        NeedImportInfo exits = exitsNames.get(name);

	                        if(exits.getValid()){
	                            exits.setValid(false);
	                            exits.getErrors().add(new NeedImportError(){{
	                                this.setErrorField("name");
	                                this.setErrorInfo("excel门店名称重复");
	                            }});
								errorNum++;
	                            successNum--;
	                        }
	                    }else{
	                        exitsNames.put(name , importInfo) ;
	                        boolean need = needDao.nameExists(name, supplier);
	                        if(need){
	                            errors.add(new NeedImportError(){{
	                                this.setErrorField("name");
	                                this.setErrorInfo("系统中已存在门店名称");
	                            }});
	                            vaild = false ;
	                        }
	                    }
					}
	                
	                importInfo.setClientNum(clientNum);
	                importInfo.setArea(findArea);
	                importInfo.setErrors(errors);
	                importInfo.setValid(vaild);
	                needImportInfos.add(importInfo);

	                if(vaild){
	                    successNum++;
	                }else{
	                    errorNum++;
	                }
	            }
				
				needImportLog.setShopType(ShopType.affiliate);
				needImportLog.setErrorNum(errorNum);
		        needImportLog.setSuccessNum(successNum);

		        needImportLogDao.persist(needImportLog);
		        for(NeedImportInfo needImportInfo : needImportInfos){

		           needImportInfo.setNeedImportLog(needImportLog);
		           needImportInfo.setSupplier(supplier);

		           needImportInfoDao.persist(needImportInfo);
		        }
			}else {
				 return null ;
			}
            
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("method:dealImportMore , excel file read error : " , e);
            return null ;

        }

        return needImportLog ;
    }


    private String getCellValue(Cell cell){
        String cellValue = "" ;
        if(null == cell){
            return cellValue ;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                DecimalFormat df = new DecimalFormat("0");
                cellValue = df.format(cell.getNumericCellValue());
                break;

            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;

            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue() + "";
                break;

            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = cell.getCellFormula() + "";
                break;

            case Cell.CELL_TYPE_BLANK: // 空值
                cellValue = "";
                break;

            case Cell.CELL_TYPE_ERROR: // 故障
                cellValue = "";
                break;

            default:
                cellValue = "";
                break;
        }
        return cellValue ;
    }

    @Override
    public boolean saveMore(NeedImportLog needImportLog, Admin admin) {
        List<NeedImportInfo> infos = needImportInfoDao.findList(needImportLog , Boolean.TRUE) ;

        if (needImportLog.getShopType() == ShopType.affiliate) {
        	for(NeedImportInfo info : infos){
        		 Need need = new Need();
                 need.setType(Need.Type.general);
                 need.setArea(info.getArea());
                 need.setName(info.getName());
                 need.setAddress(info.getAddress());
                 need.setDescription(info.getDescription());
                 need.setTel(info.getTel());
                 need.setUserName(info.getUserName());
                 need.setSupplier(info.getSupplier());
                 need.setShopType(ShopType.affiliate);
                 need.setClientNum(info.getClientNum());
                 
                 this.save(need) ;
                 
               //手机号是正常手机号时，发短信提醒
                 if(!StringUtils.isEmpty(need.getTel())){
                 	if(need.getTel().matches("1[3|4|5|7|8]\\d{9}")){
                     	String msg = "恭喜您成功开通微商小管理账号，您可微信搜索微商小管理小程序，登录订货。";
                     	bindPhoneSmsService.sendSms(need.getTel(), msg);
                     }
                 }
        	}
		} else {
			for(NeedImportInfo info : infos){
				Need need = new Need();
                need.setType(Need.Type.general);
                need.setArea(info.getArea());
                need.setName(info.getName());
                need.setTel(admin.getBindPhoneNum());
                need.setAddress(info.getAddress());
                need.setDescription(info.getDescription());
                need.setUserName(info.getUserName());
                need.setSupplier(info.getSupplier());
                need.setShopType(ShopType.direct);
                need.setClientNum(info.getClientNum());
                
//                MemberMember memberMember = new MemberMember();
//                memberMember.setName(info.getClerk());
//                
//                Member byMember = memberDao.findByMobile(info.getTel());
//                memberMember.setByMember(byMember);
//               
//                String phone = admin.getBindPhoneNum();
//                Member member = memberDao.findByMobile(phone);
//                memberMember.setMember(member);
//                
//                memberMemberDao.persist(memberMember);
                
                this.saveDirectStore(need, info.getClerk(), info.getTel());
                
              //手机号是正常手机号时，发短信提醒
                if(!StringUtils.isEmpty(need.getTel())){
                	if(need.getTel().matches("1[3|4|5|7|8]\\d{9}")){
                    	String msg = "恭喜您成功开通微商小管理账号，您可微信搜索微商小管理小程序，登录订货。";
                    	bindPhoneSmsService.sendSms(need.getTel(), msg);
                    }
                }
			}
		}
        
        return true;
    }

	@Override
	public Need modifyCheckTel(String tel , Long id) {
		return needDao.modifyCheckTel(tel , id);
	}

	/**
	 * 修改收货点状态
	 */
	@Override
	public Need updateneedStatus(Need need) {
		if(null == need.getId() || null == need.getNeedStatus()){
			return null;
		}
		Need need2 = needDao.find(need.getId());
		need2.setNeedStatus(need.getNeedStatus());
		return need2;
	}

	@Override
	public Need updateNeed(Need need,Admin admin) {
		if(null == need.getId()) {
			return null;
		}
		Need need2 = needDao.find(need.getId());
		String tel = need2.getTel();
		need2.setName(need.getName());
		need2.setArea(need.getArea());
		need2.setAddress(need.getAddress());
		need2.setUserName(need.getUserName());
		need2.setTel(need.getTel());
		need2.setDescription(need.getDescription());
		//Member member = needDao.findByMember(need.getId());
		
		// 新增属性
		need2.setClientNum(need.getClientNum());
		need2.setContacts(need.getContacts());
		need2.setContactsTel(need.getContactsTel());
		need2.setEmail(need.getEmail());
		need2.setAccountName(need.getAccountName());
		need2.setInvoice(need.getInvoice());
		need2.setBank(need.getBank());
		need2.setBankAccountNum(need.getBankAccountNum());
		need2.setIdentifier(need.getIdentifier());
		need2.setAdmin(admin);
		
		//添加更新提示
		List<AssUpdateTips> assUpdateTips = assUpdateTipsDao.findList(null, need2, AssUpdateTips.Type.individualCustomers);
		if(assUpdateTips.size()>0) {
			AssChildMember assChildMember = assChildMemberDao.find(admin);
			if(null != assChildMember) {
				for(AssUpdateTips updateTips : assUpdateTips) {
					updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
					updateTips.setAssChildMember(assChildMember);
					assUpdateTipsDao.merge(updateTips);
				}
			}else {
				for(AssUpdateTips updateTips : assUpdateTips) {
					assUpdateTipsDao.delete(updateTips);
				}
			}
			
		}else {
			List<AssChildMember> assChildMembers = assChildMemberDao.findList(need2);
			for(AssChildMember childMember : assChildMembers) {
				AssUpdateTips updateTips = new AssUpdateTips();
				updateTips.setAssChildMember(childMember);
				updateTips.setNeed(need2);
				updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
				updateTips.setType(AssUpdateTips.Type.individualCustomers);
				assUpdateTipsDao.persist(updateTips);
			}
		}
		

		if(!need.getTel().equals(tel)) {
//			分销版本修改
//			Set<Member> members = need2.getMembers();
//			for(Member member2 : members) {
				
//				Member member2 = need2.getMember();
//				member2.setUsername(need.getTel());
//				member2.setNickname(need.getUserName());
//				member2.setMobile(need.getTel());
//				
//				Set<ChildMember> childMembers = member2.getChildMembers();
//				for(ChildMember childMember : childMembers) {
//					childMember.setMember(null);
//				}
			//}
			
			Member userInfo = memberDao.findByUsername(need.getTel());
			
			if (userInfo==null) {
				userInfo = new Member();

		        userInfo.setUsername(need.getTel());
		        userInfo.setNickname(need.getUserName());
		        userInfo.setMobile(need.getTel());
		        userInfo.setOpenId("-1");

	            userInfo.setPassword(DigestUtils.md5Hex("123456"));
	            userInfo.setEmail("afeijackcool@163.com");
	            userInfo.setPoint(BigDecimal.ZERO);
	            userInfo.setBalance(BigDecimal.ZERO);
	            userInfo.setAmount(BigDecimal.ZERO);
	            userInfo.setIsEnabled(true);
	            userInfo.setIsLocked(false);
	            userInfo.setLoginFailureCount(0);
	            userInfo.setLockedDate(null);
	            userInfo.setRegisterIp("");
	            userInfo.setLoginIp("");
	            userInfo.setLoginDate(new Date());
	            userInfo.setLoginPluginId(null);
	            userInfo.setLockKey(null);
	            userInfo.setSafeKey(null);
	            userInfo.setMemberRank(memberRankDao.findDefault());
	            userInfo.setCart(null);
	            userInfo.setOrders(null);
	            userInfo.setPaymentLogs(null);
	            userInfo.setDepositLogs(null);
	            userInfo.setCouponCodes(null);
	            userInfo.setReceivers(null);
	            userInfo.setReviews(null);
	            userInfo.setConsultations(null);
	            userInfo.setFavoriteGoods(null);
	            userInfo.setProductNotifies(null);
	            userInfo.setInMessages(null);
	            userInfo.setOutMessages(null);
	            userInfo.setPointLogs(null);

	            memberDao.persist(userInfo);
			}
			need2.setMember(userInfo);
			
		}
		//根据member和企业查询地址
		Member member = need2.getMember();
		Receiver receiver = receiverDao.find(member, need2.getSupplier());
        if(receiver == null) {//如果地址不存在就添加地址
            receiver = new Receiver();
            receiver.setConsignee(need2.getUserName());
    		receiver.setPhone(need2.getTel());
    		receiver.setAddress(need2.getAddress());
    		receiver.setMember(member);
    		receiver.setIsDefault(false);
    		receiver.setArea(need2.getArea());
    		receiver.setAreaName(need2.getArea().getFullName());
    		receiver.setZipCode("200000");
    		receiver.setSupplier(need2.getSupplier());
    		receiver.setType(Receiver.Type.backstage);
    		receiverDao.persist(receiver);
        }else {//否则就修改地址
            receiver.setConsignee(need2.getUserName());
    		receiver.setPhone(need2.getTel());
    		receiver.setAddress(need2.getAddress());
    		receiver.setMember(member);
    		receiver.setArea(need2.getArea());
    		receiver.setAreaName(need2.getArea().getFullName());
    		receiverDao.merge(receiver);
        }
		return need;
	}

	@Override
	public Page<Need> findPage(Pageable pageable, Supplier supplier, SupplierSupplier supplyRelation, Type type,
			Need.NeedStatus needStatus , Need.Status status,ShopType shopType) {
		return needDao.findPage(pageable, supplier, supplyRelation, type, needStatus , status,shopType);
	}

	@Override
	public boolean telExists(String tel, Supplier supplier) {
		return needDao.telExists(tel, supplier);
	}
	
	@Override
	public Need findNeedByMemberSupplier(Supplier supplier, Member member) {
		return needDao.findNeedByMemberSupplier(supplier, member);
	}

	@Override
	public Need update(Need entity) {
		return super.update(entity);
	}

	@Override
	public Need findByTelSupplier(String tel, Supplier supplier) {
		return needDao.findByTelSupplier(tel, supplier);
	}

	@Override
	public Page<Need> findPage(AssChildMember assChildMember, Pageable pageable) {
		return needDao.findPage(assChildMember, pageable);
	}

	@Override
	public Need updateIndividualCustomers(Need need) {
		if(null == need.getId()) {
			return null;
		}
		Need pNeed = needDao.find(need.getId());
		
		String tel = pNeed.getTel();
		pNeed.setName(need.getName());
		pNeed.setArea(need.getArea());
		pNeed.setAddress(need.getAddress());
		pNeed.setUserName(need.getUserName());
		pNeed.setTel(need.getTel());
		pNeed.setDescription(need.getDescription());
		
		// 新增属性
		pNeed.setClientNum(need.getClientNum());
		pNeed.setContacts(need.getContacts());
		pNeed.setContactsTel(need.getContactsTel());
		pNeed.setEmail(need.getEmail());
		pNeed.setAccountName(need.getAccountName());
		pNeed.setInvoice(need.getInvoice());
		pNeed.setBank(need.getBank());
		pNeed.setBankAccountNum(need.getBankAccountNum());
		pNeed.setIdentifier(need.getIdentifier());

		if(!need.getTel().equals(tel)) {

			Member userInfo = memberDao.findByUsername(need.getTel());
			
			if (userInfo==null) {
				userInfo = new Member();

		        userInfo.setUsername(need.getTel());
		        userInfo.setNickname(need.getUserName());
		        userInfo.setMobile(need.getTel());
		        userInfo.setOpenId("-1");

	            userInfo.setPassword(DigestUtils.md5Hex("123456"));
	            userInfo.setEmail("afeijackcool@163.com");
	            userInfo.setPoint(BigDecimal.ZERO);
	            userInfo.setBalance(BigDecimal.ZERO);
	            userInfo.setAmount(BigDecimal.ZERO);
	            userInfo.setIsEnabled(true);
	            userInfo.setIsLocked(false);
	            userInfo.setLoginFailureCount(0);
	            userInfo.setLockedDate(null);
	            userInfo.setRegisterIp("");
	            userInfo.setLoginIp("");
	            userInfo.setLoginDate(new Date());
	            userInfo.setLoginPluginId(null);
	            userInfo.setLockKey(null);
	            userInfo.setSafeKey(null);
	            userInfo.setMemberRank(memberRankDao.findDefault());
	            userInfo.setCart(null);
	            userInfo.setOrders(null);
	            userInfo.setPaymentLogs(null);
	            userInfo.setDepositLogs(null);
	            userInfo.setCouponCodes(null);
	            userInfo.setReceivers(null);
	            userInfo.setReviews(null);
	            userInfo.setConsultations(null);
	            userInfo.setFavoriteGoods(null);
	            userInfo.setProductNotifies(null);
	            userInfo.setInMessages(null);
	            userInfo.setOutMessages(null);
	            userInfo.setPointLogs(null);

	            memberDao.persist(userInfo);
			}
			pNeed.setMember(userInfo);
		}
		return need;
	}

	@Override
	public List<Need> findList(AssChildMember assChildMember) {
		return needDao.findList(assChildMember);
	}

	@Transactional
	@Override
	public Need saveDirectStore(Need need, String shopAssistantName,
			String shopAssistantTel) {
		try {
			Member userInfo = memberDao.findByUsername(need.getTel());
	    	Admin admin = adminDao.find(need.getSupplier(), true);
	        if (userInfo == null) {
	        	userInfo = new Member();

		        userInfo.setUsername(need.getTel());
		        userInfo.setNickname(need.getUserName());
		        userInfo.setMobile(need.getTel());
		        userInfo.setOpenId("-1");
	            userInfo.setPassword(DigestUtils.md5Hex("123456"));
	            userInfo.setEmail("afeijackcool@163.com");
	            userInfo.setPoint(BigDecimal.ZERO);
	            userInfo.setBalance(BigDecimal.ZERO);
	            userInfo.setAmount(BigDecimal.ZERO);
	            userInfo.setIsEnabled(true);
	            userInfo.setIsLocked(false);
	            userInfo.setLoginFailureCount(0);
	            userInfo.setLockedDate(null);
	            userInfo.setRegisterIp("");
	            userInfo.setLoginIp("");
	            userInfo.setLoginDate(new Date());
	            userInfo.setLoginPluginId(null);
	            userInfo.setLockKey(null);
	            userInfo.setSafeKey(null);
	            userInfo.setMemberRank(memberRankDao.findDefault());
	            userInfo.setCart(null);
	            userInfo.setOrders(null);
	            userInfo.setPaymentLogs(null);
	            userInfo.setDepositLogs(null);
	            userInfo.setCouponCodes(null);
	            userInfo.setReceivers(null);
	            userInfo.setReviews(null);
	            userInfo.setConsultations(null);
	            userInfo.setFavoriteGoods(null);
	            userInfo.setProductNotifies(null);
	            userInfo.setInMessages(null);
	            userInfo.setOutMessages(null);
	            userInfo.setPointLogs(null);
	            userInfo.setAdmin(admin);

	            memberDao.persist(userInfo);
	        }
	        
	        if(userInfo.getAdmin() == null) {
	        	userInfo.setAdmin(admin);
	        }
	        
	        
	        Shop shop = new Shop();
			shop.setName(need.getName());
			shop.setUserName(need.getUserName());
			shop.setShopType(need.getShopType());
			shop.setArea(need.getArea());
			shop.setAddress(need.getAddress());
			shop.setReceiverTel(need.getReceiverTel());
			shop.setMember(userInfo);

	        need.setMember(userInfo);
	      
			needDao.persist(need);
			
			Set<Need> needs =  new HashSet<Need>();
			needs.add(need);
			shop.setNeeds(needs);
			
			shopDao.persist(shop);
			
			if(StringUtils.isEmpty(shopAssistantTel) && StringUtils.isNotEmpty(shopAssistantName)) {
				MemberMember memberb = null;
            	do {
            		String opt = RandomStringUtils.randomNumeric(10);
            		shopAssistantTel = "2" + opt;
            		memberb = memberMemberDao.findByTel(userInfo, shopAssistantTel);
    			} while (memberb != null);
			}
			
			if(StringUtils.isNotEmpty(shopAssistantTel) && StringUtils.isNotEmpty(shopAssistantName)) {
				//店员
				Member member = memberDao.findByUsername(shopAssistantTel);
				
				if(member == null) {
					member = new Member();

					member.setUsername(shopAssistantTel);
					member.setNickname(shopAssistantName);
					member.setMobile(shopAssistantTel);
					member.setOpenId("-1");
					member.setPassword(DigestUtils.md5Hex("123456"));
					member.setEmail("afeijackcool@163.com");
					member.setPoint(BigDecimal.ZERO);
					member.setBalance(BigDecimal.ZERO);
					member.setAmount(BigDecimal.ZERO);
					member.setIsEnabled(true);
					member.setIsLocked(false);
					member.setLoginFailureCount(0);
					member.setLockedDate(null);
					member.setRegisterIp("");
					member.setLoginIp("");
					member.setLoginDate(new Date());
					member.setLoginPluginId(null);
					member.setLockKey(null);
					member.setSafeKey(null);
					member.setMemberRank(memberRankDao.findDefault());
					member.setCart(null);
					member.setOrders(null);
					member.setPaymentLogs(null);
					member.setDepositLogs(null);
					member.setCouponCodes(null);
					member.setReceivers(null);
					member.setReviews(null);
					member.setConsultations(null);
					member.setFavoriteGoods(null);
					member.setProductNotifies(null);
					member.setInMessages(null);
					member.setOutMessages(null);
					member.setPointLogs(null);

		            memberDao.persist(member);
				}
				
				//判断托管账号是否存在
				//boolean bool = memberMemberDao.telExists(userInfo, member);
				MemberMember memberMember = memberMemberDao.find(userInfo, member);
				if(memberMember == null) {
					memberMember = new MemberMember();
					memberMember.setMember(userInfo);
					memberMember.setByMember(member);
					memberMember.setName(shopAssistantName);
					memberMemberDao.persist(memberMember);
				}
				//创建托管关系
				HostingShop hostingShop=new HostingShop();
				hostingShop.setMember(userInfo);
				hostingShop.setByMember(member);
				hostingShop.setShop(shop);
				hostingShop.setMemberMember(memberMember);
				hostingShopDao.persist(hostingShop);
			}
	        
	        //根据member和企业查询地址
	        Receiver receiver = receiverDao.find(userInfo, need.getSupplier());
	        if(receiver == null) {//地址不存在就添加地址
	            receiver = new Receiver();
	            receiver.setConsignee(need.getUserName());
	    		receiver.setPhone(need.getReceiverTel());
	    		receiver.setAddress(need.getAddress());
	    		receiver.setMember(userInfo);
	    		receiver.setIsDefault(false);
	    		receiver.setArea(need.getArea());
	    		receiver.setAreaName(need.getArea().getFullName());
	    		receiver.setZipCode("200000");
	    		receiver.setSupplier(need.getSupplier());
	    		receiver.setType(Receiver.Type.backstage);
	    		receiverDao.persist(receiver);
	        }else {//否则就修改地址
	        	receiver.setConsignee(need.getUserName());
	    		receiver.setPhone(need.getReceiverTel());
	    		receiver.setAddress(need.getAddress());
	    		receiver.setMember(userInfo);
	    		receiver.setArea(need.getArea());
	    		receiver.setAreaName(need.getArea().getFullName());
	    		receiverDao.merge(receiver);
	        }
	        
			
			/*List<AssChildMember> assChildMembers = assChildMemberDao.findList(need);
			if(assChildMembers != null) {
				for(AssChildMember childMember : assChildMembers) {
					AssUpdateTips updateTips = new AssUpdateTips();
					updateTips.setAssChildMember(childMember);
					updateTips.setNeed(need);
					updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
					updateTips.setType(AssUpdateTips.Type.individualCustomers);
					assUpdateTipsDao.persist(updateTips);
				}
			}*/
	        return need;
		} catch (Exception e) {
			e.printStackTrace();
	        return null;

		}
    	
    
	}

	@Override
	public boolean existTel(Supplier supplier, String tel, ShopType shopType) {
		return needDao.existTel(supplier, tel, shopType);
	}

	@Override
	public Need updateDirectNeed(Need need, Admin admin , List<ShopAssistantDto> assistantDtos) {
		if(null == need.getId()) {
			return null;
		}
		Need need2 = needDao.find(need.getId());
		//String tel = need2.getTel();
		need2.setName(need.getName());
		need2.setArea(need.getArea());
		need2.setAddress(need.getAddress());
		need2.setUserName(need.getUserName());
		//need2.setTel(need.getTel());
		need2.setDescription(need.getDescription());
		need2.setClientNum(need.getClientNum());
		need2.setContacts(need.getContacts());
		need2.setContactsTel(need.getContactsTel());
		need2.setEmail(need.getEmail());
		need2.setAccountName(need.getAccountName());
		need2.setInvoice(need.getInvoice());
		need2.setBank(need.getBank());
		need2.setBankAccountNum(need.getBankAccountNum());
		need2.setIdentifier(need.getIdentifier());
		need2.setAdmin(admin);
		need2.setReceiverTel(need.getReceiverTel());
		
		List<Shop> shops = need2.getShops();
		for(Shop sp : shops) {
			sp.setName(need.getName());
			sp.setAddress(need.getAddress());
			sp.setArea(need.getArea());
			sp.setUserName(need.getUserName());
			sp.setReceiverTel(need.getReceiverTel());
			shopDao.merge(sp);
			
		}
		
		//删除托管关系
		List<HostingShop> hostingShops = hostingShopDao.findListByShop(need2);
		for(HostingShop hs : hostingShops) {
			hostingShopDao.remove(hs);
		}
		
		Member member = need2.getMember();
		//List<MemberMember> memberMembers = memberMemberDao.findList(member, null);
		
		for(ShopAssistantDto dto : assistantDtos) {
			String tel = dto.getShopAssistantTel();
			String name = dto.getShopAssistantName();
			MemberMember memberMember = memberMemberDao.findByTel(member, tel);
			if(memberMember != null) {
				memberMember.setName(name);
				memberMemberDao.merge(memberMember);
				
				HostingShop hostingShop = new HostingShop();
				hostingShop.setMember(member);
				hostingShop.setByMember(memberMember.getByMember());
				hostingShop.setMemberMember(memberMember);
				hostingShop.setShop(shops.get(0));
				hostingShopDao.persist(hostingShop);
			}else {
				Member byMember = memberDao.findByUsername(tel);
				if(byMember == null) {
					byMember = new Member();

					byMember.setUsername(tel);
					byMember.setNickname(dto.getShopAssistantName());
					byMember.setMobile(tel);
					byMember.setOpenId("-1");
					byMember.setPassword(DigestUtils.md5Hex("123456"));
					byMember.setEmail("afeijackcool@163.com");
					byMember.setPoint(BigDecimal.ZERO);
					byMember.setBalance(BigDecimal.ZERO);
					byMember.setAmount(BigDecimal.ZERO);
					byMember.setIsEnabled(true);
					byMember.setIsLocked(false);
					byMember.setLoginFailureCount(0);
					byMember.setLockedDate(null);
					byMember.setRegisterIp("");
					byMember.setLoginIp("");
					byMember.setLoginDate(new Date());
					byMember.setLoginPluginId(null);
					byMember.setLockKey(null);
					byMember.setSafeKey(null);
					byMember.setMemberRank(memberRankDao.findDefault());
					byMember.setCart(null);
					byMember.setOrders(null);
					byMember.setPaymentLogs(null);
					byMember.setDepositLogs(null);
					byMember.setCouponCodes(null);
					byMember.setReceivers(null);
					byMember.setReviews(null);
					byMember.setConsultations(null);
					byMember.setFavoriteGoods(null);
					byMember.setProductNotifies(null);
					byMember.setInMessages(null);
					byMember.setOutMessages(null);
					byMember.setPointLogs(null);

		            memberDao.persist(byMember);
				}
				
				MemberMember memberMember2 = new MemberMember();
				memberMember2.setMember(member);
				memberMember2.setByMember(byMember);
				memberMember2.setName(dto.getShopAssistantName());
				memberMemberDao.persist(memberMember2);
				//创建托管关系
				HostingShop hostingShop=new HostingShop();
				hostingShop.setMember(member);
				hostingShop.setByMember(byMember);
				hostingShop.setShop(shops.get(0));
				hostingShop.setMemberMember(memberMember2);
				hostingShopDao.persist(hostingShop);
			}
		}
		
		//添加更新提示
		List<AssUpdateTips> assUpdateTips = assUpdateTipsDao.findList(null, need2, AssUpdateTips.Type.individualCustomers);
		if(assUpdateTips.size()>0) {
			AssChildMember assChildMember = assChildMemberDao.find(admin);
			if(null != assChildMember) {
				for(AssUpdateTips updateTips : assUpdateTips) {
					updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
					updateTips.setAssChildMember(assChildMember);
					assUpdateTipsDao.merge(updateTips);
				}
			}else {
				for(AssUpdateTips updateTips : assUpdateTips) {
					assUpdateTipsDao.delete(updateTips);
				}
			}
			
		}else {
			List<AssChildMember> assChildMembers = assChildMemberDao.findList(need2);
			for(AssChildMember childMember : assChildMembers) {
				AssUpdateTips updateTips = new AssUpdateTips();
				updateTips.setAssChildMember(childMember);
				updateTips.setNeed(need2);
				updateTips.setWhetherUpdate(AssUpdateTips.WhetherUpdate.yes);
				updateTips.setType(AssUpdateTips.Type.individualCustomers);
				assUpdateTipsDao.persist(updateTips);
			}
		}

		//根据member和企业查询地址
		//Member member = need2.getMember();
		Receiver receiver = receiverDao.find(member, need2.getSupplier());
        if(receiver == null) {//如果地址不存在就添加地址
            receiver = new Receiver();
            receiver.setConsignee(need2.getUserName());
    		receiver.setPhone(need2.getReceiverTel());
    		receiver.setAddress(need2.getAddress());
    		receiver.setMember(member);
    		receiver.setIsDefault(false);
    		receiver.setArea(need2.getArea());
    		receiver.setAreaName(need2.getArea().getFullName());
    		receiver.setZipCode("200000");
    		receiver.setSupplier(need2.getSupplier());
    		receiver.setType(Receiver.Type.backstage);
    		receiverDao.persist(receiver);
        }else {//否则就修改地址
            receiver.setConsignee(need2.getUserName());
    		receiver.setPhone(need2.getReceiverTel());
    		receiver.setAddress(need2.getAddress());
    		receiver.setMember(member);
    		receiver.setArea(need2.getArea());
    		receiver.setAreaName(need2.getArea().getFullName());
    		receiverDao.merge(receiver);
        }
		return need;
	}

	@Override
	public void refreshNeed(Need need) {
		needDao.persist(need);
	}

	@Override
	public List<Need> findList(Supplier supplier, ShopType shopType) {
		return needDao.findList(supplier, shopType);
	}


	@Override
	public List<Need> findByList(Supplier supplier, Type type) {
		return needDao.findByList(supplier, type);
	}

	@Override
	public Need find(String clientNum , Supplier supplier) {
		return needDao.find(clientNum , supplier);
	}

	@Override
	public boolean nameExists(String name, Supplier supplier) {
		return needDao.nameExists(name, supplier);
	}

	@Override
	public Page<Need> findByPage(Pageable pageable, Need need,
			String searchName, Date startDate, Date endDate) {
		return needDao.findByPage(pageable, need, searchName, startDate, endDate);
	}

	@Override
	public boolean nameExists(String name, Supplier supplier, Need need) {
		return needDao.nameExists(name, supplier, need);
	}
	

}

