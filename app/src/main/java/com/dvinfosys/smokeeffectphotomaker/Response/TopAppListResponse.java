package com.dvinfosys.smokeeffectphotomaker.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vaksys-1 on 3/7/17.
 */

public class TopAppListResponse extends CommonResponse {

    @SerializedName("row")
    private List<CategoryEntity> row;

    public List<CategoryEntity> getRow() {
        return row;
    }

    public class CategoryEntity{

        @SerializedName("name")
        private String name;

        @SerializedName("image")
        private String image;

        @SerializedName("link")
        private String link;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
