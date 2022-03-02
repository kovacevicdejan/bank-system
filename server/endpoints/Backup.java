/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import entities.Racun;
import entities.Transakcija;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import requests.Request3;

/**
 *
 * @author kd190167d
 */

@Path("backup")
public class Backup {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup="serverQueue")
    private Queue myQueue;
    
    @Resource(lookup="subsystem3Queue")
    private Queue subsystem3Queue;
    
    @GET
    @Path("podaci")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiPodatkeRezervneKopije() {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request3 request = new Request3();
        request.setRequestId(0);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem3Queue, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage) {
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<ArrayList> all_data = (ArrayList<ArrayList>)objMsg.getObject();
                ArrayList<Mesto> mesta = all_data.get(0);
                ArrayList<Filijala> filijale = all_data.get(1);
                ArrayList<Komitent> komitenti = all_data.get(2);
                ArrayList<Racun> racuni = all_data.get(3);
                ArrayList<Transakcija> transakcije = all_data.get(4);
                String response = "Mesta:\n\n";
                
                for(Mesto m: mesta) {
                    response += m.toString();
                    
                    if(m != mesta.get(mesta.size() - 1))
                        response += "\n";
                }
                
                response += "\n\nFilijale:\n\n";
                
                for(Filijala f: filijale) {
                    response += f.toString();
                    
                    if(f != filijale.get(filijale.size() - 1))
                        response += "\n";
                }
                
                response += "\n\nKomitenti:\n\n";
                
                for(Komitent k: komitenti) {
                    response += k.toString();
                    
                    if(k != komitenti.get(komitenti.size() - 1))
                        response += "\n";
                }
                
                response += "\n\nRacuni:\n\n";
                
                for(Racun r: racuni) {
                    response += r.toString();
                    
                    if(r != racuni.get(racuni.size() - 1))
                        response += "\n";
                }
                
                response += "\n\nTransakcije:\n\n";
                
                for(Transakcija t: transakcije) {
                    response += t.toString();
                    
                    if(t != transakcije.get(transakcije.size() - 1))
                        response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Racuni.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "Primljen los tip poruke sa podsistema na server;";
    }
    
    @GET
    @Path("razlika")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiRazlikuPodataka() {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request3 request = new Request3();
        request.setRequestId(1);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem3Queue, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage) {
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<ArrayList> data_diff = (ArrayList<ArrayList>)objMsg.getObject();
                ArrayList<Mesto> mesta_diff = data_diff.get(0);
                ArrayList<Filijala> filijale_diff = data_diff.get(1);
                ArrayList<Komitent> komitenti_diff = data_diff.get(2);
                ArrayList<Racun> racuni_diff = data_diff.get(3);
                ArrayList<Transakcija> transakcije_diff = data_diff.get(4);
                String response = "Mesta - razlika:\n\n";
                
                for(Mesto m: mesta_diff) {
                    response += m.toString();
                    
                    if(!m.equals(mesta_diff.get(mesta_diff.size() - 1)))
                        response += "\n";
                }
                
                response += "\n\nFilijale - razlika:\n\n";
                
                for(Filijala f: filijale_diff) {
                    response += f.toString();
                    
                    if(f != filijale_diff.get(filijale_diff.size() - 1))
                        response += "\n";
                }
                
                response += "\n\nKomitenti - razlika:\n\n";
                
                for(Komitent k: komitenti_diff) {
                    response += k.toString();
                    
                    if(k != komitenti_diff.get(komitenti_diff.size() - 1))
                        response += "\n";
                }
                
                response += "\n\nRacuni - razlika:\n\n";
                
                for(Racun r: racuni_diff) {
                    response += r.toString();
                    
                    if(r != racuni_diff.get(racuni_diff.size() - 1))
                        response += "\n";
                }
                
                response += "\n\nTransakcije - razlika:\n\n";
                
                for(Transakcija t: transakcije_diff) {
                    response += t.toString();
                    
                    if(t != transakcije_diff.get(transakcije_diff.size() - 1))
                        response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Racuni.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "Primljen los tip poruke sa podsistema na server;";
    }
    
}
