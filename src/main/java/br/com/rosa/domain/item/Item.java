package br.com.rosa.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rosa.domain.categoryItem.Category;
import br.com.rosa.domain.item.dto.ItemCadastro;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "itens")
@Entity(name = "Item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long cod;
	private byte[] img;
	private String name;
	private double valueItem;
	private double replacementValue;
	private Long amount;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Category category;
	
	public Item(ItemCadastro dados, Long idCategory, byte[] img) {
		this.img = img;
		this.cod = (long) dados.cod();
		this.name = dados.name();
		this.valueItem = dados.value();
		this.replacementValue = dados.replacementValue();
		this.amount = (long) dados.amount();
		this.category = new Category(idCategory);
	}
	
	public Item(int id) {
		this.id = (long) id;
	}
	
	public Item(Long id) {
		this.id = id;
	}

	public Item(Item item) {
		this.cod = item.getCod();
		this.name = item.getName();
		this.valueItem = item.getValueItem();

		this.amount = item.getAmount();
		this.category = new Category(item.getCategory().getId());
	}

	public void setCategory(Long categoria2) {
		this.category = new Category(categoria2);
		
	}

	public Item(String nome2, double valor2, Long quantidade2, Long id2) {
		this.name = nome2;
		this.valueItem = valor2;
		this.amount = quantidade2;
		this.category = new Category(id2);
	}
	
	@Override
	public String toString() {
		return "Cód do item: " + this.cod + " - Nome do item: " + this.name + " - Unidades: " + this.amount;
	}
	
}
