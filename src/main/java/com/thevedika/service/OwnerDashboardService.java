package com.thevedika.service;

import com.thevedika.dto.owner.OwnerDashboardResponse;
import com.thevedika.dto.owner.OwnerBookingResponse;
import com.thevedika.dto.owner.OwnerPaymentResponse;

import java.util.List;

public interface OwnerDashboardService {

    OwnerDashboardResponse getDashboard(String phone);

    List<OwnerBookingResponse> getBookings(String phone);

    List<OwnerPaymentResponse> getPayments(String phone);
}