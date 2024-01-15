package com.sukanta.springbootecom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.model.Discount;
import com.sukanta.springbootecom.model.Product;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.repository.discountRepo;
import com.sukanta.springbootecom.repository.productRepository;

@Service
public class discountService {

    private final discountRepo discountRepo;
    private final productRepository productRepository;

    public discountService(discountRepo discountRepo, productRepository productRepository) {
        this.discountRepo = discountRepo;
        this.productRepository = productRepository;
    }

    public Discount createDiscount(@NonNull Discount request, @NonNull User user) throws Exception {
        Discount discount = Discount.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(user)
                .amount(request.getAmount()).percentage(request.getPercentage())
                .build();

        if (discount != null) {
            Discount newDiscount = discountRepo.save(discount);

            if (request.getProductId() != null) {
                // Update the existing discount with id and update product with this new
                // discount ID

                Optional<List<Discount>> discount1 = discountRepo.findAllByStatusAndProductIdAndIdIsNot(true,
                        request.getProductId(), newDiscount.getId());

                if (discount1.isPresent()) {
                    updateDiscountAndProduct(newDiscount, discount1);
                }

            } else {
                Optional<List<Discount>> discount1 = discountRepo.findAllByStatusAndProductIdAndIdIsNot(true, null,
                        newDiscount.getId());

                updateDiscountAndProduct(newDiscount, discount1);
            }

            return newDiscount;
        } else {
            throw new Exception(Constant.DISCOUNT_CREATE_FAILED);
        }

    }

    private void updateDiscountAndProduct(Discount newDiscount, Optional<List<Discount>> discount1) {
        discount1.ifPresent(optionalDiscount -> {
            for (var d1 : optionalDiscount) {
                // Updating existing discount
                d1.setStatus(false);
                discountRepo.save(d1);

                // Updating product with the new discountId
                Product product = productRepository.findAllByDiscountId(d1.getId());

                if (product != null) {
                    product.setDiscountId(newDiscount.getId());

                    productRepository.save(product);
                }
            }
        });
    }

}