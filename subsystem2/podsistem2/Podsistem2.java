/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entities.Komitent;
import entities.Racun;
import entities.Transakcija;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.TypedQuery;
import requests.Request2;

/**
 *
 * @author kd190167d
 */
public class Podsistem2 {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup="subsystem2Queue")
    private static Queue myQueue;
    
    @Resource(lookup="serverQueue")
    private static Queue serverQueue;
    
    @Resource(lookup="backupQueue")
    private static Queue backupQueue;
    
    @Resource(lookup="subsystem3Queue")
    private static Queue subsystem3Queue;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("podsistem2PU");
        EntityManager em = emf.createEntityManager();
        
        while(true) {
            Message msg = consumer.receive();
            
            if(msg instanceof ObjectMessage) {
                try {
                    ObjectMessage objMsg = (ObjectMessage)msg;
                    Request2 request = (Request2)objMsg.getObject();
                    int requestId = request.getRequestId();
                    Racun racun;
                    int dozMin, idMes, idKom, idRac, idRac1, idRac2, idFil, iznos, redniBroj;
                    Komitent komitent;
                    String svrha, query, queue;
                    Transakcija transakcija;
                    TextMessage textMsg;
                    ArrayList<Racun> racuni;
                    ArrayList<Transakcija> transakcije;
                    ArrayList<Komitent> komitenti;
                    
                    switch(requestId) {
                        case 0:
                            dozMin = request.getDozMin();
                            idKom = request.getIdKom();
                            idMes = request.getIdMes();
                            komitent = em.find(Komitent.class, idKom);
                            
                            if(komitent == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadati komitent.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                                
                            racun = new Racun();
                            racun.setStanje(0);
                            racun.setDozvoljeniMinus(dozMin);
                            
                            if(dozMin <= 0)
                                racun.setStatus('A');
                            else
                                racun.setStatus('B');
                            
                            racun.setDatumVreme(new Date());
                            racun.setBrojTransakcija(0);
                            racun.setIdKom(komitent);
                            racun.setIdMes(idMes);
                            
                            em.getTransaction().begin();
                            em.persist(racun);
                            em.getTransaction().commit();
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Racun je otvoren.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 1:
                            idRac = request.getIdRac();
                            racun = em.find(Racun.class, idRac);
                            
                            if(racun == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadati racun.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            racun.setStatus('U');
                            
                            em.getTransaction().begin();
                            em.persist(racun);
                            em.getTransaction().commit();
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Racun je zatvoren.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 2:
                            idKom = request.getIdKom();
                            komitent = em.find(Komitent.class, idKom);
                            
                            if(komitent == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadati komitent.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            query = "select r from Racun r where r.idKom = :komitent";
                            TypedQuery<Racun> tq1 = em.createQuery(query, Racun.class).setParameter("komitent", komitent);
                            racuni = new ArrayList<>(tq1.getResultList());    
                            objMsg = context.createObjectMessage(racuni);
                            producer.send(serverQueue, objMsg);
                            break;
                        case 3:
                            idRac1 = request.getIdRac1();
                            idRac2 = request.getIdRac2();
                            iznos = request.getIznos();
                            svrha = request.getSvrha();
                            
                            Racun racun1 = em.find(Racun.class, idRac1);
                            Racun racun2 = em.find(Racun.class, idRac2);
                            
                            if(racun1 == null || racun2 == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji neki od zadatih racuna.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            if(racun1.getStatus() == 'U' || racun2.getStatus() == 'U') {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Neki od racuna je ugasen.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            if(racun1.getStatus() == 'B') {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Racun sa kog se uzima novac je blokiran.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            redniBroj = racun1.getBrojTransakcija() + 1;
                            
                            transakcija = new Transakcija();
                            transakcija.setRedniBroj(redniBroj);
                            transakcija.setIznos(iznos);
                            transakcija.setTip('P');
                            transakcija.setSvrha(svrha);
                            transakcija.setDatumVreme(new Date());
                            transakcija.setIdRac1(racun1);
                            transakcija.setIdRac2(racun2);

                            racun1.setBrojTransakcija(redniBroj);
                            racun1.setStanje(racun1.getStanje() - iznos);

                            if(racun1.getStanje() < racun1.getDozvoljeniMinus())
                                racun1.setStatus('B');

                            racun2.setBrojTransakcija(racun2.getBrojTransakcija() + 1);
                            racun2.setStanje(racun2.getStanje() + iznos);

                            if(racun2.getStanje() >= racun2.getDozvoljeniMinus())
                                racun2.setStatus('A');

                            em.getTransaction().begin();
                            em.persist(racun1);
                            em.persist(racun2);
                            em.persist(transakcija);
                            em.getTransaction().commit();
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Prenos je izvrsen.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 4:
                            idRac = request.getIdRac();
                            iznos = request.getIznos();
                            svrha = request.getSvrha();
                            idFil = request.getIdFil();
                            
                            racun = em.find(Racun.class, idRac);
                            
                            if(racun == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadati racun.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            if(racun.getStatus() == 'U') {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Racun je ugasen.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            redniBroj = racun.getBrojTransakcija() + 1;
                            
                            transakcija = new Transakcija();
                            transakcija.setRedniBroj(redniBroj);
                            transakcija.setIznos(iznos);
                            transakcija.setTip('U');
                            transakcija.setSvrha(svrha);
                            transakcija.setDatumVreme(new Date());
                            transakcija.setIdRac1(racun);
                            transakcija.setIdRac2(null);
                            transakcija.setIdFil(idFil);

                            racun.setBrojTransakcija(redniBroj);
                            racun.setStanje(racun.getStanje() + iznos);

                            if(racun.getStanje() >= racun.getDozvoljeniMinus())
                                racun.setStatus('A');

                            em.getTransaction().begin();
                            em.persist(racun);
                            em.persist(transakcija);
                            em.getTransaction().commit();
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Uplata je izvrsena.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 5:
                            idRac = request.getIdRac();
                            iznos = request.getIznos();
                            svrha = request.getSvrha();
                            idFil = request.getIdFil();
                            
                            racun = em.find(Racun.class, idRac);
                            
                            if(racun == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadati racun.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            if(racun.getStatus() == 'U') {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Racun je ugasen.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            if(racun.getStatus() == 'B') {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Racun je blokiran.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            redniBroj = racun.getBrojTransakcija() + 1;
                            
                            transakcija = new Transakcija();
                            transakcija.setRedniBroj(redniBroj);
                            transakcija.setIznos(iznos);
                            transakcija.setTip('I');
                            transakcija.setSvrha(svrha);
                            transakcija.setDatumVreme(new Date());
                            transakcija.setIdRac1(racun);
                            transakcija.setIdRac2(null);
                            transakcija.setIdFil(idFil);

                            racun.setBrojTransakcija(redniBroj);
                            racun.setStanje(racun.getStanje() - iznos);

                            if(racun.getStanje() < racun.getDozvoljeniMinus())
                                racun.setStatus('B');

                            em.getTransaction().begin();
                            em.persist(racun);
                            em.persist(transakcija);
                            em.getTransaction().commit();
                            
                            textMsg = context.createTextMessage();
                            textMsg.setText("Isplata je izvrsena.");
                            producer.send(serverQueue, textMsg);
                            break;
                        case 6:
                            idRac = request.getIdRac();
                            racun = em.find(Racun.class, idRac);
                            
                            if(racun == null) {
                                textMsg = context.createTextMessage();
                                textMsg.setText("Ne postoji zadati racun.");
                                producer.send(serverQueue, textMsg);
                                break;
                            }
                            
                            query = "select t from Transakcija t where (t.idRac1 = :racun or t.idRac2 = :racun)";
                            TypedQuery<Transakcija> tq2 = em.createQuery(query, Transakcija.class).setParameter("racun", racun);
                            transakcije = new ArrayList<>(tq2.getResultList());
                            objMsg = context.createObjectMessage(transakcije);
                            producer.send(serverQueue, objMsg);
                            break;
                        case 7:
                            komitent = request.getKomitent();
                            em.getTransaction().begin();
                            em.merge(komitent);
                            em.getTransaction().commit();
                            break;
                        case 8:
                            queue = request.getQueue();
                            komitenti = new ArrayList<>(em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList());
                            racuni = new ArrayList<>(em.createNamedQuery("Racun.findAll", Racun.class).getResultList());
                            transakcije = new ArrayList<>(em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList());
                            
                            ArrayList<ArrayList> all_data = new ArrayList<>();
                            all_data.add(komitenti);
                            all_data.add(racuni);
                            all_data.add(transakcije);
                            
                            objMsg = context.createObjectMessage(all_data);
                            
                            if(queue.equals("backupQueue"))
                                producer.send(backupQueue, objMsg);
                            else
                                producer.send(subsystem3Queue, objMsg);
                            
                            break;
                    }
                } catch (JMSException ex) {
                    Logger.getLogger(Podsistem2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
