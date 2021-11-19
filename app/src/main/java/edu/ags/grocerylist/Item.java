package edu.ags.grocerylist;

public class Item {

    public String Name;
    public int CheckedState;
    public int Id;
    public int IsInCart;
    public String Owner;

    private String City;
    private Double Latitude;

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    private Double Longitude;

    public Item(int id , String name, int checkedState, int isInCart, String owner)
    {
        Id = id;
        Name = name;
        CheckedState = checkedState;
        IsInCart = isInCart;
        Owner = owner;
    }

    public String toString()
    {
        return Id + "|" + Name + "|" + CheckedState;
    }


    public int getId()
    {
        return Id;
    }
    public String getName(){return Name;}
    public int getCheckedState(){return CheckedState;}
    public Item()
    {
        Id = -1;
    }
    public void setName(String name)
    {
        Name = name;
    }
    public void setCheckedState(int checkedState) {CheckedState = checkedState;}
    public void setId(int id){Id = id;}
    public void setIsInCart(int isInCart){IsInCart = isInCart;}
    public int getIsInCart(){return IsInCart;}
    public String getOwner(){return Owner;}
    public void setOwner(String owner){Owner = owner;}

}
