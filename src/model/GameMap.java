package model;

public class GameMap {
    private int id;
    private String name;
    private String backgroundUrl;
    private int baseHpDefault;
    private String pathPoints;  
    private String towerSlots;   

    public GameMap(int id, String name, String backgroundUrl,
                   int baseHpDefault, String pathPoints, String towerSlots) {
        setId(id);
        setName(name);
        setBackgroundUrl(backgroundUrl);
        setBaseHpDefault(baseHpDefault);
        setPathPoints(pathPoints);
        setTowerSlots(towerSlots);
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

    public void setBackgroundUrl(String backgroundUrl) {
        if (backgroundUrl != null && !backgroundUrl.isEmpty()) {
            this.backgroundUrl = backgroundUrl;
        }
    }

    public void setBaseHpDefault(int baseHpDefault) {
        if (baseHpDefault > 0) {
            this.baseHpDefault = baseHpDefault;
        }
    }

    public void setPathPoints(String pathPoints) {
        if (pathPoints != null) {
            this.pathPoints = pathPoints;
        }
    }

    public void setTowerSlots(String towerSlots) {
        if (towerSlots != null) {
            this.towerSlots = towerSlots;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public int getBaseHpDefault() {
        return baseHpDefault;
    }

    public String getPathPoints() {
        return pathPoints;
    }

    public String getTowerSlots() {
        return towerSlots;
    }
}

