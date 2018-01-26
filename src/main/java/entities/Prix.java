package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="PRIX")
public class Prix {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idPrix;
    private Double prix;
    private Date dateDebut;
    private Date dateFin;
    private Double tauxReduction;
    
    @ManyToOne
    @JoinColumn(name="ID_CHAMBRE")
    private Chambre chambre;
            
    public Prix() {
    }

    public Prix(Double prix, Date dateDebut, Date dateFin, Double tauxReduction) {
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tauxReduction = tauxReduction;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Double getTauxReduction() {
        return tauxReduction;
    }

    public void setTauxReduction(Double tauxReduction) {
        this.tauxReduction = tauxReduction;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }
    
    //supprime doublons des hotels
    static public List<Prix> supprimerDoublonsHotel(List<Prix> offres)
    {
    	List<Long> instances = new ArrayList<Long>();
    	
    	for(int i=0 ; i < offres.size() ; i++)
    	{
    		if(instances.contains(offres.get(i).getChambre().getHotel().getIdHotel()))
			{
				offres.remove(i);
			}
    		else{
    			instances.add(offres.get(i).getChambre().getHotel().getIdHotel());
    		}
    	   
    	}
    	return offres;
    }
    
  //supprime doublons des chambres (simple,double...)
    static public List<Prix> supprimerDoublonsChambreCategorie(List<Prix> offres)
    {
    	List<String> instances = new ArrayList<String>();
    	
    	for(int i=0 ; i < offres.size() ; i++)
    	{
    		if(instances.contains(offres.get(i).getChambre().getDescription()))
			{
				offres.remove(i);
			}
    		else{
    			instances.add(offres.get(i).getChambre().getDescription());
    		}
    	   
    	}
    	return offres;
    }

}
