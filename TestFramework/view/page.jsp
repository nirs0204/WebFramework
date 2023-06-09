<h1>Bonjour</h1>
<% int mot = (int) request.getAttribute("nombre"); %>
<p>Cle data : nombre</p>
<p>Valeur data : <% out.print(mot); %></p>