CREATE TABLE `players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(12) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_username` (`id`, `username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `failed_logins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `count` int(11) NOT NULL DEFAULT '1',
  `timestamp` int(11) NOT NULL,
  `remote_addr` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_addr` (`id`, `remote_addr`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
