package etu2061.framework;
import etu2061.framework.ModelView;
import etu2061.framework.annotation.*;

public class FileUpload {
    String name;
    Byte[] taille;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Byte[] getTaille() {
        return taille;
    }
    public void setTaille(Byte[] taille) {
        this.taille = taille;
    }
    
    public FileUpload(){}

    public FileUpload(String name, Byte[] taille){
        this.setName(name);
        this.setTaille(taille);
    }
}