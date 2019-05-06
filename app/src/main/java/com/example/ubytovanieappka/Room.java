package com.example.ubytovanieappka;

public class Room {

    private int ID;
    private String name;
    private String size;
    private String area;
    private String equip;
    private String price;
    private byte[] image;

    public Room(int ID, String name, String size, String area, String equip, String price, byte[] image) {
        this.ID = ID;
        this.name = name;
        this.size = size;
        this.area = area;
        this.equip = equip;
        this.price = price;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEquip() {
        return equip;
    }

    public void setEquip(String equip) {
        this.equip = equip;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
