package org.app.library.controllers

import org.app.common.dto.IssueBookRequest
import org.app.common.dto.StudentCommonDto
import org.app.common.models.Student
import org.app.library.dto.BookDto
import org.app.library.models.Book
import org.app.library.services.BookService
import org.app.library.services.KafkaLibraryProducer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/books")
class BooksController(
    private val kafkaLibraryProducer: KafkaLibraryProducer,
    private val bookService: BookService
) {
    val webClient = WebClient.create()

    @GetMapping
    fun welcome():ResponseEntity<StudentCommonDto>{
        val studentDto = StudentCommonDto(null,"Jeremiah","35")
        kafkaLibraryProducer.sendMessage(studentDto)
        return ResponseEntity.ok(studentDto)
    }

    @PostMapping("save")
    fun saveNewBook(@RequestBody bookDto: BookDto){
        bookService.saveBook(bookDto)
    }

    @GetMapping("all")
    fun getAllBooks(): ResponseEntity<MutableList<Book>> {
        return ResponseEntity.ok(bookService.getAllBooks())
    }

    @PostMapping("borrow")
    fun borrowBook(
        @RequestBody issueBookRequest: IssueBookRequest
    ){

        if(bookService.checkIfBookExists(issueBookRequest.bookIsbn)){
            kafkaLibraryProducer.getStudentDetails(
                issueBookRequest,
                "library-student-topic")
        }else{
            println("Book with isbn number : " + issueBookRequest.bookIsbn + " Does not exists")
        }

    }

    @GetMapping("issued")
    fun sendRequest():ResponseEntity<Any>{

        val list = mutableListOf<Mono<String>>()
        val issuedBooks = bookService.getIssuedBooks()

        issuedBooks.map { bookIssue ->
            val urlEndpoint = "http://localhost:8081/api/students/"+bookIssue.isbn
            val data = webClient.get()
                .uri(urlEndpoint)
                .retrieve().bodyToMono(String::class.java)
            list.add(data)
        }


        return ResponseEntity.ok(list)
    }

    @GetMapping("students")
    fun getStudents(): ResponseEntity<MutableList<Student>> {
        return bookService.getStudents()
    }
}