package guru.springframework.msscssm.service;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp() {

        payment = paymentService.newPayment(Payment.builder()
                .amount(new BigDecimal(12.99)).build());
    }

    @Test
    void preAuthorize() {

        paymentService.preAuthorize(payment.getId());
        System.out.println("***********" + paymentRepository.findById(payment.getId()).get().getState());
    }
}