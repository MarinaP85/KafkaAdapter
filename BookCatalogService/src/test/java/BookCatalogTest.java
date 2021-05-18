import com.sber.bookcatalog.BookCatalogApplication;
import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;
import com.sber.bookcatalog.service.CatalogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = BookCatalogApplication.class)
//@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class BookCatalogTest {

    @Autowired
    private CatalogService catalogService;

    @Test
    public void readAllAuthorsTest() {
        List<String> authors = catalogService.readAllAuthors();
        System.out.println(authors);
        Assert.assertEquals("Стивен Кинг", authors.get(0));
    }

    @Test
    public void readAuthorByIdTest() {
        AuthorDto author = catalogService.readAuthorById(32);
        if (author != null) {
            System.out.println(author.toString());
        }
        Assert.assertNotNull(author);
        Assert.assertEquals("Стивен Кинг", author.getAuthor());
    }

    @Test
    public void readBookByAuthorAndTitleTest() {
        BookDto book = catalogService.readBookByAuthorAndTitle("Роберт М. Вегнер", "Небо цвета стали");
        if (book != null) {
            System.out.println(book.toString());
        }
        Assert.assertNotNull(book);
    }

    @Test
    public void createAuthorTest() {
        BookDto book1 = new BookDto();
        book1.setBookId(123);
        book1.setTitle("testBook1");
        book1.setPrice(100);

        BookDto book2 = new BookDto();
        book2.setBookId(123);
        book2.setTitle("testBook2");
        book2.setPrice(100);

        AuthorDto author = new AuthorDto();
        author.setId(500);
        author.setAuthor("test");
        author.setBookList(Arrays.asList(book1, book2));

        Assert.assertTrue(catalogService.createAuthor(author));
    }

    @Test
    public void updateAuthorTest() {
        BookDto book1 = new BookDto();
        book1.setBookId(123);
        book1.setTitle("testBook3");
        book1.setPrice(100);

        BookDto book2 = new BookDto();
        book2.setBookId(123);
        book2.setTitle("testBook4");
        book2.setPrice(100);

        AuthorDto author = new AuthorDto();
        author.setId(500);
        author.setAuthor("test0");
        author.setBookList(Arrays.asList(book1, book2));

        Assert.assertTrue(catalogService.updateAuthor(author));
    }

    @Test
    public void deleteAuthorTest() {
        Assert.assertTrue(catalogService.deleteAuthor(500));
        Assert.assertFalse(catalogService.deleteAuthor(500));
    }
}
