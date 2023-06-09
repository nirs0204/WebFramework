<%@page import="etu2061.framework.modele.Emp" %>
<% Emp empl = (Emp) request.getAttribute("employe"); %>
<h2>Infos sur l'employe</h2>
<p>Nom de l'employe : <% out.print(empl.getNom()); %></p>
<p>Salaire de l'employe : <% out.print(empl.getSalaire()); %></p>