package com.example.ecommerce.repository;

import com.example.ecommerce.model.OrderProduct;
import com.example.ecommerce.model.OrderProductPK;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK> {
}
