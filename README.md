
# homeserver - Home IoT server

## Description

homeserver provides data visualization for home IoT devices.

Currently, only one device, Raspberry Pi, is connected to my home.
The device will send room temperature and humidity numbers,
and can turn-on/off AC or turn on/off TV via IR transmitter.

homeserver will draw charts with received temperature and humidity.

Chart data will be stored in relational database.

## Workhistory

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/04
   
    Testing chart.js 

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/03
   
    basic mybatis implementations added.

   * v1.0-SNAPSHOT - dgkim@dgkim.net 2019/06/01
   
    build a basic web application constructure based on spring-archetype.
    basic rabbitmq client implementation.
