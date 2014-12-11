package ua.pti.myatm;


import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ATMTest {
    @Test(expected = IllegalArgumentException.class)
    public void testSetMoneyWithNegativeValueInATMThrowsIllegalArgumentException()  {
        new ATM(-1);
    }


    @Test
    public void testGetMoneyInATMExpectedEquals() {
        double actionMoney = 13.5;
        ATM atm = new ATM(actionMoney);
        double expectedResult = 13.5;
        assertEquals(atm.getMoneyInATM(), expectedResult, 0.0);
    }



    @Test
    public void testGetMoneyInATMExpectedNotEquals() {
        double actionMoney = 13.5;
        ATM atm = new ATM(actionMoney);
        double expectedResult = 53.1;
        assertNotEquals(atm.getMoneyInATM(), expectedResult, 0.0);
    }

    @Test (expected = NullPointerException.class)
    public void testValidateCardForNullCardThrowsIllegalArgumentException() {
        ATM atm = new ATM(1);
        atm.validateCard(null,1);
    }

    @Test
    public void testValidateCardForBlockedCard() {
        ATM atm = new ATM(1);
        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(true);
        boolean result = atm.validateCard(card,1);

        assertEquals(result,false);
    }
    @Test
     public void testValidateCardForCardAccepted() {
        ATM atm = new ATM(1);
        Card card = mock(Card.class);
        int pinCode = 0000;
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(pinCode)).thenReturn(true);
        boolean result = atm.validateCard(card,pinCode);

        assertEquals(result, true);
    }


    @Test (expected = NoCardInserted.class)
    public void testCheckBalanceForNullCardThrowsNoCardInsertion() throws NoCardInserted {
        ATM atm = new ATM(1);

        atm.checkBalance();

    }

    @Test
    public void testCheckBalanceExcpectedAllRight() throws NoCardInserted {
        ATM atm = new ATM(1);

        Card card = mock(Card.class);
        int pinCode = 0000;
        Account account = mock(Account.class);
        double actualValue = 123;
        when(account.getBalance()).thenReturn(actualValue);
        when(card.getAccount()).thenReturn(account);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(pinCode)).thenReturn(true);
         atm.validateCard(card,pinCode);
        double expectedResult = 123;
            assertEquals(atm.checkBalance(),expectedResult,0.0);

    }

    @Test
      public void testCheckBalanceOtherResult() throws NoCardInserted {
        ATM atm = new ATM(1);

        Card card = mock(Card.class);
        int pinCode = 0000;
        Account account = mock(Account.class);
        double actualValue = 1000;
        when(account.getBalance()).thenReturn(actualValue);
        when(card.getAccount()).thenReturn(account);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(pinCode)).thenReturn(true);
         atm.validateCard(card,pinCode);
        double expectedResult = 321;
        assertNotEquals(atm.checkBalance(), expectedResult, 0.0);

    }

    @Test (expected = NoCardInserted.class)
      public void testGetCashForNullCardThrowsNoCardInserted() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount {
        ATM atm = new ATM(321);
        double amount =123;
        assertNull(atm.getCash(amount));

    }

    @Test (expected = NotEnoughMoneyInAccount.class)
    public void testGetCashThrowsNotEnoughMoneyInAccount() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount {

        double amount =132;
        ATM atm = new ATM(1);

        Card card = mock(Card.class);
        int pinCode = 0000;
        Account account = mock(Account.class);
        double actualValue = 123;
        when(account.getBalance()).thenReturn(actualValue);
        when(card.getAccount()).thenReturn(account);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(pinCode)).thenReturn(true);
       atm.validateCard(card,pinCode);
        atm.getCash(amount);
    }

    @Test (expected = NotEnoughMoneyInATM.class)
    public void testGetCashThrowsNotEnoughMoneyInATM() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount {

        double amount = 321;
        ATM atm = new ATM(1);

        Card card = mock(Card.class);
        int pinCode = 0000;
        Account account = mock(Account.class);
        double actualValue = 432;
        when(account.getBalance()).thenReturn(actualValue);
        when(card.getAccount()).thenReturn(account);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(pinCode)).thenReturn(true);
         atm.validateCard(card,pinCode);
        atm.getCash(amount);
    }

    @Test
    public void testGetCashBalanceCheckBalanceOnceAtLeast() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount {

        double amount = 321;
        ATM atm = new ATM(10000);
        Card card = mock(Card.class);
        int pinCode = 0000;
        Account account = mock(Account.class);
        double actualValue = 10000;
        when(account.getBalance()).thenReturn(actualValue);
        when(card.getAccount()).thenReturn(account);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(pinCode)).thenReturn(true);
         atm.validateCard(card,pinCode);
        atm.getCash(amount);
        verify(account, atLeastOnce()).getBalance();
    }

    @Test
    public void testGetCashBalanceOrderForGetBalanceBeforeWithDraw() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount {

        double amount = 321;
        ATM atm = new ATM(10000);
        Card card = mock(Card.class);
        int pinCode = 0000;
        Account account = mock(Account.class);
        double actualValue = 10000;
        when(account.getBalance()).thenReturn(actualValue);
        when(card.getAccount()).thenReturn(account);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(pinCode)).thenReturn(true);
        when(account.withdrow(amount)).thenReturn(amount);
        atm.validateCard(card, pinCode);
        atm.getCash(amount);
        InOrder order = inOrder(account);
        order.verify(account).getBalance();
        order.verify(account).withdrow(amount);

    }

}