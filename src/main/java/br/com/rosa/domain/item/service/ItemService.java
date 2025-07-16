package br.com.rosa.domain.item.service;

import br.com.rosa.domain.TransformAndResizeImage;
import br.com.rosa.domain.categoryItem.RepositoryCategory;
import br.com.rosa.domain.item.validation.ValidateIfExists;
import br.com.rosa.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public Page<DataItem> listItems(Pageable page, String search, String filterSearch) {

		if (search == null || search.isEmpty()) {
			System.out.println("teste");
			return repository.findAll(page).map(i -> new DataItem(i,TransformAndResizeImage.takeImage(i.getImg())));
		} else if (filterSearch.equals("cod")) {
			return repository.findAllByCode(page,search).map(i -> new DataItem(i,TransformAndResizeImage.takeImage(i.getImg())));
		} else if (filterSearch.equals("reference")){
			return repository.findAllByReference(page,search).map(i -> new DataItem(i,TransformAndResizeImage.takeImage(i.getImg())));
		} else {
			return repository.findAllByName(page,search).map(i -> new DataItem(i,TransformAndResizeImage.takeImage(i.getImg())));
		}

		/*if (search == null || search.isEmpty()) {
			return repository.findAll(page).map(i -> new DataItem(i,TransformAndResizeImage.takeImage(i.getImg())));
		} else {
			return repository.findAllByNameOrCodeOrReference(page,search).map(i -> new DataItem(i,TransformAndResizeImage.takeImage(i.getImg())));
		}*/
	}

	public Item updateItem(UpdateItem data, MultipartFile file) {

		var item = repository.getReferenceById(data.cod());

		validate.validateUpdateItem(data, item);

		checkAndUpdateNullOrBlank(item, data);

		if(!(file == null)) {
			item.setImg(TransformAndResizeImage.saveImgItem(file));
		}

		repository.save(item);

		return item;

	}

	private void checkAndUpdateNullOrBlank(Item item, UpdateItem data) {

		if(data.reference() != null) {
			item.setReference(data.reference());
		}

		if(data.name() != null) {
			item.setName(data.name());
		}
		if(data.replacementValue() != null) {
			item.setReplacementValue(data.replacementValue());
		}
		if(data.amount() >= 0) {
			item.setQuantity(data.amount());
		}
		if(data.category() != item.getCategory().getName()) {
			var category = repositoryCategory.getReferenceByName(data.category());
			item.setCategory(category.getId());
		}
	}

    public void deleteItem(Long id) {

		var itensWithContract = repository.findAllContractsWithItem(id);

		if (itensWithContract > 0) {
			throw new ValidationException("Parece que tem um item locado no contrato. Favor remover o item do contrato antes de excluir.");
		}

		repository.deleteById(id);

    }
}
