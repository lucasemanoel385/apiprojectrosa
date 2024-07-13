create table company_data(
    id bigint not null auto_increment,
    img blob not null,
    reason varchar(255) not null,
    fantasy_name varchar(255) not null,
    cnpj varchar (20) not null,
    street varchar (100) not null,
    number varchar (15) not null,
    district varchar (50) not null,
    cep varchar (50) not null,
    city varchar (100) not null,
    uf char (2) not null,
    phone1 varchar (50),
    phone2 varchar (50),
    clauses varchar(14000),
    observation varchar (500),
	primary key(id));
 
