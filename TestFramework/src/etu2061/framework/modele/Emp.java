package etu2061.framework.modele;

import etu2061.framework.annotation.UrlAnnotation;

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
    public void findAll(){
        System.out.println("Listes de tous les employes");
    }
}
