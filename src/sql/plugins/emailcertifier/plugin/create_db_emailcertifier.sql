
--
-- Structure for table emailcertifier_certifiable_email
--

DROP TABLE IF EXISTS emailcertifier_certifiable_email;
CREATE TABLE emailcertifier_certifiable_email (
id_certifiable_email int(6) NOT NULL,
email varchar(255) default '',
token long varchar,
date_creation date,
guid varchar(255) default '',
PRIMARY KEY (id_certifiable_email)
);
