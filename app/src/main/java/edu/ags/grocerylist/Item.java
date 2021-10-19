package edu.ags.grocerylist;

public class Item {

    public String Name;
    public int CheckedState;
    public int Id;

    public Item(int id , String name, int checkedState)
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

}
