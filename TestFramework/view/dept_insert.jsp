<%@page import="etu2061.framework.modele.Dept" %>
<% Dept dep = (Dept) request.getAttribute("departement"); %>
<h2>Infos sur le departement</h2>
<p>Id du dept : <% out.print(dep.getId()); %></p>
<p>Nom du dept : <% out.print(dep.getName()); %></p>