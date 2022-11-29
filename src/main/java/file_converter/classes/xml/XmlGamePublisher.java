/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package file_converter.classes.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class XmlGamePublisher {
    private String name;
    private final ArrayList<XmlDevStudio> devStudios= new ArrayList<>();

    public void addDevStudio(String name, int yearOfFoundation, String url) {
        devStudios.add(new XmlDevStudio(name, yearOfFoundation, url));
    }

    public int returnLength() {
        return devStudios.size();
    }
}
