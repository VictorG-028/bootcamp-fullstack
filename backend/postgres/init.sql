CREATE USER sysmap_bootcamp SUPERUSER PASSWORD 'sysmap_bootcamp_pass';
CREATE DATABASE "sysmap_bootcamp"
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.utf8'
       LC_CTYPE = 'en_US.utf8'
       CONNECTION LIMIT = -1;
GRANT ALL PRIVILEGES ON DATABASE sysmap_bootcamp TO sysmap_bootcamp;