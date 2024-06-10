package com.edu.hcmuaf.springserver.controller;


import com.edu.hcmuaf.springserver.config.VNPayConfig;
import com.edu.hcmuaf.springserver.dto.request.PaymentRequest;
import com.edu.hcmuaf.springserver.dto.response.PaymentResponse;
import com.edu.hcmuaf.springserver.entity.Reservation;
import com.edu.hcmuaf.springserver.entity.Seat;
import com.edu.hcmuaf.springserver.entity.Ticket;
import com.edu.hcmuaf.springserver.entity.User;
import com.edu.hcmuaf.springserver.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentController {

    @Autowired
    private UserService userService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private ShowTimeService showTimeService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/pay")
    public ResponseEntity<?> createPay(@RequestBody PaymentRequest paymentRequest, HttpServletRequest req, Authentication authentication) throws UnsupportedEncodingException, MessagingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String bankCode = "NCB";

        String username = authentication.getPrincipal().toString();
        User user = userService.getUserProfileByUsername(username);

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        if(paymentRequest.getListSeatId() == null) {
            return ResponseEntity.badRequest().body(new PaymentResponse(HttpServletResponse.SC_BAD_REQUEST, "Chưa chọn chỗ ngồi",null));
        }

        boolean checkExitsNnp_TxnRef = reservationService.checkExistReservation(vnp_TxnRef);
        if(checkExitsNnp_TxnRef) {
            vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        } else {
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < paymentRequest.getAmount(); i++) {
                if (ticketService.checkExistTicket(paymentRequest.getShowTimeId(), paymentRequest.getListSeatId().get(i))) {
                    emailService.sendTextEmail(user.getEmail(),"Kết quả đặt vé","Chỗ ngồi đã được đặt, chúng tôi xin lỗi vì điều đó. Xin hãy đặt ghế ngồi khác, xin cảm ơn!");
                    return ResponseEntity.badRequest().body(new PaymentResponse(HttpServletResponse.SC_BAD_REQUEST, "Chỗ ngồi đã được đặt",null));
                } else {
                    Reservation reservation = new Reservation();
                    reservation.setUser(user);
                    reservation.setShowTime(showTimeService.getShowTimeById(paymentRequest.getShowTimeId()));
                    reservation.setSeat(seatService.getSeatById(paymentRequest.getListSeatId().get(i)));
                    reservation.setOrder(vnp_TxnRef);
                    reservation.setPhone_number(user.getPhone_number());
                    reservation.setEmail(user.getEmail());
                    reservation.setOriginal_price(paymentRequest.getPrice());
                    reservation.setTotal_price(paymentRequest.getPrice());
                    Date date = new Date();
                    reservation.setReservation_time(date);
                    calendar.setTime(date);
                    calendar.add(Calendar.MINUTE, 15);
                    Date expired = calendar.getTime();
                    reservation.setExpired_time(expired);

                    reservation.setPayment("Đang thanh toán");

                    reservationService.createReservation(reservation);
                }

            }
        }

        String vnp_IpAddr = VNPayConfig.getIpAddress(req);

        long amount = paymentRequest.getPrice() * 100L;


        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));

        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setCode(200);
        paymentResponse.setMessage("Payment");
        paymentResponse.setUrlPayment(paymentUrl);

        return ResponseEntity.ok().body(paymentResponse);
    }

    @GetMapping("/payment-callback")
    public ResponseEntity<?> transaction(@Param("vnp_TxnRef") String vnp_TxnRef,
                                         @Param("vnp_TransactionStatus") String vnp_ResponseCode) throws MessagingException {
        if(vnp_ResponseCode.equals("00")) {
            List<Reservation> reservationList = reservationService.findReservationsByOrder(vnp_TxnRef);
            for (Reservation reservation : reservationList) {
                if(!reservation.getPayment().equals("Thanh toán thành công")) {
                    reservation.setPayment("Thanh toán thành công");

                    Ticket ticket = new Ticket();
                    ticket.setShowTime(reservation.getShowTime());
                    Seat seat = reservation.getSeat();
                    ticket.setSeat(seat);
                    ticket.setReservation(reservation);
                    ticket.setPrice(seat!=null?seat.getPrice():0);
                    String ticketCode = VNPayConfig.getRandomNumber(8);

                    if(ticketService.findTicketByTicketCode(ticketCode) != null) {
                        ticketCode = VNPayConfig.getRandomNumber(8);
                    }

                    ticket.setTicketCode(ticketCode);

                    ticketService.saveTicket(ticket);
                    reservationService.editReservation(reservation);
                    try {
                        emailService.sendHtmlEmail(ticket.getReservation().getEmail(), ticket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return ResponseEntity.ok("Thanh toán thành công đơn hàng " + vnp_TxnRef);
        }
        if(vnp_ResponseCode.equals("24")){
            List<Reservation> reservationList = reservationService.findReservationsByOrder(vnp_TxnRef);
            for (Reservation reservation : reservationList) {
                reservation.setPayment("Thanh toán thất bại");
                reservationService.editReservation(reservation);
            }
            return ResponseEntity.ok("Thanh toán đơn hàng " + vnp_TxnRef + " thất bại");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
