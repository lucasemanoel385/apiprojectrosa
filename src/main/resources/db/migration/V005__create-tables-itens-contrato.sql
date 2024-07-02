create table itens_contract(

    id bigint not null auto_increment,
    id_Item bigint not null,
    url_img varchar (200) not null,
    cod bigint not null,
	name varchar(255) not null,
    value_item_contract decimal(10,2) not null,
    replacement_value decimal(10,2) not null,
    amount int not null,
    value_total_item decimal(10,2) not null,
    start_date date not null,
    final_date date not null,
    contract_situation varchar(30),
    
    primary key(id)


)