package com.microBusiness.manage.service.ass.impl;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.dao.ass.AssCartItemDao;
import com.microBusiness.manage.dao.ass.AssListDao;
import com.microBusiness.manage.dao.ass.AssListLogDao;
import com.microBusiness.manage.dao.ass.AssListRelationDao;
import com.microBusiness.manage.dao.ass.AssListRemarksDao;
import com.microBusiness.manage.dto.AssListAndCustomerRelationDto;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.AssPurchaseListStatisticsDto;
import com.microBusiness.manage.dto.GoodNeedDto;
import com.microBusiness.manage.dto.OrderReportDto;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssCart;
import com.microBusiness.manage.entity.ass.AssCartItem;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssCustomerRelation.ShareType;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListItem;
import com.microBusiness.manage.entity.ass.AssListLog;
import com.microBusiness.manage.entity.ass.AssListRemarks;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.entity.ass.AssList.Status;
import com.microBusiness.manage.entity.ass.AssListMemberStatus;
import com.microBusiness.manage.entity.ass.AssListRelation;
import com.microBusiness.manage.entity.ass.AssShippingAddress;
import com.microBusiness.manage.service.ass.AssListService;
import com.microBusiness.manage.service.impl.BaseServiceImpl;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

@Service("assListServiceImpl")
public class AssListServiceImpl extends BaseServiceImpl<AssList, Long> implements AssListService {

	@Resource
	private AssListDao assListDao ;
	@Resource
	private AssListLogDao assListLogDao ;
	@Resource
	private AssListRelationDao assListRelationDao ;
	@Resource
	private NeedDao needDao;
	@Resource
	private AssCartItemDao assCartItemDao;
	@Resource
	private AssListRemarksDao assListRemarksDao ;
	
	@Override
	public Page<AssList> findPage(Status status, Pageable pageable,
			Supplier supplier, Date startDate, Date endDate, String searchName) {
		return assListDao.findPage(status,pageable,supplier,startDate,endDate,searchName);
	}

	@Override
	public Page<AssList> findPage(Pageable pageable,String searchValue,
			AssChildMember assChildMember, Member member) {
		Page<AssList> page=assListDao.findPage(pageable,searchValue, assChildMember, member);
		return page;
	}
	
	
	

	@Override
	public void updateStatus(AssList assList, AssListMemberStatus status,
			AssChildMember assChildMember) {
		
		AssListLog assListLog=new AssListLog();
		assListLog.setName(assChildMember.getNickName());
		assListLog.setAssList(assList);
		//分享
		if (status.equals(AssListMemberStatus.share)) {
			if (!assList.getStatus().equals(AssList.Status.end)) {
				assList.setStatus(AssList.Status.share);
			}
		}else if (status.equals(AssListMemberStatus.end)) {//终结
			assListLog.setType(AssListLog.Type.theEnd);
			assList.setStatus(AssList.Status.end);
			assListLogDao.persist(assListLog);
		}else if (status.equals(AssListMemberStatus.participate)) {//参与
			//加入关系
			if (assListRelationDao.findByChildMember(assChildMember, assList) == null) {
				AssListRelation assListRelation=new AssListRelation();
				assListRelation.setType(AssListRelation.Type.participant);
				assListRelation.setAssChildMember(assChildMember);
				assListRelation.setAssList(assList);
				assListRelation.setMember(assChildMember.getMember());
				assListRelationDao.persist(assListRelation);
			}
			assListLog.setType(AssListLog.Type.participate);
			assListLogDao.persist(assListLog);
		}else if (status.equals(AssListMemberStatus.noparticipate)) {//退出参与
			assListLog.setType(AssListLog.Type.dropOut);
			AssListRelation assListRelation=assListRelationDao.findByChildMember(assChildMember, assList);
			//删除关系
			assListRelationDao.remove(assListRelation);
			assListLogDao.persist(assListLog);
		}
		
	}

