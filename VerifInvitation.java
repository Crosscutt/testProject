/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ServerGame;

/**
 *
 * @author USUARIO
 */
public class VerifInvitation {
    private int Id;
    private boolean state;
    private int anfitrion;
    
    public VerifInvitation(int id,boolean state,int anfitrion){
        this.Id=id;
        this.state=state;
        this.anfitrion=anfitrion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(int anfitrion) {
        this.anfitrion = anfitrion;
    }
    
}
