package br.com.rosa.domain.categoryItem;

import java.util.ArrayList;
import java.util.List;

import br.com.rosa.domain.categoryItem.dto.UpdateCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rosa.domain.item.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "categorys")
@Entity(name = "Category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@JsonIgnore
	@OneToMany(mappedBy = "category")
	private List<Item> itens = new ArrayList<>();
	
	public Category(Long id) {
		this.id = id;
	}
	
	public Category(String data) {
		this.name = data;
	}


	public void attCategory(UpdateCategory data) {

		if (data.category() != null) {
			this.name = data.category();
		}
	}
}
