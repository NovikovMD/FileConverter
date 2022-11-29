/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package file_converter.classes.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonDevStudio {

    private String name;
    private int yearOfFoundation;
    private String url;

    public JsonDevStudio(String name, int yearOfFoundation, String url) {
        this.name = name;
        this.yearOfFoundation = yearOfFoundation;
        this.url = url;
    }
}
