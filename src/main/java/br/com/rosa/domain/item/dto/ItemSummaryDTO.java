package br.com.rosa.domain.item.dto;

import br.com.rosa.domain.categoryItem.Category;

public interface ItemSummaryDTO {

    Long getCod();
    String getReference();
    String getName();
    String getReplacementValue();
    Long getQuantity();
    Long getAmount();
    Category getCategory();



}
