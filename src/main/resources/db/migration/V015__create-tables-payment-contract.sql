create table contract_payment(

    id bigint not null auto_increment,
    contract_id bigint,
    payment_id bigint,
	primary key(id),
    foreign key(contract_id) references contract(id) ON DELETE CASCADE ON UPDATE NO ACTION,
    foreign key(payment_id) references payment(id) ON DELETE CASCADE ON UPDATE NO ACTION);



)