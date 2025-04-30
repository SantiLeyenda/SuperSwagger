package com.example.library.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.HashMap;
import java.util.List;




@RestController
@RequestMapping("/api/v2/books")
public class BookControllerV2 {
 
    @Autowired
    private BookRepository bookRepository;



    // REGRESA LOS LIBROS


    @Operation(summary = "Esto regresa todos los libros (V2)")

   

    @ApiResponse(
    responseCode = "200",
    description = "Lista de los libros",
    content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(value = "[{\"id\":1,\"title\":\"Libro A\",\"author\":\"Autor A\"}]")
    )
)

    @GetMapping 
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Operation(summary = "Regresa un libro por su id (V2)")
    @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Se encontro el libro",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = "{\"author\":\"Autor A\",\"version\":\"v2\"}")
        )
    ),
    @ApiResponse(responseCode = "404", description = "No se encontro el libro")
})


    


        @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookById(
        
    @Parameter(description = "Id del libro que quieres encontrar")
    @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("author", book.getAuthor());
                    response.put("version", "v2");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // HACES UN LIBRO NUEVO 


   
    @Operation(summary = "Haces un nuevo libro (v2)")
    @ApiResponse(
    responseCode = "201",
    description = "Libro creado correctamente con detalles",
    content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(value = "{\"message\":\"Libro creado exitosamente (v2)\",\"book\":{\"id\":1,\"title\":\"Nuevo Libro\"}}")
    )
    )


    @PostMapping
    public ResponseEntity<Map<String, Object>> createBook(@RequestBody Book book) {
    Book savedBook = bookRepository.save(book);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Libro creado exitosamente (v2)");
    response.put("book", savedBook);

    return ResponseEntity.status(201).body(response);
    }


    // ACTUALIZAS UN LIBRO

 

    @Operation(summary = "Esto sirve para actualizar un libro (V2)")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Se actualizo correctamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = "{\"message\":\"Libro actualizado con éxito (v2)\",\"updatedBook\":{\"id\":1,\"title\":\"Libro Actualizado\"}}")
            )
        ),
        @ApiResponse(responseCode = "404", description = "No se encontro el libro")
    })

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBook(
        @Parameter(description = "Id del libro que quieres actualizar")
        @PathVariable Long id,
        @RequestBody Book bookDetails) {
    
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setPublicationYear(bookDetails.getPublicationYear());
                    book.setGenre(bookDetails.getGenre());
                    book.setPages(bookDetails.getPages());
    
                    Book updatedBook = bookRepository.save(book);
    
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Libro actualizado con éxito (v2)");
                    response.put("updatedBook", updatedBook);
    
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(404)
                    .body(Map.of("error", "Libro no encontrado")));
    }
    


    // BORRAMOS UN LIBRO


    @Operation(summary = "Se borra un libro por su id (V2)")
    @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Se elimino correectamente el libro",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = "{\"message\":\"Libro eliminado correctamente (v2)\"}")
        )
    ),
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })


    @DeleteMapping("/{id}")
public ResponseEntity<Map<String, String>> deleteBook(
    @Parameter(description = "Id del libro que desea eliminar")
    @PathVariable Long id) {
    
    return bookRepository.findById(id)
            .map(book -> {
                bookRepository.delete(book);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Libro eliminado correctamente (v2)");
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.status(404).body(Map.of("error", "Libro no encontrado")));
}

}