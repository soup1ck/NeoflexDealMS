create table passport
(
    passport_id  bigint,
    series       varchar,
    "number"     varchar,
    issue_branch varchar,
    issue_date   date
);
create table employment
(
    employment_id           bigint,
    status                  varchar,
    employer_inn            varchar,
    salary                  decimal,
    "position"              varchar,
    work_experience_total   int,
    work_experience_current int
);
create table client
(
    client_id        bigint primary key,
    last_name        varchar,
    first_name       varchar,
    middle_name      varchar,
    birth_date       date,
    email            varchar,
    gender           varchar,
    marital_status   varchar,
    dependent_amount int,
    passport         jsonb,
    employment       jsonb,
    account          varchar
);
create table credit
(
    credit_id        bigint primary key,
    amount           decimal,
    term             int,
    monthly_payment  decimal,
    rate             decimal,
    psk              decimal,
    payment_schedule jsonb,
    insurance_enable boolean,
    salary_client    boolean,
    credit_status    varchar
);
create table status_history
(
    status      varchar,
    "time"      timestamp,
    change_type varchar
);
create table application
(
    application_id bigint primary key,
    client_id      bigint references client (client_id),
    credit_id      bigint references credit (credit_id),
    status         varchar,
    creation_date  timestamp,
    applied_offer  jsonb,
    sign_date      timestamp,
    ses_code       int,
    status_history jsonb
);
