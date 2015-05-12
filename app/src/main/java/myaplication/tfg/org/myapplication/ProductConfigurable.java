package myaplication.tfg.org.myapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jin on 2015/4/15.
 */
public class ProductConfigurable  extends Product implements Serializable{
   private String type;
    private HashMap<String,ProductSimple> productsSimple;

    public ProductConfigurable(){
        productsSimple = new HashMap<String,ProductSimple>();
    }
    public ProductConfigurable(String product_id,int image, String title, String price, String description, String size){
        super(product_id,image,title,price,description,size);
    }

    public void setType(String type){
        this.type = type;
    }
    public String getType(String type){
        return this.type;
    }

    public void addNewSimpleProduct(ProductSimple p){
        productsSimple.put(p.getProduct_id(),p);
    }

     public ProductSimple getSimpleProduct(String product_id){
         return productsSimple.get(product_id);
     }

    public void setListProductSimple(HashMap<String,ProductSimple> simple_product){
        this.productsSimple=simple_product;
    }
    public HashMap<String,ProductSimple> getListProductSimple(){
        return this.productsSimple;
    }
}
