package com.example.toshiba.virtualdt;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 05/01/2017.
 */

public class Cells {


    ArrayList<Cell> cells ;

    public Cells(){
        this.cells=new ArrayList<Cell>();
    }


    public void addCell(Cell c){
        if(c!=null){
            cells.add(c);
        }
        else {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Cell getServingCell(){
        if(cells.get(0)!=null){
            return cells.get(0);
        }
        else{
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public ArrayList<Cell> getCellNeighbors(){
        if(cells!=null && cells.size()>1){
         ArrayList<Cell> temp=new ArrayList<Cell>();
          for(int i=1;i<cells.size();i++){
              temp.add(cells.get(i));
          }
        return temp;
        }
        else{
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Cell> getAllCells(){
        return this.cells;
    }


    public boolean findCellById(Cell c){
        for(Cell e:cells){
            if(e.getId()==c.getId()){
                return true;
            }
        }
        return false;
    }


    public void filterSigleCell() {
        int i = 0;
        for (i = 0; i < this.cells.size(); i++) {
            if(i==0){
                cells.get(i).setSigle("Cellule de service");
            }
            else{
                cells.get(i).setSigle("Cellule voisine #"+i);
                cells.get(i).setMethod_access_type(19); // type de reseau inconnu
            }

        }

    }


}
