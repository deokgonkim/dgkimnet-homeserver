
# homeserver - Home IoT server

## Description

homeserver provides data visualization for home IoT devices.

Currently, only one device, Raspberry Pi, is connected to my home.
The device will send room temperature and humidity numbers,
and can turn-on/off AC or turn on/off TV via IR transmitter.

homeserver will draw charts with received temperature and humidity.

Chart data will be stored in relational database.

## Workhistory

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/15

    Testing telegram bot
        * When scheduled AC control, send notification message via telegram bot.

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/13
   
    Testing Quartz Job backed RDBMS
        * scheduling page added

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/12
   
    Testing Quartz Job backed RDBMS

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/11
   
    Enhanced IR command history
        * ir_cmd_history : columns added(created, creator_id, modified, modifier_id)
        * testing quartz job

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/09
   
    Added AC remote control page.
        * (trivial) quartz sql added.
        * AC remote control page view added.
        * AC remote control method added.
        * AC remote control history table added.
        * jquery datatables added.

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/08
   
    Tested IR Command send via MQ.
        * originally, telegram bot received IR commands.
        * added another python MQ consumer added (rpi_sensor project)

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/06
   
    Bootstraped and Time adjustable chart added.
        * bootstrap added
        * font awesome added
        * jquery added
        * startbootstrap added

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/04
   
    Testing chart.js 

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/03
   
    basic mybatis implementations added.

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/01
   
    build a basic web application constructure based on spring-archetype.
    basic rabbitmq client implementation.
