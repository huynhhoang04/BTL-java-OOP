package model;

public class Inventory {
    private int accId;
    private int itemId;
    private int qty;

    public Inventory(int accId, int itemId){
        setAccId(accId);
        setItemId(itemId);
    }

    public Inventory(int accId, int itemId, int qty){
        setAccId(accId);
        setItemId(itemId);
        setQty(qty);
    }

    public void setAccId(int accId){
        if(accId > 0){
            this.accId = accId;
        }
    }

    public void setItemId(int itemId){
        if(itemId > 0){
            this.itemId = itemId;
        }
    }

    public void setQty(int qty){
        if(qty > 0){
            this.qty = qty;
        }
    }

    public int getAccId(){
        return accId;
    }

    public int getItemId(){
        return itemId;
    }

    public String getQty(){
        String strqty = Integer.toString(qty);
        return strqty;
    }
}
