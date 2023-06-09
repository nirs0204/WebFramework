package etu2061.framework.modele;

import etu2061.framework.annotation.UrlAnnotation;
import etu2061.framework.*;

public class Emp {
    String nom;
    double salaire;

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public double getSalaire() {
        return salaire;
    }
    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public Emp(){}

    public Emp(String nom, double salaire){
        this.setNom(nom);
        this.setSalaire(salaire);
    }

    @UrlAnnotation(url = "emp-all")
    public ModelView findAll(){
        ModelView mv = new ModelView("page.jsp");
        int valeur = 20;
        mv.addItem("nombre", valeur);
        return mv;
    }

    @UrlAnnotation(url = "insert-emp")
    public ModelView save(){
        ModelView mv = new ModelView("emp_insert.jsp");
        Emp employe = new Emp(this.getNom(), this.getSalaire());
        mv.addItem("employe", employe);
        return mv;
    }
}
