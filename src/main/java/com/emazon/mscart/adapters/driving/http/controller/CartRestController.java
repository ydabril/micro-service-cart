package com.emazon.mscart.adapters.driving.http.controller;

import com.emazon.mscart.adapters.driving.http.dto.request.ArticleCartRequest;
import com.emazon.mscart.adapters.driving.http.mapper.IArticleCartRequestMapper;
import com.emazon.mscart.adapters.driving.http.utils.ArticleCartUtils;
import com.emazon.mscart.domain.api.IArticleCartServicePort;
import com.emazon.mscart.domain.model.ArticleCart;
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

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Save an article", description = "Add a new article in the cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/add-article")
    public ResponseEntity<Void> addArticleCart(@RequestBody ArticleCartRequest articleCartRequest) {
        Long userId = ArticleCartUtils.extractUserId();
        ArticleCart articleCart = articleCartRequestMapper.toModel(articleCartRequest);
        articleCartServicePort.addArticleCart(articleCart, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/delete-article/{id}")
    public String deleteArticleCart(@PathVariable Long id) {
        return "eliminar articulo del carrito";
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/buy-article")
    public String buyArticleCart() {
        return "comprar articulo";
    }
}