	@Override
	public AssList create(Set<AssCartItem> assCartItems,AssChildMember assChildMember,AssCustomerRelation assCustomerRelation, AssListRemarks assListRemarks, AssShippingAddress assShippingAddress) {
		Member member=assChildMember.getMember();
		AssList assList=new AssList();
		String sn=assList.generateSn();
		while (true) {
			if (assListDao.hasSn(sn)) {
				sn=assList.generateSn();
			}else {
				break;
			}
		}
		assList.setSn(sn);
//		if (assCustomerRelation.getSourceType().equals(AssCustomerRelation.SourceType.MOBILE)) {
//			assList.setType(AssList.Type.self);
//			//if (assChildMember.getAssShippingAddress() != null && assChildMember.getAssShippingAddress().size()>0) {
//				//AssShippingAddress assShippingAddress=assChildMember.getAssShippingAddress().get(0);
//				assList.setAssShippingAddress(assShippingAddress);
//				assList.setTel(assShippingAddress.getTel());
//				assList.setAddress(assShippingAddress.getArea().getFullName()+assShippingAddress.getAddress());
//				assList.setName(assShippingAddress.getName());
//				assList.setAddressName(assShippingAddress.getAddressName());
//			//}
//		}else { 
//			assList.setType(AssList.Type.supplier);
//			assList.setSupplier(assCustomerRelation.getSupplier());
//			if (member != null) {
//				Need need=needDao.findNeedByMemberSupplier(assCustomerRelation.getSupplier(), member);
//				assList.setNeed(need);
//				assList.setTel(need.getTel());
//				assList.setAddress(need.getArea().getFullName()+need.getAddress());
//				assList.setName(need.getUserName());
//				assList.setAddressName(need.getName());
//			}
//		}
		assList.setType(AssList.Type.self);
		if (assChildMember.getAssShippingAddress() != null && assChildMember.getAssShippingAddress().size()>0) {
			assList.setAssShippingAddress(assShippingAddress);
			assList.setTel(assShippingAddress.getTel());
			assList.setAddress(assShippingAddress.getArea().getFullName()+assShippingAddress.getAddress());
			assList.setName(assShippingAddress.getName());
			assList.setAddressName(assShippingAddress.getAddressName());
		}
		assList.setMember(member);
		assList.setStatus(AssList.Status.noshare);
		assList.setBuyType(AssList.BuyType.general);
		assList.setSinglePerson(assChildMember.getNickName());
		assList.setAssCustomerRelation(assCustomerRelation);
		assList.setClientName(assCustomerRelation.getClientName());
		assList.setAssChildMember(assChildMember);
		
		List<AssListItem> assListItems=new ArrayList<>();
		Integer quantity=0;
		for (AssCartItem assCartItem : assCartItems) {
			AssProduct assProduct=assCartItem.getAssproduct();
			AssListItem assListItem=new AssListItem();
			if (assProduct == null) {
				return null;
			}
			assListItem.setAssProduct(assProduct);
			assListItem.setAssList(assList);
			assListItem.setName(assProduct.getAssGoods().getName());
			assListItem.setQuantity(assCartItem.getQuantity());
			assListItem.setThumbnail(assProduct.getImage());
			assListItem.setSn(assProduct.getSn());
			assListItem.setSpecification(assProduct.getSpecification());
			assListItem.setUnit(assProduct.getAssGoods().getUnit());
			assListItems.add(assListItem);
			quantity+=assCartItem.getQuantity();
		}
		assList.setQuantity(quantity);
		assList.setAssListItems(assListItems);
		
		assListDao.persist(assList);
		
		if (assListRemarks != null && StringUtils.isNotBlank(assListRemarks.getDescription())) {
			assListRemarks.setAssList(assList);
			assListRemarks.setAssChildMember(assChildMember);
			assListRemarks.setName(assChildMember.getNickName());
			assListRemarksDao.persist(assListRemarks);
		}
		
		//发起关系
//		if (assListRelationDao.findByChildMember(assChildMember, assList) == null) {
		AssListRelation assListRelation=new AssListRelation();
		assListRelation.setType(AssListRelation.Type.sponsor);
		assListRelation.setAssChildMember(assChildMember);
		assListRelation.setAssList(assList);
		assListRelation.setMember(assChildMember.getMember());
		assListRelationDao.persist(assListRelation);
//		}
		
		//发起日志
		AssListLog assListLog=new AssListLog();
		assListLog.setName(assChildMember.getNickName());
		assListLog.setAssList(assList);
		assListLog.setType(AssListLog.Type.initiated);
		assListLogDao.persist(assListLog);
		
		//清除购物车
		for (AssCartItem assCartItem : assCartItems) {
			assCartItemDao.remove(assCartItem);
		}
		return assList;
	}

