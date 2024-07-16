create table itens(

    id bigint not null auto_increment,
    cod bigint not null unique,
    img blob not null,
	name varchar(255) not null unique,
    value_item decimal(10,2) not null,
    replacement_value decimal(10,2) not null,
    amount int not null,
    categoria_id bigint not null,
    

    primary key(id),
    constraint fk_categoria_id foreign key(categoria_id) references categorys(id)

)