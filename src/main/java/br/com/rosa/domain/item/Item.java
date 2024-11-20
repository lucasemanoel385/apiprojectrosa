package br.com.rosa.domain.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rosa.domain.categoryItem.Category;
import br.com.rosa.domain.item.dto.RegisterItem;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@EqualsAndHashCode(of = "cod")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {
	
	@Id
	private Long cod;
	private byte[] img;
	private String name;
	private double replacementValue;
	private Long quantity;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Category category;
	
	public Item(RegisterItem data, Long idCategory, byte[] img) {
		this.img = img;
		this.cod = (long) data.cod();
		this.name = data.name();
		this.replacementValue = data.replacementValue();
		this.quantity = (long) data.amount();
		this.category = new Category(idCategory);
	}
	
	public Item(int id) {
		this.cod = (long) id;
	}
	
	public Item(Long id) {
		this.cod = id;
	}

	public Item(Item item) {
		this.cod = item.getCod();
		this.name = item.getName();


		this.quantity = item.getQuantity();
		this.category = new Category(item.getCategory().getId());
	}

	public Item(Long codigo, String descricao, double v, Long quantidade, Long idCategory) {
		this.cod = codigo;
		this.name = descricao;
		this.replacementValue = v;
		this.quantity = quantidade;
		this.category = new Category(idCategory);
	}

	public void setCategory(Long categoria2) {
		this.category = new Category(categoria2);
		
	}

	public Item(String nome2, Long quantidade2, Long id2) {
		this.name = nome2;
		this.quantity = quantidade2;
		this.category = new Category(id2);
	}
	
	@Override
	public String toString() {
		return "Cód do item: " + this.cod + " - Nome do item: " + this.name + " - Estoque total de unidades: " + this.quantity;
	}
	
}
