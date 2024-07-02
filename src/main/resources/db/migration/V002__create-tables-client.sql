create table client(
	
	id bigint not null auto_increment,
	name_reason varchar(55) not null,
	cpf_cnpj varchar(20) not null unique,
	rg_state_registration varchar(20) not null unique,
	date_birth_company_formation datetime not null,
	email varchar(100),
	cep varchar(10) not null,
	city varchar(55) not null,
	district varchar(55) not null,
	street varchar(100) not null,
	number varchar(10) not null,
	uf varchar(2) not null,
   	phone1 varchar(20),
   	phone2 varchar(20),
   	 
	primary key(id)
);

