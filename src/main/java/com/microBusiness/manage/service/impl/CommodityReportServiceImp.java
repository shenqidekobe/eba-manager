package com.microBusiness.manage.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.noneDSA;
import org.springframework.stereotype.Service;

import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.dao.CommodityReportDao;
import com.microBusiness.manage.dto.CommodityReportDto;
import com.microBusiness.manage.dto.CommodityStatisticsDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.CommodityReportService;

@Service("commodityReportServiceImp")
public class CommodityReportServiceImp implements CommodityReportService {

	@Resource
	private CommodityReportDao commodityReportDao;

	@Override
	public List<CommodityReportDto> findBycommodityReport(Date startDate,
			Date endDate, Supplier supplier, List<Integer> status, ChildMember childMember) {
		List<CommodityReportDto> commodityReportDtos = new ArrayList<CommodityReportDto>();
		if(status.size() > 0) {
			List<CommodityReportDto> commodityReportDtos2 = new ArrayList<CommodityReportDto>();
			commodityReportDtos = commodityReportDao.findBycommodityReport(startDate, endDate, supplier,status, childMember);
			commodityReportDtos2=commodityReportDao.findBycommodityReport2(startDate, endDate, supplier,status, childMember);
			if (commodityReportDtos2.size() > 0) {
				Map<String, CommodityReportDto> map=new HashMap<>();
				for (CommodityReportDto commodityReportDto : commodityReportDtos2) {
					map.put(commodityReportDto.getProductId(), commodityReportDto);
				}
				for (String key : map.keySet()) {
					CommodityReportDto commodityReportDto2=map.get(key);
					boolean has=false;
					for (CommodityReportDto commodityReportDto : commodityReportDtos) {
						if (commodityReportDto.getProductId().equals(key)) {
							commodityReportDto.setCustomersNum(commodityReportDto.getCustomersNum()+commodityReportDto2.getCustomersNum());
							commodityReportDto.setOrderNumber(commodityReportDto.getOrderNumber()+commodityReportDto2.getOrderNumber());
							commodityReportDto.setOrderQuantity(commodityReportDto.getOrderQuantity()+commodityReportDto2.getOrderQuantity());
							commodityReportDto.setOrderAmount(commodityReportDto.getOrderAmount().add(commodityReportDto2.getOrderAmount()));
							has=true;
						}
					}
					if (!has) {
						commodityReportDtos.add(commodityReportDto2);
					}
				}
			}
		}
		return commodityReportDtos;
	}

	@Override
	public CommodityStatisticsDto findByCommodityStatistics(Date startDate,
			Date endDate, Supplier supplier, List<Integer> status, ChildMember childMember) {
		CommodityStatisticsDto commodityStatisticsDto = new CommodityStatisticsDto();
		if(status.size() > 0) {
			commodityStatisticsDto = commodityReportDao.findByCommodityStatistics(startDate, endDate,
					supplier, status, childMember);
		}
		return commodityStatisticsDto;
	}

	@Override
	public List<CommodityReportDto> purchaseOrderData(Date startDate,
			Date endDate, Supplier supplier, List<Integer> status) {
		List<CommodityReportDto> commodityReportDtos = new ArrayList<CommodityReportDto>();
		if(status.size() > 0) {
			commodityReportDtos = commodityReportDao.purchaseOrderData(startDate, endDate, supplier, status);
		}
		return commodityReportDtos;
	}

	@Override
	public List<CommodityReportDto> categoryQuery(Date startDate, Date endDate,
			Supplier supplier, List<Integer> status, ChildMember childMember) {
		List<CommodityReportDto> commodityReportDtos = new ArrayList<CommodityReportDto>();
		if(status.size() > 0) {
			commodityReportDtos = commodityReportDao.categoryQuery(startDate, endDate, supplier, status, childMember);
			List<CommodityReportDto> commodityReportDtos2 = commodityReportDao.categoryQuery2(startDate, endDate, 
					supplier, status, childMember);
			if (commodityReportDtos2.size()>0) {
				Map<String, CommodityReportDto> map=new HashMap<>();
				for (CommodityReportDto commodityReportDto : commodityReportDtos2) {
					map.put(commodityReportDto.getCategoryId(), commodityReportDto);
				}
				for (String key : map.keySet()) {
					CommodityReportDto commodityReportDto2=map.get(key);
					boolean has=false;
					for (CommodityReportDto commodityReportDto : commodityReportDtos) {
						if (commodityReportDto.getCategoryId().equals(key)) {
							commodityReportDto.setCustomersNum(commodityReportDto.getCustomersNum()+commodityReportDto2.getCustomersNum());
							commodityReportDto.setOrderNumber(commodityReportDto.getOrderNumber()+commodityReportDto2.getOrderNumber());
							commodityReportDto.setOrderQuantity(commodityReportDto.getOrderQuantity()+commodityReportDto2.getOrderQuantity());
							commodityReportDto.setGoodAmount(commodityReportDto.getGoodAmount().add(commodityReportDto2.getGoodAmount()));
							has=true;
						}
					}
					if (!has) {
						commodityReportDtos.add(commodityReportDto2);
					}
				}
			}
		}
		return commodityReportDtos;
	}

