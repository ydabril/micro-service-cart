package com.emazon.mscart.domain;

import com.emazon.mscart.domain.api.use_case.ArticleCartUseCase;
import com.emazon.mscart.domain.exception.ArticleNotFoundException;
import com.emazon.mscart.domain.exception.CategoryLimitExceededException;
import com.emazon.mscart.domain.exception.OutOfStockException;
import com.emazon.mscart.domain.model.*;
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

import static org.junit.jupiter.api.Assertions.*;
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
        Article article = new Article(1L, "name", BigDecimal.ONE, 1L, 100L, 10L,
                new Brand(1L, "brandName", "brandDescription"), categories, "image-path", LocalDate.now());

        when(articlePersistencePort.getArticleById(1L)).thenReturn(Optional.of(article));
        when(articleCartPersistencePort.findByUserIdAndArticleId(1L, 1L)).thenReturn(Optional.empty());
        when(articlePersistencePort.getEstimatedNextDate(1L)).thenReturn(LocalDate.of(2024, 1, 1)); // Devolver fecha válida

        // Act
        articleCartUseCase.addArticleCart(articleCart);

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
        Article article =new Article(1L, "name", BigDecimal.ONE, 1L, 1L, 1L,
                new Brand(1L, "brandName", "brandDescription"), categories, "image-path", LocalDate.now());

        when(articlePersistencePort.getArticleById(1L)).thenReturn(Optional.of(article));
        when(articleCartPersistencePort.findByUserIdAndArticleId(1L, 1L)).thenReturn(Optional.empty());
        when(articlePersistencePort.getEstimatedNextDate(1L)).thenReturn(LocalDate.of(2024, 1, 1));

        assertThrows(OutOfStockException.class, () -> {
            articleCartUseCase.addArticleCart(articleCart);
        });
    }

    @Test
    public void testAddArticleCart_ThrowsArticleNotFoundException() {
        // Arrange
        ArticleCart articleCart = new ArticleCart(1L, 1L, 1L, 10L, LocalDate.now(), LocalDate.now());

        when(articlePersistencePort.getArticleById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArticleNotFoundException.class, () -> {
            articleCartUseCase.addArticleCart(articleCart);
        });

        verify(articleCartPersistencePort, never()).addArticleCart(any(ArticleCart.class));
    }

    @Test
    public void testDeleteArticleCart_SuccessfulDeletion() {
        // Arrange
        Long articleCartId = 1L;
        Long userId = 1L;

        when(articleCartPersistencePort.getUserId()).thenReturn(userId);

        // Act
        articleCartUseCase.deleteArticleCart(articleCartId);

        // Assert
        verify(articleCartPersistencePort, times(1)).deleteArticleCart(articleCartId);
        verify(articleCartPersistencePort, times(1)).getUserId();
    }

    @Test
    public void testGetArticlesCart_SuccessfulRetrieval() {
        // Arrange
        Long userId = 1L;
        List<Long> articleIds = Arrays.asList(1L, 2L, 3L);
        String sortDirection = "ASC";
        Integer page = 0;
        Integer size = 10;
        String categoryName = "Electronics";
        String brandName = "Samsung";

        ArticleFilter articleFilter = new ArticleFilter(
                articleIds,
                sortDirection,
                page,
                size,
                categoryName,
                brandName
        );

        List<ArticleCart> cartArticles = new ArrayList<>();
        cartArticles.add(new ArticleCart(1L, userId, 1L, 1L, LocalDate.now(), LocalDate.now()));

        Pagination<Article> pagination = new Pagination<>(
                Arrays.asList(new Article(1L, "name", BigDecimal.TEN, 1L, 100L, 1L, null, null, "image-path", LocalDate.now())),
                page, size, 1L, 1, false, false
        );

        when(articleCartPersistencePort.getUserId()).thenReturn(userId);
        when(articleCartPersistencePort.getArticleIdsByUser(userId)).thenReturn(articleIds);
        
        when(articlePersistencePort.getArticlesCart(any(ArticleFilter.class)))
                .thenReturn(pagination);

        when(articleCartPersistencePort.findByUserId(userId)).thenReturn(cartArticles);

        // Act
        Pagination<Article> result = articleCartUseCase.getArticlesCart(articleFilter);

        // Assert
        verify(articleCartPersistencePort, times(1)).getUserId();
        verify(articleCartPersistencePort, times(1)).getArticleIdsByUser(userId);

        // Ajustar la verificación según el número real de invocaciones
        verify(articlePersistencePort, atLeastOnce()).getArticlesCart(articleFilter);

        verify(articleCartPersistencePort, times(1)).findByUserId(userId);

        assertEquals(pagination, result);
        assertNotNull(result.getTotalPrice());
    }

}