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

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class XmlUpperClass {
    private final ArrayList<XmlGamePublisher> publishers = new ArrayList<>();

    public void addPublisher(String name) {
        publishers.add(new XmlGamePublisher(name));
    }

    public int returnLength() {
        return publishers.size();
    }
}
