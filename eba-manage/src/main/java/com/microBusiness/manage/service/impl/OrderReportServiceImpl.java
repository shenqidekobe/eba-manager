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
import com.microBusiness.manage.dto.OrderReportDto;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.OrderReportService;
import com.microBusiness.manage.util.DateUtils;
import com.microBusiness.manage.util.DateformatEnum;

@Service("orderReportServiceImpl")
public class OrderReportServiceImpl implements OrderReportService {

	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;

	@Override
	public List<OrderReportDto> queryOrderReportByDate(List<Integer> status, Date startDate, Date endDate,
			Supplier supplier, String queyType, ChildMember childMember) {
		List<OrderReportDto> list = new ArrayList<>();
		if (status.size() > 0) {
			// 判断是订货单报表还是采购单报表
			if (StringUtils.equalsIgnoreCase(queyType, "orderForm")) {
				list = orderDao.queryOrderReportByDate(status, startDate, endDate, supplier, childMember);
	
			} else if (StringUtils.equalsIgnoreCase(queyType, "purchaseForm")) {
				list = orderDao.queryPurchaseFormByDate(status, startDate, endDate, supplier);
			}
		}
		Map<String, OrderReportDto> map = new HashMap<>();
		for (OrderReportDto orderReportDto : list) {
			map.put(orderReportDto.getReportDate(), orderReportDto);
		}
		List<OrderReportDto> orderReports = new ArrayList<>();
		// 两个时间差 天数
		int days = 0;
		Date now = DateUtils.currentEndTime();
		if (endDate.getTime() > now.getTime()) {
			days = DateUtils.daysBetween(startDate, now);
		} else {
			days = DateUtils.daysBetween(startDate, endDate);
		}
		for (int i = 0; i <= days; i++) {
			Date date = DateUtils.plusDays(startDate, i);
			String key = DateUtils.formatDateToString(date, DateformatEnum.yyyyMMdd2);
			OrderReportDto dto = map.get(key);
			if (dto == null) {
				dto = new OrderReportDto();
				dto.setReportDate(key);
				dto.setAmount(new BigDecimal(0));
				dto.setCustomersNumber(0);
				dto.setOrderNumber(0);
			}
			orderReports.add(dto);
		}
		
		//拆单后当前企业为供应商的订单统计合并
		if (status.size() > 0) {
			if (StringUtils.equalsIgnoreCase(queyType, "orderForm")) {
				List<OrderReportDto> list2 = orderDao.queryOrderReportByDate2(status, startDate, endDate, supplier);
				Map<String, OrderReportDto> map2 = new HashMap<>();
				for (OrderReportDto orderReportDto : list2) {
					map2.put(orderReportDto.getReportDate(), orderReportDto);
				}
				for (OrderReportDto orderReportDto : orderReports) {
					OrderReportDto orderReportDto2=map2.get(orderReportDto.getReportDate());
					if (orderReportDto2 != null) {
						orderReportDto.setAmount(orderReportDto.getAmount().add(orderReportDto2.getAmount()));
						orderReportDto.setCustomersNumber(orderReportDto.getCustomersNumber()+orderReportDto2.getCustomersNumber());
						orderReportDto.setOrderNumber(orderReportDto.getOrderNumber()+orderReportDto2.getOrderNumber());
					}
				}
			}
		}
		return orderReports;
	}

	@Override
	public Integer getCustomersNumber(List<Integer> status, Date startDate, Date endDate, Supplier supplier,
			String queyType) {
		Integer customersSum = 0;
		// 判断是订货单报表还是采购单报表
		if (status.size() > 0) {
			if (StringUtils.equalsIgnoreCase(queyType, "orderForm")) {
				customersSum = orderDao.getCustomersNumber(status, startDate, endDate, supplier);
	
			} else if (StringUtils.equalsIgnoreCase(queyType, "purchaseForm")) {
				customersSum = orderDao.getPurchaseCustomersNumber(status, startDate, endDate, supplier);
			}
		}
		return customersSum;
	}

	@Override
	public ExcelView exportOrderReport(List<Integer> status, Date startDate, Date endDate, Supplier supplier,
			String queyType, HttpServletRequest request, HttpServletResponse response) {
		String fileName = "";
		String[] title = null;
		if (StringUtils.equalsIgnoreCase(queyType, "orderForm")) {
			fileName = "订货单统计报表-" + DateUtils.formatDateToString(startDate, DateformatEnum.yyyyMMdd2) + "~"
					+ DateUtils.formatDateToString(endDate, DateformatEnum.yyyyMMdd2) + ".xls";
			title = new String[] { "序号", "日期", "订货单数", "订货客户数", "订货单金额" };
		} else if (StringUtils.equalsIgnoreCase(queyType, "purchaseForm")) {
			fileName = "采购单统计报表-" + DateUtils.formatDateToString(startDate, DateformatEnum.yyyyMMdd2) + "~"
					+ DateUtils.formatDateToString(endDate, DateformatEnum.yyyyMMdd2) + ".xls";
			title = new String[] { "序号", "日期", "购货单数", "购货客户数", "购货单金额" };
		}
		List<OrderReportDto> list = this.queryOrderReportByDate(status, startDate, endDate, supplier, queyType, null);
		Integer customersSum=this.getCustomersNumber(status, startDate, endDate, supplier, queyType);
		Map<String, Object> map=new HashMap<>();
		map.put("list", list);
		map.put("customersSum", customersSum);
		List<Map<String, Object>> lMaps=new ArrayList<>();
		lMaps.add(map);
		return this.splitOut(fileName, title, lMaps);

	}

	public ExcelView splitOut(String fileName, String[] title, List<Map<String, Object>> lMaps) {
		return new ExcelView(fileName, null, null, title, null, null, lMaps, null) {
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
				workbook.setSheetName(0, "订单报表统计");
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

				List<Map<String, Object>> lMaps = (List<Map<String, Object>>) this.getData();
				Map<String, Object> map=lMaps.get(0);
				List<OrderReportDto> list=(List<OrderReportDto>) map.get("list");
				Integer customersSum = (Integer) map.get("customersSum");
				if (CollectionUtils.isNotEmpty(list)) {
					Integer orderSum = 0;
					BigDecimal amountSum = new BigDecimal(0);

					for (OrderReportDto dto : list) {
						// 创建行
						HSSFRow row = sheet.createRow(rowNumber);
						// 序号
						row.createCell(0).setCellValue(rowNumber);
						// 日期
						row.createCell(1).setCellValue(dto.getReportDate());
						// 订货单数-购货单数
						row.createCell(2).setCellValue(dto.getOrderNumber());
						// 订货客户数-采购供应商
						row.createCell(3).setCellValue(dto.getCustomersNumber());
						// 订货单金额-采购单金额
						row.createCell(4).setCellValue(dto.getAmount().doubleValue());

						rowNumber++;

						// 计算总计
						orderSum += dto.getOrderNumber();
						amountSum = amountSum.add(dto.getAmount());
					}

					// 创建行
					HSSFRow row = sheet.createRow(rowNumber);
					// 总计
					row.createCell(1).setCellValue("总计");
					// 总单数
					row.createCell(2).setCellValue(orderSum);
					// 总客户数
					row.createCell(3).setCellValue(customersSum);
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
