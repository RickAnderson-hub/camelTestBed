The memory footprint of two indexed numeric columns in a PostgreSQL database with a billion rows can be estimated as follows:  
**Data Size:** The numeric type in PostgreSQL requires variable storage, with up to 16 bytes per entry. Assuming you're using the maximum of 16 bytes, two numeric columns would require 2 * 16 bytes * 1 billion = 32 GB of storage.  
**Index Size:** The size of an index in PostgreSQL is roughly 20 bytes * number of rows for a B-tree index, which is the default index type. Therefore, two indexes would require 2 * 20 bytes * 1 billion = 40 GB of storage.  
So, the total estimated memory footprint would be 32 GB (data) + 40 GB (indexes) = 72 GB.    

The memory footprint of an indexed string column in a PostgreSQL database with a billion rows can be estimated as follows:  
**Data Size:** The text type in PostgreSQL requires variable storage, with 1 byte per character plus 4 bytes for the length field. Assuming an average string length of 10 characters, one text column would require (10 bytes + 4 bytes) * 1 billion = 14 GB of storage.  
**Index Size:** The size of an index in PostgreSQL is roughly 20 bytes * number of rows for a B-tree index, which is the default index type. Therefore, one index would require 20 bytes * 1 billion = 20 GB of storage.  
So, the total estimated memory footprint would be 14 GB (data) + 20 GB (index) = 34 GB.

***Assumptions:***   
The performance of a PostgreSQL database with a data size of 150GB and a 20GB index can depend on several factors:  
**Hardware:** The performance of the database can be significantly affected by the hardware it's running on. This includes the speed of the CPU, the amount of RAM, and the type of storage (SSD vs HDD). More powerful hardware can handle larger databases more efficiently.  
**Configuration:** PostgreSQL has many configuration options that can be tuned for performance. These include settings for memory usage, query planning, and disk I/O. Properly configuring these settings can greatly improve performance.  
**Query Optimization:** The way queries are written can also have a big impact on performance. Using indexes effectively, avoiding full table scans, and minimizing the use of complex joins can all help to improve query performance.  
**Database Design:** The structure of the database itself can also affect performance. Normalizing data can help to reduce redundancy and improve performance, but in some cases, denormalization may be more efficient.  
**Maintenance:** Regular maintenance tasks like vacuuming and analyzing tables can help to keep the database running smoothly.   

In general, a PostgreSQL database with a data size of 150GB and a 20GB index should be able to handle a moderate to high load, assuming it's running on suitable hardware and has been properly configured and optimized. However, without more specific information about the workload and the hardware, it's difficult to provide a more precise estimate.   

***Conclusion:***
If we had a composite key, there would be a RAM penalty.  The database would be in 2NF is we had to normalize it.  If we had a single string key, we could normalize to 3NF.  I would favour a denormalized database as we are not going to be mutating the entries, only reading them for audit purposes.  This will boost performance.  

