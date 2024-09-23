package com.emazon.mscart.domain;

import com.emazon.mscart.domain.api.use_case.ArticleCartUseCase;
import com.emazon.mscart.domain.exception.ArticleNotFoundException;
import com.emazon.mscart.domain.exception.CategoryLimitExceededException;
import com.emazon.mscart.domain.exception.OutOfStockException;
import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.domain.model.ArticleCart;
import com.emazon.mscart.domain.model.Brand;
import com.emazon.mscart.domain.model.Category;
import com.emazon.mscart.domain.spi.IArticleCartPersistencePort;
import com.emazon.mscart.domain.spi.IArticlePersistencePort;
import com.emazon.mscart.domain.util.DomainConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AddArticleCartUseCaseTests {

    @Mock
    private IArticleCartPersistencePort articleCartPersistencePort;

    @Mock
    private IArticlePersistencePort articlePersistencePort;

    @InjectMocks
    private ArticleCartUseCase articleCartUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddArticleCart_SuccessfulAddition() {
        // Arrange
        ArticleCart articleCart = new ArticleCart(1L, 1L, 1L, 10L, LocalDate.now(), LocalDate.now());
        Category category = new Category(1L, "name", "description");
        Brand brand = new Brand(1L, "name", "description");
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        Article article = new Article(1L, "name", BigDecimal.ONE, 100L, brand, categories);

        when(articlePersistencePort.getArticleById(1L)).thenReturn(Optional.of(article));
        when(articleCartPersistencePort.findByUserIdAndArticleId(1L, 1L)).thenReturn(Optional.empty());
        when(articlePersistencePort.getEstimatedNextDate(1L)).thenReturn(LocalDate.of(2024, 1, 1)); // Devolver fecha válida

        // Act
        articleCartUseCase.addArticleCart(articleCart, 1L);

        // Assert
        verify(articleCartPersistencePort, times(1)).addArticleCart(articleCart);
    }

    @Test
    public void testAddArticleCart_ThrowsOutOfStockException() {
        // Arrange
        ArticleCart articleCart = new ArticleCart(1L, 1L, 1L, 10L, LocalDate.now(), LocalDate.now());
        Category category = new Category(1L, "name", "description");
        Brand brand = new Brand(1L, "name", "description");
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        Article article = new Article(1L, "name", BigDecimal.ONE, 1L, brand, categories);

        when(articlePersistencePort.getArticleById(1L)).thenReturn(Optional.of(article));
        when(articleCartPersistencePort.findByUserIdAndArticleId(1L, 1L)).thenReturn(Optional.empty());
        when(articlePersistencePort.getEstimatedNextDate(1L)).thenReturn(LocalDate.of(2024, 1, 1));

        assertThrows(OutOfStockException.class, () -> {
            articleCartUseCase.addArticleCart(articleCart, 1L);
        });
    }

    @Test
    public void testAddArticleCart_ThrowsArticleNotFoundException() {
        // Arrange
        ArticleCart articleCart = new ArticleCart(1L, 1L, 1L, 10L, LocalDate.now(), LocalDate.now());

        // Simulamos que no se encuentra el artículo
        when(articlePersistencePort.getArticleById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            articleCartUseCase.addArticleCart(articleCart, 1L);
        });

        // Verificamos que nunca se intentó agregar el artículo al carrito
        verify(articleCartPersistencePort, never()).addArticleCart(any(ArticleCart.class));
    }

    @Test
    public void testAddArticleCart_ThrowsCategoryLimitException() {
        Long userId = 1L;
        Long articleId = 1L;

        Category category = new Category(1L, "name", "description");
        List<Category> categories = Collections.singletonList(category);

        Article existingArticle = new Article(articleId, "existingArticle", BigDecimal.ONE, 100L,
                new Brand(1L, "brandName", "brandDescription"), categories);

        ArticleCart existingCartItem1 = new ArticleCart(1L, userId, 1L, 1L, LocalDate.now(), LocalDate.now());
        ArticleCart existingCartItem2 = new ArticleCart(2L, userId, 2L, 1L, LocalDate.now(), LocalDate.now());
        ArticleCart existingCartItem3 = new ArticleCart(3L, userId, 3L, 1L, LocalDate.now(), LocalDate.now());

        when(articleCartPersistencePort.findByUserId(userId))
                .thenReturn(Arrays.asList(existingCartItem1, existingCartItem2, existingCartItem3));

        when(articlePersistencePort.getArticleById(existingCartItem1.getArticleId()))
                .thenReturn(Optional.of(existingArticle));
        when(articlePersistencePort.getArticleById(existingCartItem2.getArticleId()))
                .thenReturn(Optional.of(existingArticle));
        when(articlePersistencePort.getArticleById(existingCartItem3.getArticleId()))
                .thenReturn(Optional.of(existingArticle));

        when(articlePersistencePort.getArticleById(articleId))
                .thenReturn(Optional.of(existingArticle));

        when(articlePersistencePort.getEstimatedNextDate(articleId))
                .thenReturn(LocalDate.of(2024, 1, 1));

        assertThrows(CategoryLimitExceededException.class, () -> {
            articleCartUseCase.addArticleCart(new ArticleCart(userId, articleId, 1L, 10L, LocalDate.now(), LocalDate.now()), userId);
        });
    }
}