/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import entities.Racun;
import entities.Transakcija;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Queue;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import requests.Request1;
import requests.Request2;
import requests.Request3;

/**
 *
 * @author kd190167d
 */
public class Podsistem3 {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup="subsystem3Queue")
    private static Queue myQueue;
    
    @Resource(lookup="serverQueue")
    private static Queue serverQueue;
    
    @Resource(lookup="backupQueue")
    private static Queue backuprQueue;
    
    @Resource(lookup="subsystem1Queue")
    private static Queue subsystem1Queue;
    
    @Resource(lookup="subsystem2Queue")
    private static Queue subsystem2Queue;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(myQueue);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("podsistem3PU");
        EntityManager em = emf.createEntityManager();
        
        BackupSaver backup_saver = new BackupSaver(connectionFactory, backuprQueue, subsystem1Queue, subsystem2Queue, em);
        backup_saver.start();
        
        while(true) {
            Message msg = consumer.receive();
            
            if(msg instanceof ObjectMessage) {
                try {
                    ObjectMessage objMsg = (ObjectMessage)msg;
                    Request3 zahtev = (Request3)objMsg.getObject();
                    int zahtevId = zahtev.getRequestId();
                    ArrayList<Mesto> mesta;
                    ArrayList<Filijala> filijale;
                    ArrayList<Komitent> komitenti;
                    ArrayList<Racun> racuni;
                    ArrayList<Transakcija> transakcije;
                    
                    switch(zahtevId) {
                        case(0):
                            mesta = new ArrayList<>(em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList());
                            filijale = new ArrayList<>(em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList());
                            komitenti = new ArrayList<>(em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList());
                            racuni = new ArrayList<>(em.createNamedQuery("Racun.findAll", Racun.class).getResultList());
                            transakcije = new ArrayList<>(em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList());
                            
                            ArrayList<ArrayList> all_data = new ArrayList<>();
                            all_data.add(mesta);
                            all_data.add(filijale);
                            all_data.add(komitenti);
                            all_data.add(racuni);
                            all_data.add(transakcije);
                            
                            objMsg = context.createObjectMessage(all_data);
                            producer.send(serverQueue, objMsg);
                            break;
                        case 1:
                            mesta = new ArrayList<>(em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList());
                            filijale = new ArrayList<>(em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList());
                            komitenti = new ArrayList<>(em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList());
                            racuni = new ArrayList<>(em.createNamedQuery("Racun.findAll", Racun.class).getResultList());
                            transakcije = new ArrayList<>(em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList());
                            
                            HashMap<Integer, Mesto> mesta_hashmap = new HashMap<>();
                            HashMap<Integer, Filijala> filijale_hashmap = new HashMap<>();
                            HashMap<Integer, Komitent> komitenti_hashmap = new HashMap<>();
                            HashMap<Integer, Racun> racuni_hashmap = new HashMap<>();
                            HashMap<Integer, Transakcija> transakcije_hashmap = new HashMap<>();
                            
                            for(Mesto m: mesta) {
                                mesta_hashmap.put(m.getIdMes(), m);
                            }
                            
                            for(Filijala f: filijale) {
                                filijale_hashmap.put(f.getIdFil(), f);
                            }
                            
                            for(Komitent k: komitenti) {
                                komitenti_hashmap.put(k.getIdKom(), k);
                            }
                            
                            for(Racun r: racuni) {
                                racuni_hashmap.put(r.getIdRac(), r);
                            }
                            
                            for(Transakcija t: transakcije) {
                                transakcije_hashmap.put(t.getIdTra(), t);
                            }
                            
                            
                            Request1 request1 = new Request1();
                            request1.setRequestId(7);
                            request1.setQueue("subsystem3Queue");

                            objMsg = context.createObjectMessage(request1);
                            producer.send(subsystem1Queue, objMsg);
                            msg = consumer.receive();
                            
                            objMsg = (ObjectMessage)msg;
                            all_data = (ArrayList <ArrayList>)objMsg.getObject();
                            ArrayList<Mesto> mesta_podsistem1 = all_data.get(0);
                            ArrayList<Filijala> filijale_podsistem1 = all_data.get(1);
                            
                            
                            Request2 request2 = new Request2();
                            request2.setRequestId(8);
                            request2.setQueue("subsystem3Queue");

                            objMsg = context.createObjectMessage(request2);
                            producer.send(subsystem2Queue, objMsg);
                            msg = consumer.receive();
                            
                            objMsg = (ObjectMessage)msg;
                            all_data = (ArrayList <ArrayList>)objMsg.getObject();
                            ArrayList<Komitent> komitenti_podsistem2 = all_data.get(0);
                            ArrayList<Racun> racuni_podsistem2 = all_data.get(1);
                            ArrayList<Transakcija> transakcije_podsistem2 = all_data.get(2);
                          
                            
                            ArrayList<Mesto> mesta_diff = new ArrayList<>();
                            ArrayList<Filijala> filijale_diff = new ArrayList<>();
                            ArrayList<Komitent> komitenti_diff = new ArrayList<>();
                            ArrayList<Racun> racuni_diff = new ArrayList<>();
                            ArrayList<Transakcija> transakcije_diff = new ArrayList<>();
                            
                            for(Mesto m: mesta_podsistem1) {
                                if(!mesta_hashmap.containsKey(m.getIdMes()))
                                    mesta_diff.add(m);
                            }
                            
                            for(Filijala f: filijale_podsistem1) {
                                if(!filijale_hashmap.containsKey(f.getIdFil()))
                                    filijale_diff.add(f);
                            }
                            
                            for(Komitent k: komitenti_podsistem2) {
                                if(!komitenti_hashmap.containsKey(k.getIdKom()))
                                    komitenti_diff.add(k);
                                else {
                                    Komitent komitent = komitenti_hashmap.get(k.getIdKom());
                                    
                                    if(k.getIdMes() != komitent.getIdMes()) {
                                        komitenti_diff.add(k);
                                        komitenti_diff.add(komitent);
                                    }
                                }
                            }
                            
                            for(Racun r: racuni_podsistem2) {
                                if(!racuni_hashmap.containsKey(r.getIdRac()))
                                    racuni_diff.add(r);
                                else {
                                    Racun racun = racuni_hashmap.get(r.getIdRac());
                                    
                                    if((r.getStanje() != racun.getStanje()) || !r.getStatus().equals(racun.getStatus())) {
                                        racuni_diff.add(r);
                                        racuni_diff.add(racun);
                                    }
                                }
                            }
                            
                            for(Transakcija t: transakcije_podsistem2) {
                                if(!transakcije_hashmap.containsKey(t.getIdTra()))
                                    transakcije_diff.add(t);
                            }
                            
                            ArrayList<ArrayList> data_diff = new ArrayList<>();
                            data_diff.add(mesta_diff);
                            data_diff.add(filijale_diff);
                            data_diff.add(komitenti_diff);
                            data_diff.add(racuni_diff);
                            data_diff.add(transakcije_diff);
                            
                            objMsg = context.createObjectMessage(data_diff);
                            producer.send(serverQueue, objMsg);
                            break;
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(Podsistem3.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
