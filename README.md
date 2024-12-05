# TEAM 23. Message Brokers

###  Video demonstrations
* ***Pipes-and-Filters*** - https://rutube.ru/video/private/b453fa8b3777ace74baba3b1a126e725/?p=ZSR0QAvHU0EQljDEOamzjg
* ***Event-Driven variations*** - https://rutube.ru/video/private/97308b2c4064bc12b3e94657260731de/?p=_78OPRlWeufy_9p34WldaA

### Report
In Pipes-and-filters tests, delays are between 1.22 to 1.29 seconds per message because of synchronous processing. While event-driven tests showed faster message processing (from 594 ms to 927 ms) because of asynchronous handling.
Event-driven systems, while fault-tolerant, require more RAM because of independent services and constant RabbitMQ connections. This makes sure that the failures in one service don't break others, but also increases resource usage. On the other hand, pipes-and-filters are efficient in resource usage, as all functions run sequentially in one service, reducing memory usage. However, this approach affects fault tolerance in the way that the approach weakens it since a failure in one part stops the entire pipeline. Generally, the choice depends on whether the system prioritizes resource efficiency or persistence.
