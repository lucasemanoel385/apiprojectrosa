create table user_profile(

    user_id bigint not null unique,
    profile_id bigint not null,
    foreign key(user_id) references user(id),
    foreign key(profile_id) references profile(id));


);
