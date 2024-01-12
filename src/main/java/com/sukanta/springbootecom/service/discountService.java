package com.sukanta.springbootecom.service;

import com.sukanta.springbootecom.config.LoggerUtil;
import com.sukanta.springbootecom.model.Discount;
import com.sukanta.springbootecom.model.Product;
import com.sukanta.springbootecom.repository.discountRepo;
import com.sukanta.springbootecom.repository.productRepository;
import org.slf4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class discountService {

    private final discountRepo discountRepo;
    private final productRepository productRepository;
    private final Logger log = LoggerUtil.getLogger(this);

    public discountService(discountRepo discountRepo, productRepository productRepository) {
        this.discountRepo = discountRepo;
        this.productRepository = productRepository;
    }

    public Discount createDiscount(@NonNull Discount request, @NonNull String userId) {
        Discount discount = Discount.builder().name(request.getName()).description(request.getDescription()).createdBy(userId).amount(request.getAmount()).percentage(request.getPercentage()).build();

        Discount newDiscount = discountRepo.save(discount);

        if (request.getProductId() != null) {
//            Update the existing discount with id and update product with this new discount ID

            Optional<List<Discount>> discount1 = discountRepo.findAllByStatusAndProductIdAndIdIsNot(true, request.getProductId(), newDiscount.getId());

            if (discount1.isPresent()) {
                updateDiscountAndProduct(newDiscount, discount1);
            }

        } else {
            Optional<List<Discount>> discount1 = discountRepo.findAllByStatusAndProductIdAndIdIsNot(true, null, newDiscount.getId());

            updateDiscountAndProduct(newDiscount, discount1);
        }

        return newDiscount;

    }

    private void updateDiscountAndProduct(Discount newDiscount, Optional<List<Discount>> discount1) {
        discount1.ifPresent(optionalDiscount -> {
            for (var d1 : optionalDiscount) {
//          Updating existing discount
                d1.setStatus(false);
                discountRepo.save(d1);

//          Updating product with the new discountId
                Product product = productRepository.findAllByDiscountId(d1.getId());

                if (product != null) {
                    product.setDiscountId(newDiscount.getId());

                    productRepository.save(product);
                }
            }
        });
    }

}