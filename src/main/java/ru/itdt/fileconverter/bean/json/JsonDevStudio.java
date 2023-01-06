/*
 * 23 November 2022
 *
 * The author disclaims copyright to this source code. In place of
 * a legal notice, here is a blessing:
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package ru.itdt.fileconverter.bean.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonDevStudio {
    @SerializedName("Наименование")
    @JsonProperty("Наименование")
    private String name;

    @SerializedName("Год основания")
    @JsonProperty("Год основания")
    private int yearOfFoundation;

    @SerializedName("URL")
    @JsonProperty("URL")
    private String url;

    private JsonDevStudio(Builder builder) {
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

        public JsonDevStudio build(){
            return new JsonDevStudio(this);
        }
    }
}
