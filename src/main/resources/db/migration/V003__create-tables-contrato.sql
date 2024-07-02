create table Contract(

    id bigint not null auto_increment,
    client_id bigint not null,
    date_contract date not null,
    start_date date not null,
    final_date date not null,
    discount float not null,
    value decimal(10,2) not null,
    value_total decimal(10,2) not null,
    contract_situation varchar(30) not null,
    seller varchar(40),
    observation varchar(500),
    annotations varchar(500),
    

    primary key(id),
    constraint fk_contract_cliente_id foreign key(client_id) references client(id)

)