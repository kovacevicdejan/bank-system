/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klijent;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrofit.BankaService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * @author kd190167d
 */
public class Klijent {
    
    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/server/resources/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        
        BankaService service = retrofit.create(BankaService.class);
        Scanner scanner = new Scanner(System.in);
        String naziv, adresa, svrha;
        int idMes, idKom, dozMin, idRac, idRac1, idRac2, iznos, idFil;
        Call<String> response;
        boolean end = false;
        
        while(!end) {
            try {
                System.out.println("\n1. Kreiranje mesta\n" +
                                     "2. Kreiranje filijale u mestu\n" +
                                     "3. Kreiranje komitenta\n" +
                                     "4. Promena sedišta za zadatog komitenta\n" +
                                     "5. Otvaranje racuna\n" +
                                     "6. Zatvaranje racuna\n" +
                                     "7. Kreiranje transakcije koja je prenos sume sa jednog racuna na drugi racun\n" +
                                     "8. Kreiranje transakcije koja je uplata novca na racun\n" +
                                     "9. Kreiranje transakcije koja je isplata novca sa racuna\n" +
                                     "10. Dohvatanje svih mesta\n" +
                                     "11. Dohvatanje svih filijala\n" +
                                     "12. Dohvatanje svih komitenata\n" +
                                     "13. Dohvatanje svih racuna za komitenta\n" +
                                     "14. Dohvatanje svih transakcija za racun\n" +
                                     "15. Dohvatanje svih podataka iz rezervne kopije\n" +
                                     "16. Dohvatanje razlike u podacima u originalnim podacima i u rezervnoj kopiji\n\n" +
                                     "Vas zahtev?");
                
                int request = Integer.parseInt(scanner.nextLine());
                
                switch(request) {
                    case 1:
                        System.out.println("Unesite postanski broj:");
                        String postBr = scanner.nextLine();
                        System.out.println("Unesite naziv mesta:");
                        naziv = scanner.nextLine();
                        response = service.napraviMesto(postBr, naziv);
                        System.out.println(response.execute().body());
                        break;
                    case 2:
                        System.out.println("Unesite naziv filijale:");
                        naziv = scanner.nextLine();
                        System.out.println("Unesite adresu filijale:");
                        adresa = scanner.nextLine();
                        System.out.println("Unesite id mesta:");
                        idMes = Integer.parseInt(scanner.nextLine());
                        response = service.napraviFilijalu(naziv, adresa, idMes);
                        System.out.println(response.execute().body());
                        break;
                    case 3:
                        System.out.println("Unesite naziv komitenta:");
                        naziv = scanner.nextLine();
                        System.out.println("Unesite adresu komitenta:");
                        adresa = scanner.nextLine();
                        System.out.println("Unesite id sedista komitenta:");
                        idMes = Integer.parseInt(scanner.nextLine());
                        response = service.napraviKomitenta(naziv, adresa, idMes);
                        System.out.println(response.execute().body());
                        break;
                    case 4:
                        System.out.println("Unesite id komitenta:");
                        idKom = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite id novog sedista komitenta:");
                        idMes = Integer.parseInt(scanner.nextLine());
                        response = service.promeniSedisteZaKomitenta(idKom, idMes);
                        System.out.println(response.execute().body());
                        break;
                    case 5:
                        System.out.println("Unesite dozvoljeni minus:");
                        dozMin = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite id komitenta:");
                        idKom = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite id mesta:");
                        idMes = Integer.parseInt(scanner.nextLine());
                        response = service.otvoriRacun(dozMin, idKom, idMes);
                        System.out.println(response.execute().body());
                        break;
                    case 6:
                        System.out.println("Unesite id racuna:");
                        idRac = Integer.parseInt(scanner.nextLine());
                        response = service.zatvoriRacun(idRac);
                        System.out.println(response.execute().body());
                        break;
                    case 7:
                        System.out.println("Unesite id racuna sa kog se vrsi prenos:");
                        idRac1 = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite id racuna na koji se vrsi prenos:");
                        idRac2 = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite iznos:");
                        iznos = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite svrhu prenosa:");
                        svrha = scanner.nextLine();
                        response = service.napraviPrenos(idRac1, idRac2, iznos, svrha);
                        System.out.println(response.execute().body());
                        break;
                    case 8:
                        System.out.println("Unesite id racuna na koji se vrsi uplata:");
                        idRac = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite iznos:");
                        iznos = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite svrhu uplate:");
                        svrha = scanner.nextLine();
                        System.out.println("Unesite id filijale u kojoj se obavila uplata:");
                        idFil = Integer.parseInt(scanner.nextLine());
                        response = service.napraviUplatu(idRac, iznos, svrha, idFil);
                        System.out.println(response.execute().body());
                        break;
                    case 9:
                        System.out.println("Unesite id racuna sa kog se vrsi isplata:");
                        idRac = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite iznos:");
                        iznos = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite svrhu isplate:");
                        svrha = scanner.nextLine();
                        System.out.println("Unesite id filijale u kojoj se obavila isplata:");
                        idFil = Integer.parseInt(scanner.nextLine());
                        response = service.napraviIsplatu(idRac, iznos, svrha, idFil);
                        System.out.println(response.execute().body());
                        break;
                    case 10:
                        response = service.dohvatiMesta();
                        System.out.println(response.execute().body());
                        break;
                    case 11:
                        response = service.dohvatiFilijale();
                        System.out.println(response.execute().body());
                        break;
                    case 12:
                        response = service.dohvatiKomitente();
                        System.out.println(response.execute().body());
                        break;
                    case 13:
                        System.out.println("Unesite id komitenta:");
                        idKom = Integer.parseInt(scanner.nextLine());
                        response = service.dohvatiRacuneZaKomitenta(idKom);
                        System.out.println(response.execute().body());
                        break;
                    case 14:
                        System.out.println("Unesite id racuna:");
                        idRac = Integer.parseInt(scanner.nextLine());
                        response = service.dohvatiTransakcijeZaRacun(idRac);
                        System.out.println(response.execute().body());
                        break;
                    case 15:
                        response = service.dohvatiPodatkeRezervneKopije();
                        System.out.println(response.execute().body());
                        break;
                    case 16:
                        response = service.dohvatiRazlikuPodataka();
                        System.out.println(response.execute().body());
                        break;
                    case 0:
                        System.out.println("Zavrsetak rada.");
                        end = true;
                        scanner.close();
                        break;
                    default:
                        System.out.println("Nepostojeci zahtev, molimo Vas ponovo unesite zahtev.");
                        break;
                }
                Thread.sleep(3000);
            } catch (IOException ex) {
                Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
