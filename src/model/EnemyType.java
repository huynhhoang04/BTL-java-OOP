package model;

public class EnemyType {
    private int id;
    private String name;
    private int maxHp;
    private double moveSpeed;
    private int damageToBase;
    private int rewardGold;
    private String spriteUrl;

    public EnemyType(int id, String name, int maxHp,
                     double moveSpeed, int damageToBase,
                     int rewardGold, String spriteUrl) {
        setId(id);
        setName(name);
        setMaxHp(maxHp);
        setMoveSpeed(moveSpeed);
        setDamageToBase(damageToBase);
        setRewardGold(rewardGold);
        setSpriteUrl(spriteUrl);
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    public void setMaxHp(int maxHp) {
        if (maxHp > 0) {
            this.maxHp = maxHp;
        }
    }

    public void setMoveSpeed(double moveSpeed) {
        if (moveSpeed > 0) {
            this.moveSpeed = moveSpeed;
        }
    }

    public void setDamageToBase(int damageToBase) {
        if (damageToBase >= 0) {
            this.damageToBase = damageToBase;
        }
    }

    public void setRewardGold(int rewardGold) {
        if (rewardGold >= 0) {
            this.rewardGold = rewardGold;
        }
    }

    public void setSpriteUrl(String spriteUrl) {
        if (spriteUrl != null && !spriteUrl.isEmpty()) {
            this.spriteUrl = spriteUrl;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public int getDamageToBase() {
        return damageToBase;
    }

    public int getRewardGold() {
        return rewardGold;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }
}
