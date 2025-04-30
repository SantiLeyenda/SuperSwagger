package com.example.library;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.example.library.model.Book;
import com.example.library.repository.BookRepository;






@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

 
    @Bean
    public CommandLineRunner meterUsuario(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String usuario = "admin";
            String contra = "admin123";

            if (userRepository.findByUsername(usuario).isEmpty()) {
                User user = new User();
                user.setUsername(usuario); 
                user.setPassword(passwordEncoder.encode(contra));

                userRepository.save(user);
                System.out.println("Se creo el usuario: " + usuario);
            } else {
                System.out.println("Ya existe el usuario");
            }
        };
    }


    @Bean
public CommandLineRunner meterLibros(BookRepository bookRepository) {
    return args -> {
        if (bookRepository.count() == 0) {
            Book book1 = new Book();
            book1.setTitle("Diario de Greg");
            book1.setAuthor("Kobe Bryant");
            book1.setIsbn("9780132350884");
            book1.setPublicationYear(2010);
            book1.setGenre("Ninos");
            book1.setPages(464);

            Book book2 = new Book();
            book2.setTitle("Azkaban");
            book2.setAuthor("JK Rowling");
            book2.setIsbn("9780201616224");
            book2.setPublicationYear(1999);
            book2.setGenre("Misterio");
            book2.setPages(352);

            bookRepository.save(book1);
            bookRepository.save(book2);

            System.out.println("Libros de prueba agregados a la base de datis");
        } else {
            System.out.println("Ya hay libros en la base de datos");
        }
    };
}

}
