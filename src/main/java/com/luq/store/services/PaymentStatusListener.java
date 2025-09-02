package com.luq.store.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.dto.response.payment.PaymentStatusDTO;
import com.luq.store.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentStatusListener {
    @Autowired
    OrderService oService;
    @Autowired
    OrderRepository oRepository;

    @KafkaListener(topics = "payment-status-topic", groupId = "store-group")
    public void process_payment(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PaymentStatusDTO data = mapper.readValue(message, PaymentStatusDTO.class);

        oService.updateStatus(data);
    }
}
