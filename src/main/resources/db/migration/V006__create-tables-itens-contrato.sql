create table itens_contract(

    id bigint not null auto_increment,
    cod bigint not null,
    reference varchar(50) not null,
	name varchar(500) not null,
    value_item_contract decimal(10,2) not null,
    replacement_value decimal(10,2) not null,
    quantity int not null,
    value_total_item decimal(10,2) not null,
    start_date date not null,
    final_date date not null,
    contract_situation varchar(30),
    
    primary key(id)


)