package com.emazon.mscart.adapters;

import com.emazon.mscart.adapters.driven.jpa.mysql.mapper.IArticleCartEntityMapper;
import com.emazon.mscart.adapters.driven.jpa.mysql.mapper.IArticleCartEntityMapperImpl;
import com.emazon.mscart.adapters.driving.http.controller.CartRestController;
import com.emazon.mscart.adapters.driving.http.dto.request.ArticleCartRequest;
import com.emazon.mscart.adapters.driving.http.mapper.IArticleCartRequestMapper;
import com.emazon.mscart.domain.api.IArticleCartServicePort;
import com.emazon.mscart.domain.model.ArticleCart;
import com.emazon.mscart.infraestructure.configuration.jwt.JwtAuthenticationFilter;
import com.emazon.mscart.infraestructure.configuration.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartRestController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IArticleCartServicePort cartServicePort;

    @MockBean
    private IArticleCartRequestMapper articleCartRequestMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cartControllerTest() throws Exception {
        ArticleCartRequest articleCartRequest = new ArticleCartRequest(1L, 10L);

        ArticleCart articleCart = new ArticleCart(1L, 1L, 1L, 10L, null, null);

        when(articleCartRequestMapper.toModel(any(ArticleCartRequest.class))).thenReturn(articleCart);
        doNothing().when(cartServicePort).addArticleCart(any(ArticleCart.class), 1L);

        mockMvc.perform(post("/cart/add-article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleCartRequest))
        ).andExpect(status().isCreated());

        verify(articleCartRequestMapper).toModel(any(ArticleCartRequest.class));
        verify(cartServicePort).addArticleCart(any(ArticleCart.class), 1L);
    }
}
