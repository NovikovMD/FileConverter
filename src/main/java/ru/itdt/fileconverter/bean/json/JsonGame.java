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

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonGame {
    @SerializedName("Название")
    @JsonProperty("Название")
    private String name;

    @SerializedName("Год выпуска")
    @JsonProperty("Год выпуска")
    private int year;

    @SerializedName("Издатель")
    @JsonProperty("Издатель")
    private String gamePublisher;

    @SerializedName("Платформы")
    @JsonProperty("Платформы")
    private final List<JsonPlatform> platforms = new ArrayList<>();

    @SerializedName("Разработчики")
    @JsonProperty("Разработчики")
    private final List<JsonDevStudio> devStudios = new ArrayList<>();

    private JsonGame(Builder builder) {
        name = builder.name;
        year = builder.year;
        gamePublisher = builder.gamePublisher;
    }

    public static Builder builder(){
        return new JsonGame.Builder();
    }

    public static class Builder{
        private String name;
        private int year;
        private String gamePublisher;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder gamePublisher(String gamePublisher) {
            this.gamePublisher = gamePublisher;
            return this;
        }

        public JsonGame build(){
            return new JsonGame(this);
        }
    }
}
