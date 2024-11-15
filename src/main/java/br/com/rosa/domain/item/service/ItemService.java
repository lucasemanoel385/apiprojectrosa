package br.com.rosa.domain.item.service;

import java.util.ArrayList;
import java.util.List;

import br.com.rosa.domain.TransformAndResizeImage;
import br.com.rosa.domain.categoryItem.RepositoryCategory;
import br.com.rosa.domain.item.validation.ValidateIfExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.item.dto.UpdateItem;
import br.com.rosa.domain.item.dto.DataItem;
import br.com.rosa.domain.item.dto.RegisterItem;

@Service
public class ItemService {
	
	@Autowired
	private RepositoryItem repository;

	@Autowired
	private RepositoryCategory repositoryCategory;

	@Autowired
	private ValidateIfExists validate;

	
	public Item createItem(MultipartFile file, RegisterItem dados) {

		validate.validateRegisterItem(dados.cod(), dados.category(), dados.name());

		var category = repositoryCategory.getReferenceByName(dados.category());

		if(file == null) {
			throw new NullPointerException("Imagem não selecionada");
		}

		var imgBytes = TransformAndResizeImage.saveImgItem(file);
		
		//Salva o item no banco de dados
		var item = new Item(dados, category.getId(), imgBytes);
		repository.save(item);
		return item;
	}


	public DataItem getItemId(Long id) {
		var item = repository.getReferenceById(id);

		var base64Image = TransformAndResizeImage.takeImage(item.getImg());

		DataItem i = new DataItem(item, base64Image);

		return i;
	}

	public Page<DataItem> listItems(Pageable page, String search) {

		if (search == null || search.isEmpty()) {

			var itemsParam = repository.findAll(Sort.by("name"));
			return forListItems(itemsParam, page);

		} else {

			var itemsParam = repository.findAllByNameOrCode(search);
			return forListItems(itemsParam, page);
		}
	}

	public Page<DataItem> forListItems(List<Item> items, Pageable page) {

		List<DataItem> listItems = new ArrayList<>();

		items.forEach(item -> {

			//Pega a imagem da pasta com a ulr salva no banco de dados
			var base64Image = TransformAndResizeImage.takeImage(item.getImg());

			//Criamos a instancia do DTO(Record) e adicionamos na lista
			DataItem i = new DataItem(item, base64Image);
			listItems.add(i);
		});

		//Retornar o menor numero entre os 2 parametros
		int start = Math.min((int)page.getOffset(), listItems.size());
		int end = Math.min((start + page.getPageSize()), listItems.size());
		Page<DataItem> pagee = new PageImpl<DataItem>(listItems.subList(start, end), page, listItems.size());

		return pagee;
	}

	public Item updateItem(UpdateItem data, MultipartFile file) {

		var item = repository.getReferenceById(data.id());

		validate.validateUpdateItem(data, item);

		checkAndUpdateNullOrBlank(item, data);

		if(!(file == null)) {
			item.setImg(TransformAndResizeImage.saveImgItem(file));
		}

		repository.save(item);

		return item;

	}

	private void checkAndUpdateNullOrBlank(Item item, UpdateItem data) {

		if(data.name() != null) {
			item.setName(data.name());
		}
		if(data.replacementValue() > 0) {
			item.setReplacementValue(data.replacementValue());
		}
		if(data.amount() >= 0) {
			item.setQuantity(data.amount());
		}
		if(data.category() != item.getCategory().getName() && repositoryCategory.existsByName(data.category())) {

			var category = repositoryCategory.getReferenceByName(data.category());
			item.setCategory(category.getId());
		}
	}
}
