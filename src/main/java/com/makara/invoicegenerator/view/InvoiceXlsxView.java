package com.makara.invoicegenerator.view;

import com.makara.invoicegenerator.models.entity.Invoice;
import com.makara.invoicegenerator.models.entity.ItemInvoice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import java.util.Map;

	@Component("invoice/view.xlsx")
	public class InvoiceXlsxView extends AbstractXlsxView {

	    @Override
	    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
	                                       HttpServletResponse response) throws Exception {

	        response.setHeader("Content-Disposition", "attachment; filename=\"invoice_delete.xlsx\"");
	        Invoice invoice = (Invoice) model.get("invoice");
	        Sheet sheet = workbook.createSheet();

	        MessageSourceAccessor mensajes = getMessageSourceAccessor();

	        // Стилі для таблиці
	        CellStyle theaderStyle = workbook.createCellStyle();
	        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
	        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
	        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
	        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
	        theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
	        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        CellStyle tbodyStyle = workbook.createCellStyle();
	        tbodyStyle.setBorderBottom(BorderStyle.THIN);
	        tbodyStyle.setBorderTop(BorderStyle.THIN);
	        tbodyStyle.setBorderRight(BorderStyle.THIN);
	        tbodyStyle.setBorderLeft(BorderStyle.THIN);

	        // **ДАНІ КЛІЄНТА** //
	        Row row = sheet.createRow(0);
	        Cell cell = row.createCell(0);

	        cell.setCellValue(mensajes.getMessage("text.customer.detail.title"));
	        row.getCell(0).setCellStyle(theaderStyle);

	        row = sheet.createRow(1);
	        cell = row.createCell(0);
	        cell.setCellValue(invoice.getClient().getFirstName().concat(" " + invoice.getClient().getLastName()));

	        row = sheet.createRow(2);
	        cell = row.createCell(0);
	        cell.setCellValue(invoice.getClient().getEmail());

	        // **ДАНІ РАХУНКУ** //
	        Row datosFactura = sheet.createRow(4);
	        datosFactura.createCell(0).setCellValue(mensajes.getMessage("text.invoice.view.invoiceData"));
	        datosFactura.getCell(0).setCellStyle(theaderStyle);

	        datosFactura = sheet.createRow(5);
	        datosFactura.createCell(0).setCellValue(mensajes.getMessage("text.customer.invoice.number") + ": " + invoice.getId());

	        datosFactura = sheet.createRow(6);
	        datosFactura.createCell(0).setCellValue(mensajes.getMessage("text.customer.invoice.description") + ": " + invoice.getDescription());

	        datosFactura = sheet.createRow(7);
	        datosFactura.createCell(0).setCellValue(mensajes.getMessage("text.customer.invoice.date") + ": " + invoice.getCreatedAt());

	        // **ДЕТАЛІ РАХУНКУ** //
	        Row header = sheet.createRow(9);
	        header.createCell(0).setCellValue(mensajes.getMessage("text.invoice.form.item.name"));
	        header.createCell(1).setCellValue(mensajes.getMessage("text.invoice.form.item.price"));
	        header.createCell(2).setCellValue(mensajes.getMessage("text.invoice.form.item.quantity"));
	        header.createCell(3).setCellValue(mensajes.getMessage("text.invoice.form.item.total"));

	        header.getCell(0).setCellStyle(theaderStyle);
	        header.getCell(1).setCellStyle(theaderStyle);
	        header.getCell(2).setCellStyle(theaderStyle);
	        header.getCell(3).setCellStyle(theaderStyle);

	        int rowNum = 10;

	        for (ItemInvoice item : invoice.getItems()) {
	            Row fila = sheet.createRow(rowNum++);

	            cell = fila.createCell(0);
	            cell.setCellValue(item.getProduct().getName());
	            cell.setCellStyle(tbodyStyle);

	            cell = fila.createCell(1);
	            cell.setCellValue(item.getProduct().getPrice());
	            cell.setCellStyle(tbodyStyle);

	            cell = fila.createCell(2);
	            cell.setCellValue(item.getQuantity());
	            cell.setCellStyle(tbodyStyle);

	            cell = fila.createCell(3);
	            cell.setCellValue(item.calculateAmount());
	            cell.setCellStyle(tbodyStyle);
	        }

	        // ДАНІ ПРО ЗАГАЛЬНУ СУМУ
	        Row filaTotal = sheet.createRow(rowNum);

	        cell = filaTotal.createCell(2);
	        cell.setCellValue(mensajes.getMessage("text.invoice.form.total") + ": ");
	        cell.setCellStyle(tbodyStyle);

	        cell = filaTotal.createCell(3);
	        cell.setCellValue(invoice.getTotalInvoice());
	        cell.setCellStyle(tbodyStyle);
	    }
	}