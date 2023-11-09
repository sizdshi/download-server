-- 建库
create database if not exists downloader_db;

-- 切表
use downloader_db;

drop table download;
create table if not exists download
(
    id               bigint auto_increment comment 'id' primary key,
    file_name        varchar(256)                           not null comment '文件名',
    url              varchar(256)                           not null comment '下载地址',
    user_id          varchar(256)                           null comment '发布人',
    count            bigint       default 4                 null comment '当前线程数',
    status           varchar(256) default 'STATUS_NONE'     null comment '文件状态(STATUS_NONE: 默认状态 STATUS_WAITING : 等待下载 STATUS_DOWNLOADING : 正在下载
                                                                        STATUS_PAUSED : 停止下载 STATUS_DOWNLOADED : 下载完成)',
    file_url         varchar(256)                           null comment '文件存储路径',
    task_type        varchar(32)                            null comment 'http or bt',
    torrent          varchar(256)                           null comment 'bt种子信息',
    bytes_download   bigint                                 null comment '曾经上传过的分片号',
    total_chunks     bigint                                 null comment '文件的总分片数',
    upload_file_size bigint(20) unsigned                    null COMMENT '资源大小，单位为字节',
    md5              varchar(256)                           null comment '文件MD5值',
    total_time       varchar(256)                           null comment '下载时长',
    create_time      datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete        tinyint      default 0                 not null comment '是否删除'
) comment '上传下载表';

insert into downloader_db.`download` (`id`, `file_name`, `url`, `count`, `file_url`, `task_type`, `torrent`, `bytes_download`, `total_chunks`, `upload_file_size`, `md5`, `total_time`, `create_time`, `update_time`, `is_delete`) values (1705234447153963010, '2Dm', 'www.lenna-altenwerth.biz', 4, 'www.cletus-nitzsche.co', 'http', 'agZ', 2000, 529, '500', '212648dd-f050-49bf-a0bb-71688035be7a', '500', '2023-09-22 22:55:48', '2023-09-22 22:55:48', 0);
insert into downloader_db.`download` (`id`, `file_name`, `url`, `count`, `file_url`, `task_type`, `torrent`, `bytes_download`, `total_chunks`, `upload_file_size`, `md5`, `total_time`, `create_time`, `update_time`, `is_delete`) values (1705237104270712833, 'aW', 'www.franklyn-bruen.org', 4, 'www.lamont-windler.co', 'http', 'vipb4', 2001, 8134940, '501', 'e655d637-ed11-458c-a4a7-0fe394440138', '500', '2023-09-22 22:55:48', '2023-09-22 22:55:48', 0);
insert into downloader_db.`download` (`id`, `file_name`, `url`, `count`, `file_url`, `task_type`, `torrent`, `bytes_download`, `total_chunks`, `upload_file_size`, `md5`, `total_time`, `create_time`, `update_time`, `is_delete`) values (1705237990061580289, '0z', 'www.hosea-monahan.biz', 4, 'www.gwenn-franecki.net', 'http', 'adFEe', 2002, 6240804, '502', '0949f235-35ad-4ca8-8392-b118bdd8976e', '500', '2023-09-22 22:55:48', '2023-09-22 22:55:48', 0);
insert into downloader_db.`download` (`id`, `file_name`, `url`, `count`, `file_url`, `task_type`, `torrent`, `bytes_download`, `total_chunks`, `upload_file_size`, `md5`, `total_time`, `create_time`, `update_time`, `is_delete`) values (1705237990061588976, 'bRb', 'www.zachery-gottlieb.name', 4, 'www.lyndsey-bernhard.org', 'http', 'ZUVoS', 2003, 2547, '503', '7d204bc5-3798-4bcd-91ee-36f885e5a853', '500', '2023-09-22 22:55:48', '2023-09-22 22:55:48', 0);
insert into downloader_db.`download` (`id`, `file_name`, `url`, `count`, `file_url`, `task_type`, `torrent`, `bytes_download`, `total_chunks`, `upload_file_size`, `md5`, `total_time`, `create_time`, `update_time`, `is_delete`) values (1705237990061586155, 'T8PgM', 'www.fred-shields.com', 4, 'www.lannie-osinski.name', 'http', '9n', 2004, 3, '504', 'e655d637-ed11-458c-a4a7-0fe394440138', '500', '2023-09-22 22:55:48', '2023-09-22 22:55:48', 0);
insert into downloader_db.`download` (`id`, `file_name`, `url`, `count`, `file_url`, `task_type`, `torrent`, `bytes_download`, `total_chunks`, `upload_file_size`, `md5`, `total_time`, `create_time`, `update_time`, `is_delete`) values (1705237990061580138, 'wg', 'www.edwardo-kilback.net', 4, 'www.charolette-mccullough.com', 'http', 'V06', 2005, 813292, '505', '8506b114-8117-4fcd-975e-12919cad6155', '500', '2023-09-22 22:55:48', '2023-09-22 22:55:48', 0);



