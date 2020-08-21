/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerGame;

import jdk.nashorn.internal.runtime.JSONFunctions;
import sun.security.util.Password;
import com.google.gson.Gson;
import java.util.Iterator;

/**
 *
 * @author USUARIO
 */
public class Cuenta {

    private String usuario;
    private String password;
    private int isn;
    private boolean isconnected;

    public Cuenta(){
        this.usuario="";
        this.password="";
        this.isn=0;
        this.isconnected=false;
    }
    public Cuenta(String usuario, String password,int isn) {
        this.usuario = usuario;
        this.password = password;
        this.isconnected=false;
    }
       public Cuenta(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
        this.isconnected=false;
    }

    public String getUsuario() {
        return usuario;
    }

    public int getIsn() {
        return isn;
    }

    public void setIsn(int isn) {
        this.isn = isn;
    }

 

    public boolean isIsconnected() {
        return isconnected;
    }

    public void setIsconnected(boolean isconnected) {
        this.isconnected = isconnected;
    }
    
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void main(String[] args) throws InterruptedException {

        Cuenta c = new Cuenta("juan", "1234",1);
        
        Gson g=new Gson();
        
        String s=g.toJson(c);
        System.out.println(s);
        
        
        System.out.println(c.usuario);
        System.out.println(c.password);

    }

}
