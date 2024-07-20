# **Database Migration Guide with Liquibase**

This document provides instructions for managing database schema changes using Liquibase. Follow these steps to create and apply new migrations effectively.

## **Table of Contents**
1. [Setting Up Liquibase](#setting-up-liquibase)
2. [Creating a New Migration](#creating-a-new-migration)
3. [Applying Migrations](#applying-migrations)
4. [Rolling Back Migrations](#rolling-back-migrations)
5. [Best Practices](#best-practices)

## **Setting Up Liquibase**

1. **Add Liquibase Dependency**

   Ensure Liquibase is included in your `pom.xml` or `build.gradle` file. For Maven, use:

   ```xml
   <dependency>
       <groupId>org.liquibase</groupId>
       <artifactId>liquibase-core</artifactId>
       <version>4.14.0</version>
   </dependency>
   ```

2. **Configure Liquibase**

   Set up the `liquibase.properties` file in the `src/main/resources` directory:

   ```properties
   changeLogFile=db/changelog/db.changelog-master.xml
   url=jdbc:postgresql://localhost:5432/your_database
   username=your_username
   password=your_password
   ```

## **Creating a New Migration**

1. **Create a New Changelog File**

   Changelogs are XML files that describe database schema changes. Create a new file in `src/main/resources/db/changelog`. For example, `db.changelog-1.2.xml`:

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <databaseChangeLog
       xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                           http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd">

       <changeSet id="3" author="yourname">
           <!-- Example: Add a new column -->
           <addColumn tableName="product">
               <column name="stock" type="int"/>
           </addColumn>
       </changeSet>

   </databaseChangeLog>
   ```

2. **Update the Master Changelog**

   Ensure your master changelog includes the new file. Update `src/main/resources/db/changelog/db.changelog-master.xml`:

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <databaseChangeLog
       xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                           http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd">
       <include file="db.changelog-1.1.xml"/>
       <include file="db.changelog-1.2.xml"/>
   </databaseChangeLog>
   ```
### Important Notice

> **⚠️ Warning: Please keep in mind to maintain the order in which each file is referenced.**  
> If the order of migration files or changelog files is incorrect, it may lead to data loss or corruption. Ensure that all migrations are applied sequentially and in the correct order to maintain database integrity and consistency.

3.**Define ChangeSets**

   Each `<changeSet>` element represents a single change to the database schema. Ensure each change has a unique `id` and includes a description of the change. For example:

   ```xml
   <changeSet id="3" author="yourname">
       <addColumn tableName="product">
           <column name="stock" type="int"/>
       </addColumn>
   </changeSet>
   ```

## **Applying Migrations**

1. **Run the Update Command**

   Apply the new migrations to the database:

   ```bash
   liquibase update
   ```

   This command applies all pending changesets from the master changelog.

2. **Verify Changes**

   Ensure the database schema has been updated as expected. Check the tables, columns, and constraints to confirm the changes.

## **Rolling Back Migrations**

1. **Roll Back to a Specific Changeset**

   To roll back a specific changeset by ID:

   ```bash
   liquibase rollback <changeset_id>
   ```

2. **Roll Back to a Previous Tag**

   To roll back to a specific tag, if tags are used in changelogs:

   ```bash
   liquibase rollback <tag>
   ```

3. **Roll Back a Number of Changesets**

   To undo the last N changesets:

   ```bash
   liquibase rollbackCount N
   ```

## **Best Practices**

1. **Test Migrations**

   Always test migrations in a development or staging environment before applying them to production.


2. **Document Changes**

   Clearly document the purpose of each changeset and any dependencies between changesets.


3. **Version Control**

   Include all Liquibase changelog files in your version control system to track schema changes over time.


4. **Collaborate**

   Coordinate with your team to ensure that changes are reviewed and tested.

By following these steps and best practices, you can effectively manage and apply database schema changes using Liquibase.

