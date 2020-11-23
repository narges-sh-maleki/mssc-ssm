package guru.springframework.msscssm.config;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory stateMachineFactory;

    @RepeatedTest(10)
    @Test
    void TestStateMachine() {
        StateMachine<PaymentState, PaymentEvent> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
        System.out.println("*****" + stateMachine.getState().toString());
        stateMachine.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        System.out.println("*****" + stateMachine.getState().toString());
        //stateMachine.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
        //System.out.println("*****" + stateMachine.getState().toString());
       // stateMachine.sendEvent(PaymentEvent.PRE_AUTH_DECLINED);
      //  System.out.println("*****" + stateMachine.getState().toString());



    }

    @RepeatedTest(10)
    @Test
    void TestStateMachine2() {
        StateMachine<PaymentState, PaymentEvent> stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());
        stateMachine.start();
        System.out.println("11111" + stateMachine.getState().getId().toString());
        stateMachine.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        System.out.println("22222" + stateMachine.getState().getId().toString());
        if (stateMachine.getState().getId() == PaymentState.PRE_AUTH) {
            stateMachine.sendEvent(PaymentEvent.AUTHORIZE);
            System.out.println("3333" + stateMachine.getState().getId().toString());
        }



    }
}