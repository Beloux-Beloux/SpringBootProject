# Centre Médical — Spring Boot REST + Java Swing

Application complète de gestion des médecins, patients et visites.

## Stack

- Java 17
- Spring Boot 3.5.6
- Spring Data JPA / Hibernate
- MySQL 8+
- Maven
- Lombok
- Java Swing + FlatLaf
- Apache HttpClient 5
- Jackson
- Architecture en couches: Controller -> Service -> Repository

La stratégie de suppression est **restrictive**: un médecin ou un patient ayant au moins une visite ne peut pas être supprimé. Le backend retourne HTTP 409.

## 1. Installer Java 17 et les outils Ubuntu

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk mysql-server maven git unzip
java -version
mvn -version
```

## 2. Installer IntelliJ IDEA

Option Snap officielle:

```bash
sudo snap install intellij-idea --classic
```

Puis:

```bash
intellij-idea
```

Dans IntelliJ:
1. Open -> sélectionner le dossier `centre-medical`.
2. IntelliJ détecte les deux projets Maven `backend` et `frontend`.
3. Vérifier `File -> Project Structure -> SDK = Java 17`.
4. Dans chaque projet Maven, effectuer `Reload All Maven Projects`.
5. Lombok est déjà configuré comme dépendance; IntelliJ récent gère l'annotation processing automatiquement. Si nécessaire: Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Enable.

## 3. Configurer MySQL

Démarrer MySQL:

```bash
sudo systemctl enable --now mysql
sudo systemctl status mysql
```

Créer la base et l'utilisateur:

```bash
sudo mysql
```

Puis dans MySQL:

```sql
CREATE DATABASE IF NOT EXISTS centre_medical
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'admin'@'localhost'
  IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON centre_medical.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

Le fichier `database/mysql-setup.sql` contient également ces commandes.

## 4. Créer les tables et données de test

```bash
mysql -u admin -p centre_medical < database/schema.sql
```

Mot de passe: `password`

`schema.sql` crée les tables et insère 3 médecins, 5 patients et plusieurs visites.

## 5. Lancer le backend

Terminal 1:

```bash
cd backend
mvn clean spring-boot:run
```

API:

```text
http://localhost:8080/api/medecins
http://localhost:8080/api/patients
http://localhost:8080/api/visites
```

Test rapide:

```bash
curl http://localhost:8080/api/medecins
curl http://localhost:8080/api/patients
curl http://localhost:8080/api/visites
```

## 6. Lancer le frontend

Terminal 2:

```bash
cd frontend
mvn clean compile
mvn exec:java
```

Le frontend utilise `http://localhost:8080` par défaut. Pour changer l'URL:

```bash
API_BASE_URL=http://192.168.1.10:8080 mvn exec:java
```

ou modifier `frontend/src/main/java/com/medical/ui/utils/Config.java`.

## 7. Construire les JAR

Backend:

```bash
cd backend
mvn clean package
java -jar target/centre-medical-backend-1.0.0.jar
```

Frontend:

```bash
cd frontend
mvn clean package
mvn exec:java
```

## Fonctionnalités

### Médecins
- Ajouter
- Modifier
- Supprimer avec blocage si visites associées
- Actualiser

### Patients
- Ajouter
- Modifier
- Supprimer avec blocage si visites associées
- Actualiser
- Recherche par code exact
- Recherche par nom partiel, insensible à la casse

### Visites
- Ajouter
- Modifier
- Supprimer
- Actualiser
- Vérification de l'existence du médecin et du patient
- Unicité `(codemed, codepat, date)`

## Architecture

```text
centre-medical/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/medical/
│       ├── MedicalApplication.java
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── exception/
│       ├── repository/
│       └── service/
├── frontend/
│   ├── pom.xml
│   └── src/main/java/com/medical/
│       ├── Main.java
│       └── ui/
│           ├── MainFrame.java
│           ├── dialogs/
│           ├── models/
│           ├── panels/
│           └── utils/
└── database/
    ├── mysql-setup.sql
    └── schema.sql
```

## Notes

- Les dates sont `java.time.LocalDate` partout.
- Les requêtes réseau Swing sont exécutées avec `SwingWorker`.
- Les erreurs REST sont uniformisées en JSON.
- CORS autorise les origines locales `localhost`.
