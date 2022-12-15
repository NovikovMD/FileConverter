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
public class XmlGamePublisher {
    @XmlAttribute(name = "наименование")
    private String name;
    @XmlElementWrapper(name = "ИгровыеРазработчики")
    @XmlElement(name = "Разработчик")
    private final ArrayList<XmlDevStudio> devStudios = new ArrayList<>();

    public void addDevStudio(String name, int yearOfFoundation, String url) {
        devStudios.add(new XmlDevStudio(name, yearOfFoundation, url));
    }

    public int returnLength() {
        return devStudios.size();
    }
}
