package ru.geekbrains.persist;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class CartItem implements Serializable {
	
	private UUID id;
		
    private Boolean giftWrap;

    private Integer qty;

	@Override
	public int hashCode() {
		return Objects.hash(giftWrap, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CartItem))
			return false;
		CartItem other = (CartItem) obj;
		return Objects.equals(giftWrap, other.giftWrap) && Objects.equals(id, other.id);
	}
    
    
}
