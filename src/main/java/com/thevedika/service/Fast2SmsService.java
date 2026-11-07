
package com.thevedika.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Fast2SmsService {

        private final RestClient restClient;
        private final String otpId;
        private final int otpExpiry;
        private final int otpLength;

        public Fast2SmsService(
                        @Value("${fast2sms.api-url:https://www.fast2sms.com}") String apiUrl,
                        @Value("${fast2sms.api-key}") String apiKey,
                        @Value("${fast2sms.otp-id}") String otpId,
                        @Value("${fast2sms.otp-expiry:5}") int otpExpiry,
                        @Value("${fast2sms.otp-length:6}") int otpLength) {

                this.restClient = RestClient.builder()
                                .baseUrl(apiUrl)
                                .defaultHeader(HttpHeaders.AUTHORIZATION, apiKey)
                                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .build();

                this.otpId = otpId;
                this.otpExpiry = otpExpiry;
                this.otpLength = otpLength;
        }

        public void sendOtp(String phone) {
                try {
                        String response = restClient.post()
                                        .uri("/dev/otp/send")
                                        .body(Map.of(
                                                        "mobile", phone,
                                                        "otp_id", otpId,
                                                        "otp_expiry", otpExpiry,
                                                        "otp_length", otpLength))
                                        .retrieve()
                                        .body(String.class);

                        System.out.println("Fast2SMS Send Response: " + response);

                        if (!isSuccessful(response)) {
                                throw new ResponseStatusException(
                                                HttpStatus.BAD_GATEWAY,
                                                getMessage(response, "Fast2SMS could not send OTP"));
                        }

                } catch (RestClientResponseException ex) {
                        System.err.println("Fast2SMS Send Error: " + ex.getResponseBodyAsString());

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_GATEWAY,
                                        "Fast2SMS send failed: " + ex.getResponseBodyAsString());
                }
        }

        public void verifyOtp(String phone, String otp) {
                try {
                        String response = restClient.post()
                                        .uri("/dev/otp/verify")
                                        .body(Map.of(
                                                        "mobile", phone,
                                                        "otp", otp))
                                        .retrieve()
                                        .body(String.class);

                        System.out.println("Fast2SMS Verify Response: " + response);

                        if (!isSuccessful(response)) {
                                throw new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                getMessage(response, "Invalid or expired OTP"));
                        }

                } catch (RestClientResponseException ex) {
                        System.err.println("Fast2SMS Verify Error: " + ex.getResponseBodyAsString());

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid or expired OTP");
                }
        }

        private boolean isSuccessful(String response) {
                if (response == null || response.isBlank()) {
                        return false;
                }

                return Pattern.compile("\"return\"\\s*:\\s*true")
                                .matcher(response)
                                .find();
        }

        private String getMessage(String response, String defaultMessage) {
                if (response == null || response.isBlank()) {
                        return defaultMessage;
                }

                Matcher matcher = Pattern.compile(
                                "\"message\"\\s*:\\s*\"([^\"]*)\"")
                                .matcher(response);

                return matcher.find() ? matcher.group(1) : defaultMessage;
        }
}
