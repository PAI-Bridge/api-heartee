-- drop every constraint --
alter table if exists chat
drop constraint if exists FKgi1b8sl01jboyasp8260gk8pm;

alter table if exists coin
drop constraint if exists FKicggcvlpa7nq4wmyglnucmy8k;

alter table if exists conversation
drop constraint if exists FKa5ed5197asr0sfn1js5tn6raa;

alter table if exists counsel_report_bu
drop constraint if exists FKqjmelvhce767qu0xf5nbddpjg;

alter table if exists counsel_report_dt
drop constraint if exists FKdl8u9s7c22qselrk9jhqh7l8r;

alter table if exists counsel_report_gl
drop constraint if exists FKcqq7xrsx9naxyuhcin3pjp3x7;

alter table if exists counsel_report
drop constraint if exists FKovye01sy8dgqnljrvuc3o67ng;

alter table if exists counsel_request
drop constraint if exists FK7ptxg5sup0r64i79tpmxe2seq;

alter table if exists partner
drop constraint if exists FKsxf86ugta2yhtun33tpkosjso;

alter table if exists partner_bu
drop constraint if exists FKka5lx61qn3xmn7yt6pvr9u0iw;

alter table if exists partner_dt
drop constraint if exists FKgp86ur0o6sla43gu1y1fbspti;

alter table if exists partner_gl
drop constraint if exists FKqxtjvo2rfladmx810gj7627u0;

alter table if exists payment
drop constraint if exists FK4pswry4r5sx6j57cdeulh1hx8;

-- drop every table --
drop table if exists chat cascade;
drop table if exists coin cascade;
drop table if exists conversation cascade;
drop table if exists counsel_report_bu cascade;
drop table if exists counsel_report_dt cascade;
drop table if exists counsel_report_gl cascade;
drop table if exists counsel_category cascade;
drop table if exists counsel_report cascade;
drop table if exists counsel_request cascade;
drop table if exists member cascade;
drop table if exists partner cascade;
drop table if exists partner_bu cascade;
drop table if exists partner_dt cascade;
drop table if exists partner_gl cascade;
drop table if exists payment cascade;
drop table if exists temp_conversation cascade;

-- drop sequence --
drop sequence if exists hibernate_sequence;

-- create sequence --
create sequence hibernate_sequence start 1 increment 1;

-- create member --
create table member (
    member_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    age int4,
    email varchar(255),
    gender varchar(255),
    name varchar(255),
    nickname varchar(255),
    oauth_id varchar(255),
    oauth_type varchar(255),
    primary key (member_id)
);

-- create partner --
create table partner (
    dtype varchar(31) not null,
    partner_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    age int4,
    gender varchar(255),
    mbti varchar(255),
    nickname varchar(255),
    member_id int8,
    primary key (partner_id)
);

create table partner_bu (
    info_bu varchar(255),
    partner_id int8 not null,
    primary key (partner_id)
);

create table partner_dt (
    info_dt varchar(255),
    partner_id int8 not null,
    primary key (partner_id)
);

create table partner_gl (
    info_gl varchar(255),
    partner_id int8 not null,
    primary key (partner_id)
);

-- create counsel_category --
create table counsel_category (
    counsel_category_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    code varchar(255),
    subtitle varchar(255),
    title varchar(255),
    primary key (counsel_category_id)
);

-- create counsel_request --
create table counsel_request (
    counsel_request_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    comment varchar(255),
    price int8,
    partner_id int8,
    primary key (counsel_request_id)
);

-- create counsel_report --
create table counsel_report (
    dtype varchar(31) not null,
    counsel_report_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    solution varchar(255),
    stat_a int4,
    stat_b int4,
    summary varchar(255),
    temperature int4,
    counsel_request_id int8,
    primary key (counsel_report_id)
);

create table counsel_report_bu (
    stat_bu int4,
    counsel_report_id int8 not null,
    primary key (counsel_report_id)
);

create table counsel_report_dt (
    stat_dt int4,
    counsel_report_id int8 not null,
    primary key (counsel_report_id)
);

create table counsel_report_gl (
    stat_gl int4,
    counsel_report_id int8 not null,
    primary key (counsel_report_id)
);

-- create conversation --
create table conversation (
    conversation_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    image_url varchar(255),
    sequence int4,
    counsel_request_id int8,
    primary key (conversation_id)
);

-- create temp_conversation --
create table temp_conversation (
    temp_conversation_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    data jsonb,
    price int4,
    primary key (temp_conversation_id)
);

-- create chat --
create table chat (
    chat_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    content varchar(255),
    sent_at varchar(255),
    sequence int4,
    word_count int4,
    conversation_id int8,
    primary key (chat_id)
);

-- create payment --
create table payment (
    payment_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    coin_price int4,
    coin_quantity int4,
    total_price int4,
    member_id int8,
    primary key (payment_id)
);

-- create coin --
create table coin (
    coin_id int8 not null,
    created_at timestamp,
    last_modified_at timestamp,
    quantity int4,
    member_id int8,
    primary key (coin_id)
);

-- alter references --
alter table if exists chat
    add constraint FKgi1b8sl01jboyasp8260gk8pm
    foreign key (conversation_id)
    references conversation;

alter table if exists coin
    add constraint FKicggcvlpa7nq4wmyglnucmy8k
    foreign key (member_id)
    references member;

alter table if exists conversation
    add constraint FKa5ed5197asr0sfn1js5tn6raa
    foreign key (counsel_request_id)
    references counsel_request;

alter table if exists counsel_report_bu
    add constraint FKqjmelvhce767qu0xf5nbddpjg
    foreign key (counsel_report_id)
    references counsel_report;

alter table if exists counsel_report_dt
    add constraint FKdl8u9s7c22qselrk9jhqh7l8r
    foreign key (counsel_report_id)
    references counsel_report;

alter table if exists counsel_report_gl
    add constraint FKcqq7xrsx9naxyuhcin3pjp3x7
    foreign key (counsel_report_id)
    references counsel_report;

alter table if exists counsel_report
    add constraint FKovye01sy8dgqnljrvuc3o67ng
    foreign key (counsel_request_id)
    references counsel_request;

alter table if exists counsel_request
    add constraint FK7ptxg5sup0r64i79tpmxe2seq
    foreign key (partner_id)
    references partner;

alter table if exists partner
    add constraint FKsxf86ugta2yhtun33tpkosjso
    foreign key (member_id)
    references member;

alter table if exists partner_bu
    add constraint FKka5lx61qn3xmn7yt6pvr9u0iw
    foreign key (partner_id)
    references partner;

alter table if exists partner_dt
    add constraint FKgp86ur0o6sla43gu1y1fbspti
    foreign key (partner_id)
    references partner;

alter table if exists partner_gl
    add constraint FKqxtjvo2rfladmx810gj7627u0
    foreign key (partner_id)
    references partner;

alter table if exists payment
    add constraint FK4pswry4r5sx6j57cdeulh1hx8
    foreign key (member_id)
    references member;












