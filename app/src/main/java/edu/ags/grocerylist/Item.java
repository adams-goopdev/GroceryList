package edu.ags.grocerylist;

public class Item {
    public int Id;
    public String Name;
    public boolean CheckedState;

    public Item(int id, String name, boolean checkedState)
    {
        Id = id;
        Name = name;
        CheckedState = checkedState;
    }

    public String toString()
    {
        return Id + "|" + Name + "|" + CheckedState;
    }

}
