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
import br.com.rosa.domain.categoryItem.RepositoryCategoria;
import br.com.rosa.domain.item.validation.ValidateIfExists;
import br.com.rosa.infra.exceptions.NullPointerException;
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
import br.com.rosa.domain.item.dto.AtualizarItem;
import br.com.rosa.domain.item.dto.DadosItem;
import br.com.rosa.domain.item.dto.ItemCadastro;

@Service
public class ItemService {
	
	@Autowired
	private RepositoryItem repository;

	@Autowired
	private RepositoryCategoria repositoryCategory;

	@Autowired
	private ValidateIfExists validate;
	
	private final String destinationFolder = "C:\\Users\\lucas\\Documents\\tudo de programação\\ProjetoRosa\\rosa\\src\\main\\java\\assetItem\\";
	
	public Item createItem(MultipartFile file, ItemCadastro dados) {

		validate.validateRegisterItem(dados.cod(), dados.category(), dados.name());
		/*if (repository.existsByCod(dados.cod())) {
			throw new SqlConstraintViolationException("Código do produto já existe");
		}

		if (!repositoryCategory.existsByName(dados.category())) {
			throw new SqlConstraintViolationException("Categoria não foi criada");
		}

		if (repository.existsByName(dados.name())) {
			throw new SqlConstraintViolationException("Nome do produto já cadastrado");
		}*/

		var category = repositoryCategory.getReferenceByName(dados.category());

		System.out.println(System.getProperty("user.dir") + "\\src\\main\\resources\\assetProducts");

		if(file == null) {
			throw new NullPointerException("Imagem não selecionada");
		}

		var imgBytes = TransformeAndResizeImage.saveImgItem(file);
		
		//Salva o item no banco de dados
		var item = new Item(dados, category.getId(), imgBytes);
		repository.save(item);
		return item;
	}


	public DadosItem getItemId(Long id) {
		var item = repository.getReferenceById(id);

		var base64Image = TransformeAndResizeImage.takeImage(item.getImg());

		DadosItem i = new DadosItem(item, base64Image);

		return i;
	}

	public Page<DadosItem> listItens(Pageable page, String search) {

		List<DadosItem> listItens = new ArrayList<>();

		if (search == null || search.isEmpty()) {
			var itens = repository.findAll(Sort.by("name"));

			return forListItens(itens, page);
		} else {

			var itensParam = repository.findAllByNameOrCode(search);
			return forListItens(itensParam, page);

		}

        //Puxar os itens
		//var itens = repository.findAll(Sort.by("name"));

		/*var t = itens.stream().map(c -> {
			//Pega a imagem da pasta com a ulr salva no banco de dados
			var base64Image = takeImage(c.getUrlimg());

			//Criamos a instancia do DTO(Record) e adicionamos na lista
			DadosItem i = new DadosItem(c, base64Image);

			return i;
		}).toList();*/
	}

	public Page<DadosItem> forListItens(List<Item> items, Pageable page) {

		List<DadosItem> listItens = new ArrayList<>();

		items.forEach(item -> {

			//Pega a imagem da pasta com a ulr salva no banco de dados
			var base64Image = TransformeAndResizeImage.takeImage(item.getImg());

			//Criamos a instancia do DTO(Record) e adicionamos na lista
			DadosItem i = new DadosItem(item, base64Image);
			listItens.add(i);
		});

		//Retornar o menor numero entre os 2 parametros
		int start = Math.min((int)page.getOffset(), listItens.size());
		int end = Math.min((start + page.getPageSize()), listItens.size());
		Page<DadosItem> pagina = new PageImpl<DadosItem>(listItens.subList(start, end), page, listItens.size());
		System.out.println(start + "||" +page.getOffset()+ "||" + listItens.size() + "||"+ end);

		return pagina;
	}

	/*public Page<DadosItem> listAllItens(Pageable page) {

		//Puxar os itens
		var itens = repository.findAll(Sort.by("name"));

		List<DadosItem> listItens = new ArrayList<>();

		itens.forEach(item -> {

			//Pega a imagem da pasta com a ulr salva no banco de dados
			var base64Image = CreateReadImage.takeImage(item.getUrlimg());

			//Criamos a instancia do DTO(Record) e adicionamos na lista
			DadosItem i = new DadosItem(item, base64Image);
			listItens.add(i);
		});

		//Retornar o menor numero entre os 2 parametros
		int start = Math.min((int)page.getOffset(), listItens.size());
		int end = Math.min((start + page.getPageSize()), listItens.size());
		Page<DadosItem> pagina = new PageImpl<DadosItem>(listItens);
		System.out.println(start + "||" +page.getOffset()+ "||" + listItens.size() + "||"+ end);
		return pagina;

	}*/


	public Item updateItem(AtualizarItem dados, MultipartFile file) {

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

	private void checkAndUpdateNullorBlank(Item item, AtualizarItem data) {

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

	private String saveImgItem(MultipartFile file) {

		//Dados da pasta e nome do arquivo
		var nameImage = file.getOriginalFilename().toLowerCase();
		var urlImg = destinationFolder + nameImage;

		try {
			//Cria a imagem na pastida destinada
			Files.write(Paths.get(urlImg), file.getBytes());

			//Lê a imagem
			BufferedImage imageOrigin = ImageIO.read(new File(urlImg));

			int imageWidth = 200;
			int imageHeight = 200;

			Files.delete(Paths.get(urlImg));

			//Redimensiona a imagem com a largura e altura desejada
			BufferedImage imagemRedimensionada = resizeImage(imageOrigin, imageWidth, imageHeight);

			//Desenha a imagem com sua altura e largura definidas
			Graphics2D g = imagemRedimensionada.createGraphics();
			g.drawImage(imageOrigin, 0, 0, imageWidth, imageHeight, null);
			g.dispose();

			//Salva a imagem redimensionada
			ImageIO.write(imagemRedimensionada, "png", new File(urlImg));
		} catch (Exception e) {
			throw new ValidacaoException("Formatação de imagem errada");
		}

		return nameImage;
	}

	private String takeImage(String urlImg) {
		//Pega o caminho da pasta que contem o arquivo de cada item
		Path imgUrl = Paths.get(destinationFolder + urlImg.toLowerCase());
		byte[] img = null;
		String base64Image = null;
		try {
			//Pega o arquivo e tranforma em bytes
			img = Files.readAllBytes(imgUrl);

			//Transformamos o byte em String
			base64Image = Base64.getEncoder().encodeToString(img);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return base64Image;
	}

	private static BufferedImage resizeImage(BufferedImage imageOriginal, int width, int height) {
        Image resizedImage = imageOriginal.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Desenha a imagem redimensionada
        Graphics2D g = newImage.createGraphics();
        g.drawImage(resizedImage, 0, 0, null);
        g.dispose();

        return newImage;
    }
}
