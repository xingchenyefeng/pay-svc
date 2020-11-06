drop table if exists channel_config;
create table channel_config
(
    id                      bigint      not null primary key,
    channel_code            varchar(32) not null comment '渠道编号',
    hospital_code           varchar(32) not null comment '医院编码',
    app_id                  varchar(64) not null comment '应用id',
    pay_type                varchar(8)  not null comment '支付类型',
    app_key                 varchar(128) comment '应用秘钥',
    seller_id               varchar(64) comment '商户id',
    owner_private_key       text comment '私钥',
    alipay_public_key       text comment '阿里公钥',
    owner_public_key        text comment '公钥',
    notify_url              varchar(128) comment '回调地址',
    enable_pay_channels     varchar(128) comment '允许支付的方式',
    sys_service_provider_id varchar(50) comment '返佣账号',
    cert_id                 bigint default 0 comment '证书对应的记录id',
    cert_expire             datetime comment '证书到期时间',
    active                  char(1) comment '通道状态',
    active_at               datetime comment '本次启动时间',
    creator                 bigint      not null comment '创建者',
    created_at              datetime    not null comment '创建时间',
    updater                 bigint      not null comment '最后一次修改者',
    updated_at              datetime    not null comment '修改时间'
);

create index channel_code_idx on channel_config (channel_code);
create index hospital_code_idx on channel_config (hospital_code);



drop table if exists trade_record;
create table trade_record
(
    id              bigint   not null primary key,
    transaction_id  varchar(64) comment '支付渠道交易id',
    pay_type        varchar(4) comment '支付类型',
    receive_time    datetime comment '收到请求支付时间',
    channel_code    varchar(16) comment '渠道编号',
    hospital_code   varchar(16) comment '医院代码',
    phone           varchar(20) comment '手机号',
    name            varchar(32) comment '姓名',
    id_card         varchar(18) comment '身份证号',
    buyer_id        varchar(32) comment '用户id',
    bs_type         varchar(4) comment '业务类型',
    subject         varchar(64) comment '交易内容',
    trans_introduce text comment '交易简介',
    trans_detail    text comment '交易详情',
    trade_no        varchar(64) comment '交易流水',
    out_trade_no    varchar(64) comment '内部流水ID，对账用',
    dept_code       varchar(16) comment '部门代码',
    doctor_code     varchar(64) comment '医生代码',
    device_info     varchar(64) comment '设备信息(请求支付的设备代码)',
    fee_type        varchar(16) comment '币种',
    total_amount    varchar(16) comment '总金额',
    receipt_amount  varchar(16) comment '实收金额',
    pay_amount      varchar(16) comment '支付金额',
    client_id       varchar(32) comment '客户端id',
    time_start      varchar(20) comment '交易开始时间',
    time_end        varchar(20) comment '交易结束时间',
    time_expire     varchar(20) comment '交易有效期',
    trade_type      varchar(4) comment '交易类型',
    limit_pay       varchar(64) comment '限制交易的渠道',
    state           varchar(4) comment '交易状态码',
    result_text     varchar(64) comment '状态码对应的内容',
    refund_time     varchar(20) comment '退款时间',
    refund_amount   varchar(16) comment '退款金额',
    refund_desc     varchar(64) comment '退款说明',
    creator         bigint   not null comment '创建者',
    created_at      datetime not null comment '创建时间',
    updater         bigint   not null comment '最后一次修改者',
    updated_at      datetime not null comment '修改时间'
);

create index channel_code_idx on trade_record (channel_code);
create index transaction_idx on trade_record (transaction_id);
create index trade_no_idx on trade_record (trade_no);
create index hospital_code_idx on trade_record (hospital_code);
create index pay_type_idx on trade_record (pay_type);
create index phone_idx on trade_record (phone);
create index name_idx on trade_record (name);
create index state_idx on trade_record (state);


drop table if exists hospital_config;
create table hospital_config
(
    id            bigint   not null primary key,
    hospital_code varchar(32) comment '医院编码',
    hospital_name varchar(64) comment '医院名称',
    contact_name  varchar(16) comment '联系人姓名',
    contact_phone varchar(12) comment '联系电话',
    active        char(1) comment '状态 1可用，0 不可用',
    creator       bigint   not null comment '创建者',
    created_at    datetime not null comment '创建时间',
    updater       bigint   not null comment '最后一次修改者',
    updated_at    datetime not null comment '修改时间'
);

create index hospital_code_idx on hospital_config (hospital_code);

drop table if exists bill_detail;
create table bill_detail
(
    id                   bigint   not null primary key,
    channel_code         varchar(16) comment '支付渠道编号',
    account_period       varchar(16) comment '账期',
    trans_no             varchar(64) comment '交易流水',
    out_trans_no         varchar(64) comment '院内支付订单',
    trade_type           varchar(8) comment '交易类型',
    subject              varchar(128) comment '商品名称',
    start_time           varchar(32) comment '创建时间',
    pay_time             varchar(32) comment '支付时间',
    store_id             varchar(32) comment '门店编号',
    store_name           varchar(64) comment '门店名称',
    operator             varchar(32) comment '操作员id',
    terminal_id          varchar(64) comment '终端编号',
    buyer_id             varchar(64) comment '对方账户',
    total_amount         varchar(8) comment '订单金额',
    receipt_amount       varchar(8) comment '商家实收金额',
    envelopes            varchar(8) comment '红包',
    treasure             varchar(8) comment '集分宝',
    channel_discount     varchar(8) comment '支付渠道优惠',
    store_discount       varchar(8) comment '商家优惠',
    coupon               varchar(8) comment '券核销金额',
    coupon_name          varchar(32) comment '券名称',
    store_envelopes      varchar(8) comment '店家红包',
    card_spending_amount varchar(8) comment '卡消费金额',
    out_request_no       varchar(64) comment '退款批次号/请求号',
    point                varchar(8) comment '分润',
    service_amount       varchar(8) comment '服务费',
    mark                 varchar(50) comment '备注',
    creator              bigint   not null comment '创建者',
    created_at           datetime not null comment '创建时间',
    updater              bigint   not null comment '最后一次修改者',
    updated_at           datetime not null comment '修改时间'
);

