package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dao.ChambreRepository;
import dao.CommentaireRepository;
import dao.HotelRepository;
import dao.PrixRepository;
import dao.RatingRepository;
import dao.UserRepository;
import entities.Client;
import entities.Commentaire;
import entities.Prix;


@Controller
public class RechercheController {
	
	@Autowired
	private HotelRepository hotelRepository;
	
	@Autowired
	private ChambreRepository chambreRepository;
	
	@Autowired
	private PrixRepository prixRepository;
	
	@Autowired
	private CommentaireRepository commentaireRepository;
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value="/rechercheOffres",method=RequestMethod.POST)
	public String rechercheOffres(HttpServletRequest request,ModelMap model,@RequestParam String dest,@RequestParam String date_debut,@RequestParam String date_fin,@RequestParam int nbChambre)
	{	
		
		HttpSession session = request.getSession();
		
		List<Prix> offres =  prixRepository.getAllAvailableOffers(dest, date_debut, date_fin,nbChambre);
		offres = Prix.supprimerDoublonsHotel(offres);
		model.put("offres",offres);
		
		session.setAttribute("date_d", date_debut);
		session.setAttribute("date_f", date_fin);
		
		return "home";
	}
	
	@RequestMapping(value="/hebergements",method=RequestMethod.POST)
	public String hebergements(HttpServletRequest request,ModelMap model,@RequestParam Long id_hotel)
	{
		HttpSession session = request.getSession();
		String date_d = (String) session.getAttribute("date_d");
		String date_f = (String) session.getAttribute("date_f");
		session.setAttribute("id_hotel", id_hotel);
	
		
		//tout offres dipos dans l'hotel pour la reservation
		List<Prix> offres = prixRepository.getAllAvailableOffersByIdHotel(id_hotel, date_d, date_f);
		
		//supprimer doublons hotels (pour affichers une seule fois la categorie simple,double...)
		offres = Prix.supprimerDoublonsChambreCategorie(offres);
	
		//map pour compter nombre chambre dispo par categorie(simple,double...) chambre
		Map<Integer,Integer> nbChambresDispo = new HashMap<Integer,Integer>();
		for (Prix o : offres) {
			nbChambresDispo.put(o.getChambre().getType(), chambreRepository.getNbChambreDispoByType(o.getChambre().getType(), id_hotel, date_d, date_f));
		}
		model.put("offres",offres);
		model.put("nbChambres", nbChambresDispo);
		
		
		//commentaire
		List<Commentaire> commentaires = commentaireRepository.getAllByHotel(id_hotel);
		model.put("commentaires", commentaires);
		
		//rating
		Float avgRating = ratingRepository.getAvgRatingByHotel(id_hotel);
		if(avgRating==null) avgRating = new Float(0);
		model.put("avgRating",avgRating);
		
		
		
		return "hebergements";
	}
	
	@RequestMapping(value="/hebergements",method=RequestMethod.GET)
	public String hebergements(HttpServletRequest request,ModelMap model)
	{
		HttpSession session = request.getSession();
		String date_d = (String) session.getAttribute("date_d");
		String date_f = (String) session.getAttribute("date_f");
		Long id_hotel = (Long) session.getAttribute("id_hotel");
		
		if(id_hotel == null) return "redirect:/";
		
		//tout offres dipos dans l'hotel pour la reservation
		List<Prix> offres = prixRepository.getAllAvailableOffersByIdHotel(id_hotel, date_d, date_f);
		
		//supprimer doublons hotels (pour affichers une seule fois la categorie simple,double...)
		offres = Prix.supprimerDoublonsChambreCategorie(offres);
	
		//map pour compter nombre chambre dispo par categorie(simple,double...) chambre
		Map<Integer,Integer> nbChambresDispo = new HashMap<Integer,Integer>();
		for (Prix o : offres) {
			nbChambresDispo.put(o.getChambre().getType(), chambreRepository.getNbChambreDispoByType(o.getChambre().getType(), id_hotel, date_d, date_f));
		}
		model.put("offres",offres);
		model.put("nbChambres", nbChambresDispo);
		
		
		//commentaire
		List<Commentaire> commentaires = commentaireRepository.getAllByHotel(id_hotel);
		model.put("commentaires", commentaires);
		
		//rating
		Float avgRating = ratingRepository.getAvgRatingByHotel(id_hotel);
		if(avgRating==null) avgRating = new Float(0);
		model.put("avgRating",avgRating);
		
		
		
		
		return "hebergements";
	}
	

	@RequestMapping(value="/error",method=RequestMethod.GET)
	public String error()
	{
		return "404";
	}
}
