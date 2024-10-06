package com.hitori.api;

import com.hitori.dao.BookingDAO;
import com.hitori.dao.BookingDetailDAO;
import com.hitori.entity.Account;
import com.hitori.entity.Booking;
import com.hitori.entity.BookingDetail;
import com.hitori.entity.MailInfo;
import com.hitori.service.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class BookingApi {
    public static final String CANCEL_URL = "http://localhost:8080";
    public static final String SUCCESS_URL = "http://localhost:8080/success";
    @Autowired
    MailerService mailerService;

    @Autowired
    BookingDAO bookingDAO;

    @Autowired
    BookingDetailDAO bookingDetailDAO;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    BookingService bookingService;

    @Autowired
    AccountService accountService;

    @Autowired
    PaymentService paymentService;

    @PostMapping("/create-booking")
    public ResponseEntity<String> createBooking(@RequestBody Booking newBooking, Principal principal) {
        try {
            if (principal != null) {
                String username = principal.getName();
                Account account = accountService.findById(username);

                newBooking.setCheck_in(newBooking.getCheck_in());
                newBooking.setCheck_out(newBooking.getCheck_out());
                newBooking.setTotalPrice(newBooking.getTotalPrice());
                newBooking.setRoom_name(newBooking.getRoom_name());
                newBooking.setStatus(false);
                newBooking.setCreateDate(new Date());
                newBooking.setAccount(account);
                bookingDAO.save(newBooking);
                // Save booking
                Booking savedBooking = bookingDAO.save(newBooking);

                // Tạo payment thanh toán
                BigDecimal totalPrice = new BigDecimal(newBooking.getTotalPrice());
                Payment payment = paymentService.createPayment(
                        totalPrice,
                        "USD",
                        "paypal",
                        "sale",
                        "Payment for Booking #" + savedBooking.getId(),
                        "http://localhost:8080",
                        "http://localhost:8080/success"
                );

                for (Links links : payment.getLinks()) {
                    if ("approval_url".equals(links.getRel())) {
                        return ResponseEntity.ok(links.getHref());
                    }
                }

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not get approval URL");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to make a booking");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating booking: " + e.getMessage());
        }
    }
    private Long extractBookingIdFromPaymentDescription(String description) {
        // Giả sử rằng bookingId được lưu trong mô tả thanh toán dưới dạng "#1234"
        String[] tokens = description.split("#");
        if (tokens.length > 1) {
            String bookingIdString = tokens[1]; // Trích xuất bookingId từ mô tả thanh toán
            return Long.parseLong(bookingIdString);
        }
        return null; // Trả về null nếu không tìm thấy bookingId trong mô tả thanh toán
    }

    @GetMapping("/success")
    public String successCallback(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletResponse response) {
        try {
            // Kiểm tra thông tin 'paymentId' và 'PayerID' từ PayPal
            if (paymentId == null || payerId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing payment information");
                return paymentId;
            }

            // Tiến hành xử lý xác nhận thanh toán từ PayPal
            Payment executedPayment = paymentService.executePayment(paymentId, payerId);

            if (executedPayment != null && "approved".equals(executedPayment.getState())) {
                // Xác định bookingId từ thông tin thanh toán
                String description = executedPayment.getTransactions().get(0).getDescription();
                Long bookingId = extractBookingIdFromPaymentDescription(description);

                if (bookingId != null) {
                    // Cập nhật trạng thái thanh toán thành công và chuyển hướng đến trang success
                    bookingService.updateBookingStatusPayment(bookingId, true);
                    Booking booking = bookingService.findById(bookingId); // Gán thông tin booking từ BookingService
                    // Lấy email của người dùng từ thông tin booking
                    String userEmail = getUserEmailFromBookingId(bookingId);
                    sendSuccessEmail(userEmail, booking);
                    //Tạo bookingDetail
                    BookingDetail bookingDetail = new BookingDetail();
                    bookingDetail.setBooking(booking);
                    bookingDetail.setCheck_in(booking.getCheck_in());
                    bookingDetail.setCheck_out(booking.getCheck_out());
                    bookingDetail.setStatus(1);
                    bookingDetailDAO.save(bookingDetail);
                    return "redirect:/success-page";
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "BookingId not found in payment description");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Payment not approved");
            }
        } catch (PayPalRESTException | IOException e) {
        }
        return "redirect:/success-page";
    }

    private void sendSuccessEmail(String userEmail, Booking booking) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String checkInStr = dateFormat.format(booking.getCheck_in());
            String checkOutStr = dateFormat.format(booking.getCheck_out());
            String createDateStr = dateFormat.format(booking.getCreateDate());
            Float totalPrice = booking.getTotalPrice();
            String room_name = booking.getRoom_name();

            MailInfo mail = new MailInfo(userEmail, "Payment Successful - Booking Confirmation",
                    "<html><body><h2>Booking Confirmation</h2><p><b>Check-In Date:</b> " + checkInStr + "</p>" +
                            "<p><b>Check-Out Date:</b> " + checkOutStr + "</p>" +
                            "<p><b>Creation Date:</b> " + createDateStr + "</p>" +
                            "<p><b>Total Price:</b> " + totalPrice + "<b>USD</b></p>" +
                            "<p><b>Room type:</b> " + room_name + "</p>" +
                            "<p>Dear Guest,</p><p>Thank you for booking with us.</p><p>Best regards,<br>Hiroto Hotel</p></body></html>"
            );
            mail.setFrom("hotelhitori@gmail.com");
            mail.setTo(userEmail);
            mailerService.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String getUserEmailFromBookingId(Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        if (booking != null && booking.getAccount() != null) {
            return booking.getAccount().getUsername();
        }
        return null;
    }
}