package com.thevedika.service;

import com.thevedika.dto.admin.AdminCustomerResponse;

public interface AdminCustomerService {

    AdminCustomerResponse activateCustomer(Long customerId);

    AdminCustomerResponse deactivateCustomer(Long customerId);
}