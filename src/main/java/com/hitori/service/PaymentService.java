package com.hitori.service;


import java.math.BigDecimal;
import java.util.*;

import com.hitori.dao.BookingDAO;
import com.hitori.dao.BookingDetailDAO;
import com.hitori.entity.Booking;
import com.hitori.entity.BookingDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaymentService {
    @Autowired
    private APIContext apiContext;

    @Autowired
    BookingDAO bookingDAO;

    @Autowired
    BookingService bookingService;
    @Autowired
    BookingDetailDAO bookingDetailDAO;
    @Autowired
    BookingDetailService bookingDetailService;

    public Payment createPayment(BigDecimal totalPrice,
                                 String currency,
                                 String paymentMethod,
                                 String paymentIntent,
                                 String description,
                                 String cancelUrl,
                                 String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(totalPrice.setScale(2).toString());

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(paymentMethod);

        Payment payment = new Payment();
        payment.setIntent(paymentIntent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public void handlePaymentSuccess(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        if (executedPayment.getState().equals("approved")) {
            // Lấy thông tin booking từ cơ sở dữ liệu
            Booking booking = getBookingFromPayment(executedPayment);

            if (booking != null) {
                booking.setStatus(true); // Cập nhật trạng thái của Booking thành đã thanh toán
                bookingDAO.save(booking);
            }
        }
    }

    private Booking getBookingFromPayment(Payment payment) {
        // Giả định rằng bookingId được lưu trong mô tả thanh toán dưới dạng "#1234"
        String description = payment.getTransactions().get(0).getDescription();
        String[] tokens = description.split("#");
        if (tokens.length > 1) {
            String bookingIdString = tokens[1]; // Trích xuất bookingId từ mô tả thanh toán
            Long bookingId = Long.parseLong(bookingIdString);

            // Sử dụng bookingId để lấy thông tin Booking từ cơ sở dữ liệu
            return bookingService.findById(bookingId);
        }

        return null; // Trả về null nếu không tìm thấy bookingId trong mô tả thanh toán
    }

    public Payment getPaymentById(String paymentId) throws PayPalRESTException {
        return Payment.get(apiContext, paymentId);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}