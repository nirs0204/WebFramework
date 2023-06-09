package etu2061.framework.servlet;
import etu2061.framework.Mapping;
import etu2061.framework.ModelView;
import etu2061.framework.annotation.UrlAnnotation;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;


public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> MappingUrls;

    public void init(){
        MappingUrls = new HashMap<String, Mapping>();
        ArrayList<String> classes = getClassNames("C:/Program Files/Apache Software Foundation/Tomcat 10.0/webapps/WebFramework2/WEB-INF/classes/etu2061/framework/modele");
        ArrayList<String> classesAnnotees = getClassesWithAnnotatedMethods(classes);
        setUrlMapping(classesAnnotees);
    }

    @Deprecated
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();

            String requestUrl = request.getRequestURL().toString();
            String url = requestUrl.substring(requestUrl.lastIndexOf("/")+1);

            String viewName = "";
            for (Map.Entry<String, Mapping> ma : MappingUrls.entrySet()) {
                if (ma.getKey().equals(url)) {
                    Mapping my = ma.getValue();
                    Class<?> classe = Class.forName("etu2061.framework.modele."+my.getClassName());
                    Method method = classe.getDeclaredMethod(my.getMethod());
                    Class<?> methodReturn = method.getReturnType();
                    Object obj = classe.newInstance();
                    if (ModelView.class.isAssignableFrom(methodReturn)) {
                        ModelView mv = (ModelView) method.invoke(obj);
                        viewName = mv.getUrl();
                        String cle = "";
                        Object valeur = new Object();
                        for (Map.Entry<String, Object> data : mv.getData().entrySet()) {
                            cle = data.getKey();
                            valeur = data.getValue();
                            request.setAttribute(cle, valeur);
                        }
                        RequestDispatcher dispat = request.getRequestDispatcher("view/"+viewName);
                        dispat.forward(request, response);
                        
                    } else throw new Exception("Ne retourne pas de ModelView");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    // Remplir MappingUrls
    public void setUrlMapping(ArrayList<String> classnames){
        for (String classename : classnames) {
            try {
                String nameclass = "etu2061.framework.modele."+classename;
                Class<?> clazz = Class.forName(nameclass);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(UrlAnnotation.class)) {
                        UrlAnnotation annotation = method.getAnnotation(UrlAnnotation.class);
                        String annotationValue = annotation.url();
                        String methodename = method.getName();
                        Mapping mapping = new Mapping(classename, methodename);
                        MappingUrls.put(annotationValue, mapping);  
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Recupere les classes qui ont des methodes annotees
    public ArrayList<String> getClassesWithAnnotatedMethods(ArrayList<String> classNames) {
        ArrayList<String> annotatedClasses = new ArrayList<>();
        for (String className : classNames) {
            try {
                String nameclass = "etu2061.framework.modele."+className; 
                Class<?> clazz = Class.forName(nameclass);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(UrlAnnotation.class)) {
                        annotatedClasses.add(className);
                        break;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return annotatedClasses;
    }
  
    // recupere les classes dans un packages ou path
    public ArrayList<String> getClassNames(String directoryPath) {
        ArrayList<String> classNames = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            scanClasses(directory, "", classNames);
        }
        return classNames;
    }

    private void scanClasses(File directory, String packageName, List<String> classNames) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classNames.add(className.substring(1));
                } else if (file.isDirectory()) {
                    String subPackageName = packageName + '.' + file.getName();
                    scanClasses(file, subPackageName, classNames);
                }
            }
        }
    }
}
