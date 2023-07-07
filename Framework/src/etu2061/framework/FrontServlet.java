package etu2061.framework.servlet;
import etu2061.framework.*;
import etu2061.framework.annotation.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;


public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> MappingUrls;
    HashMap<Class<?>, Object> singletonMappings;

    public void init(){
        MappingUrls = new HashMap<String, Mapping>();
        ArrayList<String> classes = getClassNames("D:/JDK/S4/WebFramework/TestFramework/WEB-INF/classes/etu2061/framework/modele");
        ArrayList<String> classesAnnotees = getClassesWithAnnotatedMethods(classes);
        setUrlMapping(classesAnnotees);
        ArrayList<String> classesAnnoted = getClassesAnnotated(classes);
        setSingletonMapping(classesAnnoted);
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

                    Method[] tabMeth = classe.getDeclaredMethods();
                    for (Method method : tabMeth) {
                        if (method.getName().equals(my.getMethod())) {
                            Collection<Part> parts = request.getParts();
                            Enumeration<String> paramNames = request.getParameterNames();
                            Field[] attribut = classe.getDeclaredFields();
                            Parameter[] tabparams = method.getParameters();
                            Class<?>[] paramTypes = method.getParameterTypes();
                            Object[] arguments = new Object[paramTypes.length];
                            Object obj = classe.getConstructor().newInstance();

                            if (tabparams.length!=0) {
                                methodWithParameter(parts, paramNames, tabparams, paramTypes, arguments, request);
                            } else {
                                methodWithoutParameter(parts, classe, obj, paramNames, attribut, request);
                            }
                            Class<?> methodReturn = method.getReturnType();
                            if (ModelView.class.isAssignableFrom(methodReturn)) {
                                ModelView mv = null;
                                if (arguments.length!=0) {
                                    mv = (ModelView) method.invoke(obj, arguments);
                                } else {
                                    mv = (ModelView) method.invoke(obj);
                                }
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

    public void getFileUpload(Part filepart) {
        try (InputStream io = filepart.getInputStream()) {
            ByteArrayOutputStream buffers = new ByteArrayOutputStream();
            byte[] buffer = new byte[(int) filepart.getSize()];
            int read;
            while ((read = io.read(buffer, 0, buffer.length)) != -1) {
                buffers.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] parts = contentDisposition.split(";");
        for (String partStr : parts) {
            if (partStr.trim().startsWith("filename"))
                return partStr.substring(partStr.indexOf('=') + 1).trim().replace("\"", "");
        }
        return null;
    }

    // sprint 8 + sprint 9
    public void methodWithParameter(Collection<Part> parts, Enumeration<String> paramNames, Parameter[] tabparams, Class<?>[] paramTypes, Object[] arguments, HttpServletRequest request) throws ServletException, IOException{
        // Parcourir les noms des paramètres
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            // Vérifier si le nom du paramètre correspond à une annotation Param
            for (int i = 0; i < tabparams.length; i++) {
                Annotation an = tabparams[i].getAnnotation(Param.class);
                if (an instanceof Param) {
                    Param p = (Param) an;
                    // Si le nom du paramètre correspond à l'annotation, affecter la valeur appropriée
                    if (p.name().equalsIgnoreCase(paramName)) {
                        String paramValue = request.getParameter(paramName);
                        // Affecter les valeurs des arguments en fonction de leur type
                        if (paramTypes[i] == String.class) {
                            arguments[i] = paramValue;
                        } else if (paramTypes[i] == int.class) {
                            arguments[i] = Integer.parseInt(paramValue);
                        } else if (paramTypes[i] == double.class) {
                            arguments[i] = Double.parseDouble(paramValue);
                        } else if (paramTypes[i] == FileUpload.class) {
                            Part filePart = request.getPart(paramName);
                            String fileName = filePart.getSubmittedFileName();
                            long fileSize = filePart.getSize();
                            Byte[] fileSizeBytes = convertToByteArray(fileSize);
                            FileUpload fileUp = new FileUpload(fileName, fileSizeBytes);
                            arguments[i] = fileUp;
                        }
                    }
                }
            }
        }
        for (Part part : parts) {
            for (int i = 0; i < tabparams.length; i++) {
                Annotation an = tabparams[i].getAnnotation(Param.class);
                if (an instanceof Param) {
                    Param p = (Param) an;
                    // Si le nom du paramètre correspond à l'annotation, affecter la valeur appropriée
                    if (p.name().equalsIgnoreCase(part.getName()) && paramTypes[i] == FileUpload.class) {
                        String fileName = getFileName(part);
                        long fileSize = part.getSize();
                        Byte[] fileSizeBytes = convertToByteArray(fileSize);
                        getFileUpload(part);
                        FileUpload fileUp = new FileUpload(fileName, fileSizeBytes);
                        arguments[i] = fileUp;
                    }
                }
            }
        }
    }

    private Byte[] convertToByteArray(long value) {
        Byte[] byteArray = new Byte[Long.SIZE / Byte.SIZE];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) (value >> (i * 8));
        }
        return byteArray;
    }
    
    // sprint 7
    public void methodWithoutParameter(Collection<Part> parts, Class<?> classe, Object obj, Enumeration<String> paramNames, Field[] attribut, HttpServletRequest request)throws ServletException, IOException{
        try {
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                for (int i = 0; i < attribut.length; i++) {
                    if (attribut[i].getName().equalsIgnoreCase(paramName)) {
                        Method meth = classe.getDeclaredMethod("set"+paramName, attribut[i].getType());
                        String paramValue = request.getParameter(paramName);
                        if(attribut[i].getType().getSimpleName().equals("int")){
                            meth.invoke(obj, Integer.valueOf(paramValue));
                        }
                        else if(attribut[i].getType() == String.class){
                            meth.invoke(obj, paramValue);
                        }
                        else if(attribut[i].getType().getSimpleName().equals("double")){
                            meth.invoke(obj, Double.valueOf(paramValue));
                        }
                    }
                }
            }
            for (Part part : parts) {
                for (int i = 0; i < attribut.length; i++) {
                    // Si le nom du paramètre correspond à l'annotation, affecter la valeur appropriée
                    if (attribut[i].getName().equalsIgnoreCase(part.getName()) && attribut[i].getType() == FileUpload.class) {
                        Method meth = classe.getDeclaredMethod("set"+part.getName(), attribut[i].getType());
                        String fileName = getFileName(part);
                        long fileSize = part.getSize();
                        Byte[] fileSizeBytes = convertToByteArray(fileSize);
                        getFileUpload(part);
                        FileUpload fileUp = new FileUpload(fileName, fileSizeBytes);
                        meth.invoke(obj, fileUp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Deprecated
    public void setSingletonMapping(ArrayList<String> classnames){
        for (String classename : classnames) {
            try {
                String nameclass = "etu2061.framework.modele."+classename;
                Class<?> clazz = Class.forName(nameclass);
                if (clazz.isAnnotationPresent(Scope.class)) {
                    Scope annotation = clazz.getAnnotation(Scope.class);
                    Boolean annotationValue = annotation.singleton();
                    if (annotationValue) {
                        Object obj = clazz.newInstance();
                        singletonMappings.put(clazz.getClass(), obj);  
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Recupere les classes qui sont annotees
    public ArrayList<String> getClassesAnnotated(ArrayList<String> classNames) {
        ArrayList<String> annotatedClasses = new ArrayList<>();
        for (String className : classNames) {
            try {
                String nameclass = "etu2061.framework.modele."+className; 
                Class<?> clazz = Class.forName(nameclass);
                if (clazz.isAnnotationPresent(Scope.class)) {
                    annotatedClasses.add(className);
                    break;
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
