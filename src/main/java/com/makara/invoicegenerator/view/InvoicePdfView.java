package com.makara.invoicegenerator.view;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.makara.invoicegenerator.models.entity.Invoice;
import com.makara.invoicegenerator.models.entity.ItemInvoice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.awt.*;
import java.util.Locale;
import java.util.Map;

@Component("invoice/view")
public class InvoicePdfView extends AbstractPdfView {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
									HttpServletRequest request, HttpServletResponse response) throws Exception {

		Invoice invoice = (Invoice) model.get("invoice");

		Locale locale = localeResolver.resolveLocale(request);

		MessageSourceAccessor mensajes = getMessageSourceAccessor();

		PdfPTable tabla = new PdfPTable(1); // columnas
		tabla.setSpacingAfter(20);

		PdfPCell cell = null;

		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.customer.detail.title", null, locale)));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);
		tabla.addCell(cell);

		tabla.addCell(invoice.getClient().getFirstName() + " " + invoice.getClient().getLastName());
		tabla.addCell(invoice.getClient().getEmail());

		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);

		cell = new PdfPCell(new Phrase(messageSource.getMessage("text.invoice.view.invoiceData", null, locale)));
		cell.setBackgroundColor(new Color(195, 230, 203));
		cell.setPadding(8f);

		tabla2.addCell(cell);
		tabla2.addCell(mensajes.getMessage("text.customer.invoice.number") + ": " + invoice.getId());
		tabla2.addCell(mensajes.getMessage("text.customer.invoice.description") + ": " + invoice.getDescription());
		tabla2.addCell(mensajes.getMessage("text.customer.invoice.date") + ": " + invoice.getCreatedAt());

		document.add(tabla);
		document.add(tabla2);

		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.setWidths(new float[]{3.5f, 1, 1, 1});
		tabla3.addCell(mensajes.getMessage("text.invoice.form.item.name"));
		tabla3.addCell(mensajes.getMessage("text.invoice.form.item.price"));
		tabla3.addCell(mensajes.getMessage("text.invoice.form.item.quantity"));
		tabla3.addCell(mensajes.getMessage("text.invoice.form.item.total"));

		for (ItemInvoice item : invoice.getItems()) {
			tabla3.addCell(item.getProduct().getName());
			tabla3.addCell(item.getProduct().getPrice().toString());

			cell = new PdfPCell(new Phrase(item.getQuantity().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tabla3.addCell(cell);
			tabla3.addCell(item.calculateAmount().toString());
		}

		cell = new PdfPCell(new Phrase(mensajes.getMessage("text.invoice.form.total") + ": "));
		cell.setColspan(3); // 3 columnas
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(invoice.getTotalInvoice().toString());

		document.add(tabla3);
	}
}