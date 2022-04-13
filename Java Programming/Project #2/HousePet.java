public abstract class    HousePet {  
protected String name, owner,favoriteFood ;

public HousePet() {
//     name = "Spot";
//     owner = "Donna";
//     favoriteFood = "cookies";
 } 

public HousePet(String n,String o,String f) {
   name=n;
   owner=o;
   favoriteFood=f;
}

public abstract void setName (String n); 
public abstract void setfavoriteFood  (String n); 
public abstract  void setOwner (String n);

public String toString() {
    String output = " I am " + name + " a house pet." 
                  + "\nMy favorite food is " + favoriteFood 
                  + ".\nMy owner is " +  owner+ ".";
   return output;
   }

} 

             
