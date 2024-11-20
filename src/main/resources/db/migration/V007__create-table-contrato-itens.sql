create table contract_itens(

    contract_id bigint,
    itens_id bigint,
    foreign key(contract_id) references contract(id) ON DELETE CASCADE ON UPDATE NO ACTION,
    foreign key(itens_id) references itens_contract(id) ON DELETE CASCADE ON UPDATE NO ACTION);
