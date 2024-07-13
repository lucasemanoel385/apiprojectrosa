package br.com.rosa.domain.item.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import br.com.rosa.domain.TransformeAndResizeImage;
import br.com.rosa.domain.categoryItem.RepositoryCategory;
import br.com.rosa.domain.item.validation.ValidateIfExists;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import br.com.rosa.infra.exceptions.ValidacaoException;
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

		var imgBytes = TransformeAndResizeImage.saveImgItem(file);
		
		//Salva o item no banco de dados
		var item = new Item(dados, category.getId(), imgBytes);
		repository.save(item);
		return item;
	}


	public DataItem getItemId(Long id) {
		var item = repository.getReferenceById(id);

		var base64Image = TransformeAndResizeImage.takeImage(item.getImg());

		DataItem i = new DataItem(item, base64Image);

		return i;
	}

	public Page<DataItem> listItens(Pageable page, String search) {

		List<DataItem> listItens = new ArrayList<>();

		if (search == null || search.isEmpty()) {
			var itens = repository.findAll(Sort.by("name"));

			return forListItens(itens, page);
		} else {

			var itensParam = repository.findAllByNameOrCode(search);
			return forListItens(itensParam, page);

		}
	}

	public Page<DataItem> forListItens(List<Item> items, Pageable page) {

		List<DataItem> listItens = new ArrayList<>();

		items.forEach(item -> {

			//Pega a imagem da pasta com a ulr salva no banco de dados
			var base64Image = TransformeAndResizeImage.takeImage(item.getImg());

			//Criamos a instancia do DTO(Record) e adicionamos na lista
			DataItem i = new DataItem(item, base64Image);
			listItens.add(i);
		});

		//Retornar o menor numero entre os 2 parametros
		int start = Math.min((int)page.getOffset(), listItens.size());
		int end = Math.min((start + page.getPageSize()), listItens.size());
		Page<DataItem> pagina = new PageImpl<DataItem>(listItens.subList(start, end), page, listItens.size());

		return pagina;
	}

	public Item updateItem(UpdateItem dados, MultipartFile file) {

		var item = repository.getReferenceById(dados.id());

		validate.validateUpdateItem(0l, dados, item);

		if (item.getCod() != dados.cod() && repository.existsByCod(dados.cod())){
			throw new SqlConstraintViolationException("Código do produto já existe");
		}

		checkAndUpdateNullorBlank(item, dados);

		if(!(file == null)) {
			item.setImg(TransformeAndResizeImage.saveImgItem(file));
		}

		repository.save(item);

		return item;

	}

	private void checkAndUpdateNullorBlank(Item item, UpdateItem data) {

		if(data.name() != null || data.name() != "") {
			item.setName(data.name());
		}
		if(data.value() > 0) {
			item.setValueItem(data.value());
		}
		if(data.replacementValue() > 0) {
			item.setReplacementValue(data.replacementValue());
		}
		if(data.amount() >= 0) {
			item.setAmount(data.amount());
		}
		if(data.category() != item.getCategory().getName() && repositoryCategory.existsByName(data.category())) {

			var category = repositoryCategory.getReferenceByName(data.category());
			item.setCategory(category.getId());
		}
	}
}
