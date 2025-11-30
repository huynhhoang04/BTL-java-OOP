package model;

public class Tower {
    private int id;
    private String towername;
    private int tier;
    private int base_dmg;
    private boolean enable;
    private String imgUrl;


    public Tower(int id, String towername, int tier, int base_dmg, String imgUrl){
            setId(id);
            setTowername(towername);
            setBaseDmg(base_dmg);
            setTier(tier);
            this.imgUrl = imgUrl;
    }

    public Tower(int tier){
        setTier(tier);
    }

    public void setTier(int tier){
        if(tier < 0){
            this.tier = 0;
        }
        if (tier > 5) {
            this.tier = 5;
        } else {
            this.tier = tier;
        }
    }

    public int geTier(){
        return tier;
    }

    public void setId(int id){
        if(id > 0){ this.id = id;}
    }


    public void setTowername(String towername){
        if(!towername.equals("")){this.towername = towername;}
    }

    public void setBaseDmg(int base_dmg){

        if(base_dmg > 0) {this.base_dmg = base_dmg;}
    }

    public int getId(){
        return id;
    }

    public String getTowerName(){
        return towername;
    }

    public String getTowerImgUrl(){
        return imgUrl;
    }

    public int getBaseDmg(){
        return base_dmg;
    }
}
