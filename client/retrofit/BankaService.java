/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *
 * @author kd190167d
 */
public interface BankaService {
    
    @POST("mesta/{postBr}/{naziv}")
    Call<String> napraviMesto(@Path("postBr") String postBr, @Path("naziv") String naziv);
    
    @GET("mesta")
    Call<String> dohvatiMesta();
    
    @POST("filijale/{naziv}/{adresa}/{idMes}")
    Call<String> napraviFilijalu(@Path("naziv") String naziv, @Path("adresa") String adresa, @Path("idMes") int idMes);
    
    @GET("filijale")
    Call<String> dohvatiFilijale();
    
    @POST("komitenti/{naziv}/{adresa}/{idMes}")
    Call<String> napraviKomitenta(@Path("naziv") String naziv, @Path("adresa") String adresa, @Path("idMes") int idMes);
    
    @GET("komitenti")
    Call<String> dohvatiKomitente();
    
    @POST("komitenti/{idKom}/{idMes}")
    Call<String> promeniSedisteZaKomitenta(@Path("idKom") int idKom, @Path("idMes") int idMes);
    
    @POST("racuni/{dozMin}/{idKom}/{idMes}")
    Call<String> otvoriRacun(@Path("dozMin") int dozMin, @Path("idKom") int idKom, @Path("idMes") int idMes);
    
    @POST("racuni/{idRac}")
    Call<String> zatvoriRacun(@Path("idRac") int idRac);
    
    @GET("racuni/{idKom}")
    Call<String> dohvatiRacuneZaKomitenta(@Path("idKom") int idKom);
    
    @POST("transakcije/prenos/{idRac1}/{idRac2}/{iznos}/{svrha}")
    Call<String> napraviPrenos(@Path("idRac1") int idRac1, @Path("idRac2") int idRac2, @Path("iznos") int iznos, @Path("svrha") String svrha);
    
    @POST("transakcije/uplata/{idRac}/{iznos}/{svrha}/{idFil}")
    Call<String> napraviUplatu(@Path("idRac") int idRac, @Path("iznos") int iznos, @Path("svrha") String svrha, @Path("idFil") int idFil);
    
    @POST("transakcije/isplata/{idRac}/{iznos}/{svrha}/{idFil}")
    Call<String> napraviIsplatu(@Path("idRac") int idRac, @Path("iznos") int iznos, @Path("svrha") String svrha, @Path("idFil") int idFil);
    
    @GET("transakcije/{idRac}")
    Call<String> dohvatiTransakcijeZaRacun(@Path("idRac") int idRac);
    
    @GET("backup/podaci")
    Call<String> dohvatiPodatkeRezervneKopije();
    
    @GET("backup/razlika")
    Call<String> dohvatiRazlikuPodataka();
    
}
