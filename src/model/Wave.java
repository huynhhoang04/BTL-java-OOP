package model;

public class Wave {
    private int id;
    private int levelId;
    private int orderIndex;
    private double startTime;
    private int enemyTypeId;
    private int enemyCount;
    private double spawnInterval;

    public Wave(int id, int levelId, int orderIndex,
                double startTime, int enemyTypeId,
                int enemyCount, double spawnInterval) {
        setId(id);
        setLevelId(levelId);
        setOrderIndex(orderIndex);
        setStartTime(startTime);
        setEnemyTypeId(enemyTypeId);
        setEnemyCount(enemyCount);
        setSpawnInterval(spawnInterval);
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public void setLevelId(int levelId) {
        if (levelId > 0) {
            this.levelId = levelId;
        }
    }

    public void setOrderIndex(int orderIndex) {
        if (orderIndex > 0) {
            this.orderIndex = orderIndex;
        }
    }

    public void setStartTime(double startTime) {
        if (startTime >= 0) {
            this.startTime = startTime;
        }
    }

    public void setEnemyTypeId(int enemyTypeId) {
        if (enemyTypeId > 0) {
            this.enemyTypeId = enemyTypeId;
        }
    }

    public void setEnemyCount(int enemyCount) {
        if (enemyCount > 0) {
            this.enemyCount = enemyCount;
        }
    }

    public void setSpawnInterval(double spawnInterval) {
        if (spawnInterval > 0) {
            this.spawnInterval = spawnInterval;
        }
    }

    public int getId() {
        return id;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public double getStartTime() {
        return startTime;
    }

    public int getEnemyTypeId() {
        return enemyTypeId;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public double getSpawnInterval() {
        return spawnInterval;
    }
}
