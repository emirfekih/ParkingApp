package com.insat.ParkingApp.Controllers;

import com.insat.ParkingApp.Dao.ParkingDao;
import com.insat.ParkingApp.Models.Parking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value="/getParkings", method = RequestMethod.GET,headers="Accept=application/json")
    @ResponseBody
    public Iterable<Parking> fetchall() {

        // Long idformateur=null;
        Iterable<Parking> users =null;
        try {
            users = parkingDao.findAll();

        }
        catch (Exception ex) {
            // return "Error creating the user: " + ex.toString();

        }
        return users; }



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
}
