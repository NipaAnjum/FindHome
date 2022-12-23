package com.example.findhome.model;

import java.util.List;

public class Item {

    private String location;
    private String price;
    private String description;
    private String shortDescription;
    private String image;
    private String contactNo;
    private String userId;
    private String itemId;
    private String status;

    public Item() {
    }

    public Item(String location, String price, String shortDescription) {
        this.location = location;
        this.price = price;
        this.shortDescription = shortDescription;
    }


    public Item(String location, String price, String description, String shortDescription, String image, String contactNo) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
        this.image = image;
        this.contactNo = contactNo;
    }

    public Item(String location, String price, String description, String shortDescription, String image, String contactNo, String userId) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
        this.image = image;
        this.contactNo = contactNo;
        this.userId = userId;
    }

    public Item(String location, String price, String description, String shortDescription, String image, String contactNo, String userId, String itemId) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
        this.image = image;
        this.contactNo = contactNo;
        this.userId = userId;
        this.itemId = itemId;
    }

    public Item(String location, String price, String description, String shortDescription, String image, String contactNo, String userId, String itemId, String status) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
        this.image = image;
        this.contactNo = contactNo;
        this.userId = userId;
        this.itemId = itemId;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    //    public Item(String location, String price, List<Integer> images, String description, String shortDescription, int totalImages) {
//        this.location = location;
//        this.price = price;
//        this.images = images;
//        this.description = description;
//        this.shortDescription = shortDescription;
//        this.totalImages = totalImages;
//    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

//    public List<Integer> getImages() {
//        return images;
//    }

//    public void setImages(List<Integer> images) {
//        this.images = images;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

//    public int getTotalImages() {
//        return totalImages;
//    }

//    public void setTotalImages(int totalImages) {
//        this.totalImages = totalImages;
//    }
}
