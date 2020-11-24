package guru.springframework.msscssm.config.guard;

import guru.springframework.msscssm.service.PaymentServiceImpl;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class PaymentIdGuard implements Guard {
    @Override
    public boolean evaluate(StateContext context) {
        return context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER)!=null;
    }
}
