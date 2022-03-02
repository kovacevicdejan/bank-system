/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Transakcija;
import java.util.ArrayList;
import java.util.Date;
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
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import requests.Request2;

/**
 *
 * @author kd190167d
 */

@Path("transakcije")
public class Transakcije {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup="serverQueue")
    private Queue myQueue;
    
    @Resource(lookup="subsystem2Queue")
    private Queue subsystem2Queue;
    
    @POST
    @Path("prenos/{idRac1}/{idRac2}/{iznos}/{svrha}")
    @Produces(MediaType.TEXT_PLAIN)
    public String napraviPrenos(@PathParam("idRac1") int idRac1, @PathParam("idRac2") int idRac2,
            @PathParam("iznos") int iznos, @PathParam("svrha") String svrha) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequestId(3);
        request.setIdRac1(idRac1);
        request.setIdRac2(idRac2);
        request.setIznos(iznos);
        request.setSvrha(svrha);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem2Queue, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage) {
            try {
                TextMessage textMsg = (TextMessage)msg;
                String response = textMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "Primljen los tip poruke sa podsistema na server.";
    }
    
    @POST
    @Path("uplata/{idRac}/{iznos}/{svrha}/{idFil}")
    @Produces(MediaType.TEXT_PLAIN)
    public String napraviUplatu(@PathParam("idRac") int idRac, @PathParam("iznos") int iznos,
            @PathParam("svrha") String svrha, @PathParam("idFil") int idFil) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequestId(4);
        request.setIdRac(idRac);
        request.setIznos(iznos);
        request.setSvrha(svrha);
        request.setIdFil(idFil);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem2Queue, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage) {
            try {
                TextMessage textMsg = (TextMessage)msg;
                String response = textMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "Primljen los tip poruke sa podsistema na server.";
    }
    
    @POST
    @Path("isplata/{idRac}/{iznos}/{svrha}/{idFil}")
    @Produces(MediaType.TEXT_PLAIN)
    public String napraviIsplatu(@PathParam("idRac") int idRac, @PathParam("iznos") int iznos,
            @PathParam("svrha") String svrha, @PathParam("idFil") int idFil) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequestId(5);
        request.setIdRac(idRac);
        request.setIznos(iznos);
        request.setSvrha(svrha);
        request.setIdFil(idFil);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem2Queue, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof TextMessage) {
            try {
                TextMessage textMsg = (TextMessage)msg;
                String response = textMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "Primljen los tip poruke sa podsistema na server.";
    }
    
    @GET
    @Path("{idRac}")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiTransakcijeZaRacun(@PathParam("idRac") int idRac) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequestId(6);
        request.setIdRac(idRac);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem2Queue, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage) {
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Transakcija> transakcije = (ArrayList<Transakcija>)objMsg.getObject();
                String response = "";
                
                for(Transakcija t: transakcije) {
                    response += t.toString();
                    
                    if(t != transakcije.get(transakcije.size() - 1))
                        response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(msg instanceof TextMessage) {
            try {
                TextMessage textMsg = (TextMessage)msg;
                String response = textMsg.getText();
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Racuni.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "Primljen los tip poruke sa podsistema na server.";
    }
    
}
