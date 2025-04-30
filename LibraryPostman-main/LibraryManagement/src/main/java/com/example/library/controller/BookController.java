package com.example.library.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// aqui abajo estan todo lo de Swagger 
// Los que dicen operation son su description
// los que dicen APIResponse es basicamente si sale bien que despliega y si sale mal que despliega


@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Operation(summary = "Esto regresa todos los libros")

    @ApiResponse(responseCode = "200", description = "Lista de los libros")


    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Operation(summary = "Regresa un libro por su id")
    @ApiResponses({

        @ApiResponse(responseCode = "200", description = "Se encontro el libro"),
        @ApiResponse(responseCode = "404", description = "No se encontro el libro")

    })

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
    @Parameter(description = "Id del libro que quieres encontras")
    @PathVariable Long id) {

        return bookRepository.findById(id)
                .map(book -> ResponseEntity.ok().body(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Haces un nuevo libro")
    @ApiResponse(responseCode = "200", description = "Se creo exitosamente el libro")

    @PostMapping
    public Book createBook(
   
    @RequestBody Book book) {
        return bookRepository.save(book);
    }


    @Operation(summary = "Esto sirve para actualizar un libro")

    @ApiResponses({

        @ApiResponse(responseCode = "200", description = "Se actualizo correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontro el libro")

    })

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
       
    
    @Parameter(description = "Id del libro que quieres actualizar")

    @PathVariable Long id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setPublicationYear(bookDetails.getPublicationYear());
                    book.setGenre(bookDetails.getGenre());
                    book.setPages(bookDetails.getPages());
                    Book updatedBook = bookRepository.save(book);
                    return ResponseEntity.ok(updatedBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Se borra un libro por su id")
    @ApiResponses({

        @ApiResponse(responseCode = "200", description = "Se elimino correectamente el libro"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")

    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
        
   @Parameter(description = "Id del libro que desea eliminar")
    @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}