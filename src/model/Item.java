package model;

public class Item {
    private int id;
    private String itemname;
    private String type;
    private int itemtier;
    private int price;
    private String imgUrl;

    public Item(int id, String itemname, int itemtier, int price, String imgUrl){
        setId(id);
        setItemName(itemname);
        setItemTier(itemtier);
        setPrice(price);
        this.imgUrl = imgUrl;
    }

    public Item(int id){    
        setId(id);
    }

    public void setId(int id){
        if(id > 0 && id< 6){
            this.id = id;
        }
    }

    public int getId(){
        return id;
    }

    public void setItemName(String itemname){
        if(!itemname.equals("")){
            this.itemname = itemname;
        }
    }

    public void setItemTier(int itemtier){
        if(itemtier > 0 && itemtier < 5){
            this.itemtier = itemtier;
        }
    }

    public void setPrice(int price){
        if(price > 0){
            this.price = price;
        }
    }

    public String getItemName(){
        return itemname;
    }

    public String getPrice(){
        String strprice = Integer.toString(price);
        return strprice;
    }

    public int getIntPrice(){
        return price;
    }

    public String getItemImgUrl(){
        return imgUrl;
    }
}
  