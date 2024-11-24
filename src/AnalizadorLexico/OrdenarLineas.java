/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import java.util.Comparator;

/**
 *
 * @author hp
 */
public class OrdenarLineas implements Comparator<Token>{
    public int compare(Token o1, Token o2){
        if (o1.getLine()<o2.getLine()) {
            return -1;
        }else if(o1.getLine()<o2.getLine()){
          return 0;
        }else{
            return 1;
        }
    }
}
