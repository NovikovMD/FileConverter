package FileConverter.Classes.JSON;

public class devStudio {

    private String name;
    private int yearOfFoundation;
    private String URL;

    public devStudio(String name, int yearOfFoundation, String URL) {
        this.name = name;
        this.yearOfFoundation = yearOfFoundation;
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfFoundation() {
        return yearOfFoundation;
    }

    public String getURL() {
        return URL;
    }

    public void setYearOfFoundation(int yearOfFoundation) {
        this.yearOfFoundation = yearOfFoundation;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
