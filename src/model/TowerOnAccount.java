package model;

public class TowerOnAccount {
    private int acctowerId;
    private int towerId;

    public TowerOnAccount(int acctowerId, int towerId){
        setAccTowerId(acctowerId);
        setTowerId(towerId);
    }

    public void setAccTowerId(int acctowerId){
        if(acctowerId > 0){
            this.acctowerId = acctowerId;
        }
    }

    public void setTowerId(int towerId){
        if(towerId > 0 && towerId < 11){
            this.towerId = towerId;
        }
    }

    public int getAccTowerId(){
        return acctowerId;
    }

    public int getTowerId(){
        return towerId;
    }
}
