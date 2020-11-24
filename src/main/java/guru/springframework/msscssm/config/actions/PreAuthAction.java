package guru.springframework.msscssm.config.actions;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.service.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PreAuthAction implements Action<PaymentState, PaymentEvent> {
    @Override
    public void execute(StateContext context) {
        if (new Random().nextInt(10) < 5){
            Message msg = MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_APPROVED).
                    setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)).
                    build();
            context.getStateMachine().sendEvent(msg);
        }
        else {
            Message msg = MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINED).
                    setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER,context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)).
                    build();
            context.getStateMachine().sendEvent(msg);
        }

    }
}
