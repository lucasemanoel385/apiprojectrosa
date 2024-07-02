create table payment(

    id bigint not null auto_increment,
    payment_value decimal(10,2) not null,
    date_payment date not null,

    primary key(id)


);
