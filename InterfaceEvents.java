/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ServerGame;

import Eventos.MenssageEvent;

import java.util.EventListener;

/**
 *
 * @author USUARIO
 */
public interface InterfaceEvents extends EventListener{
    void Mensaje(MenssageEvent msg);
    void Desconecto(int key);
}
