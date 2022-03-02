/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kd190167d
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdTra", query = "SELECT t FROM Transakcija t WHERE t.idTra = :idTra"),
    @NamedQuery(name = "Transakcija.findByRedniBroj", query = "SELECT t FROM Transakcija t WHERE t.redniBroj = :redniBroj"),
    @NamedQuery(name = "Transakcija.findByIznos", query = "SELECT t FROM Transakcija t WHERE t.iznos = :iznos"),
    @NamedQuery(name = "Transakcija.findByTip", query = "SELECT t FROM Transakcija t WHERE t.tip = :tip"),
    @NamedQuery(name = "Transakcija.findBySvrha", query = "SELECT t FROM Transakcija t WHERE t.svrha = :svrha"),
    @NamedQuery(name = "Transakcija.findByDatumVreme", query = "SELECT t FROM Transakcija t WHERE t.datumVreme = :datumVreme"),
    @NamedQuery(name = "Transakcija.findByIdFil", query = "SELECT t FROM Transakcija t WHERE t.idFil = :idFil")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdTra")
    private Integer idTra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RedniBroj")
    private int redniBroj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Iznos")
    private int iznos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Tip")
    private Character tip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Svrha")
    private String svrha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Column(name = "IdFil")
    private Integer idFil;
    @JoinColumn(name = "IdRac1", referencedColumnName = "IdRac")
    @ManyToOne(optional = false)
    private Racun idRac1;
    @JoinColumn(name = "IdRac2", referencedColumnName = "IdRac")
    @ManyToOne
    private Racun idRac2;

    public Transakcija() {
    }

    public Transakcija(Integer idTra) {
        this.idTra = idTra;
    }

    public Transakcija(Integer idTra, int redniBroj, int iznos, Character tip, String svrha, Date datumVreme) {
        this.idTra = idTra;
        this.redniBroj = redniBroj;
        this.iznos = iznos;
        this.tip = tip;
        this.svrha = svrha;
        this.datumVreme = datumVreme;
    }

    public Integer getIdTra() {
        return idTra;
    }

    public void setIdTra(Integer idTra) {
        this.idTra = idTra;
    }

    public int getRedniBroj() {
        return redniBroj;
    }

    public void setRedniBroj(int redniBroj) {
        this.redniBroj = redniBroj;
    }

    public int getIznos() {
        return iznos;
    }

    public void setIznos(int iznos) {
        this.iznos = iznos;
    }

    public Character getTip() {
        return tip;
    }

    public void setTip(Character tip) {
        this.tip = tip;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public Integer getIdFil() {
        return idFil;
    }

    public void setIdFil(Integer idFil) {
        this.idFil = idFil;
    }

    public Racun getIdRac1() {
        return idRac1;
    }

    public void setIdRac1(Racun idRac1) {
        this.idRac1 = idRac1;
    }

    public Racun getIdRac2() {
        return idRac2;
    }

    public void setIdRac2(Racun idRac2) {
        this.idRac2 = idRac2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTra != null ? idTra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTra == null && other.idTra != null) || (this.idTra != null && !this.idTra.equals(other.idTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String string = String.format("%-2d", idTra) + "  " + String.format("%-2d", redniBroj) + "  " 
                + String.format("%-8d", iznos) + "  " + tip + "  " + String.format("%-20s", svrha) + "  " + datumVreme + "  " 
                + String.format("%-4d", idRac1.getIdRac()) + "  ";
        
        if(idRac2 != null)
            string += String.format("%-4d", idRac2.getIdRac()) + "  ";
        else
            string += "null  ";
        
        if(idFil != null)
            string += String.format("%-4d", idFil);
        else
            string += "null";
        
        return string;
    }
    
}
