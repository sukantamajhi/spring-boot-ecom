package com.sukanta.springbootecom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.model.Discount;
import com.sukanta.springbootecom.model.Product;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.repository.discountRepo;
import com.sukanta.springbootecom.repository.productRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class discountService {

    private final discountRepo discountRepo;
    private final productRepository productRepository;

    public discountService(discountRepo discountRepo, productRepository productRepository) {
        this.discountRepo = discountRepo;
        this.productRepository = productRepository;
    }

    public Discount createDiscount(@NonNull Discount request, @NonNull User user) throws Exception {
        log.info(request + " <<== request" + user + " <<== user");
        Discount discount =
                Discount.builder().name(request.getName()).description(request.getDescription())
                        .amount(request.getAmount()).percentage(request.getPercentage()).build();

        if (discount != null) {
            Discount newDiscount = discountRepo.save(discount);

            if (request.getProductId() != null) {
                Optional<List<Discount>> discount1 =
                        discountRepo.findAllByStatusAndProductIdAndIdIsNot(true,
                                request.getProductId(), newDiscount.getId());

                if (discount1.isPresent()) {
                    updateDiscountAndProduct(newDiscount, discount1);
                }

            } else {
                Optional<List<Discount>> discount1 = discountRepo
                        .findAllByStatusAndProductIdAndIdIsNot(true, null, newDiscount.getId());

                updateDiscountAndProduct(newDiscount, discount1);
            }

            return newDiscount;
        } else {
            throw new Exception(Constant.DISCOUNT_CREATE_FAILED);
        }

    }

    private void updateDiscountAndProduct(Discount newDiscount,
            Optional<List<Discount>> discount1) {
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

    public List<Discount> getDiscounts() throws Exception {
        try {
            Sort sort = Sort.by(Direction.DESC, "createdAt");
            return discountRepo.findAll(sort);
        } catch (Exception e) {
            log.error("Error in getting discounts==>> " + e);

            throw new Exception(e);
        }
    }

    public Discount getDiscount(@NonNull String discountId) throws Exception {
        try {
            return discountRepo.findById(discountId).orElse(null);
        } catch (Exception e) {
            log.error("Error in getting discount==>> " + e);

            throw new Exception(e);
        }
    }

    public Discount updateDiscount(Discount request, @NonNull String discountId) throws Exception {
        try {
            Discount discount = discountRepo.findById(discountId).orElse(null);

            if (discount == null) {
                return null;
            } else {
                discount.setAmount(request.getAmount());
                discount.setPercentage(request.getPercentage());
                discount.setName(request.getName());
                discount.setDescription(request.getDescription());
                discount.setStatus(request.isStatus());

                return discountRepo.save(discount);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
