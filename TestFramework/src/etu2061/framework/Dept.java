package etu2061.framework.modele;

import etu2061.framework.annotation.*;
import etu2061.framework.*;

public class Dept {
    int id;
    String name;

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

    public Dept(){}

    public Dept(int id, String name) {
        this.setId(id);
        this.setName(name);
    }

    @UrlAnnotation(url = "dept-insert")
    public ModelView saveDept(@Param(name = "id")int id, @Param(name = "nom")String name){
        ModelView mv = new ModelView("dept_insert.jsp");
        Dept dept = new Dept(id, name);
        mv.addItem("departement", dept);
        return mv;
    }
}
