create table itens(

    cod bigint not null unique,
    img mediumblob,
	name varchar(500) not null unique,
    replacement_value decimal(10,2) not null,
    quantity int not null,
    categoria_id bigint not null,
    

    primary key(cod),
    constraint fk_categoria_id foreign key(categoria_id) references categorys(id)

)