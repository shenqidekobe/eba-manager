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
import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;

import com.microBusiness.manage.ExcelView;
import com.microBusiness.manage.dao.OrderDao;
import com.microBusiness.manage.dto.CustomerReportDto;
import com.microBusiness.manage.dto.OrderReportDto;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.CustomerReportService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

@Service("customerReportServiceImpl")
public class CustomerReportServiceImpl implements CustomerReportService{
	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;

	@Override
	public List<CustomerReportDto> queryCustomerReportByDate(List<Integer> status, Date startDate, Date endDate,
			Supplier supplier) {
		List<CustomerReportDto> list=new ArrayList<>();
		List<CustomerReportDto> list2=new ArrayList<>();
		if (status.size() > 0) {
			list=orderDao.queryCustomersReportByDate(status, startDate, endDate, supplier);
			list2=orderDao.queryCustomersReportByDate2(status, startDate, endDate, supplier);
			list.addAll(list2);
			for (CustomerReportDto customerReportDto : list) {
				CustomerReportDto dto=orderDao.queryCustomersReportGoodByDate(status, startDate, endDate, supplier, customerReportDto.getNeedId(),customerReportDto.getSupplierId());
				customerReportDto.setGoodsNumber(dto.getGoodsNumber());
				customerReportDto.setOrderQuantity(dto.getOrderQuantity());
			}
		}
		return list;
	}

	@Override
	public ExcelView exportCustomerReport(List<Integer> status, Date startDate, Date endDate, Supplier supplier,
			HttpServletRequest request, HttpServletResponse response) {
		String fileName =  "客户统计报表-" + DateUtils.formatDateToString(startDate, DateformatEnum.yyyyMMdd2) + "~"
				+ DateUtils.formatDateToString(endDate, DateformatEnum.yyyyMMdd2) + ".xls";
		String[] title ={"序号","客户名称","订货单数","订货商品数","订货单金额"};
		List<CustomerReportDto> list=this.queryCustomerReportByDate(status, startDate, endDate, supplier);
		return this.splitOut(fileName, title, list);
	}
	
	public ExcelView splitOut(String fileName, String[] title, List<CustomerReportDto> list) {
		return new ExcelView(fileName, null, null, title, null, null, list, null) {
			@Override
			public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
					HttpServletResponse response) throws Exception {
				int rowNumber = 0;

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				// 填充色
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

				// 处理表头
				HSSFSheet sheet = workbook.createSheet();
				workbook.setSheetName(0, "客户报表统计");
				sheet.setDefaultColumnWidth(15);
				sheet.createRow(0).setHeightInPoints(100);

				String[] titles = this.getTitles();
				if (titles != null && titles.length > 0) {
					HSSFRow row1 = sheet.createRow(rowNumber);
					row1.setHeight((short) 400);
					for (int i = 0; i < titles.length; i++) {
						HSSFCell cell = row1.createCell(i);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(titles[i]);
					}
					rowNumber++;
				}

				List<CustomerReportDto> list = (List<CustomerReportDto>) this.getData();
				if (CollectionUtils.isNotEmpty(list)) {
					Integer orderSum = 0;
					Integer orderQuantitySum = 0;
					BigDecimal amountSum = new BigDecimal(0);

					for (CustomerReportDto dto : list) {
						// 创建行
						HSSFRow row = sheet.createRow(rowNumber);
						// 序号
						row.createCell(0).setCellValue(rowNumber);
						// 客户名称
						row.createCell(1).setCellValue(dto.getName());
						// 订货单数
						row.createCell(2).setCellValue(dto.getOrderNumber());
						// 订货商品数
						row.createCell(3).setCellValue(dto.getOrderQuantity());
						// 订货单金额
						row.createCell(4).setCellValue(dto.getAmount().doubleValue());

						rowNumber++;

						// 计算总计
						orderSum += dto.getOrderNumber();
						orderQuantitySum += dto.getOrderQuantity();
						amountSum = amountSum.add(dto.getAmount());
					}

					// 创建行
					HSSFRow row = sheet.createRow(rowNumber);
					// 总计
					row.createCell(1).setCellValue("总计");
					// 总单数
					row.createCell(2).setCellValue(orderSum);
					//  订货商品数
					row.createCell(3).setCellValue(orderQuantitySum);
					// 总金额
					row.createCell(4).setCellValue(amountSum.doubleValue());
					rowNumber++;
				}

				response.setContentType("application/force-download");
				if (StringUtils.isNotEmpty(this.getFileName())) {
					/**
					 * 原来文件名的处理方式，火狐浏览器导出文件名出现乱码 URLEncoder.encode(fileName,
					 * "UTF-8")
					 */
					String agent = request.getHeader("USER-AGENT").toLowerCase();
					if (agent.contains("firefox")) {
						response.setHeader("Content-disposition",
								"attachment; filename=" + new String(this.getFileName().getBytes(), "ISO8859-1"));
					} else {
						response.setHeader("Content-disposition",
								"attachment; filename=" + URLEncoder.encode(this.getFileName(), "UTF-8"));
					}

				} else {
					response.setHeader("Content-disposition", "attachment");
				}
			}
		};
	}
	
	
}
