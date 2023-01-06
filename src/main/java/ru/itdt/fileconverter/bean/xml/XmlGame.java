/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package ru.itdt.fileconverter.bean.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
    private final List<XmlPlatform> platforms = new ArrayList<>();

    private XmlGame(Builder builder) {
        name = builder.name;
        year = builder.year;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String name;
        private int year;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public XmlGame build(){
            return new XmlGame(this);
        }
    }
}
