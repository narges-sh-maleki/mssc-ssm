package guru.springframework.msscssm.service;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {
    public Payment newPayment(Payment payment);

    public StateMachine<PaymentState, PaymentEvent> preAuthorize(Long paymentId);

    public StateMachine<PaymentState, PaymentEvent> authorize(Long paymentId);

    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);

}
