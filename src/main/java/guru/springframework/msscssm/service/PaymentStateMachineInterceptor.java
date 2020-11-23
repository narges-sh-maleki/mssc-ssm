package guru.springframework.msscssm.service;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentStateMachineInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    //it updates the state in the DB
    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state,
                                Message<PaymentEvent> message,
                               Transition<PaymentState, PaymentEvent> transition,
                               StateMachine<PaymentState, PaymentEvent> stateMachine) {


        Optional.ofNullable(message).ifPresent(msg ->{
          Optional.ofNullable((Long)msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER,-1l))
          .ifPresent(paymentId -> {
              Payment payment = paymentRepository.findById(paymentId).orElseGet(()->{throw new RuntimeException("Not Found");});
              payment.setState(state.getId());
              paymentRepository.save(payment);
          });
        } );

    }
}
