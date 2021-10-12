package edu.ags.grocerylist;

public class Item {

    public String Name;
    public boolean CheckedState;
    public int Id;

    public Item(int id , String name, boolean checkedState)
    {
        Id = id;
        Name = name;
        CheckedState = checkedState;
    }

    public String toString()
    {
        return Id + "|" + Name + "|" + CheckedState;
    }


    public int getId()
    {
        return Id;
    }

    public Item()
    {
        Id = -1;
    }
    public void setName(String name)
    {
        Name = name;
    }
    public void setCheckedState(Boolean checkedState) {CheckedState = checkedState;}
    public void setId(int id){Id = id;}

}
