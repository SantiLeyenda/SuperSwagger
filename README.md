# SuperSwagger

Para correr el programe baje el repo.
Abra el repo en visual studio. 
En la terminal vaya a donde este su folder de LibraryManagement.
Ya ahi, escribe mvn spring-boot:run y corralo. 
La aplicacion debe de correr en su localhost en el puerto que este escrito en el archivo de properties. 


Para poder usar los endpoints, es necesario usar el endpoint de log in usando admin y admin123. 
Al obtener el JWT token entonces es requerido picarle al candado para poder acceder y entonces usar todos los endpoints. 
Al inicial abran dos libros ya en la base de datos. 
Usando el endpoint de regresesar todos los libros podra ver los libros que existen y los ids que tienen para poder usarlos para los otros endpoints. 
Puede probar los endpoints de las dos diferentes versiones para ver las diferencias. 
Algunos endpoints si hacen o regresan csoas diferentes, otros solo regresan algo adicional. 
Para esta tarea se uso URL versioning. 
