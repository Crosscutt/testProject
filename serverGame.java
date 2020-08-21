/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerGame;

import Eventos.MenssageEvent;
import Eventos.Registro;
import Eventos.Verification;
import Servidor.Mensaje;
import Servidor.Servidor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class serverGame implements InterfaceEvents {

    private Servidor server;
    private ArrayList<Cuenta> cuentas;
    private Gson json;
    private ArrayList<Partida> partidas;
    private int numPartida;

    public serverGame(int PORT) throws IOException {
        server = new Servidor(PORT);
        server.init();
        server.setServerGame(this);
        this.cuentas = new ArrayList<>();
        this.partidas = new ArrayList<>();
        this.json = new Gson();
        this.numPartida = 0;
    }

    public Servidor getServer() {
        return server;
    }

    public void setServer(Servidor server) {
        this.server = server;
    }

    public ArrayList<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(ArrayList<Partida> partidas) {
        this.partidas = partidas;
    }

    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(ArrayList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    @Override
    public void Mensaje(MenssageEvent msg) {
        String data = (String) msg.getMsg();
        Mensaje ct = json.fromJson(data.toString(), Mensaje.class);
        System.out.println(ct.getComando());
        if (!ct.getComando().equals(" ")) {
            switch (ct.getComando()) {
                case "LOGIN":
                    VerificarCuenta(ct);
                    break;
                case "REGISTER":
                    RegistrarCuenta(ct);
                    break;
                case "MENSAGE":
                    this.server.enviarMensaje(ct);
                    break;
                case "INVITATION":
                    this.invitation(ct);
                    break;
                case "VERIFYINVITATION":
                    this.verificarInvitacion(ct);
                    break;
            }
        }
    }

    @Override
    public void Desconecto(int key) {
        Cuenta cuenta = changeStatus(key);
        ArrayList<Integer> destinatarios = new ArrayList<>();
        for (Cuenta cuenta_p : this.usuariosConectados()) {  
            destinatarios.add(cuenta_p.getIsn());
        }
        for (Partida partida : this.getPartidas()) {  // ponemos en false la partida si esque un jugador se desconecta
            if (partida.getJugadores().get(0).getIsn() == cuenta.getIsn() || partida.getJugadores().get(1).getIsn() == cuenta.getIsn()) {
                partida.setIsPlaying(false);
            }
        }
        Mensaje mensaje = new Mensaje(0, cuenta, destinatarios, "DELETEUSER");
        this.server.enviarMensaje(mensaje);
    }

    private Cuenta changeStatus(int Key) { /// cuando un usuario de desconecta solo cambiamos su estado a false
        for (Cuenta cuenta : this.getCuentas()) {
            if (cuenta.getIsn() == Key) {
                cuenta.setIsconnected(false);
                return cuenta;
            }
        }
        return null;
    }

    private void VerificarCuenta(Mensaje ct) {
        Cuenta cuenta = json.fromJson(ct.getBody().toString(), Cuenta.class);
        for (int i = 0; i < cuentas.size(); i++) {
            Cuenta cuent_pivote = cuentas.get(i);
            if (cuent_pivote.getUsuario().equals(cuenta.getUsuario()) && cuent_pivote.getPassword().equals(cuenta.getPassword())) {
                cuent_pivote.setIsn(cuenta.getIsn());
                cuent_pivote.setIsconnected(true);
                enviarLista(ct); // envamos la lista al nuevo usuario logeado
                enviarUsuario(cuenta); // enviamos el nuevo usuario conectado a todos los que ya estan logeados
                System.out.println("Si existe");
            }
        }
    }

    private void RegistrarCuenta(Mensaje ct) {
        Cuenta cuenta = json.fromJson(ct.getBody().toString(), Cuenta.class);
        if (!existeCuenta(cuenta)) {   // Verificamos si la cuenta existe 
            this.cuentas.add(cuenta);
            this.server.enviarMensaje(new Mensaje(ct.getOrigen(), "Registro exitoso", ct.getDestino(), " "));
        } else {
            this.server.enviarMensaje(new Mensaje(ct.getOrigen(), "El usuario ya existe", ct.getDestino(), " "));
        }

    }

    public boolean existeCuenta(Cuenta ct) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getUsuario() == ct.getUsuario()) {
                return true;
            }
        }
        return false;
    }

    private void enviarLista(Mensaje ct) {

        ArrayList<Cuenta> auxList = usuariosConectados();
        Mensaje msg = new Mensaje(ct.getOrigen(), auxList, ct.getDestino(), "LISTUSERS");
        this.server.enviarMensaje(msg);
    }

    public ArrayList<Cuenta> usuariosConectados() {
        ArrayList<Cuenta> auxList = new ArrayList<>();
        for (Cuenta cuenta : this.cuentas) {
            if (cuenta.isIsconnected()) {
                auxList.add(cuenta);
            }
        }
        return auxList;
    }

    private void enviarUsuario(Cuenta ct) {
        ArrayList<Cuenta> auxList = usuariosConectados();
        ArrayList<Integer> sendList = new ArrayList<>();
        for (Cuenta cuenta : auxList) {
            if (!(ct.getUsuario().equals(cuenta.getUsuario()) && ct.getPassword().equals(cuenta.getPassword()))) {
                sendList.add(cuenta.getIsn());
            }
        }
        this.server.enviarMensaje(new Mensaje(0, ct, sendList, "UPDATELIST"));
    }

    private void invitation(Mensaje ct) {
        this.numPartida = this.numPartida + 1;
        java.lang.reflect.Type listType = new TypeToken<ArrayList<Cuenta>>() {
        }.getType();
        ArrayList<Cuenta> jugadoresList = json.fromJson(ct.getBody().toString(), listType);
        Partida partida = new Partida(this.numPartida, jugadoresList, false);   /// creamos la partida con su id y la lista de jugadores
        this.partidas.add(partida);/// Añadimos a la lista de partidas del serverGame
        VerifInvitation VInvitation = new VerifInvitation(partida.getId(), false, ct.getOrigen());
        Mensaje newMsg = new Mensaje(0, VInvitation, ct.getDestino(), "INVITATION");
        this.server.enviarMensaje(newMsg);
    }

    private void verificarInvitacion(Mensaje ct) {
        VerifInvitation verifinvitation = this.json.fromJson(ct.getBody().toString(), VerifInvitation.class);
        for (Partida piv : partidas) {
            if (piv.getId() == verifinvitation.getId()) {
                if (verifinvitation.isState()) {
                    piv.setIsPlaying(true);
                    this.server.enviarMensaje(new Mensaje(ct.getOrigen(), "Partida creada exitosamente", ct.getDestino(), "MENSAGE"));
                } else {
                    this.server.enviarMensaje(new Mensaje(ct.getOrigen(), "No se pudo crear la partida", ct.getDestino(), "MENSAGE"));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        serverGame sg = new serverGame(5000);
    }

}
