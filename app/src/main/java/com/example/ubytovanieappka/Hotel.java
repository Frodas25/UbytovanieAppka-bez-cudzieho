package com.example.ubytovanieappka;

public class Hotel {

    private int ID;
    private String name;
    private String city;
    private String street;
    private byte[] image;

    public Hotel(int ID, String name, String city, String street, byte[] image) {
        this.ID = ID;
        this.name = name;
        this.city = city;
        this.street = street;
        this.image = image;
    }

    public long getID() { return ID; }

    public void setID(int ID) { this.ID = ID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getStreet() { return street; }

    public void setStreet(String street) { this.street = street; }

    public byte[] getImage() { return image; }

    public void setImage(byte[] image) { this.image = image; }
}
