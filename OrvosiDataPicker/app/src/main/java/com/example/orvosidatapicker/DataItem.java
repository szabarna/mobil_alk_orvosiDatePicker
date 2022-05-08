package com.example.orvosidatapicker;

public class DataItem {

    private String name;
    private String type;
    private float rating;


    public DataItem(String name, String type, float rating) {
        this.name = name;
        this.type = type;
        this.rating = rating;

    }


    // getters

    public String getName() {   return name;   }
    public String getType() {   return type;   }
    public float getRating() {  return rating;  }


    // setters
    public void setName(String name) {  this.name = name;  }
    public void setType(String type) {   this.type = type;  }
    public void setRating(float rating) {  this.rating = rating;  }
}
