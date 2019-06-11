-- every minute : 60*24*7 = 10080
create table recent_week (
    id int not null auto_increment,
    agent_id varchar(80),
    datetime datetime,
    name varchar(80),
    type varchar(20),
    last_value double,
    min_value double,
    max_value double,
    updated timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY unique_index (agent_id, name, type, datetime)
) ENGINE=InnoDB;

-- every 5 minute : (60/5) * 24 * 30 = 8640
create table recent_month;

-- every 1 hour : (60/60) * 24 * 365 = 8760
create table recent_year;

-- history table of IR remote control.
create table ir_cmd_history (
    id int primary key auto_increment,
    agent_id varchar(80),
    datetime timestamp default 0,
    cmd varchar(20),
    result varchar(80),
    created timestamp,
    creator_id varchar(80),
    modified timestamp default current_timestamp on update current_timestamp,
    modifier_id varchar(80)
) ENGINE=InnoDB;