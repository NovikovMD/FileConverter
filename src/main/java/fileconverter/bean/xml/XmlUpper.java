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

import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@Getter
@XmlRootElement(name = "ИгроваяИндустрия")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlUpper {
    @XmlElementWrapper(name = "ИгровыеИздатели")
    @XmlElement(name = "Издатель")
    private final ArrayList<XmlGamePublisher> publishers = new ArrayList<>();

    public void addPublisher(String name) {
        publishers.add(new XmlGamePublisher(name));
    }

    public int returnLength() {
        return publishers.size();
    }
}
