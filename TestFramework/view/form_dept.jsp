<h2>Insertion Dept</h2>
<form action="${pageContext.request.contextPath}/dept-insert" method="post" enctype="multipart/form-data">
    <p>Id : <input type="number" name="Id"></p>
    <p>Nom : <input type="text" name="Nom"></p>
    <p>Fichier : <input type="file" name="Fichier"></p>
    <p><input type="submit" value="Inserer"></p>
</form>