	@Override
	public void comeAgain(AssChildMember assChildMember, AssList assList) {
		AssCart assCart=assChildMember.getAssCart();
		Set<AssCartItem> assCartItems =assCart.getCartItems(assList.getAssCustomerRelation().getId());
		if (assCartItems!= null && assCartItems.size() > 0) {
			//清除购物车
			for (AssCartItem assCartItem : assCartItems) {
				assCartItemDao.remove(assCartItem);
			}
		}
		List<AssListItem> assListItems=assList.getAssListItems();
		for (AssListItem assListItem : assListItems) {
			if (assListItem.getAssProduct() != null && assListItem.getAssProduct().getAssGoods() != null && assListItem.getAssProduct().getAssGoods().isDeleted()) {
				continue;
			}
			AssCartItem assCartItem=new AssCartItem();
			assCartItem.setCart(assCart);
			assCartItem.setQuantity(assListItem.getQuantity());
			assCartItem.setAssproduct(assListItem.getAssProduct());
			assCartItem.setAssCustomerRelation(assList.getAssCustomerRelation());
			assCartItemDao.persist(assCartItem);
		}
	}

	@Override
	public Long pressMonthCountPurchaseList(AssChildMember assChildMember,
			Date startDate, Date endDate) {
		return assListDao.pressMonthCountPurchaseList(assChildMember, startDate, endDate);
	}

	@Override
	public Page<AssList> findPage(Pageable pageable, String searchValue,
			AssChildMember assChildMember, Date startDate, Date endDate) {
		return assListDao.findPage(pageable, searchValue, assChildMember, startDate, endDate);
	}

	@Override
	public List<AssListStatisticsDto> findByList(AssChildMember assChildMember) {
		return assListDao.findByList(assChildMember);
	}

	/**
	 * 采购清单看板
	 */
	@Override
	public Map<Integer , List<AssListStatisticsDto>> purchaseListKanban(
			AssChildMember assChildMember) {
		List<AssListStatisticsDto> assListStatisticsDtos = assListDao.findByList(assChildMember);
		List<AssListStatisticsDto> pAssListStatisticsDtos = new ArrayList<AssListStatisticsDto>();
		if(assListStatisticsDtos.size() > 0) {
			//起始年份
			Integer startYear = assListStatisticsDtos.get(0).getYears();
			//起始月份
			Integer startMonth = assListStatisticsDtos.get(0).getMonths();
			//起始日期
			Date startDate = DateUtils.specifyMonthStartTime(startYear, startMonth);
			//获取两个时间段内所有的月份
			List<Date> list = DateUtils.getDateList(startDate, new Date());
			
			for(Date date : list) {
				AssListStatisticsDto assListStatisticsDto = new AssListStatisticsDto();
				int year = DateUtils.getSpecifiedDateByYear(date);
				int month = DateUtils.getSpecifiedDateByMonth(date);
				assListStatisticsDto.setYears(year);
				assListStatisticsDto.setMonths(month);
				pAssListStatisticsDtos.add(assListStatisticsDto);
			}
			for(AssListStatisticsDto pdto : pAssListStatisticsDtos) {
				for(AssListStatisticsDto dto : assListStatisticsDtos) {
					if(pdto.getYears().equals(dto.getYears()) && pdto.getMonths().equals(dto.getMonths())) {
						pdto.setCounts(dto.getCounts());
					}
				}
				if(pdto.getCounts() == null) {
					pdto.setCounts(0);
				}
			}
			
		}
		
		//按照年份来分组
		Map<Integer , List<AssListStatisticsDto>> map = new HashMap<Integer, List<AssListStatisticsDto>>();
		for(AssListStatisticsDto dto : pAssListStatisticsDtos) {
			boolean bool = map.containsKey(dto.getYears());
			if(bool) {
				List<AssListStatisticsDto> assListDto = map.get(dto.getYears());
				assListDto.add(dto);
			}else {
				List<AssListStatisticsDto> assListStatisticsDtos2 = new ArrayList<AssListStatisticsDto>();
				assListStatisticsDtos2.add(dto);
				map.put(dto.getYears(), assListStatisticsDtos2);
			}
		}
		return map;
	}

