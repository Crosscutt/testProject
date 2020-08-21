/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ServerGame;

import java.util.ArrayList;

/**
 *
 * @author USUARIO
 */
public class Partida {
    private int Id;
    private ArrayList<Cuenta> jugadores;
    private boolean[] statusInvitation;
    private boolean isPlaying;
    
    public Partida(int Id,ArrayList<Cuenta> jugadores,boolean isPlaying){
        this.Id=Id;
        this.jugadores=jugadores;
        this.isPlaying=isPlaying;
        this.statusInvitation=new boolean[jugadores.size()];
    }

    public boolean[] getStatusInvitation() {
        return statusInvitation;
    }

    public void setStatusInvitation(boolean[] statusInvitation) {
        this.statusInvitation = statusInvitation;
    }
    
    public void setStatusIndex(int i,boolean  status){
        this.statusInvitation[i]=status;
    }

    public ArrayList<Cuenta> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Cuenta> jugadores) {
        this.jugadores = jugadores;
    }

    public boolean isIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
    
    
}
