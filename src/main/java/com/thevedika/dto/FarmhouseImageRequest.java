
package com.thevedika.dto;

public class FarmhouseImageRequest {

    private String imageUrl;
    private Integer displayOrder;
    private Boolean coverImage;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Boolean coverImage) {
        this.coverImage = coverImage;
    }
}
