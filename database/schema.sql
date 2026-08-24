CREATE TABLE IF NOT EXISTS medecin (
    codemed VARCHAR(30) PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    grade VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS patient (
    codepat VARCHAR(30) PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    sexe VARCHAR(20) NOT NULL,
    adresse VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS visiter (
    codemed VARCHAR(30) NOT NULL,
    codepat VARCHAR(30) NOT NULL,
    date DATE NOT NULL,
    PRIMARY KEY (codemed, codepat, date),
    CONSTRAINT fk_visiter_medecin FOREIGN KEY (codemed)
        REFERENCES medecin(codemed)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_visiter_patient FOREIGN KEY (codepat)
        REFERENCES patient(codepat)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT IGNORE INTO medecin VALUES
('MED001','Rakoto','Jean','Cardiologue'),
('MED002','Rasoanaivo','Marie','Pédiatre'),
('MED003','Andrianina','Paul','Généraliste');

INSERT IGNORE INTO patient VALUES
('PAT001','Randria','Aina','F','Antananarivo'),
('PAT002','Rabe','Hery','M','Fianarantsoa'),
('PAT003','Rakoto','Mialy','F','Toamasina'),
('PAT004','Razafy','Tiana','M','Antsirabe'),
('PAT005','Ando','Nantenaina','F','Mahajanga');

INSERT IGNORE INTO visiter VALUES
('MED001','PAT001','2026-08-01'),
('MED001','PAT002','2026-08-03'),
('MED002','PAT003','2026-08-04'),
('MED003','PAT004','2026-08-05'),
('MED002','PAT005','2026-08-06');
