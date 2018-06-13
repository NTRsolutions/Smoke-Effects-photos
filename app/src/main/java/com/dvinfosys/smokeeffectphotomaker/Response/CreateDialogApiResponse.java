package com.dvinfosys.smokeeffectphotomaker.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaksys-android-63 on 29/7/17.
 */

public class CreateDialogApiResponse extends CommonResponse{

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("link")
    private String link;

    @SerializedName("image")
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
