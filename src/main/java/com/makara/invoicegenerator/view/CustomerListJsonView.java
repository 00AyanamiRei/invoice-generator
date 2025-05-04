package com.makara.invoicegenerator.view;


import com.makara.invoicegenerator.models.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

@Component("list.json")
@SuppressWarnings("unchecked")
public class CustomerListJsonView extends MappingJackson2JsonView{

	@Override
	protected Object filterModel(Map<String, Object> model) {

		model.remove("title");
		model.remove("page");


		Page<Customer> clientes =  (Page<Customer>) model.get("customers");
		model.remove("customers");

		model.put("customers", clientes.getContent());

		return super.filterModel(model);
	}



}
