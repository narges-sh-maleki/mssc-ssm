package guru.springframework.msscssm.service;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PaymentServiceImpl implements PaymentService {


    public static final String PAYMENT_ID_HEADER = "payment-id";
    private final PaymentRepository paymentRepository;
   private final StateMachineFactory stateMachineFactory;
   private final PaymentStateMachineInterceptor smi;

    @Override
    public Payment newPayment(Payment payment) {
        payment.setState(PaymentState.NEW);
        return paymentRepository.save(payment);
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuthorize(Long paymentId) {
        StateMachine<PaymentState,PaymentEvent> stateMachine = this.build(paymentId);
        this.sendEvent(stateMachine,PaymentEvent.PRE_AUTHORIZE,paymentId);
        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> authorize(Long paymentId) {
        StateMachine<PaymentState,PaymentEvent> stateMachine = this.build(paymentId);
        this.sendEvent(stateMachine,PaymentEvent.AUTHORIZE,paymentId);




        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
        StateMachine<PaymentState,PaymentEvent> stateMachine = this.build(paymentId);
        this.sendEvent(stateMachine,PaymentEvent.AUTH_DECLINED,paymentId);


        return null;
    }

    /***
     * it retrieves the latest state from DB and sets it to the StateMachine and returns it.
     * it also configures the State Machine to write the state to the DB before every change in the state
     * @param paymentId
     * @return StateMachine
     */

    private StateMachine<PaymentState,PaymentEvent> build(Long paymentId){
        //Payment payment = paymentRepository.getOne(paymentId);
        Payment payment =  paymentRepository.findById(paymentId).orElseGet(()->{throw new RuntimeException("not found");});

        StateMachine<PaymentState,PaymentEvent> stateMachine = stateMachineFactory.getStateMachine(paymentId.toString());
        stateMachine.stop();
        stateMachine.getStateMachineAccessor().
                doWithAllRegions(sma -> {
                    //we configure the machine so that before any change in the states it writes the state to DB
                    sma.addStateMachineInterceptor(smi);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(),null,null,null));
                });

        stateMachine.start();
        return stateMachine;
    }

    private void sendEvent(StateMachine<PaymentState,PaymentEvent> sm, PaymentEvent event, Long paymentId){
        Message message = MessageBuilder.withPayload(event)
                .setHeader(PAYMENT_ID_HEADER, paymentId).build();

        sm.sendEvent(message);
    }


}
