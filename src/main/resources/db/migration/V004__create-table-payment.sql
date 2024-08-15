create table payment(

    id bigint not null auto_increment,
    payment_value decimal(10,2) not null,
    date_payment date not null,
    contract_id bigint not null,

    primary key(id),
    CONSTRAINT fk_payment_contract_id FOREIGN KEY (contract_id) REFERENCES contract(id) ON DELETE CASCADE


);
