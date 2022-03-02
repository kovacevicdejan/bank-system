/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import java.util.ArrayList;
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
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import requests.Request1;
import requests.Request2;

/**
 *
 * @author kd190167d
 */
public class Podsistem1 {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup="subsystem1Queue")
    private static Queue myQueue;
    
    @Resource(lookup="subsystem2Queue")
    private static Queue subsystem2Queue;
    
    @Resource(lookup="subsystem3Queue")
    private static Queue subsystem3Queue;
    
    @Resource(lookup="backupQueue")
    private static Queue backupQueue;
    
    @Resource(lookup="serverQueue")
    private static Queue serverQueue;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("podsistem1PU");
        EntityManager em = emf.createEntityManager();
        
        while(true) {
            Message msg = consumer.receive();
            
            if(msg instanceof ObjectMessage) {
                try {
                    ObjectMessage objMsg = (ObjectMessage)msg;
                    Request1 request = (Request1)objMsg.getObject();
                    int requestId = request.getRequestId();
                    String postBr, naziv, adresa, queue;
                    int idMes, idKom;
                    Mesto mesto;
                    Komitent komitent;
                    TextMessage textMsg;
                    ArrayList<Mesto> mesta;
                    ArrayList<Filijala> filijale;
                    ArrayList<Komitent> komitenti;
                    Request2 request2;
                    
                    switch(requestId) {
                        case 0:
                            postBr = request.getPostBr();
                            naziv = request.getNaziv();
                            
                            mesto = new Mesto();
                            mesto.setPostanskiBroj(postBr);
                            mesto.setNaziv(naziv);
                            
                            em.getTransaction().begin();
                            em.persist(mesto);
                            em.getTransaction().commit();
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Mesto je kreirano.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 1:
                            mesta = new ArrayList<>(em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList());
                            objMsg = context.createObjectMessage(mesta);
                            producer.send(serverQueue, objMsg);
                            break;
                        case 2:
                            naziv = request.getNaziv();
                            adresa = request.getAdresa();
                            idMes = request.getIdMes();
                            mesto = em.find(Mesto.class, idMes);
                            
                            if(mesto == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadato mesto.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            Filijala filijala = new Filijala();
                            filijala.setNaziv(naziv);
                            filijala.setAdresa(adresa);
                            filijala.setIdMes(mesto);
                            
                            em.getTransaction().begin();
                            em.persist(filijala);
                            em.getTransaction().commit();
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Filijala je kreirana.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 3:
                            filijale = new ArrayList<>(em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList());
                            objMsg = context.createObjectMessage(filijale);
                            producer.send(serverQueue, objMsg);
                            break;
                        case 4:
                            naziv = request.getNaziv();
                            adresa = request.getAdresa();
                            idMes = request.getIdMes();
                            mesto = em.find(Mesto.class, idMes);
                            
                            if(mesto == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadato mesto.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            komitent = new Komitent();
                            komitent.setNaziv(naziv);
                            komitent.setAdresa(adresa);
                            komitent.setIdMes(idMes);
                            
                            em.getTransaction().begin();
                            em.persist(komitent);
                            em.getTransaction().commit();
                            
                            request2 = new Request2();
                            request2.setRequestId(7);
                            request2.setKomitent(komitent);
                            objMsg = context.createObjectMessage(request2);
                            producer.send(subsystem2Queue, objMsg);
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Komitent je kreiran.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 5:
                            komitenti = new ArrayList<>(em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList());
                            objMsg = context.createObjectMessage(komitenti);
                            producer.send(serverQueue, objMsg);
                            break;
                        case 6:
                            idKom = request.getIdKom();
                            idMes = request.getIdMes();
                            komitent = em.find(Komitent.class, idKom);
                            mesto = em.find(Mesto.class, idMes);
                            
                            if(komitent == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadati komitent.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            if(mesto == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadato mesto.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
 
                            komitent.setIdMes(idMes);
                            
                            em.getTransaction().begin();
                            em.persist(komitent);
                            em.getTransaction().commit();
                            
                            request2 = new Request2();
                            request2.setRequestId(7);
                            request2.setKomitent(komitent);
                            objMsg = context.createObjectMessage(request2);
                            producer.send(subsystem2Queue, objMsg);
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Komitentu je promenjeno sediste.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 7:
                            queue = request.getQueue();
                            mesta = new ArrayList<>(em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList());
                            filijale = new ArrayList<>(em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList());
                            
                            ArrayList<ArrayList> all_data = new ArrayList<>();
                            all_data.add(mesta);
                            all_data.add(filijale);
                            
                            objMsg = context.createObjectMessage(all_data);
                            
                            if(queue.equals("backupQueue")) {
                                producer.send(backupQueue, objMsg);
                            }
                            else
                                producer.send(subsystem3Queue, objMsg);
                            
                            break;
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(Podsistem1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
