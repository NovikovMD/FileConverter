/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package FileConverter.Classes.JSON;

/**
 * Third node in Json file.
 * Contains its name, year of foundation and url to website.
 *
 * @author Novikov Matthew
 */
public class JsonDevStudio {

    private String Name;
    private int yearOfFoundation;
    private String url;

    public JsonDevStudio(String name, int yearOfFoundation, String url) {
        this.Name = name;
        this.yearOfFoundation = yearOfFoundation;
        this.url = url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String mName) {
        this.Name = mName;
    }

    public int getYearOfFoundation() {
        return yearOfFoundation;
    }

    public String getUrl() {
        return url;
    }

    public void setYearOfFoundation(int yearOfFoundation) {
        this.yearOfFoundation = yearOfFoundation;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
