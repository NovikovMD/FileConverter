/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package fileconverter.bean.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlGame {
    @XmlAttribute(name = "название")
    private String name;
    @XmlAttribute(name = "годВыпуска")
    private int year;
    @XmlElementWrapper(name = "Платформы")
    @XmlElement(name = "Платформа")
    private final ArrayList<XmlPlatform> platforms = new ArrayList<>();

    public void addPlatform(String name) {
        platforms.add(new XmlPlatform(name));
    }

    public int returnLength() {
        return platforms.size();
    }
}
