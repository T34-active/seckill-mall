package com.ateng.mall.config;

import com.ateng.mall.product.Product;
import com.ateng.mall.product.ProductRepository;
import com.ateng.mall.stock.StockService;
import com.ateng.mall.user.User;
import com.ateng.mall.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    private final StockService stockService;

    @Override
    public void run(String... args) {
        initUsers();
        initProductsAndStock();
    }

    private void initUsers() {
        if (!userRepository.existsByUsername("demo")) {
            User demo = new User();
            demo.setUsername("demo");
            demo.setPassword(passwordEncoder.encode("123456"));
            demo.setRole("USER");
            userRepository.save(demo);
            log.info("初始化演示用户 demo / 123456");
        }
    }

    private void initProductsAndStock() {
        if (productRepository.count() == 0) {
            List<Product> seeds = Arrays.asList(
                    createProduct("\u673a\u68b0\u952e\u76d8", "299.00", 50),
                    createProduct("\u65e0\u7ebf\u9f20\u6807", "99.00", 100),
                    createProduct("\u663e\u793a\u5668 27\u5bf8", "1299.00", 20)
            );
            productRepository.saveAll(seeds);
            log.info("初始化商品 {} 个", seeds.size());
        }

        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            stockService.initStock(product.getId(), product.getStock());
            log.info("预热 Redis 库存 productId={} stock={}", product.getId(), product.getStock());
        }
    }

    private Product createProduct(String name, String price, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        product.setStock(stock);
        product.setEnabled(true);
        return product;
    }
}
