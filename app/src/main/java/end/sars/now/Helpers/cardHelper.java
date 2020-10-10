package end.sars.now.Helpers;

public class cardHelper {
    String title;
    int icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public cardHelper(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }
}