drop table if exists bill;
create table bill
(
    id                  bigint   not null primary key,
    channel_code        varchar(16) comment '支付渠道编号',
    pay_type            varchar(16) comment '支付类型',
    account_period      varchar(16) comment '账期',
    trade_type          varchar(8) comment '交易类型',
    store_num           varchar(32) comment '门店编号',
    store_name          varchar(32) comment '门店名称',
    trans_count         varchar(8) comment '交易笔数',
    refund_count        varchar(8) comment '退费笔数',
    amount              varchar(10) comment '总金额',
    revenue             varchar(10) comment '商家实收',
    discount            varchar(10) comment '支付宝|微信优惠',
    merchant_discount   varchar(10) comment '商家优惠',
    card_consume_amount varchar(10) comment '卡消费金额',
    service_charge      varchar(10) comment '服务费',
    share_benefit       varchar(10) comment '分润',
    actual_amount       varchar(10) comment '实收净额',
    creator             bigint   not null comment '创建者',
    created_at          datetime not null comment '创建时间',
    updater             bigint   not null comment '最后一次修改者',
    updated_at          datetime not null comment '修改时间'
);

drop table if exists user;
create table user
(
    id            bigint      not null primary key,
    username      varchar(32) not null comment '用户名',
    avatar        varchar(255) comment '头像',
    password      varchar(128) comment '密码',
    real_name     varchar(20) comment '真实姓名',
    hospital_code varchar(32) not null comment '所在医院/单位',
    status        char(1) comment '状态 1: 有效,0: 无效',
    creator       bigint      not null comment '创建者',
    created_at    datetime    not null comment '创建时间',
    updater       bigint      not null comment '最后一次修改者',
    updated_at    datetime    not null comment '修改时间'
);

-- auto-generated definition
drop table if exists user_role;
create table user_role
(
    id         bigint   not null
        primary key,
    role_id    bigint   not null comment '角色id',
    user_id    bigint   not null comment '用户id',
    creator    bigint   not null comment '创建者',
    created_at datetime not null comment '创建时间',
    updater    bigint   not null comment '最后一次修改者',
    updated_at datetime not null comment '修改时间'
);

drop table if exists role_menu;
create table role_menu
(
    id         bigint   not null
        primary key,
    role_id    bigint   not null comment '角色id',
    menu_id    bigint   not null comment '菜单id',
    creator    bigint   not null comment '创建者',
    created_at datetime not null comment '创建时间',
    updater    bigint   not null comment '最后一次修改者',
    updated_at datetime not null comment '修改时间'
);


-- auto-generated definition
drop table if exists role;
create table role
(
    id          bigint                  not null
        primary key,
    name        varchar(50)             not null comment '菜单名称',
    remark      varchar(255) default '' null comment '备注',
    data_scope  varchar(255) default '' null comment '数据权限',
    level       tinyint      default 0  null comment '角色级别',
    permission  varchar(255) default '' null comment '权限',
    enabled     tinyint      default 1  null comment '是否可用',
    creator     bigint                  not null comment '创建者',
    created_at  datetime                not null comment '创建时间',
    updater     bigint                  not null comment '最后一次修改者',
    updated_at  datetime                not null comment '修改时间',
    default_tag tinyint      default 0  not null comment '默认标记',
    constraint name
        unique (name)
);

-- auto-generated definition
drop table if exists menu;
create table menu
(
    id             bigint                   not null
        primary key,
    name           varchar(50)              not null comment '菜单名称',
    component      varchar(255)             null comment '组件',
    pid            bigint       default 0   null comment '上级菜单ID',
    sort           int          default 0   null comment '排序',
    icon           varchar(100) default ''  not null comment '图标',
    path           varchar(255) default ''  not null comment '链接路径',
    cache          tinyint      default 0   null comment '是否开启缓存',
    hidden         tinyint      default 0   null comment '是否隐藏',
    component_name varchar(100) default '-' null comment '组件名称',
    permission     varchar(255) default ''  not null comment '权限',
    type           tinyint      default 0   null comment '类型',
    enabled        tinyint      default 1   null comment '是否可用',
    creator        bigint                   not null comment '创建者',
    created_at     datetime                 not null comment '创建时间',
    updater        bigint                   not null comment '最后一次修改者',
    updated_at     datetime                 not null comment '修改时间'
);

drop table if exists channel_cert;
create table channel_cert
(
    id         bigint   not null
        primary key,
    file_name varchar(64) comment '文件名',
    cert       blob     null comment '证书',
    creator    bigint   not null comment '创建者',
    created_at datetime not null comment '创建时间',
    updater    bigint   not null comment '最后一次修改者',
    updated_at datetime not null comment '修改时间'
);

/**
  未实现
 */
# drop table if exists trade_record_log;
# create table trade_record_log
# (
#     id              bigint   not null
#         primary key,
#     trade_record_id bigint   not null comment '交易主记录id',
#     handler_code varchar(8) not null comment '交易方法代码',
#     handler_name varchar(64) not null comment '交易方法名',
#     creator         bigint   not null comment '创建者',
#     created_at      datetime not null comment '创建时间',
#     updater         bigint   not null comment '最后一次修改者',
#     updated_at      datetime not null comment '修改时间'
# );
