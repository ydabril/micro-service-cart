package com.emazon.mscart.adapters.driving.http.controller;

import com.emazon.mscart.adapters.driving.http.dto.request.ArticleCartRequest;
import com.emazon.mscart.adapters.driving.http.dto.response.ArticleResponse;
import com.emazon.mscart.adapters.driving.http.mapper.IArticleCartRequestMapper;
import com.emazon.mscart.adapters.driving.http.mapper.IArticleCartResponseMapper;
import com.emazon.mscart.adapters.driven.jpa.mysql.util.ArticleCartUtils;
import com.emazon.mscart.adapters.driving.http.utils.ValidateParametersConstants;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import com.emazon.mscart.domain.api.IArticleCartServicePort;
import com.emazon.mscart.domain.model.ArticleCart;
import com.emazon.mscart.domain.model.ArticleFilter;
import com.emazon.mscart.domain.model.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@Validated
@RequiredArgsConstructor
public class CartRestController {
    private final IArticleCartServicePort articleCartServicePort;
    private final IArticleCartRequestMapper articleCartRequestMapper;
    private final IArticleCartResponseMapper articleCartResponseMapper;

    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Save an article", description = "Add a new article in the cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/add-article")
    public ResponseEntity<Void> addArticleCart(@RequestBody ArticleCartRequest articleCartRequest) {
        ArticleCart articleCart = articleCartRequestMapper.toModel(articleCartRequest);
        articleCartServicePort.addArticleCart(articleCart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Delete an article from cart", description = "Delete a article in the cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/delete-article/{id}")
    public void deleteArticleCart(@PathVariable Long id) {
        articleCartServicePort.deleteArticleCart(id);
    }


    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/list-articles")
    public ResponseEntity<Pagination<ArticleResponse>> getArticlesCart(
            @Min(value = 0, message = ValidateParametersConstants.PAGE_NUMBER_NEGATIVE)
            @RequestParam(defaultValue = "0") int page,

            @Min(value = 1, message = ValidateParametersConstants.PAGE_SIZE_INVALID)
            @RequestParam(defaultValue = "10") int size,

            @Pattern(regexp = ValidateParametersConstants.SORT_DIRECTION_PATTERN, message = ValidateParametersConstants.INVALID_SORT_DIRECTION)
            @RequestParam(defaultValue = "ASC") String sortDirection,

            @RequestParam(required = false) String categoryName,

            @RequestParam(required = false) String brandName
    ) {
        Pagination<ArticleResponse> articles = articleCartResponseMapper
                .toPaginationResponse(articleCartServicePort.getArticlesCart(new ArticleFilter(null, sortDirection, page, size, categoryName, brandName)));

        return ResponseEntity.ok(articles);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/buy-article")
    public String buyArticleCart() {
        return "comprar articulo";
    }
}
