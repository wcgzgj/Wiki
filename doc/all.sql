insert into `test`(id,name) values(2,'FARO_Z')


drop table if exists `demo`;
create table `demo` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    primary key (`id`)
) engine=innodb default charset =utf8mb4 comment '测试';

insert into `demo` (id,name) values(2,'测试2');

select * from demo;