package model;

public class Level {
    private int id;
    private String name;
    private int mapId;
    private int difficulty;
    private Integer baseHpOverride;
    private int startGold;
    private String description;

    public Level(int id, String name, int mapId,
                 int difficulty, Integer baseHpOverride,
                 int startGold, String description) {
        setId(id);
        setName(name);
        setMapId(mapId);
        setDifficulty(difficulty);
        setBaseHpOverride(baseHpOverride);
        setStartGold(startGold);
        setDescription(description);
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

    public void setMapId(int mapId) {
        if (mapId > 0) {
            this.mapId = mapId;
        }
    }

    public void setDifficulty(int difficulty) {
        if (difficulty > 0) {
            this.difficulty = difficulty;
        }
    }

    public void setBaseHpOverride(Integer baseHpOverride) {
        if (baseHpOverride == null || baseHpOverride > 0) {
            this.baseHpOverride = baseHpOverride;
        }
    }

    public void setStartGold(int startGold) {
        if (startGold >= 0) {
            this.startGold = startGold;
        }
    }

    public void setDescription(String description) {
        this.description = description; 
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMapId() {
        return mapId;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Integer getBaseHpOverride() {
        return baseHpOverride;
    }

    public int getStartGold() {
        return startGold;
    }

    public String getDescription() {
        return description;
    }
}

