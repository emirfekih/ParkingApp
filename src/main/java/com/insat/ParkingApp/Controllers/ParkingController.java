package com.insat.ParkingApp.Controllers;

import com.google.common.hash.Hashing;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.insat.ParkingApp.Dao.ParkingDao;
import com.insat.ParkingApp.Models.Parking;
import com.insat.ParkingApp.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * Created by ASUS on 04/04/2017.
 */
@RestController
public class ParkingController {
    @Autowired
    ParkingDao parkingDao;

    @RequestMapping(value="/addParking", method = RequestMethod.GET,headers="Accept=application/json")
    @ResponseBody
    public String create(double longitude, double latitude, String nom, String horaire, String placeDisponibles, Double moyenne) {
        Parking park = null;
        try {
            park = new Parking(longitude,latitude,nom,horaire,placeDisponibles,moyenne);
           parkingDao.save(park);
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "Parking succesfully added! (nom= " + park.getNom() + ")";
    }

    @RequestMapping(value="/findParking", method = RequestMethod.GET,headers="Accept=application/json")
    @ResponseBody
    public String findParkingDistance() {
DistanceMatrix trix = null;
        try {
            GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyA6rvM51qb0mlNn9P7CrhzuYTuu73n1Hvw");
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
           trix = req.origins("Vancouver BC","Seattle")
                    .destinations("San Francisco","Victoria BC")
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
                    .language("fr-FR")
                    .await();








        }
        catch (Exception ex) {
            return "Error Finding the Parking: " + ex.toString();
        }
        return trix.rows[0].elements[0].distance.toString();
    }


    @RequestMapping(value="/getParkings", method = RequestMethod.GET,headers="Accept=application/json")
    @ResponseBody
    public Iterable<Parking> fetchall() {


        Iterable<Parking> parkings =null;
        try {

            parkings = parkingDao.findAll();

        }
        catch (Exception ex) {
            // return "Error creating the user: " + ex.toString();

        }
        return parkings; }




    @RequestMapping(value="/deleteParking", method = RequestMethod.GET,headers="Accept=application/json")
    @ResponseBody
    public String delete(long id) {
        try {
            Parking user = parkingDao.findOne(id);
            parkingDao.delete(user);

        }
        catch (Exception ex) {
            return "Error deleting the Parking: " + ex.toString();
        }
        return "Parking succesfully deleted!";
    }

    @RequestMapping(value="/updateParking",method = RequestMethod.GET,headers="Accept=application/json")
    @ResponseBody
    public String updateParking(String nom,double longitude,double latitude, String nvnom,String horaire, String placeDisponibles) {
        try {
            Parking parking = parkingDao.findOneByNom(nom);

            parking.setLongitude(longitude);
            parking.setNom(nvnom);
            parking.setLatitude(latitude);
            parking.setHoraire(horaire);
            parking.setPlaceDisponibles(placeDisponibles);

            parkingDao.save(parking);

        }
        catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "Parking!";
    }
}