	@Override
	public List<AssPurchaseListStatisticsDto> assListStatistics(Date startDate,
			Date endDate, AssChildMember assChildMember, ShareType shareType) {
		List<AssPurchaseListStatisticsDto> list = assListDao.assListStatistics(startDate, endDate, assChildMember, shareType);
		
		Map<String, AssPurchaseListStatisticsDto> map = new HashMap<String, AssPurchaseListStatisticsDto>();
		for(AssPurchaseListStatisticsDto dto : list) {
			map.put(dto.getCreateDate(), dto);
		}
		List<AssPurchaseListStatisticsDto> listStatisticsDtos = new ArrayList<AssPurchaseListStatisticsDto>();
		// 两个时间差 天数
		/*int days = 0;
		Date now = DateUtils.currentEndTime();
		if (endDate.getTime() > now.getTime()) {
			days = DateUtils.daysBetween(startDate, now);
		} else {
			days = DateUtils.daysBetween(startDate, endDate);
		}*/
		int days = DateUtils.daysBetween(startDate, endDate);
		for (int i = 0; i <= days; i++) {
			Date date = DateUtils.plusDays(startDate, i);
			String key = DateUtils.formatDateToString(date, DateformatEnum.MMdd2);
			AssPurchaseListStatisticsDto dto = map.get(key);
			if (dto == null) {
				dto = new AssPurchaseListStatisticsDto();
				dto.setCreateDate(key);
				dto.setNumber(0);
				
			}
			listStatisticsDtos.add(dto);
		}
		return listStatisticsDtos;
	}

	/**
	 * 采购清单统计详情(商品看板)
	 */
	@Override
	public Page<AssListAndCustomerRelationDto> assListStatisticsDetails(
			AssChildMember assChildMember, String startDate, String endDate,
			String searchValue, ShareType shareType,Pageable pageable) {
		return assListDao.assListStatisticsDetails(assChildMember, startDate, endDate, searchValue, shareType, pageable);
	}

	@Override
	public Page<AssListAndCustomerRelationDto> findPageBySupplier(Supplier supplier,  Date startDate, Date endDate,
			String searchValue, Pageable pageable) {
		// TODO Auto-generated method stub
		return assListDao.findPageBySupplier(supplier, startDate, endDate, searchValue, pageable);
	}

	@Override
	public AssListAndCustomerRelationDto findDetailsById(Long id) {
		// TODO Auto-generated method stub
		return assListDao.findDetailsById(id);
	}

	
	@Override
	public void selectedReport(Long[] ids, Supplier supplier, HttpServletRequest request,
			HttpServletResponse response) {
		String filename = "采购清单" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".xls";
		String[] title = new String[]{"清单编号" , "员工" , "下单人" , "主题" , "采购方" , "收货人" , "收货人联系方式" , "收货地址","商品名称","规格","单位","采购数量","状态","创建时间"};
		List<AssListAndCustomerRelationDto> list=assListDao.findDetailsListById(ids);
		
		this.exportExcel(title, list, filename,supplier, request, response);
	}
	
	@Override
	public void reportDownload(String searchValue, Date startDate, Date endDate, Supplier supplier,
			HttpServletRequest request, HttpServletResponse response) {
		String filename = "采购清单" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".xls";
		String[] title = new String[]{"清单编号" , "员工" , "下单人" , "主题" , "采购方" , "收货人" , "收货人联系方式" , "收货地址","商品名称","规格","单位","采购数量","状态","创建时间"};
		List<AssListAndCustomerRelationDto> list=assListDao.findListBySupplier(supplier, startDate, endDate, searchValue);
		
		this.exportExcel(title, list, filename,supplier, request, response);
		
	}

