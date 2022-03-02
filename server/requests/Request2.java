/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Komitent;
import java.io.Serializable;

/**
 *
 * @author kd190167d
 */
public class Request2 implements Serializable {
    
    private int requestId;
    private int dozMin;
    private int idKom;
    private int idMes;
    private int idRac;
    private int idRac1;
    private int idRac2;
    private int iznos;
    private String svrha;
    private int idFil;
    private Komitent komitent;
    private String queue;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getDozMin() {
        return dozMin;
    }

    public void setDozMin(int dozMin) {
        this.dozMin = dozMin;
    }

    public int getIdKom() {
        return idKom;
    }

    public void setIdKom(int idKom) {
        this.idKom = idKom;
    }

    public int getIdMes() {
        return idMes;
    }

    public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

    public int getIdRac() {
        return idRac;
    }

    public void setIdRac(int idRac) {
        this.idRac = idRac;
    }

    public int getIdRac1() {
        return idRac1;
    }

    public void setIdRac1(int idRac1) {
        this.idRac1 = idRac1;
    }

    public int getIdRac2() {
        return idRac2;
    }

    public void setIdRac2(int idRac2) {
        this.idRac2 = idRac2;
    }

    public int getIznos() {
        return iznos;
    }

    public void setIznos(int iznos) {
        this.iznos = iznos;
    }

     public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public int getIdFil() {
        return idFil;
    }

    public void setIdFil(int idFil) {
        this.idFil = idFil;
    }
    
    public Komitent getKomitent() {
        return komitent;
    }

    public void setKomitent(Komitent komitent) {
        this.komitent = komitent;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
    
}
