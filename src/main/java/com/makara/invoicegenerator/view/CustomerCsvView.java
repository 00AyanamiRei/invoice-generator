package com.makara.invoicegenerator.view;

import com.makara.invoicegenerator.models.entity.Customer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.util.Map;

@Component("list.csv")
public class CustomerCsvView extends AbstractView {

	public CustomerCsvView() {
		setContentType("text/csv");
	}
	
	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition", "attachment; filename=\"customers.csv\"");
		response.setContentType(getContentType());
		
		Page<Customer> clients = (Page<Customer>) model.get("customers");
		
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(),
										CsvPreference.STANDARD_PREFERENCE);

		String[] header = {"id", "firstName", "lastName", "email", "createdAt"}; // Імена мають відповідати моделі
		beanWriter.writeHeader(header);

		for (Customer customer : clients) { // Змінна має бути customer, а не customers
			beanWriter.write(customer, header);
		}
		
		beanWriter.close();
		
		
	}
}