	private void exportExcel(String[] title, List<AssListAndCustomerRelationDto> list, String filename,
			Supplier supplier, HttpServletRequest request, HttpServletResponse response) {
		int rowNumber1 = 0;
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		//填充色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		cellStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);

		HSSFFont font = workbook.createFont();

		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		cellStyle.setFont(font);

		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		//处理表头
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "采购清单");
		sheet.setDefaultColumnWidth(15);
		sheet.createRow(0).setHeightInPoints(100);
		if (title != null && title.length > 0) {
			HSSFRow row1 = sheet.createRow(rowNumber1);
			row1.setHeight((short) 400);
			for (int i = 0 ; i < title.length; i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(title[i]);
			}
			rowNumber1++;
		}
		if(CollectionUtils.isNotEmpty(list)){
			for(AssListAndCustomerRelationDto dto : list) {
				AssList assList=dto.getAssList();
				List<AssListItem> assListItems=assList.getAssListItems();
				//判断商品是否是本企业分享出去的
				for (AssListItem assListItem : assListItems) {
					AssGoodDirectory assGoodDirectory=assListItem.getAssProduct().getAssGoods().getAssCustomerRelation().getAssGoodDirectory();
					if (assGoodDirectory != null) {
						if (supplier.equals(assGoodDirectory.getSupplier())) {
							//创建行
							HSSFRow row = sheet.createRow(rowNumber1);
							//清单编号
							row.createCell(0).setCellValue(assList.getSn());
							//员工
							row.createCell(1).setCellValue(dto.getAdminName());
							//下单人
							row.createCell(2).setCellValue(assList.getSinglePerson());
							//主题
							row.createCell(3).setCellValue(dto.getpTheme());
							//采购方
							row.createCell(4).setCellValue(assList.getAddressName());
							//收货人
							row.createCell(5).setCellValue(assList.getName());
							//收货人联系方式
							row.createCell(6).setCellValue(assList.getTel());
							//收货地址
							row.createCell(7).setCellValue(assList.getAddress());
							//商品名称
							row.createCell(8).setCellValue(assListItem.getName());
							//规格
							row.createCell(9).setCellValue(assListItem.getSpecification());
							//单位
							String unit="";
							if (AssGoods.Unit.one.equals(assListItem.getUnit())) {
								unit="个";
							}else if (AssGoods.Unit.pieces.equals(assListItem.getUnit())) {
								unit="件";
							}else if (AssGoods.Unit.bottle.equals(assListItem.getUnit())) {
								unit="瓶";
							}else if (AssGoods.Unit.box.equals(assListItem.getUnit())) {
								unit="箱";
							}else if (AssGoods.Unit.bag.equals(assListItem.getUnit())) {
								unit="袋";
							}else if (AssGoods.Unit.barrel.equals(assListItem.getUnit())) {
								unit="桶";
							}else if (AssGoods.Unit.opies.equals(assListItem.getUnit())) {
								unit="份";
							}else if (AssGoods.Unit.frame.equals(assListItem.getUnit())) {
								unit="盒";
							}else if (AssGoods.Unit.pack.equals(assListItem.getUnit())) {
								unit="包";
							}
							row.createCell(10).setCellValue(unit);
							//采购数量
							row.createCell(11).setCellValue(assListItem.getQuantity());
							//状态
							String status="";
							if (AssList.Status.share.equals(assList.getStatus())) {
								status="已分享";
							}else if (AssList.Status.noshare.equals(assList.getStatus())) {
								status="未分享";
							}else if (AssList.Status.end.equals(assList.getStatus())) {
								status="已终结";
							}
							row.createCell(12).setCellValue(status);
							//创建时间
							row.createCell(13).setCellValue(DateUtils.convertToString(assList.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
							
							rowNumber1++;
						}
					}
				}
			}
			
		}
		response.setContentType("application/force-download");
		if (StringUtils.isNotEmpty(filename)) {
			try {
				String agent = request.getHeader("USER-AGENT").toLowerCase();
				if(agent.contains("firefox")) {
					response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "ISO8859-1"));
				}else{
					response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			response.setHeader("Content-disposition", "attachment");
		}
		
		try  
        {  
			OutputStream stream = response.getOutputStream();
			workbook.write(stream);
			stream.close();
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
	}

}
