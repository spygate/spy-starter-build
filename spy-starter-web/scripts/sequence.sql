create table spy_seq_cnt (
    cnt_key_seq varchar(20) primary key not null,
    cnt_seq_cod varchar(10) not null,
    cnt_sed_val varchar(10) not null,
    cnt_seq_fmt varchar(10) not null default 'SS',
    cnt_ttl_len integer not null default 16,
    cnt_rtv_buf integer not null default 20,
    cnt_seq_dat integer,
    cnt_seq_cnt long,
    cnt_rcd_sts varchar(1) not null default 'Y',
    cnt_rcd_ver integer not null default 1,
    UNIQUE KEY uidx_key1 (cnt_seq_cod, cnt_sed_val)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;