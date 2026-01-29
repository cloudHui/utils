package music;

import java.util.Objects;

public class HifiniMusic {
    private String name;

    private String downUrl;

    private String savePath;

    public HifiniMusic() {
    }

    public String getName() {
        return name;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    @Override
    public String toString() {
        return "HifiniMusic{" + "name='" + name + '\'' + ", downUrl='" + downUrl + '\'' + ", savePath='" + savePath + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HifiniMusic music = (HifiniMusic) o;
        return Objects.equals(name, music.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}