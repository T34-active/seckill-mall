package com.ateng.mall.order;

import com.ateng.mall.auth.UserPrincipal;
import com.ateng.mall.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderVO> create(@AuthenticationPrincipal UserPrincipal principal,
                                       @Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.ok(orderService.createOrder(principal, request));
    }

    @GetMapping
    public ApiResponse<List<OrderVO>> myOrders(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(orderService.myOrders(principal.getId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderVO> detail(@AuthenticationPrincipal UserPrincipal principal,
                                       @PathVariable Long id) {
        return ApiResponse.ok(orderService.getOrder(principal.getId(), id));
    }
}
