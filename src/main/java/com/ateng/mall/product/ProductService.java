package com.ateng.mall.product;

import com.ateng.mall.common.BizException;
import com.ateng.mall.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockService stockService;

    public List<ProductVO> listProducts() {
        List<Product> products = productRepository.findByEnabledTrueOrderByIdAsc();
        List<ProductVO> result = new ArrayList<ProductVO>();
        for (Product product : products) {
            result.add(toVO(product));
        }
        return result;
    }

    public ProductVO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BizException("商品不存在"));
        return toVO(product);
    }

    public Product requireProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BizException("商品不存在"));
    }

    private ProductVO toVO(Product product) {
        Integer redisStock = stockService.getStock(product.getId());
        return ProductVO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .redisStock(redisStock)
                .build();
    }
}
