import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.bookcatalog.BookCatalogApplication;
import com.sber.bookcatalog.exception.ServiceException;
import com.sber.bookcatalog.model.Author;
import com.sber.bookcatalog.model.Book;
import com.sber.bookcatalog.service.BookCatalogService;
import org.h2.tools.Server;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes = BookCatalogApplication.class)
@EntityScan("com.sber.bookcatalog.model")
@AutoConfigureMockMvc
public class BookCatalogTest {

    @Autowired
    private BookCatalogService bookCatalogService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MockMvc mockMvc;

    private static Book book1;
    private static Book book2;
    private static Author author;
    private static Author testAuthor;

    @BeforeAll
    public static void beforeTests() throws SQLException {
        Server.createTcpServer().start();

        book1 = new Book("testBook1", 100);
        book2 = new Book("testBook2", 200);

        author = new Author("test");
        author.setBookList(Arrays.asList(book1, book2));

    }

    @AfterAll
    public static void clear() {
        author = null;
        book1 = null;
        book2 = null;
    }

    @BeforeEach
    public void initBase() throws ServiceException {
        JdbcTestUtils.deleteFromTables(new JdbcTemplate(dataSource), "BOOK", "AUTHOR");

        Author author = new Author("???????????? ??. ????????????");
        Book book1 = new Book("??????????-????", 380);
        Book book2 = new Book("????????????-??????????", 390);
        Book book3 = new Book("???????? ?????????? ??????????", 370);
        Book book4 = new Book("???????????? ???????? ????????", 390);
        Book book5 = new Book("???????????? ?????????????? ??????????", 410);
        author.setBookList(Arrays.asList(book1, book2, book3, book4, book5));
        bookCatalogService.createAuthor(author);

        author = new Author("???????????? ????????????????????");
        book1 = new Book("?????? ????????????????????????????", 310);
        book2 = new Book("?????????????????? ??????????????", 340);
        book3 = new Book("?????????? ????????????", 320);
        book4 = new Book("?????? ??????????????????", 300);
        book5 = new Book("???????????????? ??????????", 330);
        Book book6 = new Book("?????????? ????????????????", 315);
        Book book7 = new Book("?????????????????? ??????????", 350);
        author.setBookList(Arrays.asList(book1, book2, book3, book4,
                book5, book6, book7));
        bookCatalogService.createAuthor(author);

        author = new Author("?????????????? ????????????");
        book1 = new Book("?????????? ??????????????????", 330);
        book2 = new Book("???? ?????????? ????", 350);
        book3 = new Book("?????????????? ????????????", 334);
        book4 = new Book("?????????????? ?? ????????????????", 362);
        book5 = new Book("Natura bestiarum", 370);
        book6 = new Book("?????????????????????? ????????????", 386);
        book7 = new Book("?? ???? ????????????", 410);
        Book book8 = new Book("???????? ???????? ????????", 430);
        author.setBookList(Arrays.asList(book1, book2, book3, book4,
                book5, book6, book7, book8));
        testAuthor = bookCatalogService.createAuthor(author);
        System.out.println(testAuthor);
    }

//    @AfterEach
//    public void clearBase() {
//        JdbcTestUtils.deleteFromTables(new JdbcTemplate(dataSource), "BOOK", "AUTHOR");
//    }

    @Test
    public void readAllAuthorsTest() {
        List<Author> authors;
        try {
            authors = bookCatalogService.readAllAuthors();
            //System.out.println(authors);
            Assertions.assertFalse(authors.isEmpty());
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void readListAuthorsTest() {
        List<String> authors;
        try {
            authors = bookCatalogService.readListAuthors();
            System.out.println(authors);
            Assertions.assertTrue(authors.contains("???????????? ??. ????????????"));
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void readAuthorByIdTest() {
        Author author;
        try {
            author = bookCatalogService.readAuthorById(testAuthor.getId());
            if (author != null) {
                System.out.println(author.toString());
            }
            Assertions.assertNotNull(author);
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void readBookByAuthorAndTitleTest() {
        Book book;
        try {
            book = bookCatalogService.readBookByAuthorAndTitle("???????????? ??. ????????????", "???????? ?????????? ??????????");
            if (book != null) {
                System.out.println(book.toString());
            }
            Assertions.assertNotNull(book);
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void createAuthorTest() {
        try {
            Author returnAuthor = bookCatalogService.createAuthor(author);
            Assertions.assertNotNull(returnAuthor);
            System.out.println(returnAuthor.getName());
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void deleteAuthorTest() {
        try {
            Assertions.assertTrue(bookCatalogService.deleteAuthor(testAuthor.getId()));
            Assertions.assertFalse(bookCatalogService.deleteAuthor(testAuthor.getId()));
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void updateAuthorTest() {
        try {
            Author returnAuthor = bookCatalogService.createAuthor(author);

            book1.setTitle("testBook3");
            book2.setTitle("testBook4");
            author.setBookList(Arrays.asList(book1, book2));

            Assertions.assertTrue(bookCatalogService.updateAuthor(returnAuthor.getId(),
                    author));
            Author testAuthor = bookCatalogService.readAuthorById(returnAuthor.getId());
            if (testAuthor != null) {
                System.out.println(testAuthor.toString());
            }
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }


    }

    @Test
    public void readAllAuthors_AuthorController() throws Exception {
        mockMvc.perform(
                get("/authors/all")
                        .header("Content-Language", "ru, en"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.))
                .andExpect(jsonPath("$[*].name",
                        containsInAnyOrder("???????????? ??. ????????????", "???????????? ????????????????????", "?????????????? ????????????")));
    }

    @Test
    public void readListAuthors_AuthorController() throws Exception {
        mockMvc.perform(
                get("/authors/list")
                        .header("Content-Language", "ru, en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void readAuthorById_AuthorController() throws Exception {
        mockMvc.perform(
                get("/authors/{id}", testAuthor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testAuthor.getId()))
                .andExpect(jsonPath("$.name")
                        .value("?????????????? ????????????"));
    }

    @Test
    public void createAuthor_AuthorController() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(
                post("/authors")
                        .content(mapper.writeValueAsString(author))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void updateAuthor_AuthorController() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        testAuthor.setName("?????????????? ????????????1");

        mockMvc.perform(
                put("/authors/{id}", testAuthor.getId())
                        .content(mapper.writeValueAsString(testAuthor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Author resultAuthor = bookCatalogService.readAuthorById(testAuthor.getId());
        if (resultAuthor != null) {
            System.out.println(resultAuthor.toString());
        } else {
            System.out.println("?????????? ???? ????????????");
        }
    }

    @Test
    public void deleteAuthor_AuthorController() throws Exception {
        mockMvc.perform(
                delete("/authors/{id}", testAuthor.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void readBooksByAuthorAndTitle_AuthorController() throws Exception {
        mockMvc.perform(
                get("/authors")
                        .param("author", "???????????? ??. ????????????")
                        .param("title", "???????????? ???????? ????????"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(390));
    }

}
