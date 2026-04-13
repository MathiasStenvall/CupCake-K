package entities;

import java.util.ArrayList;
import java.util.List;

public class CupcakeList {

    private List<Cupcake> cupcakeList;

    public CupcakeList (){
        cupcakeList = new ArrayList<>();
    }

    public void addCupcake(Cupcake cupcake){
        cupcakeList.add(cupcake);
    }

    public List<Cupcake> getCupcakeList() {
        return cupcakeList;
    }

    public void findCupcakeID(Cupcake cupcake){

    }
}
