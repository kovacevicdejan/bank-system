/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Racun;
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

@Path("racuni")
public class Racuni {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup="serverQueue")
    private Queue myQueue;
    
    @Resource(lookup="subsystem2Queue")
    private Queue subsystem2Queue;
    
    @POST
    @Path("{dozMin}/{idKom}/{idMes}")
    @Produces(MediaType.TEXT_PLAIN)
    public String otvoriRacun(@PathParam("dozMin") int dozMin, @PathParam("idKom") int idKom, @PathParam("idMes") int idMes) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequestId(0);
        request.setDozMin(dozMin);
        request.setIdKom(idKom);
        request.setIdMes(idMes);
        
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
    @Path("{idRac}")
    @Produces(MediaType.TEXT_PLAIN)
    public String zatvoriRacun(@PathParam("idRac") int idRac) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequestId(1);
        request.setIdRac(idRac);
        
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
    @Path("{idKom}")
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiRacuneZaKomitenta(@PathParam("idKom") int idKom) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request2 request = new Request2();
        request.setRequestId(2);
        request.setIdKom(idKom);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem2Queue, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage) {
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Racun> racuni = (ArrayList<Racun>)objMsg.getObject();
                String response = "";
                
                for(Racun r: racuni) {
                    response += r.toString();
                    
                    if(r != racuni.get(racuni.size() - 1))
                        response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Racuni.class.getName()).log(Level.SEVERE, null, ex);
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
