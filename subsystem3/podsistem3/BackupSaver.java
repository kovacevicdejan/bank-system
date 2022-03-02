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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import requests.Request1;
import requests.Request2;

/**
 *
 * @author kd190167d
 */
public class BackupSaver extends Thread {
    
    private ConnectionFactory connectionFactory;
    private Queue myQueue;
    private Queue subsystem1Queue;
    private Queue subsystem2Queue;
    private EntityManager em;
    
    public BackupSaver(ConnectionFactory connectionFactory, Queue myQueue,
            Queue subsystem1Queue, Queue subsystem2Queue, EntityManager em) {
        this.connectionFactory = connectionFactory;
        this.myQueue = myQueue;
        this.subsystem1Queue = subsystem1Queue;
        this.subsystem2Queue = subsystem2Queue;
        this.em = em;
    }

    @Override
    public void run() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(myQueue);
        
        Message msg;
        ObjectMessage objMsg;
        ArrayList<ArrayList> all_data;
        
        while(true) {
            try {
                Request1 request1 = new Request1();
                request1.setRequestId(7);
                request1.setQueue("backupQueue");
                
                objMsg = context.createObjectMessage(request1);
                producer.send(subsystem1Queue, objMsg);
                msg = consumer.receive();
                
                objMsg = (ObjectMessage)msg;
                all_data = (ArrayList <ArrayList>)objMsg.getObject();
                ArrayList<Mesto> mesta_podsistem1 = all_data.get(0);
                ArrayList<Filijala> filijale_podsistem1 = all_data.get(1);
                
                
                Request2 request2 = new Request2();
                request2.setRequestId(8);
                request2.setQueue("backupQueue");
                
                objMsg = context.createObjectMessage(request2);
                producer.send(subsystem2Queue, objMsg);
                msg = consumer.receive();
               
                objMsg = (ObjectMessage)msg;
                all_data = (ArrayList <ArrayList>)objMsg.getObject();
                ArrayList<Komitent> komitenti_podsistem2 = all_data.get(0);
                ArrayList<Racun> racuni_podsistem2 = all_data.get(1);
                ArrayList<Transakcija> transakcije_podsistem2 = all_data.get(2);
                
                em.getTransaction().begin();
                
                for(Mesto m: mesta_podsistem1) {
                    em.merge(m);
                    em.flush();
                }

                for(Filijala f: filijale_podsistem1) {
                    em.merge(f);
                    em.flush();
                }

                for(Komitent k: komitenti_podsistem2) {
                    em.merge(k);
                    em.flush();
                }

                for(Racun r: racuni_podsistem2) {
                    em.merge(r);
                    em.flush();
                }

                for(Transakcija t: transakcije_podsistem2) {
                    em.merge(t);
                    em.flush();
                }
                
                em.getTransaction().commit();
                
                System.out.println("backup saving finished");
                Thread.sleep(120000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BackupSaver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JMSException ex) {
                Logger.getLogger(BackupSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
