-- every minute : 60*24*7 = 10080
create table recent_week (
    id int primary key auto_increment,
    agent_id varchar(80),
    datetime datetime,
    name varchar(80),
    type varchar(20),
    last_value double,
    min_value double,
    max_value double,
    updated timestamp
);

-- every 5 minute : (60/5) * 24 * 30 = 8640
create table recent_month;

-- every 1 hour : (60/60) * 24 * 365 = 8760
create table recent_year;