package etu2061.framework.servlet;
import etu2061.framework.Mapping;
import etu2061.framework.annotation.UrlAnnotation;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> MappingUrls = new HashMap<>();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            String requestUrl = request.getRequestURL().toString();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Ma Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            for (HashMap.Entry<String, Mapping> entry : MappingUrls.entrySet()) {
                String key = entry.getKey();
                Mapping value = entry.getValue();
                out.println("<p>Annotation : " + key + "/ Classe : "+ value.getClassName() +"/ Methode : "+ value.getMethod() +"</p>");
            }
            out.println("<p>URL de la requete: " + requestUrl + "</p>");
            out.println("</html>");
            out.println("</body>");

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

    public  String[] getClasse(String path){
        ArrayList<String> listeClasse=new ArrayList<String>();
        File modele = new File(path);
        String[] classeTab = modele.list();
        for(int i=0; i < classeTab.length; i++){
            String [] java = classeTab[i].split("[.]");
            if(java[1].equalsIgnoreCase("class")){
                listeClasse.add(java[0]);
            }
        }
        return listeClasse.toArray(new String[listeClasse.size()]);
    }

    public  void annotation(String path){
        try {
            String[] classe = getClasse(path);
            for(int i =0 ; i< classe.length; i++){
                String className  = "etu2061.framework.modele." +classe[i];
                Class<?> notreClasse = Class.forName(className);
                Method [] methods = notreClasse.getDeclaredMethods();
                for (Method method : methods) {
                    Annotation[] url = method.getAnnotations();
                    if(url.length > 0 ){
                        UrlAnnotation annot = methods[i].getAnnotation(UrlAnnotation.class);
                        System.out.println(annot.toString());
                        Mapping mapping = new Mapping(classe[i],method.getName());         
                        MappingUrls.put(annot.url(), mapping);
                        //System.out.println(annot.url()+" / "+classe[i]+" / "+methods[i].getName());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        MappingUrls = new  HashMap<String, Mapping>();
        String path = "etu2061/framework/modele/";
        annotation(path);
    }
}
