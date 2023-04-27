package etu2061.framework.servlet;

import etu2061.framework.Mapping;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> MappingUrls;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            String requestUrl = request.getRequestURL().toString();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Ma Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Bienvenue sur ma Servlet!</h1>");
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
}
