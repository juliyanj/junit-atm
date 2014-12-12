package ua.pti.myatm;

public class ATM {
    
    private double moneyInATM;
    private Card card;
    
    //Можно задавать количество денег в банкомате 
    ATM(double moneyInATM) {
        if (moneyInATM<=0){
            throw new IllegalArgumentException();
        }
        else {
            
            this.moneyInATM = moneyInATM;
            this.card = null;
        }
    }

    // Возвращает количество денег в банкомате
    public double getMoneyInATM() {
         return this.moneyInATM;
    }
        
   //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    public boolean validateCard(Card card, int pinCode){
        if(!card.checkPin(pinCode)||card.isBlocked()) {
            return false;
        } 
        else {
            this.card = card;
            return true;
        }       
    }
    
    //Возвращает сколько денег есть на счету
    public double checkBalance()throws NoCardInserted {
         if(this.card == null) {
            throw new NoCardInserted();
        } 
         else {
            return this.card.getAccount().getBalance();
        }
    }
    
    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    public double getCash(double amount) throws NoCardInserted, NotEnoughMoneyInAccount, NotEnoughMoneyInATM {
        if(this.card == null) {
            throw new NoCardInserted();
        } 
        else {
            Account account = this.card.getAccount();
            if(account.getBalance() < amount) {
                throw new NotEnoughMoneyInAccount();
            } else if(this.getMoneyInATM() < amount) {
                throw new NotEnoughMoneyInATM();
            } else {
                this.moneyInATM -= account.withdrow(amount);
                return account.getBalance();
            }
        }
    }
}
