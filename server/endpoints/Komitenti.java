/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Komitent;
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
import requests.Request1;

/**
 *
 * @author kd190167d
 */

@Path("komitenti")
public class Komitenti {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup="serverQueue")
    private Queue myQueue;
    
    @Resource(lookup="subsystem1Queue")
    private Queue subsystem1Queue;
    
    @POST
    @Path("{naziv}/{adresa}/{idMes}")
    @Produces(MediaType.TEXT_PLAIN)
    public String napraviKomitenta(@PathParam("naziv") String naziv, @PathParam("adresa") String adresa, @PathParam("idMes") int idMes) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request1 request = new Request1();
        request.setRequestId(4);
        request.setNaziv(naziv);
        request.setAdresa(adresa);
        request.setIdMes(idMes);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem1Queue, objMsg);
        
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
    @Produces(MediaType.TEXT_PLAIN)
    public String dohvatiKomitente() {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request1 request = new Request1();
        request.setRequestId(5);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem1Queue, objMsg);
        
        Message msg = consumer.receive();
        consumer.close();
        context.close();
        
        if(msg instanceof ObjectMessage) {
            try {
                objMsg = (ObjectMessage)msg;
                ArrayList<Komitent> komitenti = (ArrayList<Komitent>)objMsg.getObject();
                String response = "";
                
                for(Komitent k: komitenti) {
                    response += k.toString();
                    
                    if(k != komitenti.get(komitenti.size() - 1))
                        response += "\n";
                }
                
                return response;
            } catch (JMSException ex) {
                Logger.getLogger(Mesta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "Primljen los tip poruke sa podsistema na server.";
    }
    
    @POST
    @Path("{idKom}/{idMes}")
    @Produces(MediaType.TEXT_PLAIN)
    public String promeniSedisteZaKomitenta(@PathParam("idKom") int idKom, @PathParam("idMes") int idMes) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(myQueue);
        JMSProducer producer = context.createProducer();
        
        Request1 request = new Request1();
        request.setRequestId(6);
        request.setIdKom(idKom);
        request.setIdMes(idMes);
        
        ObjectMessage objMsg = context.createObjectMessage(request);
        producer.send(subsystem1Queue, objMsg);
        
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
    
}
