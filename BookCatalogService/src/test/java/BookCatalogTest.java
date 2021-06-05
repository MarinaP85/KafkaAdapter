import com.fasterxml.jackson.databind.ObjectMapper;
import com.sber.bookcatalog.BookCatalogApplication;
import com.sber.bookcatalog.exception.ServiceException;
import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;
import com.sber.bookcatalog.service.BookCatalogService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = BookCatalogApplication.class)
@AutoConfigureMockMvc
public class BookCatalogTest {

    @Autowired
    private BookCatalogService bookCatalogService;

    @Autowired
    private MockMvc mockMvc;

    private static BookDto book1;
    private static BookDto book2;
    private static AuthorDto author;

    @BeforeClass
    public static void beforeTests() {
        book1 = new BookDto();
        book1.setBookId(123);
        book1.setTitle("testBook1");
        book1.setPrice(100);

        book2 = new BookDto();
        book2.setBookId(123);
        book2.setTitle("testBook2");
        book2.setPrice(100);

        author = new AuthorDto();
        author.setId(500);
        author.setAuthor("test");
        author.setBookList(Arrays.asList(book1, book2));
    }

    @Test
    public void readAllAuthorsTest() {
        List<String> authors;
        try {
            authors = bookCatalogService.readAllAuthors();
            System.out.println(authors);
            Assert.assertEquals("Стивен Кинг", authors.get(0));
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void readAuthorByIdTest() {
        AuthorDto author;
        try {
            author = bookCatalogService.readAuthorById(32);
            if (author != null) {
                System.out.println(author.toString());
            }
            Assert.assertNotNull(author);
            Assert.assertEquals("Стивен Кинг", author.getAuthor());
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void readBookByAuthorAndTitleTest() {
        BookDto book;
        try {
            book = bookCatalogService.readBookByAuthorAndTitle("Роберт М. Вегнер", "Небо цвета стали");
            if (book != null) {
                System.out.println(book.toString());
            }
            Assert.assertNotNull(book);
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void createAuthorTest() {
        try {
            Assert.assertTrue(bookCatalogService.createAuthor(author));
            Assert.assertFalse(bookCatalogService.createAuthor(author));
            Assert.assertTrue(bookCatalogService.deleteAuthor(author.getId()));
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void deleteAuthorTest() {
        try {
            //Assert.assertTrue(bookCatalogService.createAuthor(author));
            Assert.assertTrue(bookCatalogService.deleteAuthor(author.getId()));
            Assert.assertFalse(bookCatalogService.deleteAuthor(author.getId()));
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }

    }

    @Test
    public void updateAuthorTest() {
        try {
            Assert.assertTrue(bookCatalogService.createAuthor(author));

            book1.setTitle("testBook3");
            book2.setTitle("testBook4");
            author.setBookList(Arrays.asList(book1, book2));

            Assert.assertTrue(bookCatalogService.updateAuthor(author));
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }


    }

    @Test
    public void readAllAuthors_AuthorController() throws Exception {
        mockMvc.perform(
                get("/authors/all")
                        .header("genre", "fantasy"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void readAuthorById_AuthorController() throws Exception {
        mockMvc.perform(
                get("/authors/{id}", 32))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(32))
                .andExpect(jsonPath("$.author").value("Стивен Кинг"));
    }

    @Test
    public void createAuthor_AuthorController() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(
                post("/authors")
                        .content(mapper.writeValueAsString(author))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateAuthor_AuthorController() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        book1.setTitle("testBook5");
        book2.setTitle("testBook6");
        author.setBookList(Arrays.asList(book1, book2));

        mockMvc.perform(
                put("/authors")
                        .content(mapper.writeValueAsString(author))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAuthor_AuthorController() throws Exception {
        mockMvc.perform(
                delete("/authors/{id}", author.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void readBooksByAuthorAndTitle_AuthorController() throws Exception {
        mockMvc.perform(
                get("/authors")
                        .param("author", "Стивен Кинг")
                        .param("title", "Колдун и кристалл"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(149))
                .andExpect(jsonPath("$.price").value(390));
    }

    @AfterClass
    public static void afterTests() {
        author = null;
        book1 = null;
        book2 = null;
    }
}
