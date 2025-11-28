package model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private long gold;
    private int base_level;

    //cai con chắc tơ này là login xong thì nó game cần chạy dự vào thông tin trong này nhá như kiểu có vào nhiều tiền là gold ấy với cái base leval bao nhiêu và cần id để xác định
    public User(int id,  String username,String role,long gold ,int base_level){
        this.id = id;
        setUsername(username);
        setGold(gold);
        setRole(role);
        setBaseLeval(base_level);
    }

    // cho login nhá bấy nì va cho car dawng ky luon ko can email dau vi em huynh luc tao db quen con me no column email r
    public User(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    //setter và getter nhá cho e quân hiệu
    public void setUsername(String username){
        if(!username.equals("")){
            this.username = username;
        }
    }


    public void setPassword(String password){
        if(!password.equals("")){
            this.password = password;
        }
    }

    public String getUsername(){
        return username;


    }


    public String getPassword(){

        return password;
    }


    public void setRole(String role){
        this.role = "PLAYER";

    }




    public void setGold(long gold){
        if(gold < 0){
            this.gold = gold;
        }
        else{
            this.gold = gold;
        }
    }

    public void setBaseLeval(int base_level){
        if(base_level < 1){
            this.base_level = 1;
        }
        if (base_level > 5) {
            this.base_level = 5;
        } else {
            this.base_level = base_level;;
        }
    }

    public String getRole(){
        return role;
    }

    public long getGold(){
        return gold;
    }

    public int getBaseLevel(){
        return base_level;
    }

    public int getId(){
        return id;
    }
}

//coode của em huynh


