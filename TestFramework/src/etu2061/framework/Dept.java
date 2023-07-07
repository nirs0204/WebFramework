package etu2061.framework.modele;

import etu2061.framework.annotation.*;
import etu2061.framework.*;

public class Dept {
    int id;
    String name;
    FileUpload file;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public FileUpload getFile() {
        return file;
    }
    public void setFile(FileUpload file) {
        this.file = file;
    }

    public Dept(){}

    public Dept(int id, String name, FileUpload file) {
        this.setId(id);
        this.setName(name);
        this.setFile(file);
    }

    @UrlAnnotation(url = "dept-insert")
    public ModelView saveDept(@Param(name = "id")int id, @Param(name = "nom")String name, @Param(name = "Fichier")FileUpload file){
        ModelView mv = new ModelView("dept_insert.jsp");
        Dept dept = new Dept(id, name, file);
        mv.addItem("departement", dept);
        return mv;
    }
}
