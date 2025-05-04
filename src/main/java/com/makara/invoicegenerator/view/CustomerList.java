package com.makara.invoicegenerator.view;

import com.makara.invoicegenerator.models.entity.Customer;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "customers")
public class CustomerList {

    @XmlElement(name = "customer")
    public List<Customer> customers;

    public CustomerList() {
    }

    public CustomerList(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

}