    @Override
    public CommodityStatisticsDto purchasingCommodityStatistics(Date startDate, Date endDate,
            Supplier supplier, List<Integer> status) {
    	CommodityStatisticsDto commodityStatisticsDto = new CommodityStatisticsDto();
    	if(status.size() > 0) {
    		commodityStatisticsDto = commodityReportDao.purchasingCommodityStatistics(startDate, endDate, supplier, status);
    	}
        return commodityStatisticsDto;
    }

    /**
     * 订货单导出
     */
	@Override
	public ExcelView orderExport(Date startDate, Date endDate,
			Supplier supplier, List<Integer> status,String exportType) {
		String fileName = supplier.getName() + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-订货单商品统计报表.xls";
        
        String[] titles = new String[]{"序号" , "商品名称" , "订货单数" , "订货商品数" , "订货客户数" , "订货金额"};
        ExcelView excelView = null;
        if(exportType.equalsIgnoreCase("good")) {
        	List<CommodityReportDto> commodityReportDtos = commodityReportDao.findBycommodityReport(startDate, endDate, supplier,status, null);
            
            CommodityStatisticsDto commodityStatisticsDto =  commodityReportDao.findByCommodityStatistics(startDate, endDate,
            		supplier, status, null);
            Map<Object, Object> map = new HashMap<Object, Object>();
            map.put("commodityReportDtos", commodityReportDtos);
            map.put("commodityStatisticsDto", commodityStatisticsDto);
            excelView = this.downOrder(fileName, map, titles);
        }else {
        	List<CommodityReportDto> commodityReportDtos = commodityReportDao.categoryQuery(startDate, endDate,
        			supplier, status, null);
        	excelView = this.downOrder(fileName, commodityReportDtos, titles);
        }
        
		return excelView;
	}

