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
public class XmlDevStudio {
    @XmlAttribute(name = "наименование")
    private String name;

    @XmlAttribute(name = "годОснования")
    private int yearOfFoundation;

    @XmlAttribute(name = "URL")
    private String url;

    @XmlElementWrapper(name = "Игры")
    @XmlElement(name = "Игра")
    private final List<XmlGame> games = new ArrayList<>();

    private XmlDevStudio(Builder builder) {
        name = builder.name;
        yearOfFoundation = builder.yearOfFoundation;
        url = builder.url;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String name;
        private int yearOfFoundation;
        private String url;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder yearOfFoundation(int yearOfFoundation) {
            this.yearOfFoundation = yearOfFoundation;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public XmlDevStudio build(){
            return new XmlDevStudio(this);
        }
    }
}
