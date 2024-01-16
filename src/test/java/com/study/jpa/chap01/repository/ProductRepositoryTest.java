package com.study.jpa.chap01.repository;

import com.study.jpa.chap01.entity.Product;
import com.study.jpa.chap01.entity.Product.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap01.entity.Product.Category.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback   // 테스트 진행 후 롤백
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void insertBeforeTest() {
        productRepository.saveAll(Arrays.asList(
                Product.builder()
                        .name("아이폰")
                        .price(2000000)
                        .category(Category.FASHION)
                        .build(),
                Product.builder()
                        .name("탕수육")
                        .price(15000)
                        .category(Category.FOOD)
                        .build(),
                Product.builder()
                        .name("구두")
                        .price(300000)
                        .category(FASHION)
                        .build(),
                Product.builder()
                        .name("주먹밥")
                        .price(1500)
                        .category(FOOD)
                        .build()
        ));
    }

    @Test
    @DisplayName("상품을 DB에 저장한다.")
    void saveTest() {
        //given
        Product product = Product.builder()
                .name("정장")
                .price(120000)
                .category(FASHION)
                .build();
        //when
        Product save = productRepository.save(product);
        //then
        Assertions.assertNotNull(save);
        Assertions.assertEquals(product.getName(), save.getName());
        Assertions.assertEquals(product.getPrice(), save.getPrice());
        Assertions.assertEquals(product.getCategory(), save.getCategory());

    }

    @Test
    @DisplayName("1번 상품을 삭제함")
    void deleteTest() {
        //given
        long id = 1L;

        //when
        productRepository.deleteById(id);

        //then
        Optional<Product> product = productRepository.findById(id);
    }

    @Test
    @DisplayName("3번 상품을 단일조회함")
    void findOneTest() {
        //given
        long id = 3L;

        //when
        Optional<Product> product = productRepository.findById(id);

        //then
        System.out.println("product = " + product);

        // null 체크를 간소화하기 위한 Optional타입
        // ifPresent는 null이 아니면 람다의 코드 진행, null이면 무시
        product.ifPresent(p -> {
            assertEquals("구두", p.getName());
            assertNotNull(product);
        });

        // product가 null이면 새로운 new Product를 반환하고
        // null이 아니면 Optional안에서 꺼내서 반환
        Product ppp = product.orElse(new Product());

        // null이면 예외를 발생, null이 아니면 Optional에서 꺼내서 반환
        //Product pppp = product.orElseThrow(e -> {
        //    new NullPointerException("ㄴㄴ");
        //});


    }


    @Test
    @DisplayName("2번 상품의 이름과 카테고리를 수정한다.")
    void modifyTest() {
        //given
        long id = 2L;
        String newName = "짜장면";
        Product.Category newCategory = FOOD;
        //when
        /*
            JPA에서는 수정 메서드를 따로 제공하지 않음
            단일 조회를 수행한 후 setter를 호출해서 값을 변경하고
            다시 save를 하면 UPDATE문이 나감
         */
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        product.setName(newName);
        product.setCategory(newCategory);
        productRepository.save(product);

        //then
    }

    @Test
    @DisplayName("상품을 전체조회하면 상품의 총 개수가 4개여야함")
    void findAllTest() {
        //given

        //when
        List<Product> products = productRepository.findAll();

        //then
        System.out.println("\n\n\n");
        products.forEach(System.out::println);
        System.out.println("\n\n\n");

        assertEquals(4, products.size());
    }




}