	@Override
	public ExcelView downOrder(String fileName, final Map<Object, Object> data,
			String[] titles) {
		return new ExcelView(fileName, titles , data){
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
				//创建表头
				HSSFSheet sheet;
				if (StringUtils.isNotEmpty(this.getSheetName())) {
					sheet = workbook.createSheet(this.getSheetName());
				} else {
					sheet = workbook.createSheet();
				}

				int rowNumber = 0;
				String[] titles = this.getTitles() ;

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
				if (titles != null && titles.length > 0) {
					HSSFRow header = sheet.createRow(rowNumber);
					header.setHeight((short) 400);
					for (int i = 0 , len = titles.length; i < len; i++) {
						HSSFCell cell = header.createCell(i);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(titles[i]);

					}
					rowNumber++;
				}
				List<CommodityReportDto> commodityReportDtos = (List<CommodityReportDto>) data.get("commodityReportDtos");
				CommodityStatisticsDto commodityStatisticsDto = (CommodityStatisticsDto) data.get("commodityStatisticsDto");
				if(CollectionUtils.isNotEmpty(commodityReportDtos)){
					Integer goodTotal=0;
					BigDecimal totalAmount=BigDecimal.ZERO;
		            for(CommodityReportDto commodityReportDto : commodityReportDtos){
		                HSSFRow row = sheet.createRow(rowNumber);
		                //序号
		                row.createCell(0).setCellValue(rowNumber);
		                //商品名称
		                if(commodityReportDto.getSpecification().equalsIgnoreCase("[]")) {
		                	row.createCell(1).setCellValue(commodityReportDto.getName());
		                }else {
		                	row.createCell(1).setCellValue(commodityReportDto.getName()+" "+commodityReportDto.getSpecification());
		                }
		                //采购单数
		                row.createCell(2).setCellValue(commodityReportDto.getOrderNumber());
		                //采购商品数
		                row.createCell(3).setCellValue(commodityReportDto.getOrderQuantity());
		                //采购供应商数
		                row.createCell(4).setCellValue(commodityReportDto.getCustomersNum());
		                //采购金额
		                row.createCell(5).setCellValue(commodityReportDto.getOrderAmount().doubleValue());
		                rowNumber++;
		                goodTotal+=commodityReportDto.getOrderQuantity();
		                totalAmount=totalAmount.add(commodityReportDto.getOrderAmount());
		            }
		            if(null != commodityStatisticsDto) {
		                HSSFRow row = sheet.createRow(rowNumber);
		                //序号
		                row.createCell(0).setCellValue("");
		                //商品名称
		                row.createCell(1).setCellValue("总计");
		                //订货单数
		                row.createCell(2).setCellValue(commodityStatisticsDto.getOrderTotal());
		                //订货商品数s
		                row.createCell(3).setCellValue(goodTotal);
		                //订货客户数
		                row.createCell(4).setCellValue(commodityStatisticsDto.getNumberOfCustomers());
		                //订货金额
		                row.createCell(5).setCellValue(totalAmount.doubleValue());
		            }
				}

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
					/**
					 * 原来文件名的处理方式，火狐浏览器导出文件名出现乱码
					 * URLEncoder.encode(fileName, "UTF-8")
					 */
					String agent = request.getHeader("USER-AGENT").toLowerCase();
					if(agent.contains("firefox")) {
						response.setHeader("Content-disposition", "attachment; filename=" + new String(this.getFileName().getBytes(), "ISO8859-1"));
					}else{
						response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(this.getFileName(), "UTF-8"));
					}
					
				} else {
					response.setHeader("Content-disposition", "attachment");
				}

			}
		};
	}

	/**
	 * 采购单导出
	 */
	@Override
	public ExcelView purchasedGoodsExported(Date startDate, Date endDate,
			Supplier supplier, List<Integer> status) {
		String[] titles = new String[]{"序号" , "商品名称" , "采购单数" , "采购商品数" , "采购供应商数" , "采购金额"};
		String fileName = supplier.getName() + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-采购商品统计报表.xls";
		List<CommodityReportDto> commodityReportDtos = commodityReportDao.purchaseOrderData(startDate, endDate, supplier, status);
        CommodityStatisticsDto commodityStatisticsDto = commodityReportDao.purchasingCommodityStatistics(startDate, endDate, supplier, status);
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("commodityReportDtos", commodityReportDtos);
        map.put("commodityStatisticsDto", commodityStatisticsDto);
		return this.downpurchasedGoods(fileName, map, titles);
	}

	@Override
	public ExcelView downpurchasedGoods(String fileName,
			final Map<Object, Object> data, String[] titles) {
		return new ExcelView(fileName, titles , data){
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
				//创建表头
				HSSFSheet sheet;
				if (StringUtils.isNotEmpty(this.getSheetName())) {
					sheet = workbook.createSheet(this.getSheetName());
				} else {
					sheet = workbook.createSheet();
				}

				int rowNumber = 0;
				String[] titles = this.getTitles() ;

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
				if (titles != null && titles.length > 0) {
					HSSFRow header = sheet.createRow(rowNumber);
					header.setHeight((short) 400);
					for (int i = 0 , len = titles.length; i < len; i++) {
						HSSFCell cell = header.createCell(i);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(titles[i]);

					}
					rowNumber++;
				}
				List<CommodityReportDto> commodityReportDtos = (List<CommodityReportDto>) data.get("commodityReportDtos");
				CommodityStatisticsDto commodityStatisticsDto = (CommodityStatisticsDto) data.get("commodityStatisticsDto");
				if(CollectionUtils.isNotEmpty(commodityReportDtos)){
	                for(CommodityReportDto commodityReportDto : commodityReportDtos){
	                    HSSFRow row = sheet.createRow(rowNumber);
	                    //序号
	                    row.createCell(0).setCellValue(rowNumber);
	                    //商品名称
	                    row.createCell(1).setCellValue(commodityReportDto.getName()+" "+commodityReportDto.getSpecification());
	                    //采购单数
	                    row.createCell(2).setCellValue(commodityReportDto.getOrderNumber());
	                    //采购商品数
	                    row.createCell(3).setCellValue(commodityReportDto.getOrderQuantity());
	                    //采购供应商数
	                    row.createCell(4).setCellValue(commodityReportDto.getSuppliersNum());
	                    //采购金额
	                    row.createCell(5).setCellValue(commodityReportDto.getOrderAmount().doubleValue());
	                    rowNumber++;
	                }
	                HSSFRow row = sheet.createRow(rowNumber);
	                //序号 
	                row.createCell(0).setCellValue("");
	                //商品名称
	                row.createCell(1).setCellValue("总计");
	                //采购单数
	                row.createCell(2).setCellValue(commodityStatisticsDto.getOrderTotal());
	                //采购商品数
	                row.createCell(3).setCellValue(Integer.parseInt(commodityStatisticsDto.getTotal()));
	                //采购供应商数
	                row.createCell(4).setCellValue(commodityStatisticsDto.getNumberOfSuppliers());
	                //采购金额
	                row.createCell(5).setCellValue(commodityStatisticsDto.getTotalAmount().doubleValue());
	        }

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
					/**
					 * 原来文件名的处理方式，火狐浏览器导出文件名出现乱码
					 * URLEncoder.encode(fileName, "UTF-8")
					 */
					String agent = request.getHeader("USER-AGENT").toLowerCase();
					if(agent.contains("firefox")) {
						response.setHeader("Content-disposition", "attachment; filename=" + new String(this.getFileName().getBytes(), "ISO8859-1"));
					}else{
						response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(this.getFileName(), "UTF-8"));
					}
					
				} else {
					response.setHeader("Content-disposition", "attachment");
				}

			}
		};
	}

	@Override
	public ExcelView downOrder(String fileName,
			List<CommodityReportDto> commodityReportDtos, String[] titles) {
		return new ExcelView(fileName, null, null, titles , null, null, commodityReportDtos, null){
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
				//创建表头
				HSSFSheet sheet;
				if (StringUtils.isNotEmpty(this.getSheetName())) {
					sheet = workbook.createSheet(this.getSheetName());
				} else {
					sheet = workbook.createSheet();
				}

				int rowNumber = 0;
				String[] titles = this.getTitles() ;

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
				if (titles != null && titles.length > 0) {
					HSSFRow header = sheet.createRow(rowNumber);
					header.setHeight((short) 400);
					for (int i = 0 , len = titles.length; i < len; i++) {
						HSSFCell cell = header.createCell(i);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(titles[i]);

					}
					rowNumber++;
				}

				//处理内容
				List<CommodityReportDto> datas = (List<CommodityReportDto>) this.getData();

				if(CollectionUtils.isNotEmpty(datas)){
					Integer goodSum = 0;
					BigDecimal amountSum = new BigDecimal(0);
	                for(CommodityReportDto commodityReportDto : datas){
	                    HSSFRow row = sheet.createRow(rowNumber);
	                    //序号
	                    row.createCell(0).setCellValue(rowNumber);
	                    //商品分类
	                    row.createCell(1).setCellValue(commodityReportDto.getName());
	                    //订货单数
	                    row.createCell(2).setCellValue(commodityReportDto.getOrderNumber());
	                    //订货商品数
	                    row.createCell(3).setCellValue(commodityReportDto.getOrderQuantity());
	                    //订货客户数
	                    row.createCell(4).setCellValue(commodityReportDto.getCustomersNum());
	                    //商品金额
	                    row.createCell(5).setCellValue(commodityReportDto.getGoodAmount().doubleValue());
	                    
	                 // 计算总计
	                    goodSum += commodityReportDto.getOrderQuantity();
						amountSum = amountSum.add(commodityReportDto.getGoodAmount());
	                    rowNumber++;
	                }
	                HSSFRow row = sheet.createRow(rowNumber);
	                //序号
	                row.createCell(0).setCellValue("");
	                //商品名称
	                row.createCell(1).setCellValue("总计");
	                //订货单数 
	                row.createCell(2).setCellValue("");
	                //订货商品数
	                row.createCell(3).setCellValue(goodSum);
	                //采购供应商数
	                row.createCell(4).setCellValue("");
	                //采购金额
	                row.createCell(5).setCellValue(amountSum.doubleValue());
				}

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
					/**
					 * 原来文件名的处理方式，火狐浏览器导出文件名出现乱码
					 * URLEncoder.encode(fileName, "UTF-8")
					 */
					String agent = request.getHeader("USER-AGENT").toLowerCase();
					if(agent.contains("firefox")) {
						response.setHeader("Content-disposition", "attachment; filename=" + new String(this.getFileName().getBytes(), "ISO8859-1"));
					}else{
						response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(this.getFileName(), "UTF-8"));
					}
					
				} else {
					response.setHeader("Content-disposition", "attachment");
				}

			}
		};
	}


}
