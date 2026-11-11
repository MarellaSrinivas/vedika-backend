package com.thevedika.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import com.thevedika.dto.payment.CreatePaymentOrderResponse;
import com.thevedika.dto.payment.PaymentVerificationResponse;
import com.thevedika.dto.payment.VerifyPaymentRequest;
import com.thevedika.entity.Booking;
import com.thevedika.entity.BookingPayment;
import com.thevedika.repository.BookingPaymentRepository;
import com.thevedika.repository.BookingRepository;
import com.thevedika.service.PaymentService;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PaymentServiceImpl implements PaymentService {

        private static final BigDecimal ADVANCE_AMOUNT = new BigDecimal("1000.00");

        private final BookingRepository bookingRepository;
        private final BookingPaymentRepository paymentRepository;

        @Value("${razorpay.key-id:}")
        private String razorpayKeyId;

        @Value("${razorpay.key-secret:}")
        private String razorpayKeySecret;

        public PaymentServiceImpl(
                        BookingRepository bookingRepository,
                        BookingPaymentRepository paymentRepository) {

                this.bookingRepository = bookingRepository;
                this.paymentRepository = paymentRepository;
        }

        @Override
        @Transactional
        public CreatePaymentOrderResponse createOrder(
                        String bookingReference,
                        String customerPhone) {

                validateRazorpayConfiguration();

                Booking booking = bookingRepository
                                .findByBookingReferenceForUpdate(bookingReference)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Booking not found"));

                validateBookingOwner(booking, customerPhone);

                if (!"PENDING_PAYMENT".equals(booking.getBookingStatus())) {
                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "This booking is not awaiting payment");
                }

                if (booking.getTotalAmount() == null
                                || booking.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid booking amount");
                }

                String paymentOption = booking.getPaymentOption();

                BigDecimal amountToPay;

                if ("ADVANCE".equalsIgnoreCase(paymentOption)) {

                        if (booking.getTotalAmount().compareTo(ADVANCE_AMOUNT) < 0) {
                                throw new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Booking total must be at least ₹1,000 for advance payment");
                        }

                        amountToPay = ADVANCE_AMOUNT;

                } else if ("FULL".equalsIgnoreCase(paymentOption)) {

                        amountToPay = booking.getTotalAmount();

                } else {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid booking payment option");
                }

                long amountInPaise = amountToPay
                                .setScale(2, RoundingMode.UNNECESSARY)
                                .movePointRight(2)
                                .longValueExact();

                try {
                        RazorpayClient client = new RazorpayClient(
                                        razorpayKeyId, razorpayKeySecret);

                        JSONObject orderRequest = new JSONObject();

                        orderRequest.put("amount", amountInPaise);
                        orderRequest.put("currency", "INR");
                        orderRequest.put(
                                        "receipt",
                                        bookingReference + "-" + System.currentTimeMillis());

                        JSONObject notes = new JSONObject();
                        notes.put("booking_reference", bookingReference);
                        notes.put("payment_option", paymentOption);

                        orderRequest.put("notes", notes);

                        Order razorpayOrder = client.orders.create(orderRequest);

                        String orderId = razorpayOrder.get("id").toString();

                        BookingPayment payment = new BookingPayment();

                        payment.setBooking(booking);
                        payment.setRazorpayOrderId(orderId);
                        payment.setAmount(amountToPay);
                        payment.setCurrency("INR");
                        payment.setPaymentOption(paymentOption);
                        payment.setStatus("CREATED");

                        paymentRepository.save(payment);

                        return new CreatePaymentOrderResponse(
                                        razorpayKeyId,
                                        orderId,
                                        amountInPaise,
                                        "INR",
                                        bookingReference,
                                        paymentOption,
                                        customerPhone);

                } catch (RazorpayException ex) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_GATEWAY,
                                        "Unable to create Razorpay order",
                                        ex);
                }
        }

        @Override
        @Transactional
        public PaymentVerificationResponse verifyPayment(
                        VerifyPaymentRequest request,
                        String customerPhone) {

                validateRazorpayConfiguration();

                BookingPayment paymentRecord = paymentRepository
                                .findByRazorpayOrderId(request.getRazorpayOrderId())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Payment order not found"));

                Booking booking = bookingRepository
                                .findByBookingReferenceForUpdate(
                                                request.getBookingReference())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Booking not found"));

                validateBookingOwner(booking, customerPhone);

                if (!paymentRecord.getBooking().getId().equals(booking.getId())) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Payment order does not belong to this booking");
                }

                if ("CAPTURED".equals(paymentRecord.getStatus())) {
                        if (!request.getRazorpayPaymentId().equals(
                                        paymentRecord.getRazorpayPaymentId())) {
                                throw new ResponseStatusException(
                                                HttpStatus.CONFLICT,
                                                "This order has already been paid");
                        }

                        return buildResponse(booking, "Payment already verified");
                }

                try {
                        JSONObject signatureData = new JSONObject();

                        signatureData.put(
                                        "razorpay_order_id",
                                        request.getRazorpayOrderId());

                        signatureData.put(
                                        "razorpay_payment_id",
                                        request.getRazorpayPaymentId());

                        signatureData.put(
                                        "razorpay_signature",
                                        request.getRazorpaySignature());

                        boolean signatureValid = Utils.verifyPaymentSignature(
                                        signatureData,
                                        razorpayKeySecret);

                        if (!signatureValid) {
                                throw new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Invalid Razorpay payment signature");
                        }

                        RazorpayClient client = new RazorpayClient(
                                        razorpayKeyId, razorpayKeySecret);

                        com.razorpay.Payment razorpayPayment = client.payments.fetch(request.getRazorpayPaymentId());

                        Object orderIdValue = razorpayPayment.get("order_id");
                        Object paymentStatusValue = razorpayPayment.get("status");
                        Object currencyValue = razorpayPayment.get("currency");
                        Object amountValue = razorpayPayment.get("amount");

                        String orderId = orderIdValue != null
                                        ? orderIdValue.toString()
                                        : null;

                        String paymentStatus = paymentStatusValue != null
                                        ? paymentStatusValue.toString()
                                        : null;

                        String currency = currencyValue != null
                                        ? currencyValue.toString()
                                        : null;

                        if (amountValue == null) {
                                throw new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Razorpay payment amount is missing");
                        }

                        long actualAmount = Long.parseLong(amountValue.toString());

                        long expectedAmount = paymentRecord.getAmount()
                                        .movePointRight(2)
                                        .longValueExact();

                        if (!request.getRazorpayOrderId().equals(orderId)
                                        || actualAmount != expectedAmount
                                        || !"INR".equalsIgnoreCase(currency)) {

                                throw new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Payment details do not match the booking order");
                        }

                        if (!"captured".equalsIgnoreCase(paymentStatus)) {
                                paymentRecord.setStatus(
                                                paymentStatus.toUpperCase());

                                paymentRepository.save(paymentRecord);

                                throw new ResponseStatusException(
                                                HttpStatus.CONFLICT,
                                                "Payment has not been captured yet. Please contact support if money was deducted.");
                        }

                        paymentRecord.setRazorpayPaymentId(
                                        request.getRazorpayPaymentId());

                        paymentRecord.setStatus("CAPTURED");

                        paymentRepository.save(paymentRecord);

                        BigDecimal amountPaid = booking.getAmountPaid()
                                        .add(paymentRecord.getAmount());

                        BigDecimal balanceDue = booking.getTotalAmount()
                                        .subtract(amountPaid);

                        if (balanceDue.compareTo(BigDecimal.ZERO) < 0) {
                                throw new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Payment exceeds the booking balance");
                        }

                        booking.setAmountPaid(amountPaid);
                        booking.setBalanceDue(balanceDue);

                        booking.setPaymentStatus(
                                        balanceDue.compareTo(BigDecimal.ZERO) == 0
                                                        ? "PAID"
                                                        : "PARTIALLY_PAID");

                        booking.setBookingStatus("CONFIRMED");

                        bookingRepository.save(booking);

                        return buildResponse(
                                        booking,
                                        "Payment successful. Your booking is confirmed.");

                } catch (RazorpayException ex) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_GATEWAY,
                                        "Unable to verify payment with Razorpay",
                                        ex);
                }
        }

        private void validateBookingOwner(
                        Booking booking,
                        String customerPhone) {

                if (booking.getCustomer() == null
                                || !booking.getCustomer().getPhone().equals(customerPhone)) {

                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "You cannot access this booking");
                }
        }

        private void validateRazorpayConfiguration() {

                if (razorpayKeyId == null || razorpayKeyId.isBlank()
                                || razorpayKeySecret == null
                                || razorpayKeySecret.isBlank()) {

                        throw new ResponseStatusException(
                                        HttpStatus.SERVICE_UNAVAILABLE,
                                        "Razorpay is not configured on the server");
                }
        }

        private PaymentVerificationResponse buildResponse(
                        Booking booking,
                        String message) {

                return new PaymentVerificationResponse(
                                true,
                                booking.getBookingReference(),
                                booking.getPaymentStatus(),
                                booking.getBookingStatus(),
                                booking.getTotalAmount(),
                                booking.getAmountPaid(),
                                booking.getBalanceDue(),
                                message);
        }
}